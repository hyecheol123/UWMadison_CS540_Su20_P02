///////////////////////////////// FILE HEADER /////////////////////////////////
//
// Title:           UWMadison_CS540_Su20_P02
// This File:       P2.java
// Files:           Dataset.java, DecisionTreeNode.java, P2.java
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
import java.util.ArrayList;
import java.util.Collections;

/**
 * Main class for P2
 */
public class P2 {
  static Dataset dataset;

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
    dataset = new Dataset("breast-cancer-wisconsin.data", "test.txt");
    
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

  /**
   * calculate information gain
   * 
   * @param trainFeature ArrayList contains the training set's feature
   * @param trainLabel ArrayList contains the training set's label
   * @param feature feature index
   * @param threshold threshold for testing
   * @return information gain
   */
  private static double
      informationGain(ArrayList<Integer[]> trainFeature, ArrayList<Integer> trainLabel, int feature, double threshold) {
    // compute H(Y)
    Integer[] labelCounts = {Collections.frequency(trainLabel, 2), Collections.frequency(trainLabel, 4)};
    double h_y = binaryEntropy(labelCounts);

    // Compute H(Y|X)
    double h_yx = 0;
    ArrayList<Integer> labelSmaller = new ArrayList<>();
    ArrayList<Integer> labelGreater = new ArrayList<>();
    // Split the labels with respect to the threshold
    for(int i = 0; i < trainFeature.size(); i++) {
      if(trainFeature.get(i)[feature - 2] > threshold) {
        labelGreater.add(trainLabel.get(i));
      } else {
        labelSmaller.add(trainLabel.get(i));
      }
    }
    // Calculate entropy for each case (smaller and greater)
    if(labelSmaller.size() != 0) {
      labelCounts[0] = Collections.frequency(labelSmaller, 2);
      labelCounts[1] = Collections.frequency(labelSmaller, 4);
      h_yx += ((double)labelSmaller.size() / trainFeature.size()) * binaryEntropy(labelCounts);
    }
    if(labelGreater.size() != 0) {
      labelCounts[0] = Collections.frequency(labelGreater, 2);
      labelCounts[1] = Collections.frequency(labelGreater, 4);
      h_yx += ((double)labelGreater.size() / trainFeature.size()) * binaryEntropy(labelCounts);
    }

    return h_y - h_yx;
  }

  /**
   * Train Decision tree on given training (sub)set with given features
   * 
   * @param trainDataFeature Feature part of Training (sub)set
   * @param trainDataLabel Label part of Training (sub)set
   * @param featureList list of feature index that will be used to train the decision tree
   * @return root node of the decision tree
   */
  private static DecisionTreeNode trainDecisionTree(ArrayList<Integer[]> trainDataFeature,
      ArrayList<Integer> trainDataLabel, ArrayList<Integer> featureList) {
    int bestFeature = -1; // Indicates the best feature index (Max Information Gain)
    double bestThreshold = -1; // Use Binary Split with mid-points (Range = [1, 10])
    double bestInformationGain = Double.NEGATIVE_INFINITY;
    DecisionTreeNode node = null;

    // Iterate through all the feature, and find best feature and threshold (maximize the information gain)
    for(int featureIndex : featureList) {
      for(double testThreshold = 0.5; testThreshold < 11; testThreshold++) {
        // Calculate Information gain and compare wit previous best
        double informationGain = informationGain(trainDataFeature, trainDataLabel, featureIndex, testThreshold);
        if(bestInformationGain < informationGain) {
          bestInformationGain = informationGain;
          bestFeature = featureIndex;
          bestThreshold = testThreshold;
        }
      }
    }

    // Split the dataset based on the best feature and threshold
    ArrayList<Integer[]> leftDataFeature = new ArrayList<>();
    ArrayList<Integer[]> rightDataFeature = new ArrayList<>();
    ArrayList<Integer> leftDataLabel = new ArrayList<>();
    ArrayList<Integer> rightDataLabel = new ArrayList<>();
    for(int i = 0; i < trainDataFeature.size(); i++) {
      if(trainDataFeature.get(i)[bestFeature - 2] > bestThreshold) { // right case
        rightDataFeature.add(trainDataFeature.get(i));
        rightDataLabel.add(trainDataLabel.get(i));
      } else { // left case
        leftDataFeature.add(trainDataFeature.get(i));
        leftDataLabel.add(trainDataLabel.get(i));
      }
    }

    // If bestInformation gain is 0, additional information does not make any difference (Current Node is Leaf)
    // When the best threshold and feature do not make valid split, current node is leaf
    if(bestInformationGain == 0 || leftDataFeature.size() == 0 || rightDataFeature.size() == 0) { // leaf
      // make new node with dominant class labels
      if(Collections.frequency(trainDataLabel, 2) >= Collections.frequency(trainDataLabel, 4)) {
        node = new DecisionTreeNode(bestFeature, bestThreshold, 2);
      } else {
        node = new DecisionTreeNode(bestFeature, bestThreshold, 4);
      }
    } else { // non-leaf
      node = new DecisionTreeNode(bestFeature, bestThreshold, null);
      // Train for its children
      node.setRightChild(trainDecisionTree(rightDataFeature, rightDataLabel, featureList));
      node.setLeftChild(trainDecisionTree(leftDataFeature, leftDataLabel, featureList));
    }

    return node;
  }
}