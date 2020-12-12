/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.patronus.fluctuations.data;

/**
 *
 * @author bhaduri
 */
public class MultiFractalSpectrum {
    private Double hq;
    private Double dq;

    public MultiFractalSpectrum(Double hq, Double dq) {
        this.hq = hq;
        this.dq = dq;
    }

    public Double getHq() {
        return hq;
    }

    public Double getDq() {
        return dq;
    }
    
    
}
