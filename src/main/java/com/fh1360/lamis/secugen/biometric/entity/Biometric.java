package com.fh1360.lamis.secugen.biometric.entity;

import lombok.Data;

import java.util.List;

@Data
public class Biometric {
    List<byte[]> templates;
}
