/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.patronus.fluctuations.core;

import org.patronus.fluctuations.utils.LogUtil;
import org.patronus.fluctuations.utils.LinSpace;
import org.patronus.fluctuations.data.DataSliceStartEnd;
import org.patronus.fluctuations.data.ScaleMappedFluctuations;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.math3.stat.regression.SimpleRegression;

/**
 *
 * @author dgrfi
 */
public class Fluctuations1D extends Fluctuations<Integer,List<Double>> {

    public Fluctuations1D(List<Double> inputData, Integer SCALE_MAX, Integer SCALE_MIN, Double CHI_SQUARE_REJECT_CUT, int NUMBER_OF_SCALES) {
        
        super(inputData, SCALE_MAX, SCALE_MIN, CHI_SQUARE_REJECT_CUT, NUMBER_OF_SCALES);
    }

    public Fluctuations1D(List<Double> inputData) {
        
        super(inputData, 1024, 16, 0.7, 19);
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
    protected double prepareFluctuationsForAScaleAndSlice(DataSliceStartEnd<Integer> dataSliceStartEnd, Integer scaleSize) {
        SimpleRegression sr = new SimpleRegression(true);
        for (int i =  dataSliceStartEnd.getStart(); i <=  dataSliceStartEnd.getEnd(); i++) {
            sr.addData(i, inputData.get(i));
        }
        double meanResidualSquaredSum = sr.getSumSquaredErrors() / scaleSize;
        double sqrtMeanResidualSquaredSum = Math.pow(meanResidualSquaredSum, 0.5);
        return sqrtMeanResidualSquaredSum;
    }

    @Override
    protected void prepareScaleMappedFluctuations() {
        prepareScales();
        scaleMappedFluctuationsList = scales.stream().map(scale -> partitionAndGetFluctuationsForAScale(scale)).collect(Collectors.toList());
        //scaleMappedFluctuationsList.stream().forEach(scm->System.out.println(scm.getLogOfScale()+","+scm.getLogOfQuadraticMeanOfFluctuations()));
    }

}
