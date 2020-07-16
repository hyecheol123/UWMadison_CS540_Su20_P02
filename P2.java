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

    // Close resultFileWriter
    resultFileWriter.close();
  }
}