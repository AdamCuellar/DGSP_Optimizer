/******************************************************************************
*  A Teaching GA					  Developed by Hal Stringer & Annie Wu, UCF
*  Version 2, January 18, 2004
*******************************************************************************/

import java.io.*;
import java.util.*;
import java.text.*;

public class Chromo
{
/*******************************************************************************
*                            INSTANCE VARIABLES                                *
*******************************************************************************/

	public Map<String, String> chromo;
	public double rawFitness;
	public double sclFitness;
	public double proFitness;

/*******************************************************************************
*                            INSTANCE VARIABLES                                *
*******************************************************************************/

	private static double randnum;

/*******************************************************************************
*                              CONSTRUCTORS                                    *
*******************************************************************************/

	public Chromo(){
		chromo = new HashMap<>();
		int groupSize = Parameters.stockList.size()/Parameters.numGenes;
		int randInt;

		// TODO: edit to fit representation outlined in proposal

		//  Create a random groups
		String geneBit;
		for (int i=0; i<Parameters.numGenes; i++){
			String group = "";
			for (int j=0; j<Parameters.geneSize; j++){
				randInt = Search.r.nextInt(Parameters.stockList.size());
				if(randInt < 10) {
					geneBit = "0" + randInt;
				}
				else
				{
					geneBit = Integer.toString(randInt);
				}

				group = group + geneBit;
			}

			String portfolio = "";
			for(int k=0; k<7; k++) {
				randnum = Search.r.nextDouble();
				if(randnum > 0.5) {
					portfolio += "1";
				}
				else{
					portfolio += "0";
				}
			}

			portfolio += (Search.r.nextInt(10) + 1);

			this.chromo.put(group, portfolio);
		}


		this.rawFitness = -1;   //  Fitness not yet evaluated
		this.sclFitness = -1;   //  Fitness not yet scaled
		this.proFitness = -1;   //  Fitness not yet proportionalized
	}


/*******************************************************************************
*                                MEMBER METHODS                                *
*******************************************************************************/

	//  Get Alpha Represenation of a Gene **************************************

//	public String getGeneAlpha(int geneID){
//		int start = geneID * Parameters.geneSize;
//		int end = (geneID+1) * Parameters.geneSize;
//		String geneAlpha = this.chromo.substring(start, end);
//		return (geneAlpha);
//	}

	//  Get Integer Value of a Gene (Positive or Negative, 2's Compliment) ****

//	public int getIntGeneValue(int geneID){
//		String geneAlpha = "";
//		int geneValue;
//		char geneSign;
//		char geneBit;
//		geneValue = 0;
//		geneAlpha = getGeneAlpha(geneID);
//		for (int i=Parameters.geneSize-1; i>=1; i--){
//			geneBit = geneAlpha.charAt(i);
//			if (geneBit == '1') geneValue = geneValue + (int) Math.pow(2.0, Parameters.geneSize-i-1);
//		}
//		geneSign = geneAlpha.charAt(0);
//		if (geneSign == '1') geneValue = geneValue - (int)Math.pow(2.0, Parameters.geneSize-1);
//		return (geneValue);
//	}

	//  Get Integer Value of a Gene (Positive only) ****************************

//	public int getPosIntGeneValue(int geneID){
//		String geneAlpha = "";
//		int geneValue;
//		char geneBit;
//		geneValue = 0;
//		geneAlpha = getGeneAlpha(geneID);
//		for (int i=Parameters.geneSize-1; i>=0; i--){
//			geneBit = geneAlpha.charAt(i);
//			if (geneBit == '1') geneValue = geneValue + (int) Math.pow(2.0, Parameters.geneSize-i-1);
//		}
//		return (geneValue);
//	}

	//  Mutate a Chromosome Based on Mutation Type *****************************

