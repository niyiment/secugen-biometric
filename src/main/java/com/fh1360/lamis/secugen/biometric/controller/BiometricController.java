package com.fh1360.lamis.secugen.biometric.controller;

import SecuGen.FDxSDKPro.jni.SGFDxSecurityLevel;
import com.fh1360.lamis.secugen.biometric.dto.BiometricResult;
import com.fh1360.lamis.secugen.biometric.dto.BiometricTemplate;
import com.fh1360.lamis.secugen.biometric.dto.Device;
import com.fh1360.lamis.secugen.biometric.entity.Biometric;
import com.fh1360.lamis.secugen.biometric.service.SecugenManager;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.apache.commons.lang3.RandomStringUtils;

/**
 * @author
 */
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/biometrics")
public class BiometricController {
    private final RestTemplate restTemplate = new RestTemplate();
    private final static String BASE_URL = "/api/biometrics";
    private List<BiometricTemplate> templates = new ArrayList<>();
    private boolean templatesUpdated;

    @Autowired
    private SecugenManager secugenManager;
    
    private static final Logger logger = LoggerFactory.getLogger(BiometricController.class);

    @GetMapping("/server")
    public String getServerUrl() {
        return secugenManager.getSecugenProperties().getServerUrl();
    }

    @GetMapping("/readers")
    public ResponseEntity<Object> getReaders() {
        List<Device> devices = secugenManager.getDevices();
        return ResponseEntity.ok(devices);
    }

    @PostMapping("/identify")
    public BiometricResult identify(@RequestParam String reader, @RequestParam String server, @RequestParam String accessToken,
                                    @RequestBody Biometric biometric){

        logger.info("STARTED CAPTURING +++++++++++++++++");
        Long readerId = secugenManager.getDeviceId(reader);
        secugenManager.boot(readerId);
        if (secugenManager.getError() > 0L) {
            return null;
        }

        BiometricResult result = new BiometricResult();
        try {
            BiometricTemplate registerBiometric = secugenManager.captureFingerPrint();
            AtomicReference<Boolean> matched = new AtomicReference<>(false);
            result.setTemplate(registerBiometric.getTemplate());
            if (registerBiometric.getTemplate().length > 200) {
                long sl = SGFDxSecurityLevel.SL_NORMAL;
                if (biometric.getTemplates() == null || biometric.getTemplates().isEmpty()) {
                    logger.info("Querying server for templates...");
                    Instant start = Instant.now();
                    templates = biometricTemplates(server, accessToken);
                    logger.info("Query Duration: {} secs", Duration.between(start, Instant.now()).getSeconds());
                }
                byte[] scannedTemplate = registerBiometric.getTemplate();
                for (BiometricTemplate template : templates) {
                    if (template.getTemplate().length > 0) {
                        matched.set(secugenManager.matchTemplate(template.getTemplate(), scannedTemplate));
                        if (matched.get()) {
                            logger.info("Fingerprint already exist");
                            String id = template.getId();
                            result.setId(id);
                            result.setMessage("PATIENT_IDENTIFIED");
                            return result;
                        }
                    }
                }

                result.setMessage("PATIENT_NOT_IDENTIFIED");
                result.setTemplate(scannedTemplate);
                result.setIso(true);
                BiometricTemplate template = new BiometricTemplate();
                template.setTemplate(scannedTemplate);
                template.setId("currentUser_" + RandomStringUtils.randomAlphabetic(5));
                templates.add(template);

                return result;
            } else {
                logger.info("Error Message: {}", registerBiometric.getId());
                result.setMessage("COULD_NOT_CAPTURE_TEMPLATE");
                return result;
            }

        } catch (Exception exception) {
            logger.info("Finger Print Capture Error: " + exception.getMessage());
            result.setMessage("COULD_NOT_CAPTURE_TEMPLATE");
            return result;
        }
    }

    @PostMapping("/enrol")
    public BiometricResult enrol(@RequestParam String reader, @RequestParam String server, @RequestParam String accessToken,
                                 @RequestBody Biometric biometric) {
        try {
            reader = URLDecoder.decode(reader, "UTF-8");
        } catch (UnsupportedEncodingException ignored) {
        }
        if (scannerIsNotSet(reader)) {
            return null;
        }

        return identify(accessToken, reader, server, biometric);
    }

    private boolean scannerIsNotSet(String reader) {
        Long readerId = secugenManager.getDeviceId(reader);
        for (Device device : secugenManager.getDevices()) {
            if (device.getId().equals(reader)) {
                secugenManager.boot(readerId);
                return false;
            }
        }
        return true;
    }

    @SneakyThrows
    private List<BiometricTemplate> biometricTemplates(String server, String accessToken) {
        String url = server + "/biometrics";
        if (!url.startsWith("http://")) {
            url = "http://" + url;
        }
        ResponseEntity<List<BiometricTemplate>> response = restTemplate.exchange(
                RequestEntity.get(new URI(url)).header("Authorization", "Bearer " + accessToken)
                        .header("Content-Type", "application/json").build(),
                new ParameterizedTypeReference<List<BiometricTemplate>>() {
                });
        return response.getBody();
    }

    @GetMapping
    public List<BiometricTemplate> getTemplates(@RequestParam String server, @RequestParam String token) {
        return biometricTemplates(server, token);
    }
}
