/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.patronus.fluctuations.core;

import org.patronus.fluctuations.utils.LogUtil;
import org.patronus.fluctuations.utils.LinSpace;
import org.patronus.fluctuations.data.DataSliceStartEnd;
import org.patronus.fluctuations.data.MatrixCoordinate;
import org.patronus.fluctuations.data.ScaleMappedFluctuations;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.regression.OLSMultipleLinearRegression;

/**
 *
 * @author dgrfi
 */
public class Fluctuations2D extends Fluctuations<MatrixCoordinate, RealMatrix> {

    public Fluctuations2D(RealMatrix inputData, MatrixCoordinate SCALE_MAX, MatrixCoordinate SCALE_MIN, Double CHI_SQUARE_REJECT_CUT, int NUMBER_OF_SCALES) {
        super(inputData, SCALE_MAX, SCALE_MIN, CHI_SQUARE_REJECT_CUT, NUMBER_OF_SCALES);
    }

    public Fluctuations2D(RealMatrix inputData) {
        super(inputData, new MatrixCoordinate(inputData.getRowDimension(), inputData.getColumnDimension()), new MatrixCoordinate(16, 16), 0.7, 19);

    }

    @Override
    protected void prepareScales() {
        Double columnExponentMin = LogUtil.logBaseK(SCALE_MIN.getColumn());
        Double columnExponentMax = LogUtil.logBaseK(SCALE_MAX.getColumn());

        Double rowExponentMin = LogUtil.logBaseK(SCALE_MIN.getRow());
        Double rowExponentMax = LogUtil.logBaseK(SCALE_MAX.getRow());

        LinSpace colomnExpLinSpace = new LinSpace(columnExponentMin, columnExponentMax, NUMBER_OF_SCALES);
        LinSpace rowExpLinSpace = new LinSpace(rowExponentMin, rowExponentMax, NUMBER_OF_SCALES);

        scales = new ArrayList<>();
        for (int expCounter = 0; expCounter < NUMBER_OF_SCALES; expCounter++) {
            int columnScaleSize = (int) Math.round(Math.pow(2, colomnExpLinSpace.getLinSpaceList().get(expCounter)));
            int rowScaleSize = (int) Math.round(Math.pow(2, rowExpLinSpace.getLinSpaceList().get(expCounter)));
            MatrixCoordinate matrixScale = new MatrixCoordinate(rowScaleSize,columnScaleSize);
            scales.add(matrixScale);
            //System.out.println(columnScaleSize+" "+rowScaleSize);
        }
    }

    @Override
    protected ScaleMappedFluctuations partitionAndGetFluctuationsForAScale(MatrixCoordinate scaleSize) {
        int columnScaleSize = scaleSize.getColumn();
        int rowScaleSize = scaleSize.getRow();
        int columnCount = inputData.getColumnDimension();
        int rowCount = inputData.getRowDimension();
        int noOfColumnSlices = columnCount / columnScaleSize;
        int noOfRowSlices = rowCount / rowScaleSize;
        List<Integer> startColIndexes = new ArrayList<>();
        List<Integer> endColIndexes = new ArrayList<>();

        for (int scaleCounter = 0; scaleCounter < noOfColumnSlices; scaleCounter++) {
            int startColIndex = columnScaleSize * scaleCounter;
            int endColIndex = startColIndex + columnScaleSize - 1;
            startColIndexes.add(startColIndex);
            endColIndexes.add(endColIndex);
        }
        List<Integer> startRowIndexes = new ArrayList<>();
        List<Integer> endRowIndexes = new ArrayList<>();

        for (int scaleCounter = 0; scaleCounter < noOfRowSlices; scaleCounter++) {
            int startRowIndex = rowScaleSize * scaleCounter;
            int endRowIndex = startRowIndex + rowScaleSize - 1;
            startRowIndexes.add(startRowIndex);
            endRowIndexes.add(endRowIndex);
        }
        List<DataSliceStartEnd<MatrixCoordinate>> subMatrixCoordinatesList = new ArrayList<>();
        int numOfPartitions = 0;
        for (int colSliceCounter = 0; colSliceCounter < noOfColumnSlices; colSliceCounter++) {
            for (int rowSliceCounter = 0; rowSliceCounter < noOfRowSlices; rowSliceCounter++) {
                int startCol = startColIndexes.get(colSliceCounter);
                int startRow = startRowIndexes.get(rowSliceCounter);
                int endCol = endColIndexes.get(colSliceCounter);
                int endRow = endRowIndexes.get(rowSliceCounter);
                MatrixCoordinate startCoordinate = new MatrixCoordinate(startRow, startCol);
                MatrixCoordinate endCoordinate = new MatrixCoordinate(endRow, endCol);
                DataSliceStartEnd<MatrixCoordinate> dataSliceStartEnd = new DataSliceStartEnd(startCoordinate, endCoordinate);
                
                subMatrixCoordinatesList.add(dataSliceStartEnd);
                numOfPartitions++;
            }
        }
        List<Double> flucuationsListForAScale = subMatrixCoordinatesList.stream().map(m -> prepareFluctuationsForAScaleAndSlice(m, scaleSize)).collect(Collectors.toList());
        Integer areaForScaleSize = scaleSize.getColumn()*scaleSize.getRow();
        ScaleMappedFluctuations mappedFluctuations = new ScaleMappedFluctuations(areaForScaleSize, flucuationsListForAScale);
        return mappedFluctuations;
    }

