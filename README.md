# UWMadison_CS540_Su20_P01

Repository for the 2nd programming assignment of UW-Madison CS540 Summer 2020 course (Introduction to Artificial Intelligence)


## Tasks
Implement decision tree to diagnose whether a patient has some disease based on their symptoms and medical test results.  


## Dataset
The dataset is given [here](https://archive.ics.uci.edu/ml/datasets/breast+cancer+wisconsin+%28original%29). We are using `breast-cancer-wisconsin.data`. This file is also uploaded to this repository.  
The dataset contains value of `?`, meaning missing entry. While parse the dataset, we are required to handle `?` sign properly.  
To see the description of this dataset, please visit [here](https://github.com/hyecheol123/UWMadison_CS540_Su20_P02/blob/master/breast-cancer-wisconsin.names).

### Data Pre-process
Code for daa preprocess and load is located in separated class, [Dataset.java](https://github.com/hyecheol123/UWMadison_CS540_Su20_P02/blob/master/Dataset.java)
- Remove rows with missing entry
- id is not used while training/testing. Remove id field
