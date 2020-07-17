# UWMadison_CS540_Su20_P01

Repository for the 2nd programming assignment of UW-Madison CS540 Summer 2020 course (Introduction to Artificial Intelligence)


## Goal
Implement decision tree to diagnose whether a patient has some disease based on their symptoms and medical test results.  


## Dataset
The dataset is given [here](https://archive.ics.uci.edu/ml/datasets/breast+cancer+wisconsin+%28original%29). We are using `breast-cancer-wisconsin.data`. This file is also uploaded to this repository.  
The dataset contains value of `?`, meaning missing entry. While parse the dataset, we are required to handle `?` sign properly.  
To see the description of this dataset, please visit [here](https://github.com/hyecheol123/UWMadison_CS540_Su20_P02/blob/master/breast-cancer-wisconsin.names).

### Data Pre-process
Code for daa preprocess and load is located in separated class, [Dataset.java](https://github.com/hyecheol123/UWMadison_CS540_Su20_P02/blob/master/Dataset.java)
- Remove rows with missing entry
- id is not used while training/testing. Remove id field

### List of Variables
1. Sample code number: id number
2. Clump Thickness: 1 - 10
3. Uniformity of Cell Size: 1 - 10
4. Uniformity of Cell Shape: 1 - 10
5. Marginal Adhesion: 1 - 10
6. Single Epithelial Cell Size: 1 - 10
7. Bare Nuclei: 1 - 10
8. Bland Chromatin: 1 - 10
9. Normal Nucleoli: 1 - 10
10. Mitoses: 1 - 10
11. Class: (2 for benign, 4 for malignant)


## Tasks
- Data Loading, Test of Important Function Implementation
  - Related Question: Q1, Q2
- Train a binary decision stump (decision tree with depth 1) using the following feature: 5 (indexed according to the above list).
  - Related Question: Q3, Q4
- Train a binary decision tree using the following features: 8, 4, 9, 7, 3 (indexed according to the same list).
  - Related Question: Q5, Q6
- Classify the following patients using your tree.
  - Related Question: Q7
- Prune the tree so that the maximum depth is 6. The root is at depth 0.
  - Related Question: Q8, Q9


## Questions
- **Q1**  
  Enter the total number of positive and negative instances in the training set (two integers, comma-separated, in the order, benign, malignant).
  - Count the existance of each level in training set while loading data
- **Q2**  
  Enter the initial entropy at the root before the split (one number, rounded to 4 decimal places).
  - Before any split, the probability for each label is only depend on the number of label in entire train set.
  - Test for implementation of [entropy function](https://github.com/hyecheol123/UWMadison_CS540_Su20_P02/blob/master/P2.java#L149).
- **Q3**  
  For the decision stump (Part 1), enter the number of positive and negative instances in the training set above and below the threshold (four integers, comma-separated, in the order: above-benign, below-benign, above-malignant, below-malignant).
  - Tracking information of counts inside DecisionTreeNode.
  - Test for implementation of [training function](https://github.com/hyecheol123/UWMadison_CS540_Su20_P02/blob/master/P2.java#L212).
- **Q4**  
  For the decision stump (Part 1), enter the information gain after the split (one number, rounded to 4 decimal places).
  - Save the maximum information gain during training time.
  - Test for implementation of [training function](https://github.com/hyecheol123/UWMadison_CS540_Su20_P02/blob/master/P2.java#L212).
- **Q5**  
  Input the binary decision tree in the format below (`tree_full`).  
  ```
  if (x8 <= 5)
    if (x4 <= 2) return 2
    else return 4
  else
    if (x4 <= 8) return 4
    else return 2
  ```
  - Use [recurrsive private helper method](https://github.com/hyecheol123/UWMadison_CS540_Su20_P02/blob/master/P2.java#L278)
  - Test for implementation of [training function](https://github.com/hyecheol123/UWMadison_CS540_Su20_P02/blob/master/P2.java#L212) and understanding of Depth First Search
- **Q6**  
  Enter the maximum depth of this tree. The root is at depth 0. For example, if you only have "if ..., else ...", you should enter 1.
  - Test for implementation of [recurrsive private helper method](https://github.com/hyecheol123/UWMadison_CS540_Su20_P02/blob/master/P2.java#L278) perviously used
- **Q7**  
  Input the class labels on the test set (200 integers, either 2 or 4, comma separated, in one line). (`label_full`)
  - Use [test method](https://github.com/hyecheol123/UWMadison_CS540_Su20_P02/blob/master/P2.java#L319)
  - Test for the general algorithm and implementation of test method
- **Q8**  
  Input the pruned binary decision tree using the format used in Q5 (`tree_pruned`).
  - Use [recursive helper method](https://github.com/hyecheol123/UWMadison_CS540_Su20_P02/blob/master/P2.java#L346) to prune the tree
  - Done without validation set
  - The node at depth 6 has been substitute to new leaf node having class label of dominant label.
  - Test for implementation of pruning algorithm
- **Q9**  
  Input the class labels on the test set (200 integers, either 2 or 4, comma separated, in one line). (`label_pruned`)
  - Use [test method](https://github.com/hyecheol123/UWMadison_CS540_Su20_P02/blob/master/P2.java#L319) with pruned tree
  - Test for implementation of pruning algorithm