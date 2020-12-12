/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.patronus.fluctuations.driver;

import java.io.File;

/**
 *
 * @author dgrfi
 */
public class RunAlone {

    public static void main(String args[]) {
//        String inputFilePath = "/home/dgrfi/MEGA/DGRFFractal/testdata/tanpurasm.csv";
//        //String imageFilePath = "/home/dgrfi/MEGA/DGRFFractal/testdata/DFA2D/radha.jpg";
//        String imageFilePath = "/home/bhaduri/MEGA/Paper/2DRaash/dancefacegray/01-1.jpg";
//        String dxaInputFirst = "/home/bhaduri/MEGA/DGRFFractal/testdata/C1.preECG.csv";
//        String dxaInputSecond = "/home/bhaduri/MEGA/DGRFFractal/testdata/C1.medECG.csv";
//
//        TestUtils.getMFXD(dxaInputFirst, dxaInputSecond);
        //TestUtils.testOLD(inputFilePath);
        parseAndExecute(args);
    }

    private static void parseAndExecute(String args[]) {
        if (args.length ==0 ) {
            System.out.println("No arguments provided. Type -h for help.");
            System.exit(0);
        }
        CLIParser clip = new CLIParser(args);
        
        boolean help = clip.switchPresent("-h");
        if (help) {
            System.out.println("java -jar MFDFA.jar -a [1D|2D|2DM|X] [-d] file1 [file 2:in case of X]");
            System.out.println("Examples:");
            System.out.println("-a X file1.csv file2.csv {Multifractal Cross correlation between file1 and file2}\n"
                    + "-a X -q file1.csv file2.csv {Multifractal Cross correlation Scale vs Fluctuations values for all q values}\n"
                    + "-a 1D file.csv {Multifractal spectrum width and Husrt Exponent for file.csv}\n"
                    + "-a 1D folder  {Multifractal spectrum widtdh and Hurst Exponent for all csv files in folder [folder]}\n"
                    + "-a 1D -d file.csv {Scale vs Fluctuations values(Hurst Exponent) and Tq vs Dq values (Multifractal spectrum) for file.csv}\n"
                    + "-a 1D -q file.csv {Scale vs Fluctuations values for all q values (-5 to 5 with gap of .1) for file.csv}\n"
                    + "-a 2D folder {2 Dimensional Multifractal spectrum width and Hurst Exponent for all jpg image files in folder [folder]}\n"
                    + "-a 2D file.jpg {(Hurst Exponent) (Multifractal spectrum width) for file.jpg}\n"
                    + "-a 2D -d file.jpg {2 Dimensional Scale vs Fluctuations values(Hurst Exponent) and Tq vs Dq values (Multifractal spectrum) for file.jpg}\n"
                    + "-a 2D -q file.jpg {2 Dimensional Scale vs Fluctuations values for all q values (-5 to 5 with gap of .1) for file.jpg}\n"
                    + "-a 2DM folder {2 Dimensional Multifractal spectrum width and Hurst Exponent for all csv files in folder [folder]}\n"
                    + "-a 2DM file.csv {(Hurst Exponent) (Multifractal spectrum width) for file.csv}\n"
                    + "-a 2DM -d file.csv {2 Dimensional Scale vs Fluctuations values(Hurst Exponent) and Tq vs Dq values (Multifractal spectrum) for file.csv}\n"
                    + "-a 2DM -q file.csv {2 Dimensional Scale vs Fluctuations values for all q values (-5 to 5 with gap of .1) for file.csv}\n"
                    + "matrix for csv file should be organised [row,col,cellvalue]\n");
            System.exit(0);
        }
        String analysisType = clip.switchValue("-a");
        if (!analysisType.matches("1D|2D|2DM|X")) {
            System.out.println("Must specify proper analysis type [1D|2D|X].");
            System.exit(0);
        }
        boolean details = clip.switchPresent("-d");
        boolean qdetails = clip.switchPresent("-q");
        boolean hqdetails = clip.switchPresent("-h");
        if (hqdetails) {
            if (!qdetails) {
                System.out.println("-q must be specified for calculation Hurst exponent for each q.");
                System.exit(0);
            }
        }

        String[] inputFileFolder = clip.targets();
        if (inputFileFolder.length == 0) {
            System.out.println("Must specify the target file or folder.");
            System.exit(0);
        }
        if (!ExecuteFunctions.folderFileExists(inputFileFolder[0])) {
            System.out.println("Input file " + inputFileFolder[0] + " does not exist");
            System.exit(0);
        }
        if (analysisType.equals("X")) {
            if (inputFileFolder.length < 2) {
                System.out.println("Must specify the 2 target files.");
                System.exit(0);
            }
            if (!ExecuteFunctions.folderFileExists(inputFileFolder[1])) {
                System.out.println("Input file " + inputFileFolder[1] + " does not exist");
                System.exit(0);
            }
            if (ExecuteFunctions.isFolder(inputFileFolder[0])) {
                System.out.println("Input file " + inputFileFolder[0] + " should not be folder ");
                System.exit(0);
            }
            if (ExecuteFunctions.isFolder(inputFileFolder[1])) {
                System.out.println("Input file " + inputFileFolder[1] + " should not be folder ");
                System.exit(0);
            }
            if (qdetails) {
                ExecuteFunctions.runMFDXAQForFile(new File(inputFileFolder[0]), new File(inputFileFolder[1]));
            } else {
                ExecuteFunctions.runMFDXAForFile(new File(inputFileFolder[0]), new File(inputFileFolder[1]));
            }
        }
        if (details) {
            if (ExecuteFunctions.isFolder(inputFileFolder[0])) {
                System.out.println("Input files should not be folder");
                System.exit(0);
            }
        }
        if (qdetails) {
            if (ExecuteFunctions.isFolder(inputFileFolder[0])) {
                System.out.println("Input files should not be folder");
                System.exit(0);
            }
        }
        switch (analysisType) {
            case "1D":
                if (details) {
                    ExecuteFunctions.runMFDFA1DForFileDetail(new File(inputFileFolder[0]));
                } else if (qdetails) {
                    ExecuteFunctions.runMFDFA1DForFileQDetail(new File(inputFileFolder[0]));
                } else {
                    if (ExecuteFunctions.isFolder(inputFileFolder[0])) {
                        ExecuteFunctions.runMFDFA1DForFolder(new File(inputFileFolder[0]));
                    } else {
                        ExecuteFunctions.runMFDFA1DForFile(new File(inputFileFolder[0]), true);
                    }
                }
                break;
            case "2D":
                if (details) {
                    ExecuteFunctions.runMFDFA2DForImageFileDetailsForAllColor(new File(inputFileFolder[0]));
                } else if (qdetails) {
                    ExecuteFunctions.runMFDFA2DForImageFileDetailsQForAllColor(new File(inputFileFolder[0]));
                }else {
                    if (ExecuteFunctions.isFolder(inputFileFolder[0])) {
                        ExecuteFunctions.runMFDFA2DForImageFolder(new File(inputFileFolder[0]));
                    } else {
                        ExecuteFunctions.runMFDFA2DForImageFileForAllColor(new File(inputFileFolder[0]), true);
                    }
                }
                break;
            case "2DM":
                if (details) {
                    ExecuteFunctions.runMFDFA2DDetailsForCSVFile(new File(inputFileFolder[0]));
                } else if (qdetails) {
                    ExecuteFunctions.runMFDFA2DDetailsQForCSVFile(new File(inputFileFolder[0]));
                }else {
                    if (ExecuteFunctions.isFolder(inputFileFolder[0])) {
                        ExecuteFunctions.runMFDFA2DForCSVFolder(new File(inputFileFolder[0]));
                    } else {
                        ExecuteFunctions.runMFDFA2DForCSV(new File(inputFileFolder[0]),true);
                    }
                }
                break;
            default:
                break;
        }

    }
}
