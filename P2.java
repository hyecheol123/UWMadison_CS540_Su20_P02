///////////////////////////////// FILE HEADER /////////////////////////////////
//
// Title:           UWMadison_CS540_Su20_P02
// This File:       P2.java
// Files:           Dataset.java, P2.java
// External Class:  None
//
// GitHub Repo:    https://github.com/hyecheol123/UWMadison_CS540_Su20_P02
//
// Author
// Name:            Hyecheol (Jerry) Jang
// Email:           hyecheol.jang@wisc.edu
// Lecturer's Name: Young Wu
// Course:          CS540 (LEC 002 / Epic), Summer 2020
//
///////////////////////////// OUTSIDE REFERENCE  //////////////////////////////
//
// List of Outside Reference
//   1.
//
////////////////////////////////// KNOWN BUGS /////////////////////////////////
//
// List of Bugs
//   1.
//
/////////////////////////////// 80 COLUMNS WIDE //////////////////////////////

import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Scanner;

/**
 * Main class for P2
 */
public class P2 {
  /**
   * Main method of P2
   * 
   * @param args Command Line Arguments (CLAs)
   * @throws IOException Occurred when I/O Operation Interrupted
   */
  public static void main(String[] args) throws IOException {
    // Get netID and result file location
    Scanner consoleScnr = new Scanner(System.in);
    System.out.print("Enter Result File Location(Name): ");
    String resultFileLoc = consoleScnr.nextLine();
    System.out.println("Need NetID to properly format result file");
    System.out.print("Enter Your UWMadison NetID: ");
    String netID = consoleScnr.nextLine();
    consoleScnr.close();
    // Initialize resultFileWriter
    BufferedWriter resultFileWriter = new BufferedWriter(new FileWriter(new File(resultFileLoc)));
    // Format header of result file
    resultFileWriter.append("Outputs:\n@id\n" + netID + "\n");
    resultFileWriter.flush();

    // Retrieve Datasets
    Dataset dataset = new Dataset("breast-cancer-wisconsin.data", "test.txt");
    
    // Q1: total number of positive and negative instances in the training set
    resultFileWriter.append("@answer_1\n");
    resultFileWriter.append(dataset.getLabelCountsString() + "\n");
    resultFileWriter.flush();

    // Q2: initial entropy at the root before the split (4 decimal places)
    resultFileWriter.append("@answer_2\n");
    resultFileWriter.append(String.format("%.4f\n", binaryEntropy(dataset.getLabelCounts())));
    resultFileWriter.flush();

    // Close resultFileWriter
    resultFileWriter.close();
  }

  /**
   * calcuate binary entropy
   * 
   * @param p0 probability for 1st label
   * @return binary entropy
   */
  private static double binaryEntropy(double p0) {
    if (p0 == 0 || p0 == 1) { // very certain cases
      return 0;
    } else {
      double p1 = 1 - p0; // 2nd label probability
      return -(p0 * (Math.log(p0) / Math.log(2)) + p1 * (Math.log(p1) / Math.log(2)));
    }
  }

  /**
   * calculate binary entropy
   * 
   * @param labelCounts Integer array contains number of each cases appears at the node
   * @return binary entropy
   */
  private static double binaryEntropy(Integer[] labelCounts) {
    if(labelCounts[0] == 0 | labelCounts[1] == 0) { // very certain cases
      return 0;
    } else {
      // Calculate Probability
      int total = labelCounts[0] + labelCounts[1];
      double p0 = (double)labelCounts[0] / total;
      double p1 = (double)labelCounts[1] / total;

      return -(p0 * (Math.log(p0) / Math.log(2)) + p1 * (Math.log(p1) / Math.log(2)));
    }
  }
}