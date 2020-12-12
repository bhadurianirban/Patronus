/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.patronus.fluctuations.driver;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.patronus.fluctuations.Fluc1D.InputProcess.ReadData;
import org.patronus.fluctuations.Fluc2D.InputProcess.Image;
import org.patronus.fluctuations.Fluc2D.InputProcess.ReadCSVMatrix;
import org.patronus.fluctuations.Fluc2D.InputProcess.ReadImage;
import org.patronus.fluctuations.FlucX.InputProcess.ReadDataX;
import org.patronus.fluctuations.core.Fluctuations;
import org.patronus.fluctuations.data.QScaleFluctuations;
import org.patronus.fluctuations.results.MFDFAResults;

/**
 *
 * @author dgrfi
 */
public class ExecuteFunctions {

    public static void runMFDFA1DForFolder(File folder) {
        File fileList[] = folder.listFiles();
        List<File> filesInFolder = Arrays.stream(fileList).filter(s -> s.getName().contains(".csv")).collect(Collectors.toList());
        if (filesInFolder.isEmpty()) {
            System.out.println("There are no csv data files in folder");
            System.exit(0);
        }
        System.out.println(getHeader());
        filesInFolder.forEach(f -> runMFDFA1DForFile(f, false));
    }

    public static void runMFDFA1DForFile(File inputData, boolean header) {
        Fluctuations f = new ReadData(inputData).getInputDataSeries().getFluctuations1D();
        MFDFAResults results = f.getMFDFAResults();
        if (header) {
            System.out.println(getHeader());
        }
        System.out.println(inputData.getName() + "," + results.toString());
    }

    public static void runMFDFA1DForFileDetail(File inputData) {
        if (!(inputData.getName().toLowerCase().endsWith(".csv"))) {
            System.out.println("Input file should be data file with .csv extension.");
            System.exit(0);
        }
        Fluctuations f = new ReadData(inputData).getInputDataSeries().getFluctuations1D();
        MFDFAResults results = f.getMFDFAResults();
        System.out.println("Scale,Fluctuation");
        results.getScaleMappedFluctuationsList().forEach(scm -> System.out.println(scm.getLogOfScale() + "," + scm.getLogOfQuadraticMeanOfFluctuations()));
        System.out.println("Hq,Dq");
        results.getMultiFractalSpectrumList().forEach(scm -> System.out.println(scm.getHq() + "," + scm.getDq()));

    }

    public static void runMFDFA1DForFileQDetail(File inputData) {
        if (!(inputData.getName().toLowerCase().endsWith(".csv"))) {
            System.out.println("Input file should be data file with .csv extension.");
            System.exit(0);
        }
        Fluctuations f = new ReadData(inputData).getInputDataSeries().getFluctuations1D();
        MFDFAResults results = f.getMFDFAResults();
        //List<Double> qLinspaceList = results.getqLinSpqceList();
        String fileName = inputData.getName();
        System.out.println("file,q,Scale,Fluctuation,HurrstQ,Intercept,Rsquare,SE");
        List<QScaleFluctuations> qScaleFluctuationsList = results.getqScaleFluctuationsList();
        qScaleFluctuationsList.stream().forEach(qsf -> System.out.println(fileName + ","
                + qsf.getQ() + ","
                + qsf.getLogOfScale() + ","
                + qsf.getLogOfQPoweredMeanOfFluctuations() + ","
                + qsf.getHurstExponentForQ() + ","
                + qsf.getHurstExponentInterceptForQ() + ","
                + qsf.getHurstExponentRSquareForQ() + ","
                + qsf.getHurstExponentSEForQ()
        ));
//        System.out.println("q,Scale,Fluctuation");
//        qLinspaceList.stream().forEach(q -> {
//            results.getScaleMappedFluctuationsList().stream().forEach(scmf -> System.out.println(q + "," + scmf.getLogOfScale() + "," + scmf.getLogOfQPoweredMeanOfFluctuations(q)));
//        });

    }

