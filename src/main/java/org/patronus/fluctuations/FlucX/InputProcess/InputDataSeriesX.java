/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.patronus.fluctuations.FlucX.InputProcess;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.patronus.fluctuations.core.Fluctuations;
import org.patronus.fluctuations.core.Fluctuations1D;
import org.patronus.fluctuations.core.FluctuationsX;

/**
 *
 * @author dgrfi
 */
public class InputDataSeriesX {
    private final List<Double> inputDataSeriesFirst;
    private final List<Double> inputDataSeriesSecond;
    private List<Double> cumulativeDataSeriesFirst;
    private List<Double> cumulativeDataSeriesSecond;

    public InputDataSeriesX(List<Double> inputDataSeriesFirst, List<Double> inputDataSeriesSecond) {
        this.inputDataSeriesFirst = inputDataSeriesFirst;
        this.inputDataSeriesSecond = inputDataSeriesSecond;
    }
    

    private List<Double> cumulateDataSeries(List<Double> inputDataSeries) {
        Double mean = inputDataSeries.stream().mapToDouble(a->a).average().getAsDouble();
        List<Double> meanSubtractedDataSeries = inputDataSeries.stream().map(m->(m-mean)).collect(Collectors.toList());
        Double cumval =meanSubtractedDataSeries.get(0);
        List<Double> cumulativeDataSeries = new ArrayList<>();
        cumulativeDataSeries.add(cumval);
        for (int i=1;i<meanSubtractedDataSeries.size();i++) {
            cumval= cumval+meanSubtractedDataSeries.get(i);
            cumulativeDataSeries.add(cumval);
        }
        return cumulativeDataSeries;
    }

    
    public Fluctuations getFluctuationsX() {
        cumulativeDataSeriesFirst = cumulateDataSeries(inputDataSeriesFirst);
        cumulativeDataSeriesSecond = cumulateDataSeries(inputDataSeriesSecond);
        Fluctuations fd = new FluctuationsX(cumulativeDataSeriesFirst,cumulativeDataSeriesSecond);
        return fd;
    }

    
}
