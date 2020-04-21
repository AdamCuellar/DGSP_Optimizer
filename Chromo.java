/******************************************************************************
*  A Teaching GA					  Developed by Hal Stringer & Annie Wu, UCF
*  Version 2, January 18, 2004
*******************************************************************************/

import java.io.*;
import java.lang.reflect.Parameter;
import java.util.*;
import java.text.*;

import static java.util.Collections.reverse;

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
		List<Integer> noDuplicates = new ArrayList<>();;

		//  Create a random groups
		String geneBit;
		for (int i=0; i<Parameters.numGenes; i++){
			String group = "";
			noDuplicates.clear();
			for (int j=0; j<Parameters.geneSize; j++){
				randInt = Search.r.nextInt(Parameters.stockList.size());

				// make sure we dont have any duplicate stocks
				while(noDuplicates.contains(randInt)) {
					randInt = Search.r.nextInt(Parameters.stockList.size());
				}

				if(randInt < 10) {
					geneBit = "0" + randInt;
				}
				else
				{
					geneBit = Integer.toString(randInt);
				}

				noDuplicates.add(randInt);

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

			portfolio += (Search.r.nextInt(9) + 1);

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

	public static List<String> getRand() {
		List<String> randGen = new ArrayList<>();
		int groupSize = Parameters.stockList.size()/Parameters.numGenes;
		int randInt;
		List<Integer> noDuplicates = new ArrayList<>();

		//  Create a random groups
		String geneBit;
		String group = "";
		for (int j=0; j<Parameters.geneSize; j++){
			randInt = Search.r.nextInt(Parameters.stockList.size());

			// make sure we dont have any duplicate stocks
			while(noDuplicates.contains(randInt)) {
				randInt = Search.r.nextInt(Parameters.stockList.size());
			}

			if(randInt < 10) {
				geneBit = "0" + randInt;
			}
			else
			{
				geneBit = Integer.toString(randInt);
			}

			noDuplicates.add(randInt);

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

		portfolio += (Search.r.nextInt(9) + 1);

		randGen.add(group);
		randGen.add(portfolio);
		return randGen;
	}

	// get 2-year return for individual
	public float getPortReturn(String group, String portfolio) {
		float portReturn= 0;
		String[] gr = splitToNChar(group, 2);
		double bi = (double)Integer.parseInt(portfolio.substring(0,7), 2) / 127.0;
		int units = Integer.parseInt(portfolio.substring(7));

		for (String s : gr) {
			int ticker = Integer.parseInt(s);
			List<Float> returns = Parameters.yearReturns.get(ticker);
			// only get last two years
			float r = returns.get(returns.size() - 1) + returns.get(returns.size() - 2);

			if (bi > 0.5) {
				portReturn += (units*1000) * r;
			}
		}

		return portReturn;
	}

	public double getSortinoRatio(){
		double sortino;
		double prt = 1000.0;
		double avgPortReturn = 0;
		double dsd = 0;
		boolean here = false;

		for(Map.Entry<String, String> entry : chromo.entrySet()) {
			float portReturn = getPortReturn(entry.getKey(), entry.getValue());
			avgPortReturn += portReturn;
			dsd += Math.pow(Double.min(0, portReturn - prt), 2);
		}

		avgPortReturn /= chromo.size();
		dsd = Math.sqrt(dsd);
		sortino = (avgPortReturn - prt)/dsd;

		return sortino;
	}

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
					int newUnit = (Search.r.nextInt(9) + 1);
					portfolio = new String(tempArray) + newUnit;
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
		int k = Parameters.popSize/10;

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
			int[] selectedMembers = new int[k];

			// Select k members for tournament
			for(int i=0; i < k; i++)
			{
				randnum = Search.r.nextDouble();
				selectedMembers[i] = (int) (randnum * Parameters.popSize);
			}

			// Find best member as parent
			int bestChromoIndex = 0;
			for(int i=1; i < k; i++)
			{
				if (Search.member[selectedMembers[i]].rawFitness < Search.member[selectedMembers[bestChromoIndex]].rawFitness)
					bestChromoIndex = i;
			}

			return selectedMembers[bestChromoIndex];
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

			case 1:     // Two Phase Crossover
				Map <String, String> newChromo1 = new HashMap<>();
				Map <String, String> newChromo2 = new HashMap<>();

				int randInt = Search.r.nextInt(parent1.chromo.size() - 1) + 1;
				int iterated = 0;

				String key;
				String value;

				// switch values at random point randInt from parent1
				for(Map.Entry<String, String> entry : parent1.chromo.entrySet()) {
					key = entry.getKey();
					value = entry.getValue();
					if (iterated < randInt) {
						newChromo1.put(key, value);
					} else {
						newChromo2.put(key, value);
					}

					iterated += 1;
				}

				iterated = 0;

				// switch values at random point randInt from parent2
				for(Map.Entry<String, String> entry : parent2.chromo.entrySet()) {
					key = entry.getKey();
					value = entry.getValue();
					if (iterated < randInt) {
						newChromo2.put(key, value);
					} else {
						newChromo1.put(entry.getKey(), entry.getValue());
					}

					iterated += 1;
				}

				while(newChromo1.size() < 5) {
					List<String> rand = getRand();
					newChromo1.put(rand.get(0), rand.get(1));
				}

				while(newChromo2.size() < 5) {
					List<String> rand = getRand();
					newChromo2.put(rand.get(0), rand.get(1));
				}

				child1.chromo = newChromo1;
				child2.chromo = newChromo2;

				break;
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