    public static void runMFDXAForFile(File inputDataFirst, File inputDataSecond) {
        Fluctuations f = new ReadDataX(inputDataFirst, inputDataSecond).getInputDataSeries().getFluctuationsX();
        MFDFAResults results = f.getMFDFAResults();
        System.out.println("File," + getHeader());
        System.out.println(inputDataFirst.getName() + "," + inputDataSecond.getName() + "," + results.toString());
    }

//    public static void runMFDXAQForFile(File inputDataFirst, File inputDataSecond) {
//        Fluctuations f = new ReadDataX(inputDataFirst, inputDataSecond).getInputDataSeries().getFluctuationsX();
//        MFDFAResults results = f.getQScaleResults();
//        List<Double> qLinspaceList = results.getqLinSpqceList();
//        String fileName = inputDataFirst.getName() + "," + inputDataSecond.getName();
//        System.out.println("file-1,file-2,q,Scale,Fluctuation");
//        qLinspaceList.stream().forEach(q -> {
//            results.getScaleMappedFluctuationsList().stream().forEach(scmf -> System.out.println(fileName + "," + q + "," + scmf.getLogOfScale() + "," + scmf.getLogOfQPoweredMeanOfFluctuations(q)));
//        });
//    }
    public static void runMFDXAQForFile(File inputDataFirst, File inputDataSecond) {
        Fluctuations f = new ReadDataX(inputDataFirst, inputDataSecond).getInputDataSeries().getFluctuationsX();
        String fileName = inputDataFirst.getName() + "," + inputDataSecond.getName();
        System.out.println("file-1,file-2,q,Scale,Fluctuation,HurrstQ,Intercept,Rsquare,SE");
        MFDFAResults results = f.getMFDFAResults();
        List<QScaleFluctuations> qScaleFluctuationsList = results.getqScaleFluctuationsList();
        qScaleFluctuationsList.stream().forEach(qsf -> System.out.println(fileName + ","
                + qsf.getQ() + ","
                + qsf.getLogOfScale() + ","
                + qsf.getLogOfQPoweredMeanOfFluctuations() + ","
                + qsf.getHurstExponentForQ() + ","
                + qsf.getHurstExponentInterceptForQ() + ","
                + qsf.getHurstExponentRSquareForQ() + ","
                + qsf.getHurstExponentSEForQ()
        ));
    }

//    public static void runMFDXAHQForFile(File inputDataFirst, File inputDataSecond) {
//        Fluctuations f = new ReadDataX(inputDataFirst, inputDataSecond).getInputDataSeries().getFluctuationsX();
//        String fileName = inputDataFirst.getName() + "," + inputDataSecond.getName();
//        System.out.println("file-1,file-2,q,Scale,Fluctuation");
//        MFDFAResults results = f.getQScaleResults();
//        List<QScaleFluctuations> qScaleFluctuationsList = results.getqScaleFluctuationsList();
//        qScaleFluctuationsList.stream().forEach(qsf -> System.out.println(fileName + "," + qsf.getQ() + "," + qsf.getLogOfScale() + "," + qsf.getLogOfQPoweredMeanOfFluctuations()));
//    }
    public static void runMFDFA2DForImageFolder(File folder) {
        File fileList[] = folder.listFiles();
        List<File> filesInFolder = Arrays.stream(fileList).filter(s -> s.getName().toLowerCase().contains(".jpg")).collect(Collectors.toList());
        if (filesInFolder.isEmpty()) {
            System.out.println("There are no image files in folder");
            System.exit(0);
        }
        System.out.println("Color," + getHeader());
        filesInFolder.forEach(f -> runMFDFA2DForImageFileForAllColor(f, false));
    }

    public static void runMFDFA2DForCSVFolder(File folder) {
        File fileList[] = folder.listFiles();
        List<File> filesInFolder = Arrays.stream(fileList).filter(s -> s.getName().toLowerCase().contains(".csv")).collect(Collectors.toList());
        if (filesInFolder.isEmpty()) {
            System.out.println("There are no csv files in folder");
            System.exit(0);
        }
        System.out.println(getHeader());
        filesInFolder.forEach(f -> runMFDFA2DForCSV(f, false));
    }