	public void doMutation(){

		Map <String, String> mutChromo = new HashMap<>();
		char x;

		switch (Parameters.mutationType){

		case 1:     // Two Phase Mutation
			int randPos1;
			int randPos2;
			char[] tempArray;
			for(Map.Entry<String, String> entry : chromo.entrySet()) {
				String[] group = splitToNChar(entry.getKey(), 2);
				String portfolio = entry.getValue();
				if(Search.r.nextDouble() < Parameters.mutationRate) {
					String temp;

					randPos1 = Search.r.nextInt(group.length);
					randPos2 = Search.r.nextInt(group.length);

					// swap stocks
					temp = group[randPos1];
					group[randPos1] = group[randPos2];
					group[randPos2] = temp;

					// flip random bit
					temp = portfolio.substring(0, 7);
					randPos1 = Search.r.nextInt(temp.length());
					tempArray = temp.toCharArray();

					if (tempArray[randPos1] == '1') {
						tempArray[randPos1] = '0';
					} else {
						tempArray[randPos1] = '1';
					}

					// change unit randomly
					portfolio = String.valueOf(tempArray) + (Search.r.nextInt(10) + 1);
					mutChromo.put(String.join("",group), portfolio);
				}
				else {
					mutChromo.put(String.join("",group), portfolio);
				}
			}
			this.chromo = mutChromo;
			break;

		default:
			System.out.println("ERROR - No mutation method selected");
		}
	}

/*******************************************************************************
*                             STATIC METHODS                                   *
*******************************************************************************/

	//  Select a parent for crossover ******************************************

	public static int selectParent(){

		double rWheel = 0;
		int j = 0;
		int k = 0;

		switch (Parameters.selectType){

		case 1:     // Proportional Selection
			randnum = Search.r.nextDouble();
			for (j=0; j<Parameters.popSize; j++){
				rWheel = rWheel + Search.member[j].proFitness;
				if (randnum < rWheel) return(j);
			}
			break;

		case 3:     // Random Selection
			randnum = Search.r.nextDouble();
			j = (int) (randnum * Parameters.popSize);
			return(j);

		case 2:     //  Tournament Selection

		default:
			System.out.println("ERROR - No selection method selected");
		}
	return(-1);
	}

	//  Produce a new child from two parents  **********************************

	public static void mateParents(int pnum1, int pnum2, Chromo parent1, Chromo parent2, Chromo child1, Chromo child2){

		int xoverPoint1;
		int xoverPoint2;

		switch (Parameters.xoverType){

		case 1:     //  Single Point Crossover

//			//  Select crossover point
//			xoverPoint1 = 1 + (int)(Search.r.nextDouble() * (Parameters.numGenes * Parameters.geneSize-1));
//
//			//  Create child chromosome from parental material
//			child1.chromo = parent1.chromo.substring(0,xoverPoint1) + parent2.chromo.substring(xoverPoint1);
//			child2.chromo = parent2.chromo.substring(0,xoverPoint1) + parent1.chromo.substring(xoverPoint1);
//			break;

		case 2:     //  Two Point Crossover

		case 3:     //  Uniform Crossover

		default:
			System.out.println("ERROR - Bad crossover method selected");
		}

		//  Set fitness values back to zero
		child1.rawFitness = -1;   //  Fitness not yet evaluated
		child1.sclFitness = -1;   //  Fitness not yet scaled
		child1.proFitness = -1;   //  Fitness not yet proportionalized
		child2.rawFitness = -1;   //  Fitness not yet evaluated
		child2.sclFitness = -1;   //  Fitness not yet scaled
		child2.proFitness = -1;   //  Fitness not yet proportionalized
	}

	//  Produce a new child from a single parent  ******************************

	public static void mateParents(int pnum, Chromo parent, Chromo child){

		//  Create child chromosome from parental material
		child.chromo = parent.chromo;

		//  Set fitness values back to zero
		child.rawFitness = -1;   //  Fitness not yet evaluated
		child.sclFitness = -1;   //  Fitness not yet scaled
		child.proFitness = -1;   //  Fitness not yet proportionalized
	}

	//  Copy one chromosome to another  ***************************************

	public static void copyB2A (Chromo targetA, Chromo sourceB){

		targetA.chromo = sourceB.chromo;

		targetA.rawFitness = sourceB.rawFitness;
		targetA.sclFitness = sourceB.sclFitness;
		targetA.proFitness = sourceB.proFitness;
		return;
	}

	/**
	 * Source code of snippet: https://kodejava.org/how-to-split-a-string-by-a-number-of-characters/
	 * Author: Wayan Saryada
	 * Published on: July 8, 2019
	 * Visited on: Febrary 22, 2020
	 *
	 * Split text into n number of characters.
	 *
	 * @param text the text to be split.
	 * @param size the split size.
	 * @return an array of the split text.
	 */
	public static String[] splitToNChar(String text, int size) {
		List<String> parts = new ArrayList<>();

		int length = text.length();
		for (int i = 0; i < length; i += size) {
			parts.add(text.substring(i, Math.min(length, i + size)));
		}
		return parts.toArray(new String[0]);
	}

}   // End of Chromo.java ******************************************************
