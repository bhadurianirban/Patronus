/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.patronus.fluctuations.core;

import org.patronus.fluctuations.utils.LinSpace;
import org.patronus.fluctuations.results.MFDFAResults;
import org.patronus.fluctuations.results.DFAResults;
import org.patronus.fluctuations.data.DataSliceStartEnd;
import org.patronus.fluctuations.data.ScaleMappedFluctuations;
import org.patronus.fluctuations.data.MultiFractalSpectrum;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.patronus.fluctuations.data.QScaleFluctuations;

/**
 *
 * @author bhaduri
 */
public abstract class Fluctuations<T, D> {

    protected List<T> scales;
    protected D inputData;
    protected T SCALE_MAX;
    protected T SCALE_MIN;
    protected Double CHI_SQUARE_REJECT_CUT;
    protected int NUMBER_OF_SCALES;

    protected List<ScaleMappedFluctuations> scaleMappedFluctuationsList;
    protected SimpleRegression scaleRMSLogFit;

    private Double linSpaceDistanceInBetween;

    MFDFAResults mfdfaResults;

    public Fluctuations(D inputData, T SCALE_MAX, T SCALE_MIN, Double CHI_SQUARE_REJECT_CUT, int NUMBER_OF_SCALES) {
        this.inputData = inputData;
        this.SCALE_MAX = SCALE_MAX;
        this.SCALE_MIN = SCALE_MIN;
        this.CHI_SQUARE_REJECT_CUT = CHI_SQUARE_REJECT_CUT;
        this.NUMBER_OF_SCALES = NUMBER_OF_SCALES;
    }

    protected abstract void prepareScales();

    protected abstract ScaleMappedFluctuations partitionAndGetFluctuationsForAScale(T scaleSize);

    protected abstract double prepareFluctuationsForAScaleAndSlice(DataSliceStartEnd<T> dataSeriesSliceStartEndCoordinate, T scaleSize);

    protected abstract void prepareScaleMappedFluctuations();

    public List<T> getScales() {
        prepareScales();
        return scales;
    }

    public void getFluctutations() {
        prepareScaleMappedFluctuations();
    }

    protected void prepareHurst() {
        //prepareScales();
        prepareScaleMappedFluctuations();
        prepareScaleRMSLogFit();
    }

    private void prepareScaleRMSLogFit() {
        scaleRMSLogFit = new SimpleRegression(true);
        scaleMappedFluctuationsList.stream().forEach(scm -> scaleRMSLogFit.addData(scm.getLogOfScale(), scm.getLogOfQuadraticMeanOfFluctuations()));

    }

    protected double calcPSVGChiSquareVal() {
        double hurstExponentChiSquare;
        int listSize = 0;
        Double expectLogOfSliceSecondOrderRMS;
        Double actualLogOfSliceSecondOrderRMS;
        Double diffExpectedActual;
        Double absExpectLogOfSliceSecondOrderRMS;
        Double squaredDiffDivExpected;
        Double sumOfSquaredDiffDivExpected = 0.0;

        for (int i = 0; i < scaleMappedFluctuationsList.size(); i++) {

            expectLogOfSliceSecondOrderRMS = scaleRMSLogFit.predict(scaleMappedFluctuationsList.get(i).getLogOfScale());
            actualLogOfSliceSecondOrderRMS = scaleMappedFluctuationsList.get(i).getLogOfQuadraticMeanOfFluctuations();
            absExpectLogOfSliceSecondOrderRMS = Math.abs(expectLogOfSliceSecondOrderRMS);
            diffExpectedActual = Math.abs(expectLogOfSliceSecondOrderRMS - actualLogOfSliceSecondOrderRMS);

            if (diffExpectedActual <= CHI_SQUARE_REJECT_CUT) {
                squaredDiffDivExpected = (diffExpectedActual * diffExpectedActual) / absExpectLogOfSliceSecondOrderRMS;

                sumOfSquaredDiffDivExpected = sumOfSquaredDiffDivExpected + squaredDiffDivExpected;
                listSize++;
            }
        }

        if (listSize < 3) {
            Logger.getLogger(Fluctuations.class.getName()).log(Level.SEVERE, "Chi square could not be calculated");
            hurstExponentChiSquare = 999.0;
        } else {
            int degFreedom = listSize - 2;//2 is because there are expected and actual is for 2
            hurstExponentChiSquare = sumOfSquaredDiffDivExpected / degFreedom;
        }
        return hurstExponentChiSquare;
    }

    public DFAResults getDFAResults() {
        prepareHurst();
        double hurstExponent;
        double hurstExponentSE;
        double hurstExponentRSquare;
        double hurstExponentChiSquare;
        double gammaX;
        hurstExponent = scaleRMSLogFit.getSlope();
        hurstExponentSE = scaleRMSLogFit.getSlopeStdErr();
        hurstExponentRSquare = scaleRMSLogFit.getRSquare();
        hurstExponentChiSquare = calcPSVGChiSquareVal();

        DFAResults dfaResults = new DFAResults(scaleMappedFluctuationsList, hurstExponent, hurstExponentSE, hurstExponentRSquare, hurstExponentChiSquare);
        return dfaResults;

    }

