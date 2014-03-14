package org.nerds.driver;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import org.encog.ml.data.MLDataPair;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.persist.EncogDirectoryPersistence;
import org.nerds.util.FileMgmt;
import org.nerds.util.Normalization;

public class NetworkTest {

	static String NEURAL_NETWORK = "Network/_SMc_yahoo-est_420.150.5_0.04.eg";
	static String VALIDATION_DATASET = "Network/combined_final.txt";
	static String OUTPUT_PATH = "/Users/agniva/Desktop/output_label.txt";
	
	public static void main(String args[])
	{
		
		BasicNetwork network = (BasicNetwork)EncogDirectoryPersistence.loadObject(new File(NEURAL_NETWORK));

		
		
		double[][] input = FileMgmt.getSimpleInput(VALIDATION_DATASET);
		double[][] output = new double[input.length][5];
		
		
		ArrayList<String> labels = FileMgmt.getNames();
		
		input = Normalization.normalizeMatrix(input);
		
		System.out.println("size = "+input.length + " vector size = "+input[0].length);
		
		MLDataSet trainingSet = new BasicMLDataSet(input, output);
		
		int count = 0;
		String contents = new String("");
		
		for(MLDataPair pair: trainingSet ) 
		{
			int index = 0;
		
			index = network.winner(pair.getInput());
				String data = labels.get(count)+ "\t "+(index+1)+"\n";
				contents = contents.concat(data);
			
				count++;
			
		}
		
		try {
			FileMgmt.writeToFile(OUTPUT_PATH, contents);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		System.out.println("*****************************************");
		System.out.println("*****************************************");
		System.out.println("Neural Network Prediction Completed");
		System.out.println("Input count = "+network.getInputCount());
		System.out.println("Output count = "+network.getLayerCount());
		System.out.println("Neuron count in 1st layer = "+network.getLayerTotalNeuronCount(1));
		System.out.println("Neuron count in 2nd layer = "+network.getLayerTotalNeuronCount(2));
		System.out.println("Neuron count in 3rd layer = "+network.getLayerTotalNeuronCount(3));
	}
}
