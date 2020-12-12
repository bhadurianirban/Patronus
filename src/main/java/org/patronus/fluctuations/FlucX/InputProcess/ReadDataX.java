/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.patronus.fluctuations.FlucX.InputProcess;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author dgrfi
 */
public class ReadDataX {

    private final File dataSeriesFileFirst;
    private final File dataSeriesFileSecond;

    public ReadDataX(File dataSeriesFileFirst, File dataSeriesFileSecond) {
        this.dataSeriesFileFirst = dataSeriesFileFirst;
        this.dataSeriesFileSecond = dataSeriesFileSecond;
    }

    public InputDataSeriesX getInputDataSeries() {
        List<Double> inputDataSeriesFirst = new ArrayList<>();
        try {
            BufferedReader bf = new BufferedReader(new FileReader(dataSeriesFileFirst));
            String line;
            while ((line = bf.readLine()) != null) {
                inputDataSeriesFirst.add(Double.parseDouble(line));
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ReadDataX.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ReadDataX.class.getName()).log(Level.SEVERE, null, ex);
        }
        List<Double> inputDataSeriesSecond = new ArrayList<>();
        try {
            BufferedReader bf = new BufferedReader(new FileReader(dataSeriesFileSecond));
            String line;
            while ((line = bf.readLine()) != null) {
                inputDataSeriesSecond.add(Double.parseDouble(line));
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ReadDataX.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ReadDataX.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new InputDataSeriesX(inputDataSeriesFirst,inputDataSeriesSecond);
    }

}
