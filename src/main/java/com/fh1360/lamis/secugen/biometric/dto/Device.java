package com.fh1360.lamis.secugen.biometric.dto;

import com.fh1360.lamis.secugen.biometric.enumeration.DeviceNames;
import lombok.Data;

@Data
public class Device {
    private String id;
    private String name;   
    private DeviceNames deviceName;
    private int imageWidth, 
            imageHeight, 
            contrast, 
            brightness, 
            gain, 
            imageDPI, 
            FWVersion;
}
