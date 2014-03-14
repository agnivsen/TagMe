package org.nerds.util;

public class Normalization 
{

	public static double[][] normalizeMatrix(double[][] matrix)
	{
		double[][] minMax = new double[matrix[0].length][2];
		
		for(int j = 0; j<matrix[0].length; j++)
		{
			minMax[j][0] = matrix[0][j];
			minMax[j][1] = matrix[0][j];
			for(int i = 1; i<matrix.length; i++)
			{
				if(minMax[j][0]<matrix[i][j])
				{
					minMax[j][0] = matrix[i][j];
				}
				
				if(minMax[j][1]>matrix[i][j])
				{
					minMax[j][1] = matrix[i][j];
				}
			}
			//System.out.println("Min max "+minMax[j][0]+"  "+minMax[j][1]);
		}
		
		//System.out.println("+++++++++++++++");
		
		for(int i = 0; i<matrix.length; i++)
		{
			for(int j = 0; j<matrix[0].length; j++)
			{
				matrix[i][j] = ((matrix[i][j] - minMax[j][1])/(minMax[j][0] - minMax[j][1]));
			}
		}
		
//		for(int i = 0; i<matrix.length; i++)
//		{
//			for(int j = 0; j<matrix[0].length; j++)
//			{
//				matrix[j][i] = ((matrix[j][i] - minMax[j][0])/(minMax[j][1] - minMax[j][0]));
//				System.out.print(matrix[j][i]+"\t");
//			}
//			System.out.println();
//		}
//		
		//System.out.println("+++++++++++++++");
		
		return matrix;
	}
	
	public static void main(String args[])
	{
		
		double[][] mat= {{	1,	2,		3,		4,	5, 		10},
					  {		2,	3.4,	4,		5,	3.8, 	11},
					  {		3,	4,		2.6,	6,	7,		9.8},
					  {		4,	5,		6,		6.5,8,		7.5},
					  {		1.5,6,		7,		8,	9,		11.6}};
		
		mat = normalizeMatrix(mat);
		
		for(int i = 0; i<mat.length; i++)
		{
			for(int j = 0; j<mat[0].length; j++)
			{
				System.out.print(mat[i][j] + "   ");
			}
			System.out.println();
		}
	}
	
}
