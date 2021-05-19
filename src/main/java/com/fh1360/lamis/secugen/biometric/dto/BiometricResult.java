
package com.fh1360.lamis.secugen.biometric.dto;

import lombok.Data;

@Data
public class BiometricResult {
    private String id;
    private Long patientId;
    private Long facilityId;
    private String hospitalNum;
    private String name;
    private String address;
    private String phone;
    private String gender;
    private String message;
    private Boolean inFacility = true;
    private byte[] template;
    private boolean identified;
    private boolean iso;
}