    private static void runMFDFA2DForImageFileForAColor(File inputData, boolean header, Image.COLOR color) {
        //new ReadImage(inputData).getInputMatrix(color).printOrigMatrix();
        Fluctuations f = new ReadImage(inputData).getInputMatrix(color).getFluctuations2D(Boolean.TRUE);
        MFDFAResults results = f.getMFDFAResults();
        if (header) {
            System.out.println("Color," + getHeader());
        }
        System.out.println(color.name() + "," + inputData.getName() + "," + results.toString());
    }

    public static void runMFDFA2DForCSV(File inputData, boolean header) {
        //new ReadCSVMatrix(inputData).getInputMatrix().printOrigMatrix();
        //new ReadImage(inputData).writeMatrixToCSV("/home/bhaduri/MEGA/DGRFFractal/testdata/DFA2D/Gheu.csv", Image.COLOR.BLUE);
        Fluctuations f = new ReadCSVMatrix(inputData).getInputMatrix().getFluctuations2D(Boolean.TRUE);
        MFDFAResults results = f.getMFDFAResults();
        if (header) {
            System.out.println(getHeader());
        }
        System.out.println(inputData.getName() + "," + results.toString());
    }

    private static void runMFDFA2DForImageFileDetailsForAColor(File inputData, Image.COLOR color) {
        Fluctuations f = new ReadImage(inputData).getInputMatrix(color).getFluctuations2D(Boolean.TRUE);
        MFDFAResults results = f.getMFDFAResults();
        System.out.println("Scale,Fluctuation");
        results.getScaleMappedFluctuationsList().forEach(scm -> System.out.println(color.name() + "," + scm.getLogOfScale() + "," + scm.getLogOfQuadraticMeanOfFluctuations()));
        System.out.println("Hq,Dq");
        results.getMultiFractalSpectrumList().forEach(scm -> System.out.println(color.name() + "," + scm.getHq() + "," + scm.getDq()));
    }

    private static void runMFDFA2DForImageFileDetailsQForAColor(File inputData, Image.COLOR color) {
        Fluctuations f = new ReadImage(inputData).getInputMatrix(color).getFluctuations2D(Boolean.TRUE);
//        MFDFAResults results = f.getQScaleResults();
//        List<Double> qLinspaceList = results.getqLinSpqceList();
        MFDFAResults results = f.getMFDFAResults();
        String fileName = inputData.getName();
        System.out.println("file,Color,q,Scale,Fluctuation,HurrstQ,Intercept,Rsquare,SE");
        List<QScaleFluctuations> qScaleFluctuationsList = results.getqScaleFluctuationsList();
        qScaleFluctuationsList.stream().forEach(qsf -> System.out.println(fileName + ","
                + color.name() + ","
                + qsf.getQ() + ","
                + qsf.getLogOfScale() + ","
                + qsf.getLogOfQPoweredMeanOfFluctuations() + ","
                + qsf.getHurstExponentForQ() + ","
                + qsf.getHurstExponentInterceptForQ() + ","
                + qsf.getHurstExponentRSquareForQ() + ","
                + qsf.getHurstExponentSEForQ()
        ));
//        System.out.println("Color,q,Scale,Fluctuation");
//        qLinspaceList.stream().forEach(q -> {
//            results.getScaleMappedFluctuationsList().stream().forEach(scmf -> System.out.println(color.name() + "," + q + "," + scmf.getLogOfScale() + "," + scmf.getLogOfQPoweredMeanOfFluctuations(q)));
//        });
    }

    private static void runMFDFA2DDetailsForCSV(File inputData) {
        Fluctuations f = new ReadCSVMatrix(inputData).getInputMatrix().getFluctuations2D(Boolean.TRUE);
        MFDFAResults results = f.getMFDFAResults();
        System.out.println("Scale,Fluctuation");
        results.getScaleMappedFluctuationsList().forEach(scm -> System.out.println(scm.getLogOfScale() + "," + scm.getLogOfQuadraticMeanOfFluctuations()));
        System.out.println("Hq,Dq");
        results.getMultiFractalSpectrumList().forEach(scm -> System.out.println(scm.getHq() + "," + scm.getDq()));
    }

