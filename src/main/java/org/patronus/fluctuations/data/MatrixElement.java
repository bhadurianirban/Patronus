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
public class MatrixElement extends MatrixCoordinate {
    private Double cellValue;
    public MatrixElement(Integer row, Integer column,Double cellValue) {
        super(row, column);
        this.cellValue = cellValue;
    }

    public Double getCellValue() {
        return cellValue;
    }

    public void setCellValue(Double cellValue) {
        this.cellValue = cellValue;
    }
    
}
