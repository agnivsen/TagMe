package org.nerds.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

public class FileMgmt {
	
	/**imageDataType: String type representing the image format  */
	public static String imageDataType = "jpg";
	public static String seperator = " ";
	
	public static ArrayList<String> names;
	
	
	/**
	 * @return the names
	 */
	public static ArrayList<String> getNames() {
		return names;
	}

	/**
	 * @param names the names to set
	 */
	public static void setNames(ArrayList<String> names) {
		FileMgmt.names = names;
	}

	private static int imageCount;

	/**
	 * 
	 * @param vectorFilePath	Path to the file containing the image feature list in '*.txt' format
	 * @param imageLabelPath	Path to the file containing the image classification labels
	 * @return	HashMap<String, HashMap<String, ArrayList<Double>>> 1st String contains image name, 2nd String contains image label, ArrayList<Double> contains feature vector
	 */
	public static HashMap<String, HashMap<String, ArrayList<Double>>> getDataSet (String vectorFilePath, String imageLabelPath)
	{
		
		HashMap<String, HashMap<String, ArrayList<Double>>> dataSet = new HashMap<String, HashMap<String,ArrayList<Double>>>();
		
		BufferedReader brLabel = null, br = null;
		
		int total = 0;
		
		try 
		{
			HashMap<String, String> labelMap = new HashMap<String, String>();
			FileInputStream labelStream = new FileInputStream(imageLabelPath);
			DataInputStream labelIn = new DataInputStream(labelStream);
			brLabel = new BufferedReader(new InputStreamReader(labelIn));
			String label;
			
			 while ((label = brLabel.readLine()) != null)  
			 {
				 String[] labelTab = label.trim().split(seperator);
				 labelMap.put(labelTab[0], labelTab[1]);
			 }
			
			FileInputStream vectorStream = new FileInputStream(vectorFilePath);
			DataInputStream in = new DataInputStream(vectorStream);
			br = new BufferedReader(new InputStreamReader(in));
		    String strLine;
		    //Read File Line By Line
		    while ((strLine = br.readLine()) != null)   
		    {
		    	total++;
		    	String[] name =  strLine.split(imageDataType);
		    	name[0]+=imageDataType;
		    	String[] vectorSet = name[1].split(seperator);		//image feature vector
		    	ArrayList<Double> vectorSetList = new ArrayList<Double>();
		    	for(int i = 0; i<vectorSet.length; i++)
		    	{
		    		try
		    		{
		    		vectorSetList.add(Double.parseDouble(vectorSet[i].trim()));	//converting the feature vectors to double datatype
		    		}
		    		catch (NumberFormatException nme)
		    		{
		    			if(vectorSet[i].length()>0)
		    			{
		    			vectorSetList.add(-1.0000);
		    			System.out.println("Be Warned : Number formatException for : "+name[0]+"jpg : -1.000 added as vector element, instead of ***"+vectorSet[i]+"*** , which triggered the exception.");
		    			}
		    		}
		    	}
		    	
		    	HashMap<String, ArrayList<Double>> subMap = new HashMap<String, ArrayList<Double>>();
		    	subMap.put(labelMap.get(name[0]), vectorSetList);
		    	dataSet.put(name[0], subMap);
		    	
		    }
		    
		   // System.out.println(vectors);
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			setImageCount(total);
			try {
				brLabel.close();
				br.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (NullPointerException npe)
			{
				npe.printStackTrace();
			}
			
		}
		
		
		return dataSet;
	}

	/**
	 * @return the imageCount
	 */
	public static int getImageCount() {
		return imageCount;
	}

	/**
	 * @param imageCount the imageCount to set
	 */
	public static void setImageCount(int imageCount) {
		FileMgmt.imageCount = imageCount;
	}
	
	
	/**
	 * 
	 * @param path	Path to the file containing the image feature list in '*.txt' format
	 * @return	double dimensional array of double datatype containing feature vectors for all images
	 */
	public static double[][] getSimpleInput(String path)
	{
		BufferedReader br = null;
				
		FileInputStream vectorStream;
		
		ArrayList<ArrayList<Double>> set = new ArrayList<ArrayList<Double>>();
		int vectorSize = 0, entrySize = 0;
		
		names = new ArrayList<String>();
		
		try {
			vectorStream = new FileInputStream(path);
			DataInputStream in = new DataInputStream(vectorStream);
			br = new BufferedReader(new InputStreamReader(in));
		    String label;
		    
		    while ((label = br.readLine()) != null)  
			 {
				 String[] labelTab = label.trim().split(seperator);
				 //System.out.println("length = "+labelTab.length);
				 ArrayList<Double> data = new ArrayList<Double>();
				 //System.out.println(count);
				 for(int i = 1; i<labelTab.length; i++)
				 {
					 data.add(Double.parseDouble(labelTab[i].trim()));
					 //System.out.print(data.get(i-1));
				 }
				 names.add(labelTab[0]);
				 //System.out.println();
				 entrySize++;
				 set.add(data);
				 vectorSize = data.size();
			 }
		   
		    
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			try {
				br.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		double[][] matrix = new double[set.size()][vectorSize];
		
		for(int  i = 0; i<entrySize; i++)
		{
			for(int j = 0; j<vectorSize; j++)
			{
				matrix[i][j] = set.get(i).get(j);
			}
		}
		
		
		return matrix;
	}
	
	
	public static void writeToFile(String path, String data) throws IOException
	{
		File file = new File(path);
		 
		// if file doesnt exists, then create it
		if (!file.exists()) {
			file.createNewFile();
		}

		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(data);
		bw.close();

	}

	


}
