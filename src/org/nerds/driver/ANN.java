package org.nerds.driver;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.encog.Encog;
import org.encog.engine.network.activation.ActivationTANH;
import org.encog.ml.data.MLData;
import org.encog.ml.data.MLDataPair;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.training.propagation.scg.ScaledConjugateGradient;
import org.encog.persist.EncogDirectoryPersistence;
import org.nerds.util.FileMgmt;
import org.nerds.util.Normalization;

public class ANN {

	
	/**@param vectorSize	Declares the number of feature vector in the image */
	public static int vectorSize = 252;
	
	public static float threshold_1 = 0.02f;
	public static float threshold_2 = 0.04f;
	public static float threshold_3 = 0.06f;
	
	public static final String FILENAME = "Network/_SMc.eg";
	public static final String FILENAME_1 = "Network/_SMc_1_2.eg";
	public static final String FILENAME_2 = "Network/_SMc_2_2.eg";
	
	static final int[] NETWORK_NEURONS = {420,150,5};
	
	public static double[][] input, output;
	
	public static void main(String[] args) 
	{
		// TODO Auto-generated method stub
		
		BasicNetwork network = new BasicNetwork();
		network.addLayer(new BasicLayer(null,true,vectorSize));
		
		for(int i = 0; i<NETWORK_NEURONS.length; i++)
		{
			network.addLayer(new BasicLayer(new ActivationTANH(),(i==(NETWORK_NEURONS.length-1)?false:true),NETWORK_NEURONS[i]));
		}

		
		network.getStructure().finalizeStructure();
		network.reset();

		HashMap<String, HashMap<String, ArrayList<Double>>> dataSet =  FileMgmt.getDataSet("Network/combined_training.txt", "TextData/labels_new.txt");
	
		System.out.println("total = "+FileMgmt.getImageCount());
		
		prepareDataSet(dataSet);
		
		boolean first = false, second = false;

		
		MLDataSet trainingSet = new BasicMLDataSet(input, output);
		
		
		System.out.println("Total record count : "+trainingSet.getRecordCount());
		
		final ScaledConjugateGradient train = new ScaledConjugateGradient(network, trainingSet);
		
		int epoch = 1;
		
		 DecimalFormat f = new DecimalFormat("##.00000000000000");
		 do {
			train.iteration();
			System.out.println("Epoch #" + epoch + " Error:" + f.format(train.getError()));
			epoch++;
			if(first)
				System.out.print("\t****");
			if(second)
				System.out.print("\t****");
			
			if((train.getError()<threshold_1) && !first)
			{
				first=!first;
				EncogDirectoryPersistence.saveObject(new File(FILENAME_1), network);
			}
			
			if((train.getError()<threshold_2) && !second)
			{
				second=!second;
				EncogDirectoryPersistence.saveObject(new File(FILENAME_2), network);
			}
			
		} while(train.getError() >threshold_3);
		train.finishTraining();
		
		int iIndex = 0, rIndex = 0;
		int correct = 0;
		
		System.out.println("Neural Network Results : ");
		for(MLDataPair pair: trainingSet ) 
		{
			final MLData outputFinal = network.compute(pair.getInput());
			
			double ideal = -1, result = -1;
			
			for(int i = 0; i<5; i++)
			{
				//result =  outputFinal.getData(i)>(result)?outputFinal.getData(i):result;
				if(outputFinal.getData(i)>(result))
				{
					result = outputFinal.getData(i);
					iIndex = i;
				}
				if(pair.getIdeal().getData(i)>ideal)
				{
					ideal = pair.getIdeal().getData(i);
					rIndex = i;
				}
				//ideal =  pair.getIdeal().getData(i)>ideal?pair.getIdeal().getData(i):ideal;
				
			}
			if(rIndex == iIndex)
			{
				correct++;
			}
			System.out.println("Result : "+rIndex + " ideal : "+iIndex);
		}
		
		System.out.println("Correct %age : ("+correct+"/500)*100");
		
//		EncogPersistedCollection encog = new EncogPersistedCollection(MarketLoaderStrategy.LoadedConfig.FILENAME, FileMode.Open);
//		BasicNetwork network = (BasicNetwork)encog.Find(MarketLoaderStrategy.LoadedConfig.MARKET_NETWORK);
		
		EncogDirectoryPersistence.saveObject(new File(FILENAME), network);
		Encog.getInstance().shutdown();
		
	}
	/**
	 * 
	 * @param dataSet HashMap<String, HashMap<String, ArrayList<Double>>>
	 */

	public static void prepareDataSet(HashMap<String, HashMap<String, ArrayList<Double>>> dataSet)
	{
		/**
		 * This method takes the HashMap comprising of image name, image label and image feature vectors and converts them to two double dimensional arrays:
		 * input[][] - array of feature vectors for all images
		 * output[][] - array of ideal labels for all images
		 * the first dimension of output and input are of the same size, i.e, the number of images in the data set.
		 * The input and output array needs to be declared globally, since this method returns void.
		 */
		
		input = new double[FileMgmt.getImageCount()][vectorSize];
		output = new double[FileMgmt.getImageCount()][5];
		
		
		Iterator entries = dataSet.entrySet().iterator();
		int count = 0;
		while (entries.hasNext()) {
		  Entry thisEntry = (Entry) entries.next();
		  String key = (String) thisEntry.getKey();
		  HashMap<String, ArrayList<Double>> value = (HashMap<String, ArrayList<Double>>) thisEntry.getValue();
		  Iterator maps = value.entrySet().iterator();
		  while (maps.hasNext()) {
		    Entry thisMap = (Entry) maps.next();
		    String label = (String) thisMap.getKey();
		    int labelNum = Integer.parseInt(label);
		    ArrayList<Double> vectors = (ArrayList<Double>) thisMap.getValue();
		   // System.out.println("Image : "+key+" labeled : "+label + " size of vectors: "+vectors.size());
		    System.out.println("@:"+count);
		    for(int i = 0; i<vectorSize/*vectors.size()*/; i++)
		    {
		    	
		    	input[count][i] = vectors.get(i);
		    }
		    
		    for(int i = 0; i<5; i++)
		    {
		    	output[count][i] = (i==(labelNum-1))?1:0;
		    }
		    
		  }
		  
		  count++;
		}
		
		input = Normalization.normalizeMatrix(input);
	}
	
	
}
