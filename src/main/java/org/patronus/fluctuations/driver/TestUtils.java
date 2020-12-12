/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.patronus.fluctuations.driver;



import org.patronus.fluctuations.Fluc1D.InputProcess.ReadData;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.patronus.fluctuations.Fluc2D.InputProcess.Image;
import org.patronus.fluctuations.Fluc2D.InputProcess.Image.COLOR;
import org.patronus.fluctuations.Fluc2D.InputProcess.ReadImage;
import org.patronus.fluctuations.FlucX.InputProcess.ReadDataX;
import org.patronus.fluctuations.core.Fluctuations;
import org.patronus.fluctuations.core.FluctuationsX;
import org.patronus.fluctuations.results.MFDFAResults;
import org.patronus.fluctuations.data.MatrixCoordinate;



/**
 *
 * @author dgrfi
 */
public class TestUtils {
//    public static void cumulateSeries(String inputFilePath) {
//        File inputDataFile = new File(inputFilePath);
//        List <Double> cumulativeDataSeries = new ReadData(inputDataFile).getInputDataSeries().getCumulativeDataSeries();
//        for (int i=0;i<cumulativeDataSeries.size();i++) {
//            System.out.println(cumulativeDataSeries.get(i));
//        }
//    }

    public static void getScales(String inputFilePath) {
        File inputDataFile = new File(inputFilePath);
        List<Integer> scales = new ReadData(inputDataFile).getInputDataSeries().getFluctuations1D().getScales();
        scales.forEach(System.out::println);
    }

    public static void getScales2D(String inputFilePath) {
        File inputDataFile = new File(inputFilePath);
        List<MatrixCoordinate> scales = new ReadImage(inputDataFile).getInputMatrix(COLOR.RED).getFluctuations2D(Boolean.FALSE).getScales();
        scales.forEach(s -> System.out.println(s.getRow() + "," + s.getColumn()));
    }
//    public static void getFluctuations(String inputFilePath) {
//        File inputDataFile = new File(inputFilePath);
//        new ReadData(inputDataFile).getInputDataSeries().getFD().getFluctutations();
//    }

    public static void getMF(String inputFilePath) {
        File inputDataFile = new File(inputFilePath);
        Fluctuations fq = new ReadData(inputDataFile).getInputDataSeries().getFluctuations1D();
        MFDFAResults mfdfaResults = fq.getMFDFAResults();
        //mfdfaResults.getMultiFractalSpectrumList().stream().forEach(mf->System.out.println(mf.getHq()+","+mf.getDq()));
        System.out.println(mfdfaResults.toString());
    }

    public static void getMF2D(String inputFilePath) {
        File inputDataFile = new File(inputFilePath);
        Fluctuations fq = new ReadImage(inputDataFile).getInputMatrix(COLOR.RED).getFluctuations2D(Boolean.TRUE);
        MFDFAResults mfdfaResults = fq.getMFDFAResults();
        //mfdfaResults.getMultiFractalSpectrumList().stream().forEach(mf->System.out.println(mf.getHq()+","+mf.getDq()));
        System.out.println(mfdfaResults.toString());
    }

    public static void getMFXD(String inputFilePathFirst, String inputFilePathSecond) {
        File inputDataFileFirst = new File(inputFilePathFirst);
        File inputDataFileSecond = new File(inputFilePathSecond);

        Fluctuations fq = new ReadDataX(inputDataFileFirst,inputDataFileSecond).getInputDataSeries().getFluctuationsX();
        MFDFAResults mfdfaResults = fq.getMFDFAResults();
        //mfdfaResults.getMultiFractalSpectrumList().stream().forEach(mf->System.out.println(mf.getHq()+","+mf.getDq()));
        System.out.println(mfdfaResults.toString());

    }

//    public static void testOLD(String inputFilePath) {
//        File inputDataFile = new File(inputFilePath);
//        List<Double> inputDataSeries = new ReadData(inputDataFile).getInputDataSeries().getInputDataSeries();
//        List<Double> cumulativeSeries = TimeSeriesUtil.getCumTimeSeries(new ArrayList(inputDataSeries));
//        CalculateFq FqVal = new CalculateFq(1024.0, 16.0, 19, cumulativeSeries);
//        secondOrderRMS[] secondOrderRMS = FqVal.getFVector();
//        List<secondOrderRMS> secondOrderRMSList = Arrays.asList(secondOrderRMS);
//        secondOrderRMSList.stream().forEach(m -> System.out.println(m.getLogOfSliceSize() + "," + m.getLogOfSliceSecondOrderRMS()));
//
//    }

//    public static void testDXAOLD(String inputFilePathFirst, String inputFilePathSecond) {
//        File inputDataFileFirst = new File(inputFilePathFirst);
//        List<Double> inputDataSeriesFirst = new ReadData(inputDataFileFirst).getInputDataSeries().getInputDataSeries();
//        List<Double> cumulativeSeriesFirst = TimeSeriesUtil.getCumTimeSeries(new ArrayList(inputDataSeriesFirst));
//
//        File inputDataFileSecond = new File(inputFilePathSecond);
//        List<Double> inputDataSeriesSecond = new ReadData(inputDataFileSecond).getInputDataSeries().getInputDataSeries();
//        List<Double> cumulativeSeriesSecond = TimeSeriesUtil.getCumTimeSeries(new ArrayList(inputDataSeriesSecond));
//        CalculateMFDXAFq FqVal = new CalculateMFDXAFq(1024.0, 16.0, 19, cumulativeSeriesFirst, cumulativeSeriesSecond);
//        CalculateMFDXAFD FDVal = new CalculateMFDXAFD(FqVal.getFVector(), 0.7);
//
//        //Ending MFDFA Calculation...................
//        //Retrieve MFDFA Calculation results................
//        Double hurstExponent = FDVal.getHurstExp();
//        Double hurstExponentSE = FDVal.getHurstExpSE();
//        Double rSquaredVal = FDVal.getHurstExpRSquare();
//        Double chiSquaredVal = FDVal.getMFDXAChiSquareVal();
//        Double gammaX = FDVal.getGammaX();
//        
//        System.out.println("hurstExponent"+hurstExponent);
//        System.out.println("hurstExponentSE"+hurstExponentSE);
//        System.out.println("rSquaredVal"+rSquaredVal);
//        System.out.println("chiSquaredVal"+chiSquaredVal);
//        System.out.println("gammaX"+gammaX);
//    }
}
