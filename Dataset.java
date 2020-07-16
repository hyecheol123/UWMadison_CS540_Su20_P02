///////////////////////////////// FILE HEADER /////////////////////////////////
//
// Title:           UWMadison_CS540_Su20_P02
// This File:       Dataset.java
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
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Dataset Class for P2
 * 
 * Load/Save train/test sets
 */
public class Dataset {
  // Instance Variables
  // Train Set
  private ArrayList<Integer[]> trainFeature;
  private ArrayList<Integer> trainLabel;
  // Test Set
  private ArrayList<Integer[]> testFeature;
  // Dataset Characteristics
  String trainSetFileLoc; // location of train set file
  String testSetFileLoc; // location of test set file

  /**
   * Constructor of Dataset
   * 
   * @param trainSetFileLoc location of train set file
   * @param testSetFileLoc location of test set file
   * @throws IOException Occurred when I/O Operation Interrupted
   */
  Dataset(String trainSetFileLoc, String testSetFileLoc) throws IOException {
    this.trainSetFileLoc = trainSetFileLoc;
    this.testSetFileLoc = testSetFileLoc;

    // Initialize ArrayList for Datasets
    trainFeature = new ArrayList<>();
    trainLabel = new ArrayList<>();
    testFeature = new ArrayList<>();

    // Parse train/test set file
    loadTrainSet();
    loadTestSet();
  }

  /**
   * private helper method to load train set
   * 
   * @throws IOException Occurred when I/O Operation Interrupted
   */
  private void loadTrainSet() throws IOException {
    Scanner fileReader = new Scanner(new File(trainSetFileLoc));

    while(fileReader.hasNextLine()) {
      String line = fileReader.nextLine();

      // Check for valid data entry
      // If '?' exists, the row is invalid.
      if(line.indexOf('?') == -1) { // only use lines without '?'
        String[] token = line.split(","); // tokenize line, separated by comma

        // Load features
        // id is ignored as it is not used
        Integer[] feature = new Integer[token.length - 2];
        for(int i = 1; i < token.length - 1; i++) { // last entry is label
          feature[i - 1] = Integer.parseInt(token[i]);
        }
        trainFeature.add(feature);

        // Load label
        trainLabel.add(Integer.parseInt(token[token.length - 1]));
      }
    }
    fileReader.close();
  }

  /**
   * private helper method to load test set
   * 
   * @throws IOException Occurred when I/O Operation Interrupted
   */
  private void loadTestSet() throws IOException {
    Scanner fileReader = new Scanner(new File(testSetFileLoc));

    while(fileReader.hasNextLine()) {
      String line = fileReader.nextLine();

      // Check for valid data entry
      // If '?' exists, the row is invalid.
      if(line.indexOf('?') == -1) { // only use lines without '?'
        String[] token = line.split(","); // tokenize line, separated by comma

        // Load features
        // id is ignored as it is not used
        Integer[] feature = new Integer[token.length - 1];
        for(int i = 1; i < token.length; i++) {
          feature[i - 1] = Integer.parseInt(token[i]);
        }
        testFeature.add(feature);
      }
    }
    fileReader.close();
  }

  /**
   * Accessor for trainFeature
   * 
   * @return ArrayList contains feature of train set
   */
  public ArrayList<Integer[]> getTrainFeature() {
    return trainFeature;
  }

  /**
   * Accessor for trainLabel
   * 
   * @return ArrayList contains label of train set
   */
  public ArrayList<Integer> getTrainLabel() {
    return trainLabel;
  }

  /**
   * Accessor for testFeature
   * 
   * @return ArrayList contains feature of test set
   */
  public ArrayList<Integer[]> getTestFeature() {
    return testFeature;
  }
}