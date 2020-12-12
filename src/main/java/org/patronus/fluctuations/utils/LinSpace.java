package org.patronus.fluctuations.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.util.Precision;

public class LinSpace {

    private final Double start;
    private final Double end;
    private final int numberOfScales;
    private Double distanceInBetween;
    private List<Double> linSpaceList;

    public LinSpace(Double start, Double end, int numberOfScales) {
        this.start = start;
        this.end = end;
        this.numberOfScales = numberOfScales;

    }

//    private void calcLinSpaceList() {
//        linSpaceList = new ArrayList<>();
//        Double totalDistance = end - start;
//        distanceInBetween = totalDistance / (numberOfScales - 1);
//        Double linSpace = start;
//        linSpaceList.add(linSpace);
//        for (int i = 1; i < numberOfScales; i++) {
//            linSpace = linSpace + distanceInBetween;
//            linSpaceList.add(Precision.round(linSpace, 1));
//            //linSpaceList.add(linSpace);
//        }
//
//    }

    public void calcLinSpaceList() {
        linSpaceList = new ArrayList<>();;
        BigDecimal bigStart = new BigDecimal(start);
        BigDecimal bigEnd = new BigDecimal(end);
        BigDecimal bigDivision = new BigDecimal(numberOfScales - 1);
        BigDecimal bigStep = bigEnd.subtract(bigStart).divide(bigDivision, 16, RoundingMode.HALF_UP);
        //Double step = (end - start)/(totalCount-1);
        BigDecimal linValue = new BigDecimal(start);
        linSpaceList.add(linValue.doubleValue());
        for (int i = 1; i < numberOfScales; i++) {
            linValue = linValue.add(bigStep);
            linSpaceList.add(linValue.doubleValue());
        }
        distanceInBetween = bigStep.doubleValue();
    }

    public List<Double> getLinSpaceList() {
        calcLinSpaceList();
        return linSpaceList;
    }

    public Double getDistanceInBetween() {
        return distanceInBetween;
    }

}