    @Override
    protected double prepareFluctuationsForAScaleAndSlice(DataSliceStartEnd<MatrixCoordinate> dataSeriesSliceStartEndCoordinate, MatrixCoordinate scaleSize) {
        //here there are two independant variables which are the coordinates of the matrix so for x variables k = 2
        //Let us declare an 2D array of 2 columns
        //If start col and start row is 0,0 and end col and end row is 3,3 we will get a 2D array like this with 16 pairs
        // {0,0}
        // {0,1}
        // {0,2}
        // {0,3}
        // {1,0}
        // {1,1}
        //....
        // {3,3}
        int startRow = dataSeriesSliceStartEndCoordinate.getStart().getRow();
        int endRow = dataSeriesSliceStartEndCoordinate.getEnd().getRow();
        int startColumn = dataSeriesSliceStartEndCoordinate.getStart().getColumn();
        int endColumn = dataSeriesSliceStartEndCoordinate.getEnd().getColumn();
        
        
        int width = endColumn - startColumn + 1;
        int height = endRow - startRow + 1;
        int totalObservations = width * height;
        //System.out.println("totalObservations "+totalObservations);
        double x[][] = new double[totalObservations][2];
        double y[] = new double[totalObservations];
        int observationNumber = 0;
        for (int row = startRow; row <= endRow; row++) {
            for (int col = startColumn; col <= endColumn; col++) {
                x[observationNumber][0] = row;
                x[observationNumber][1] = col;
                y[observationNumber] = inputData.getEntry(row, col);
                //System.out.println("y "+y[observationNumber]+" "+ArrayUtils.toString(x[observationNumber]));
                observationNumber++;
            }
        }
        OLSMultipleLinearRegression multipleLinearRegression = new OLSMultipleLinearRegression();
        multipleLinearRegression.newSampleData(y, x);
        multipleLinearRegression.setNoIntercept(false);

        Double residualSquaredSum;

        residualSquaredSum = multipleLinearRegression.calculateResidualSumOfSquares();

        //System.out.println(ArrayUtils.toString(residuals));
        double meanResidualSquaredSum = residualSquaredSum / totalObservations;
        //System.out.println(meanResidualSquaredSum);
        
        double sqrtMeanResidualSquaredSum = Math.pow(meanResidualSquaredSum,0.5);
        //System.out.println(meanResidualSquaredSum);
        return sqrtMeanResidualSquaredSum;
    }

    @Override
    protected void prepareScaleMappedFluctuations() {
        prepareScales();
        scaleMappedFluctuationsList = scales.stream().map(scale -> partitionAndGetFluctuationsForAScale(scale)).collect(Collectors.toList());
    }

}
