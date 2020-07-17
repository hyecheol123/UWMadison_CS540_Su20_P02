///////////////////////////////// FILE HEADER /////////////////////////////////
//
// Title:           UWMadison_CS540_Su20_P02
// This File:       DecisionTreeNode.java
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

/**
 * Class for Decision Tree's Node
 */
public class DecisionTreeNode {
  // Instance Variables
  private int feature;
  private double threshold;
  private Integer classLabel; // only used when the node is leaf (null if non-leaf)
  private DecisionTreeNode[] children = new DecisionTreeNode[2]; // left and right, in order

  /**
   * Constructor for DecisionTreeNode
   * 
   * @param feature index of feature
   * @param threshold threshold of this node
   * @param classLabel null if this node is non-leaf, otherwise, assign proper class label of leaf node.
   */
  DecisionTreeNode(int feature, double threshold, Integer classLabel) {
    this.feature = feature;
    this.threshold = threshold;
    this.classLabel = classLabel;
  }

  /**
   * Check for leaf node
   * 
   * @return true if this node is leaf
   */
  public boolean isLeaf() {
    // leaf if classLabel is not null
    return (classLabel != null);
  }

  /**
   * Accessor for classLabel
   * 
   * @return null if this node is non-leaf, otherwise, class label belongs to the leaf node
   */
  public Integer getClassLabel() {
    return classLabel;
  }
  
  /**
   * Accessor for feature
   * 
   * @return feature index
   */
  public int getFeature() {
    return feature;
  }

  /**
   * Accessor for threshold
   * 
   * @return threshold value
   */
  public double getThreshold() {
    return threshold;
  }
  
  /**
   * Setter for left child
   * 
   * @param leftChild Left child of this node
   */
  public void setLeftChild(DecisionTreeNode leftChild) {
    children[0] = leftChild;
  }

  /**
   * Setter for right child
   * 
   * @param rightChild Right child of this node
   */
  public void setRightChild(DecisionTreeNode rightChild) {
    children[1] = rightChild;
  }

  /**
   * Accessor of left child of this node
   * 
   * @return Left child of this node
   */
  public DecisionTreeNode getLeftChild() {
    return children[0];
  }

  /**
   * Accessor of right child of this node
   * 
   * @return Right child of this node
   */
  public DecisionTreeNode getRightChild() {
    return children[1];
  }
}