package com.fh1360.lamis.secugen.biometric.entity;

import lombok.Data;

@Data
public class BiometricTemplate {
    private String id;
    private Boolean iso;
    private byte[] template;
}
