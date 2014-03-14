TagMe
=====

Project File for TagMe! Machine Learning Competition 


1.	Import and install the TagMe.zip file into any Java IDE (preferably Eclipse).

2.	There are two classes inside the project which needs to be modified for running the application: ANN.java and NetworkTest.java


3.	For Training and Validation:
a.	Open ANN.java
b.	Set variable vectorSize – Number of feature vectors in input file
c.	Set variables threshold_1, threshold_2, threshold_3 – Three error values for which the Neural Network is saved to the disk
d.	Set variables FILENAME, FILENAME_1,FILENAME_2 – The three file names under which the network is saved for threshold_1, threshold_2 and threshold_3
e.	NETWORK_NEURONS – The number of elements in this array denotes the number of layers in the neural network (excluding the first layer, which is always set to the size of the input feature vectors). Each elements of this array denotes the number of neurons in the layer corresponding to that element of the array. Ensure that the last layer of the array has 5 neurons.
f.	Set variable inputFeature – path to the input feature vector list (in *.txt format)
g.	Set variable inputLabels - path to the validation labels (in *.txt format)

4.	For training the Neural Network, set all the appropriate variables and run ANN.java. It should run for quite a few minutes, or even hours, depending upon the size of the feature vectors and number of neurons.

On completion of training, it should display “Neural Network Training Completed” on the console.

5.	For Prediction
a.	Open NetworkTest.java
b.	Set variable NEURAL_NETWORK – Path to the *.eg file which holds the previously trained neural network
c.	VALIDATION_DATASET – Path to the feature vectors for final classification of images
d.	OUTPUT_PATH – Fully qualified path name for storing the output labels

6.	For classification using the Neural Network, run NetworkTest.java after setting the appropriate paths. It should print “Neural Network Prediction Completed” on the console, followed by the details of the network used.

The final output labels should be saved in the specified path.

