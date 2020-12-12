/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.patronus.fluctuations.results;

import org.patronus.fluctuations.data.ScaleMappedFluctuations;
import java.util.List;

/**
 *
 * @author dgrfi
 */
public class DFAResults {
    protected List<ScaleMappedFluctuations> scaleMappedFluctuationsList;
    protected double hurstExponent;
    protected double hurstExponentSE;
    protected double hurstExponentRSquare;
    protected double hurstExponentChiSquare;
    protected double gammaX;

    public DFAResults(List<ScaleMappedFluctuations> scaleMappedFluctuationsList, double hurstExponent, double hurstExponentSE, double hurstExponentRSquare, double hurstExponentChiSquare) {
        this.scaleMappedFluctuationsList = scaleMappedFluctuationsList;
        this.hurstExponent = hurstExponent;
        this.hurstExponentSE = hurstExponentSE;
        this.hurstExponentRSquare = hurstExponentRSquare;
        this.hurstExponentChiSquare = hurstExponentChiSquare;
        this.gammaX = gammaX = 2 - (2*hurstExponent);
    }

    public List<ScaleMappedFluctuations> getScaleMappedFluctuationsList() {
        return scaleMappedFluctuationsList;
    }

    public double getHurstExponent() {
        return hurstExponent;
    }

    public double getHurstExponentSE() {
        return hurstExponentSE;
    }

    public double getHurstExponentRSquare() {
        return hurstExponentRSquare;
    }

    public double getHurstExponentChiSquare() {
        return hurstExponentChiSquare;
    }

    public double getGammaX() {
        return gammaX;
    }
    
    @Override
    public String toString() {
        String s = hurstExponent+","+hurstExponentSE+","+hurstExponentRSquare+","+hurstExponentChiSquare+","+gammaX;
        return s;
    }
    
}
