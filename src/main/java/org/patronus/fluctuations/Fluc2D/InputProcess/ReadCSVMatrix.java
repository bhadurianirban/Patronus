/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.patronus.fluctuations.Fluc2D.InputProcess;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.patronus.fluctuations.data.MatrixElement;

/**
 *
 * @author dgrfi
 */
public class ReadCSVMatrix {

    private final File csvFile;
    
    private InputMatrix randomWalkMatix;
    private RealMatrix csvMatrix;
    public ReadCSVMatrix(File csvFile) {
        this.csvFile = csvFile;
    }

    public InputMatrix getInputMatrix() {
        String cvsSplitBy = ",";
        List<MatrixElement> matrixElementList = new ArrayList<>();
        try {
            BufferedReader bf = new BufferedReader(new FileReader(csvFile));
            String line;
            //line = bf.readLine();
            while ((line = bf.readLine()) != null) {
                String[] lineItems = line.split(cvsSplitBy);
                Integer rowNumber = Integer.parseInt(lineItems[0]);
                Integer colNumber = Integer.parseInt(lineItems[1]);
                Double cellValue = Double.parseDouble(lineItems[2]);
                MatrixElement matrixElement = new MatrixElement(rowNumber, colNumber, cellValue);
                matrixElementList.add(matrixElement);
            }
        } catch (IOException ex) {
            Logger.getLogger(ReadCSVMatrix.class.getName()).log(Level.SEVERE, null, ex);
        }
        Integer rowDimension = matrixElementList.stream().max(Comparator.comparing(m->m.getRow())).get().getRow();
        Integer columnDimension = matrixElementList.stream().max(Comparator.comparing(m->m.getColumn())).get().getColumn();
        //System.out.println(rowDimension*columnDimension);
        System.out.println("Matrix of size "+(rowDimension+1)+" X "+(columnDimension+1));
        Double csvArray[][] = new Double[rowDimension+1][columnDimension+1];
        for (MatrixElement  matrixElement: matrixElementList) {
            csvArray[matrixElement.getRow()][matrixElement.getColumn()]= matrixElement.getCellValue();
        }
        csvMatrix = new Array2DRowRealMatrix(rowDimension+1,columnDimension+1);
        for (MatrixElement  matrixElement: matrixElementList) {
            csvMatrix.setEntry(matrixElement.getRow(), matrixElement.getColumn(), matrixElement.getCellValue());
        }
        randomWalkMatix = new InputMatrix(csvMatrix);
        return randomWalkMatix;
    }

}