    public static void runMFDFA2DDetailsQForCSVFile(File inputData) {
        if (!(inputData.getName().toLowerCase().endsWith(".csv"))) {
            System.out.println("Input file should be data file with .csv extension.");
            System.exit(0);
        }
        Fluctuations f = new ReadCSVMatrix(inputData).getInputMatrix().getFluctuations2D(Boolean.TRUE);
//        MFDFAResults results = f.getQScaleResults();
//        List<Double> qLinspaceList = results.getqLinSpqceList();
//        System.out.println("q,Scale,Fluctuation");
//        qLinspaceList.stream().forEach(q -> {
//            results.getScaleMappedFluctuationsList().stream().forEach(scmf -> System.out.println(q + "," + scmf.getLogOfScale() + "," + scmf.getLogOfQPoweredMeanOfFluctuations(q)));
//        });
        MFDFAResults results = f.getMFDFAResults();
        String fileName = inputData.getName();
        System.out.println("file,q,Scale,Fluctuation,HurrstQ,Intercept,Rsquare,SE");
        List<QScaleFluctuations> qScaleFluctuationsList = results.getqScaleFluctuationsList();
        qScaleFluctuationsList.stream().forEach(qsf -> System.out.println(fileName + ","
                + qsf.getQ() + ","
                + qsf.getLogOfScale() + ","
                + qsf.getLogOfQPoweredMeanOfFluctuations() + ","
                + qsf.getHurstExponentForQ() + ","
                + qsf.getHurstExponentInterceptForQ() + ","
                + qsf.getHurstExponentRSquareForQ() + ","
                + qsf.getHurstExponentSEForQ()
        ));
    }

    private static String getHeader() {
        String s = "File,hurstExponent,hurstExponentSE,hurstExponentRSquare,hurstExponentChiSquare,gammaX,multiFractalSpectrumWidth";
        return s;
    }

    public static void runMFDFA2DForImageFileDetailsForAllColor(File inputData) {
        if (!(inputData.getName().toLowerCase().endsWith(".jpg"))) {
            System.out.println("Input file should be image file with .jpg extension.");
            System.exit(0);
        }
        runMFDFA2DForImageFileDetailsForAColor(inputData, Image.COLOR.RED);
        runMFDFA2DForImageFileDetailsForAColor(inputData, Image.COLOR.GREEN);
        runMFDFA2DForImageFileDetailsForAColor(inputData, Image.COLOR.BLUE);
    }

    public static void runMFDFA2DForImageFileDetailsQForAllColor(File inputData) {
        if (!(inputData.getName().toLowerCase().endsWith(".jpg"))) {
            System.out.println("Input file should be image file with .jpg extension.");
            System.exit(0);
        }
        runMFDFA2DForImageFileDetailsQForAColor(inputData, Image.COLOR.RED);
        runMFDFA2DForImageFileDetailsQForAColor(inputData, Image.COLOR.GREEN);
        runMFDFA2DForImageFileDetailsQForAColor(inputData, Image.COLOR.BLUE);
    }

    public static void runMFDFA2DDetailsForCSVFile(File inputData) {
        if (!(inputData.getName().toLowerCase().endsWith(".csv"))) {
            System.out.println("Input file should be data file with .csv extension.");
            System.exit(0);
        }
        runMFDFA2DDetailsForCSV(inputData);
    }

    public static void runMFDFA2DForImageFileForAllColor(File inputData, boolean header) {
        if (header) {
            System.out.println("Color," + getHeader());
        }
        runMFDFA2DForImageFileForAColor(inputData, false, Image.COLOR.RED);
        runMFDFA2DForImageFileForAColor(inputData, false, Image.COLOR.GREEN);
        runMFDFA2DForImageFileForAColor(inputData, false, Image.COLOR.BLUE);
    }

    public static boolean folderFileExists(String folderFilePath) {
        File f = new File(folderFilePath);
        return f.exists();
    }

    public static boolean isFolder(String folderFilePath) {
        File f = new File(folderFilePath);
        return f.isDirectory();
    }

}
