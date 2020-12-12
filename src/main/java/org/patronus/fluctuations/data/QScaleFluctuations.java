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
public class QScaleFluctuations {

    Double q;
    Double logOfScale;
    Double logOfQPoweredMeanOfFluctuations;
    Double hurstExponentForQ;
    Double hurstExponentInterceptForQ;
    Double hurstExponentSEForQ;
    Double hurstExponentRSquareForQ;

    public Double getQ() {
        return q;
    }

    public void setQ(Double q) {
        this.q = q;
    }

    public Double getLogOfScale() {
        return logOfScale;
    }

    public void setLogOfScale(Double logOfScale) {
        this.logOfScale = logOfScale;
    }

    public Double getLogOfQPoweredMeanOfFluctuations() {
        return logOfQPoweredMeanOfFluctuations;
    }

    public void setLogOfQPoweredMeanOfFluctuations(Double logOfQPoweredMeanOfFluctuations) {
        this.logOfQPoweredMeanOfFluctuations = logOfQPoweredMeanOfFluctuations;
    }

    public Double getHurstExponentForQ() {
        return hurstExponentForQ;
    }

    public void setHurstExponentForQ(Double hurstExponentForQ) {
        this.hurstExponentForQ = hurstExponentForQ;
    }

    public Double getHurstExponentInterceptForQ() {
        return hurstExponentInterceptForQ;
    }

    public void setHurstExponentInterceptForQ(Double hurstExponentInterceptForQ) {
        this.hurstExponentInterceptForQ = hurstExponentInterceptForQ;
    }

    public Double getHurstExponentSEForQ() {
        return hurstExponentSEForQ;
    }

    public void setHurstExponentSEForQ(Double hurstExponentSEForQ) {
        this.hurstExponentSEForQ = hurstExponentSEForQ;
    }

    public Double getHurstExponentRSquareForQ() {
        return hurstExponentRSquareForQ;
    }

    public void setHurstExponentRSquareForQ(Double hurstExponentRSquareForQ) {
        this.hurstExponentRSquareForQ = hurstExponentRSquareForQ;
    }

}
