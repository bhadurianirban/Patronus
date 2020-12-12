/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.patronus.fluctuations.Fluc1D.InputProcess;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.patronus.fluctuations.core.Fluctuations;
import org.patronus.fluctuations.core.Fluctuations1D;

/**
 *
 * @author dgrfi
 */
public class InputDataSeries {
    private final List<Double> inputDataSeries;
    private List<Double> cumulativeDataSeries;
    public InputDataSeries(List<Double> inputDataSeries) {
        this.inputDataSeries = inputDataSeries;
    }

    public List<Double> getInputDataSeries() {
        return inputDataSeries;
    }
    
    private void cumulateDataSeries() {
        Double mean = inputDataSeries.stream().mapToDouble(a->a).average().getAsDouble();
        List<Double> meanSubtractedDataSeries = inputDataSeries.stream().map(m->(m-mean)).collect(Collectors.toList());
        Double cumval =meanSubtractedDataSeries.get(0);
        cumulativeDataSeries = new ArrayList<>();
        cumulativeDataSeries.add(cumval);
        for (int i=1;i<meanSubtractedDataSeries.size();i++) {
            cumval= cumval+meanSubtractedDataSeries.get(i);
            cumulativeDataSeries.add(cumval);
        }
    }

    public List<Double> getCumulativeDataSeries() {
        cumulateDataSeries();
        return cumulativeDataSeries;
    }
    public Fluctuations getFluctuations1D() {
        cumulateDataSeries();
        Fluctuations fd = new Fluctuations1D(cumulativeDataSeries);
        return fd;
    }

    
}
