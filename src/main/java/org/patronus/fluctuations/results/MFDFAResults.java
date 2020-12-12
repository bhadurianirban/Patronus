/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.patronus.fluctuations.results;

import java.util.ArrayList;
import org.patronus.fluctuations.data.ScaleMappedFluctuations;
import org.patronus.fluctuations.data.MultiFractalSpectrum;
import java.util.List;
import org.patronus.fluctuations.data.QScaleFluctuations;

/**
 *
 * @author bhaduri
 */
public class MFDFAResults extends DFAResults {

    private List<MultiFractalSpectrum> multiFractalSpectrumList;
    private Double multiFractalSpectrumWidth;
    private List<Double> qLinSpqceList;
    private List<QScaleFluctuations> qScaleFluctuationsList;

    public MFDFAResults(List<ScaleMappedFluctuations> scaleMappedFluctuationsList, double hurstExponent, double hurstExponentSE, double hurstExponentRSquare, double hurstExponentChiSquare) {
        super(scaleMappedFluctuationsList, hurstExponent, hurstExponentSE, hurstExponentRSquare, hurstExponentChiSquare);
    }

    public MFDFAResults(DFAResults dfAResults) {
        super(dfAResults.scaleMappedFluctuationsList, dfAResults.hurstExponent, dfAResults.hurstExponentSE, dfAResults.hurstExponentRSquare, dfAResults.hurstExponentChiSquare);
    }

    public List<MultiFractalSpectrum> getMultiFractalSpectrumList() {
        return multiFractalSpectrumList;
    }

    public void setMultiFractalSpectrumList(List<MultiFractalSpectrum> multiFractalSpectrumList) {
        this.multiFractalSpectrumList = multiFractalSpectrumList;
    }

    public Double getMultiFractalSpectrumWidth() {
        return multiFractalSpectrumWidth;
    }

    public void setMultiFractalSpectrumWidth(Double multiFractalSpectrumWidth) {
        this.multiFractalSpectrumWidth = multiFractalSpectrumWidth;
    }

    public List<Double> getqLinSpqceList() {
        return qLinSpqceList;
    }

    public void setqLinSpqceList(List<Double> qLinSpqceList) {
        this.qLinSpqceList = qLinSpqceList;
    }

    public List<QScaleFluctuations> getqScaleFluctuationsList() {
        return qScaleFluctuationsList;
    }

    public void setqScaleFluctuationsList(List<QScaleFluctuations> qScaleFluctuationsList) {
        this.qScaleFluctuationsList = qScaleFluctuationsList;
    }
    
  

    @Override
    public String toString() {
        String ps = super.toString();
        String s = ps + "," + multiFractalSpectrumWidth;
        return s;
    }

}
