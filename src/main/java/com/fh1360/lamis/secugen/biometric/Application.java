package com.fh1360.lamis.secugen.biometric;

import SecuGen.FDxSDKPro.jni.SGDeviceInfoParam;
import com.fh1360.lamis.secugen.biometric.dto.BiometricTemplate;
import com.fh1360.lamis.secugen.biometric.enumeration.DeviceNames;
import com.fh1360.lamis.secugen.biometric.service.SecugenManager;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

    private static Logger logger = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        logger.info("APPLICATION LAUNCHED");
    }

}

//@SpringBootApplication
//public class Application implements CommandLineRunner {
// 
//    private static Logger logger = LoggerFactory.getLogger(Application.class);
//    
//    @Autowired
//    private SecugenManager secugenManager;
// 
//    public static void main(String[] args) {
//        logger.info("STARTING THE APPLICATION");
//        SpringApplication.run(Application.class, args);
//        logger.info("APPLICATION FINISHED");
//    }
//  
//    @Override
//    public void run(String... args) {
//        logger.info("EXECUTING : command line runner");
//        Long error = secugenManager.boot();
//        if (error==null || error == 0L){
//            logger.info("EXECUTING : Device Information Below");
//            
//            Gson gson = new Gson();
//            logger.info(gson.toJson(secugenManager.getBasicDeviceInfo()));
//            
//            BiometricTemplate biometricTemplate = secugenManager.registerFingerPrint();
//            
//            logger.info(gson.toJson(biometricTemplate));
//        }
//    }
//}
