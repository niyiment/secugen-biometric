package com.fh1360.lamis.secugen.biometric.dto;

import lombok.Data;

@Data
public class BiometricRequest {

    private Long facilityId;
    
    private Long patientId;
    
    private String finger;
    
    private byte[] template;
}
