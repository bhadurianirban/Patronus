/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.patronus.fluctuations.Fluc1D.InputProcess;

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
public class ReadData {
    private final File dataSeriesFile;

    public ReadData(File dataSeriesFile) {
        this.dataSeriesFile = dataSeriesFile;
    }
    
    public InputDataSeries getInputDataSeries() {
        List<Double> inputDataSeries = new ArrayList<>();
        try {
            BufferedReader bf = new BufferedReader(new FileReader(dataSeriesFile));
            String line;
            while ((line = bf.readLine()) != null) {
                inputDataSeries.add(Double.parseDouble(line));
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ReadData.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ReadData.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new InputDataSeries(inputDataSeries);
    }
    
}
