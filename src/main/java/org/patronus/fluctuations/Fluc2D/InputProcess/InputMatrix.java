/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.patronus.fluctuations.Fluc2D.InputProcess;

import java.util.Arrays;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.patronus.fluctuations.core.Fluctuations;
import org.patronus.fluctuations.core.Fluctuations2D;

/**
 *
 * @author bhaduri
 */
public class InputMatrix {

    private RealMatrix origMatrix;
    private RealMatrix randomWalkMatrix;
    private RealMatrix meanSubtractedMatrix;
    private Double matrixMean;
    private Double matrixMaxValue;

    public InputMatrix(RealMatrix origMatrix) {
        this.origMatrix = origMatrix;

    }

    public InputMatrix(double[][] origMatrix) {
        this.origMatrix = new Array2DRowRealMatrix(origMatrix);

    }

    public void printOrigMatrix() {
        System.out.println(origMatrix.getRowDimension()+"X"+origMatrix.getColumnDimension());
        for (int row = 0; row < origMatrix.getRowDimension(); row++) {
            double rowV[] = origMatrix.getRow(row);
            System.out.println(ArrayUtils.toString(rowV));
        }

    }

    private void prepareMatrixMean() {

        double mat[][] = origMatrix.getData();

        double flatArray[] = Arrays.stream(mat)
                .flatMapToDouble(Arrays::stream)
                .toArray();
        matrixMean = Arrays.stream(flatArray).average().getAsDouble();

        //System.out.println(s);
    }

    private void cumulateMatrix() {
        int columnCount = origMatrix.getColumnDimension();
        int rowCount = origMatrix.getRowDimension();

        Array2DRowRealMatrix columnMeanSubtractedMatrix = new Array2DRowRealMatrix(rowCount, columnCount);
        for (int col = 0; col < columnCount; col++) {
            double[] fullColumn = origMatrix.getColumn(col);
            double fullColumnMean = Arrays.stream(fullColumn).average().getAsDouble();
            double[] columnMeanSubtracted = Arrays.stream(fullColumn).map(i -> i - fullColumnMean).toArray();
            for (int row = 1; row < columnMeanSubtracted.length; row++) {
                columnMeanSubtracted[row] = columnMeanSubtracted[row] + columnMeanSubtracted[row - 1];
            }
            columnMeanSubtractedMatrix.setColumn(col, columnMeanSubtracted);

        }

        Array2DRowRealMatrix rowMeanSubtractedMatrix = new Array2DRowRealMatrix(rowCount, columnCount);
        for (int row = 0; row < rowCount; row++) {
            double[] fullRow = origMatrix.getRow(row);
            double fullRowMean = Arrays.stream(fullRow).average().getAsDouble();
            double[] rowMeanSubtracted = Arrays.stream(fullRow).map(i -> i - fullRowMean).toArray();
            for (int col = 1; col < rowMeanSubtracted.length; col++) {
                rowMeanSubtracted[col] = rowMeanSubtracted[col] + rowMeanSubtracted[col - 1];
            }
            rowMeanSubtractedMatrix.setRow(row, rowMeanSubtracted);

        }
        randomWalkMatrix = rowMeanSubtractedMatrix.add(columnMeanSubtractedMatrix);

    }

    private void prepareMeanSubtractedMatrix() {
        meanSubtractedMatrix = origMatrix.scalarAdd(0 - matrixMean);
    }

    private void prepareNormalisedMatrix() {
        double mat[][] = origMatrix.getData();

        double flatArray[] = Arrays.stream(mat)
                .flatMapToDouble(Arrays::stream)
                .toArray();

        matrixMaxValue = Arrays.stream(flatArray).max().getAsDouble();
        origMatrix = origMatrix.scalarMultiply(1 / matrixMaxValue);
    }

    public Double getMatrixMean(Boolean normalised) {
        if (normalised) {
            prepareNormalisedMatrix();
        }
        prepareMatrixMean();
        return matrixMean;
    }

    public RealMatrix getNormalisedMatrix() {

        prepareNormalisedMatrix();
        return this.origMatrix;
    }

    public RealMatrix getMeanSubtractedMatrix(Boolean normalised) {

        if (normalised) {
            prepareNormalisedMatrix();
        }
        prepareMatrixMean();
        prepareMeanSubtractedMatrix();
        return meanSubtractedMatrix;
    }

    public RealMatrix getCumulativeMatrix(Boolean normalised) {

        if (normalised) {
            prepareNormalisedMatrix();
        }
        //prepareMatrixMean();
        //prepareMeanSubtractedMatrix();
        cumulateMatrix();

        return randomWalkMatrix;
    }

    public Fluctuations getFluctuations2D(Boolean normalised) {

        return new Fluctuations2D(getCumulativeMatrix(normalised));
    }

//    public FQ getFQ(Boolean normalised) {
//
//        return new FQ(getCumulativeMatrix(normalised));
//    }
//    public FD getFA(Boolean normalised, int numberOfScales) {
//
//        if (normalised) {
//            prepareNormalisedMatrix();
//        }
//        prepareMatrixMean();
//        prepareMeanSubtractedMatrix();
//        //cumulateMatrix();
//        return new FD(meanSubtractedMatrix);
//    }
//
//    public FD getFA(Boolean normalised) {
//
//        if (normalised) {
//            prepareNormalisedMatrix();
//        }
//        prepareMatrixMean();
//        prepareMeanSubtractedMatrix();
//        //cumulateMatrix();
//        return new FD(meanSubtractedMatrix);
//    }
}
