/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.patronus.fluctuations.core;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.patronus.fluctuations.data.DataSliceStartEnd;
import org.patronus.fluctuations.data.ScaleMappedFluctuations;
import org.patronus.fluctuations.utils.LinSpace;
import org.patronus.fluctuations.utils.LogUtil;

/**
 *
 * @author dgrfi
 */
public class FluctuationsX extends Fluctuations<Integer, List<Double>> {

    private final List<Double> inputDataSeriesSecond;

    public FluctuationsX(List<Double> inputDataSeriesFirst, List<Double> inputDataSeriesSecond, Integer SCALE_MAX, Integer SCALE_MIN, Double CHI_SQUARE_REJECT_CUT, int NUMBER_OF_SCALES) {
        super(inputDataSeriesFirst, SCALE_MAX, SCALE_MIN, CHI_SQUARE_REJECT_CUT, NUMBER_OF_SCALES);
        this.inputDataSeriesSecond = inputDataSeriesSecond;
    }

    public FluctuationsX(List<Double> inputDataSeriesFirst, List<Double> inputDataSeriesSecond) {
        super(inputDataSeriesFirst, 1024, 16, 0.7, 19);
        this.inputDataSeriesSecond = inputDataSeriesSecond;
    }

    @Override
    protected void prepareScales() {
        Double scaleExponentMin = LogUtil.logBaseK(SCALE_MIN);
        Double scaleExponentMax = LogUtil.logBaseK(SCALE_MAX);
        LinSpace scaleExpLinSpace = new LinSpace(scaleExponentMin, scaleExponentMax, NUMBER_OF_SCALES);
        //scaleExpLinSpace.getLinSpaceList().stream().forEach(System.out::println);

        scales = scaleExpLinSpace.getLinSpaceList().stream().map(m -> {
            Integer scaleSize = (int) Math.round(Math.pow(2, m));
            return scaleSize;
        }).collect(Collectors.toList());
    }

    @Override
    protected ScaleMappedFluctuations partitionAndGetFluctuationsForAScale(Integer scaleSize) {
        Integer dataCount = inputData.size();
        if (dataCount > inputDataSeriesSecond.size()) {
            dataCount = inputDataSeriesSecond.size();
        }
        Integer noOfSlices = dataCount / scaleSize;
        List<DataSliceStartEnd<Integer>> dataSeriesSliceStartEndCoordinates = new ArrayList<>();
        for (int scaleCounter = 0; scaleCounter < noOfSlices; scaleCounter++) {
            int start = scaleSize * scaleCounter;
            int end = start + scaleSize - 1;
            DataSliceStartEnd<Integer> dataSeriesSliceStartEndCoordinate = new DataSliceStartEnd(start, end);
            dataSeriesSliceStartEndCoordinates.add(dataSeriesSliceStartEndCoordinate);
        }
        List<Double> flucuationsListForAScale = dataSeriesSliceStartEndCoordinates.stream().map(m -> prepareFluctuationsForAScaleAndSlice(m, scaleSize)).collect(Collectors.toList());

        ScaleMappedFluctuations mappedFluctuations = new ScaleMappedFluctuations(scaleSize, flucuationsListForAScale);
        return mappedFluctuations;
    }

    @Override
    protected double prepareFluctuationsForAScaleAndSlice(DataSliceStartEnd<Integer> dataSeriesSliceStartEndCoordinate, Integer scaleSize) {
        SimpleRegression srFirst = new SimpleRegression(true);
        SimpleRegression srSecond = new SimpleRegression(true);
        double[] timeSeriesSliceFirst = new double[scaleSize];
        double[] timeSeriesSliceSecond = new double[scaleSize];
        int actualValuesCounter = 0;
        for (int i = dataSeriesSliceStartEndCoordinate.getStart(); i <= dataSeriesSliceStartEndCoordinate.getEnd(); i++) {
            srFirst.addData(i, inputData.get(i));
            srSecond.addData(i, inputDataSeriesSecond.get(i));
            timeSeriesSliceFirst[actualValuesCounter] = inputData.get(i);
            timeSeriesSliceSecond[actualValuesCounter] = inputDataSeriesSecond.get(i);
            actualValuesCounter++;
        }
        double[] fitPredictValuesFirst = new double[scaleSize];
        double[] fitPredictValuesSecond = new double[scaleSize];
        for (int i = dataSeriesSliceStartEndCoordinate.getStart(); i <= dataSeriesSliceStartEndCoordinate.getEnd(); i++) {
            fitPredictValuesFirst[i-dataSeriesSliceStartEndCoordinate.getStart()] = srFirst.predict(i);
            fitPredictValuesSecond[i-dataSeriesSliceStartEndCoordinate.getStart()] = srSecond.predict(i);
        }


        Double RMSOfSliceVal = calculateRMSVectorProduct(timeSeriesSliceFirst, timeSeriesSliceSecond, fitPredictValuesFirst, fitPredictValuesSecond);

        return RMSOfSliceVal;
    }

    @Override
    protected void prepareScaleMappedFluctuations() {
        
        prepareScales();
        scaleMappedFluctuationsList = scales.stream().map(scale -> partitionAndGetFluctuationsForAScale(scale)).collect(Collectors.toList());
    }

    private Double calculateRMSVectorProduct(double[] timeSeriesActualFirst, double[] timeSeriesActualSecond,
            double[] timeSeriesFitFirst, double[] timeSeriesFitSecond) {
        
        Double[] fitActualDiffValsFirst = new Double[timeSeriesActualFirst.length];
        Double[] fitActualDiffValsSecond = new Double[timeSeriesActualSecond.length];
        Double total = 0.0;
        
        for (int i = 0; i < timeSeriesActualFirst.length; i++) {
            fitActualDiffValsFirst[i] = timeSeriesActualFirst[i] - timeSeriesFitFirst[i];
            fitActualDiffValsSecond[i] = timeSeriesActualSecond[i] - timeSeriesFitSecond[i];
            total += fitActualDiffValsFirst[i] * fitActualDiffValsSecond[i];
        }
        

        Double RMSVecProduct = Math.sqrt(Math.abs(total / timeSeriesActualFirst.length));

        return RMSVecProduct;

    }

}
