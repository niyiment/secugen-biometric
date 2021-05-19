package com.fh1360.lamis.secugen.biometric.dto;

import lombok.Data;

@Data
public class BiometricTemplate {

    private String id;
    
    private byte[] image;
    
    private int imageQuality;
    
    private byte[] template;
    
    private int imageHeight;
    
    private int imageWeight;
    
    private int imageResolution;
    
    private int matchingScore;
}
