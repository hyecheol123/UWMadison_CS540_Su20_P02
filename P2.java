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
import java.util.Arrays;

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

    // Decision Stump (Part 1)
    ArrayList<Integer> featureList = new ArrayList<>(Arrays. asList(5));
    DecisionTreeNode root = trainDecisionTree(dataset.getTrainFeature(), dataset.getTrainLabel(), featureList);
    // Q3: enter the number of positive and negative instances in the training set above and below the threshold
    resultFileWriter.append("@answer_3\n");
    resultFileWriter.append(root.getRightChild().getLabelCounts()[0] + "," + root.getLeftChild().getLabelCounts()[0] 
        + "," + root.getRightChild().getLabelCounts()[1] + "," + root.getLeftChild().getLabelCounts()[1] + "\n");
    resultFileWriter.flush();
    // Q4: enter the information gain after the split (4 decimal places)
    resultFileWriter.append("@answer_4\n");
    resultFileWriter.append(String.format("%.4f\n", root.getInformationGain()));
    resultFileWriter.flush();

    // Decision Tree Training (Part 2)
    featureList = new ArrayList<>(Arrays.asList(8, 4, 9, 7, 3));
    root = trainDecisionTree(dataset.getTrainFeature(), dataset.getTrainLabel(), featureList);
    // Q5: binary decision tree String
    StringBuffer decisionTreeStringBuffer = new StringBuffer();
    int depth = depthStringDecisionTree(root, decisionTreeStringBuffer, 0);
    resultFileWriter.append("@tree_full\n");
    resultFileWriter.append(decisionTreeStringBuffer.toString().trim() + "\n");
    resultFileWriter.flush();
    // Q6: maximum depth of tree
    resultFileWriter.append("@answer_6\n");
    resultFileWriter.append(depth + "\n");
    resultFileWriter.flush();

    // Testing
    ArrayList<Integer> testLabel = new ArrayList<>();
    for(Integer[] testFeature : dataset.getTestFeature()) {
      testLabel.add(testLabel(testFeature, root));
    }
    // Q7: class labels of test set
    String outputString = testLabel.toString();
    outputString = outputString.substring(1, outputString.length() - 1).replaceAll(" ", "");
    resultFileWriter.append("@label_full\n");
    resultFileWriter.append(outputString + "\n");
    resultFileWriter.flush();

    // Pruning
    if(pruning(null, root, -1, 0, 6) > 6) {
      System.out.println("Error!!");
    }
    // Q8: the pruned binary decision tree String
    decisionTreeStringBuffer = new StringBuffer();
    depthStringDecisionTree(root, decisionTreeStringBuffer, 0);
    resultFileWriter.append("@tree_pruned\n");
    resultFileWriter.append(decisionTreeStringBuffer.toString().trim() + "\n");
    resultFileWriter.flush();

    // Close resultFileWriter
    resultFileWriter.append("@answer_10\nNone");
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
        node = new DecisionTreeNode(bestFeature, bestThreshold, 2, 
            Collections.frequency(trainDataLabel, 2), Collections.frequency(trainDataLabel, 4), bestInformationGain);
      } else {
        node = new DecisionTreeNode(bestFeature, bestThreshold, 4,
            Collections.frequency(trainDataLabel, 2), Collections.frequency(trainDataLabel, 4), bestInformationGain);
      }
    } else { // non-leaf
      node = new DecisionTreeNode(bestFeature, bestThreshold, null,
          Collections.frequency(trainDataLabel, 2), Collections.frequency(trainDataLabel, 4), bestInformationGain);
      // Train for its children
      node.setRightChild(trainDecisionTree(rightDataFeature, rightDataLabel, featureList));
      node.setLeftChild(trainDecisionTree(leftDataFeature, leftDataLabel, featureList));
    }

    return node;
  }

  /**
   * Recursive helper method to make string of decision rules and find depth
   * For Q5 and Q6
   * 
   * @param root root of (sub)tree
   * @param outputBuffer Strings will be made by StringBuffer
   * @param depth depth of current node(root)
   * @return depth from current root
   */
  private static int depthStringDecisionTree(DecisionTreeNode root, StringBuffer outputBuffer, int depth) {
    // leaf node: write return command
    if(root.isLeaf()) {
      outputBuffer.append(" return ").append(root.getClassLabel());
      return depth;
    }
    // non-leaf: write indent, decision rules
    else {
      // left-side
      // indent
      outputBuffer.append("\n");
      for(int i = 0; i < depth; i++) {
        outputBuffer.append("  ");
      }
      // decision rule
      outputBuffer.append("if (x").append(root.getFeature()).append(" <= ").append(root.getThreshold()).append(")");
      // traverse on the left-side child
      int leftDepth = depthStringDecisionTree(root.getLeftChild(), outputBuffer, depth + 1);

      // right-side
      // indent
      outputBuffer.append("\n");
      for(int i = 0; i < depth; i++) {
        outputBuffer.append("  ");
      }
      // decision rule
      outputBuffer.append("else");
      // traverse on the right-side child
      int rightDepth = depthStringDecisionTree(root.getRightChild(), outputBuffer, depth + 1);

      return Math.max(leftDepth, rightDepth);
    }
  }

  /**
   * Generate label of test output with given decision tree
   * 
   * @param testFeature test instance
   * @param decisionTree decision tree to be used for test
   * @return 2 when postivie, 4 when negative
   */
  private static int testLabel(Integer[] testFeature, DecisionTreeNode decisionTree) {
    // iterate down the tree when the node is not leaf
    while(!decisionTree.isLeaf()) {
      if(testFeature[decisionTree.getFeature() - 2] > decisionTree.getThreshold()) { // larger than threshold
        decisionTree = decisionTree.getRightChild(); // iterate on right subtree
      } else { // smaller than threshold
        decisionTree = decisionTree.getLeftChild(); // iterate on left subtree
      }
    }

    // reached to the leaf node
    return decisionTree.getClassLabel(); // return class label of the leaf
  }

  /**
   * Recursive helper method to prune the decision tree
   * 
   * @param parent parent node of this node (For root node, assign null)
   * @param current current node
   * @param direction Indicate related direction of current node from the parent.
   *                  If current node is left child of parent, direction is 0.
   *                  If current node is right chlid of parent, direction is 1.
   *                  If current node is root node, direction is -1
   * @param depth current node's depth
   * @param maxDepth maximum allowed depth (Strictly larger than 0)
   * @return depth of tree
   */
  private static int pruning(DecisionTreeNode parent, DecisionTreeNode current, int direction, int depth, int maxDepth) {
    if(current.isLeaf()) { // when the node is leaf
      return depth;
    } else {
      if(depth == maxDepth) { // need to prune the tree
        // This node becomes leaf node with dominant label
        Integer classLabel;
        if(current.getLabelCounts()[0] >= current.getLabelCounts()[1]) {
          classLabel = 2;
        } else {
          classLabel = 4;
        }

        if(direction == 0) { // left node
          parent.setLeftChild(new DecisionTreeNode(current.getFeature(), current.getThreshold(), classLabel,
              current.getLabelCounts()[0], current.getLabelCounts()[1], current.getInformationGain()));
        } else { // right node
          parent.setRightChild(new DecisionTreeNode(current.getFeature(), current.getThreshold(), classLabel,
              current.getLabelCounts()[0], current.getLabelCounts()[1], current.getInformationGain()));
        }

        return depth;
      } else { // run pruning recursively on its children
        return Math.max(pruning(current, current.getLeftChild(), 0, depth + 1, maxDepth),
            pruning(current, current.getRightChild(), 1, depth + 1, maxDepth));
      }
    }
  }
}