    private void prepareQLinSpaceList() {
        LinSpace linSpace = new LinSpace(-5.0, 5.0, 101);
        mfdfaResults.setqLinSpqceList(linSpace.getLinSpaceList());
        this.linSpaceDistanceInBetween = linSpace.getDistanceInBetween();
    }

//    private void prepareFQ() {
//        scaleMappedQRMSList = matrixScales.stream().map(ms -> prepareScaleMappedRMS(ms)).collect(Collectors.toList());
//    }
    private void prepareFQ() {
        mfdfaResults = new MFDFAResults(getDFAResults());
        prepareQLinSpaceList();

        //now fluctuations are there for all scales in List<ScaleMappedFluctuations> scaleMappedFluctuationsList
        //if there are 19 scales the list size will be 19
        //for FD we have calculated the quadratic mean of the fluctuations for each 19 scales and got 19 quadratic means
        //here for each q we need to calculate the q powered mean of the fluctuations
        //for example to calculate qpowered mean for q=2.0 
        //List<Double> qPoweredMeanForAScale = scaleMappedFluctuationsList.stream().map(scmf -> scmf.getLogOfQPoweredMeanOfFluctuations(2.0)).collect(Collectors.toList());
        //Hq
        List<Double> tqList = mfdfaResults.getqLinSpqceList().stream().map(q -> prepareTq(q)).collect(Collectors.toList());
        List<Double> hqList = new ArrayList<>();
        int prevQCounter;
        for (int qCounter = 1; qCounter < tqList.size(); qCounter++) {
            prevQCounter = qCounter - 1;
            Double hq = (tqList.get(qCounter) - tqList.get(prevQCounter)) / linSpaceDistanceInBetween;
            hqList.add(hq);
        }
        List<Double> dqList = new ArrayList<>();
        for (int i = 0; i < (mfdfaResults.getqLinSpqceList().size() - 1); i++) {
            Double q = mfdfaResults.getqLinSpqceList().get(i);
            Double hq = hqList.get(i);
            Double tq = tqList.get(i);
            Double dq = (q * hq) - tq;
            dqList.add(dq);
        }
        List<MultiFractalSpectrum> multiFractalSpectrumList = new ArrayList<>();
        for (int i = 0; i < hqList.size(); i++) {
            MultiFractalSpectrum multiFractalSpectrum = new MultiFractalSpectrum(hqList.get(i), dqList.get(i));
            multiFractalSpectrumList.add(multiFractalSpectrum);
        }
        Double minHq = hqList.stream().mapToDouble(a -> a).min().getAsDouble();
        Double maxHq = hqList.stream().mapToDouble(a -> a).max().getAsDouble();
        Double multiFractalSpectrumWidth = maxHq - minHq;
        mfdfaResults.setMultiFractalSpectrumWidth(multiFractalSpectrumWidth);
        mfdfaResults.setMultiFractalSpectrumList(multiFractalSpectrumList);

    }

    private Double prepareTq(Double q) {

        SimpleRegression scaleQPoweredMeanFit = new SimpleRegression(true);
        scaleMappedFluctuationsList.stream().forEach(scmf -> scaleQPoweredMeanFit.addData(scmf.getLogOfScale(), scmf.getLogOfQPoweredMeanOfFluctuations(q)));
        Double Hq = scaleQPoweredMeanFit.getSlope();
        Double Tq = (Hq * q) - 1;
        return Tq;
    }

    private void prepareFluctiationsListForAllq() {
        List<QScaleFluctuations> qScaleFluctuationsList = new ArrayList<>();
        mfdfaResults.getqLinSpqceList().stream().forEach(q -> {
            SimpleRegression fitForAq = new SimpleRegression();
            List<QScaleFluctuations> qScaleFluctuationsListForAq = scaleMappedFluctuationsList.stream().map(scmf
                    -> prepareFluctuationsForAScaleAndAq(scmf, q, fitForAq)).collect(Collectors.toList());
            Double hurstExponentForQ = fitForAq.getSlope();
            Double hurstExponentInterceptForQ = fitForAq.getIntercept();
            Double hurstExponentSEForQ = fitForAq.getSlopeStdErr();
            Double hurstExponentRSquareForQ = fitForAq.getRSquare();
            qScaleFluctuationsListForAq.forEach(qsf-> {
                qsf.setHurstExponentForQ(hurstExponentForQ);
                qsf.setHurstExponentInterceptForQ(hurstExponentInterceptForQ);
                qsf.setHurstExponentRSquareForQ(hurstExponentRSquareForQ);
                qsf.setHurstExponentSEForQ(hurstExponentSEForQ);
            });
            qScaleFluctuationsList.addAll(qScaleFluctuationsListForAq);
        });
        mfdfaResults.setqScaleFluctuationsList(qScaleFluctuationsList);
    }

    private QScaleFluctuations prepareFluctuationsForAScaleAndAq(ScaleMappedFluctuations scmf, Double q, SimpleRegression fitForAq) {

        QScaleFluctuations qScaleFluctuations = new QScaleFluctuations();
        qScaleFluctuations.setQ(q);
        Double logOfScale = scmf.getLogOfScale();
        qScaleFluctuations.setLogOfScale(logOfScale);
        Double logOfQPoweredMeanOfFluctuations = scmf.getLogOfQPoweredMeanOfFluctuations(q);
        qScaleFluctuations.setLogOfQPoweredMeanOfFluctuations(logOfQPoweredMeanOfFluctuations);
        fitForAq.addData(scmf.getLogOfScale(), logOfQPoweredMeanOfFluctuations);
        return qScaleFluctuations;
    }

    private List<Double> getQLinSpaceList() {
        prepareQLinSpaceList();
        return mfdfaResults.getqLinSpqceList();
    }

    public MFDFAResults getMFDFAResults() {
        prepareFQ();
        prepareFluctiationsListForAllq();
        return mfdfaResults;
    }

//    public MFDFAResults getQScaleResults() {
//        mfdfaResults = new MFDFAResults(getDFAResults());
//        prepareQLinSpaceList();
//        prepareFluctiationsListForAllq();
//        return mfdfaResults;
//    }
}
