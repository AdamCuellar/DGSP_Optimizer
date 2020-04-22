/******************************************************************************
*  A Teaching GA					  Developed by Hal Stringer & Annie Wu, UCF
*  Version 2, January 18, 2004
*******************************************************************************/

import java.io.*;
import java.util.*;
import java.text.*;

public class Parameters
{

/*******************************************************************************
*                            INSTANCE VARIABLES                                *
*******************************************************************************/

	public static String expID;
	public static String problemType;

	public static String dataInputFileName;

	public static int numRuns;
	public static int generations;
	public static int popSize;

	public static int genCap;
	public static int fitCap;

	public static String minORmax;
	public static int selectType;
	public static int scaleType;

	public static int xoverType;
	public static double xoverRate;
	public static int mutationType;
	public static double mutationRate;

	public static long seed;
	public static int numGenes;
	public static int geneSize;

	public static List<String> stockList;
	public static Map<Integer, List<Float>> yearReturns;

/*******************************************************************************
*                              CONSTRUCTORS                                    *
*******************************************************************************/

	public Parameters(String parmfilename) throws java.io.IOException{

		String readLine;
		BufferedReader parmInput = new BufferedReader(new FileReader (parmfilename));

		expID = parmInput.readLine().substring(30);
		problemType = parmInput.readLine().substring(30);

		dataInputFileName = parmInput.readLine().substring(30);
		stockList = parseStockList(dataInputFileName);
		yearReturns = parseStockData();

		numRuns = Integer.parseInt(parmInput.readLine().substring(30).trim());
		generations = Integer.parseInt(parmInput.readLine().substring(30).trim());
		popSize = Integer.parseInt(parmInput.readLine().substring(30).trim());

		selectType = Integer.parseInt(parmInput.readLine().substring(30).trim());
		scaleType = Integer.parseInt(parmInput.readLine().substring(30).trim());

		xoverType = Integer.parseInt(parmInput.readLine().substring(30).trim());
		xoverRate = Double.parseDouble(parmInput.readLine().substring(30).trim());
		mutationType = Integer.parseInt(parmInput.readLine().substring(30).trim());
		mutationRate = Double.parseDouble(parmInput.readLine().substring(30).trim());

		seed = Long.parseLong(parmInput.readLine().substring(30).trim());
		numGenes = Integer.parseInt(parmInput.readLine().substring(30).trim());
		geneSize = Integer.parseInt(parmInput.readLine().substring(30).trim());

		parmInput.close();

		if (scaleType==0 || scaleType==2) minORmax = "max";
		else minORmax = "min";

	}

/*******************************************************************************
*                                MEMBER METHODS                                *
*******************************************************************************/

	public static List<String> parseStockList(String filename) throws IOException {
		List<String> temp = new ArrayList<String>();
		String st;
		BufferedReader br = new BufferedReader(new FileReader(filename));

		while ((st = br.readLine()) != null)
			temp.add(st);

		if(temp.size() > 0) {
			Collections.sort(temp);
		}

		return temp;
	}

	public static Map<Integer, List<Float>> parseStockData() throws IOException {
		File folder = new File("./stockData/");
		File[] listOfFiles = folder.listFiles();
		BufferedReader br;
		String line;
		Map<Integer, List<Float>> tickerReturns = new HashMap<>();

		for(File file : listOfFiles){
			br = new BufferedReader(new FileReader(file));
			List<Float> yearReturns = new ArrayList<>();

			// skip the first line of header info
			br.readLine();

			while ((line = br.readLine()) != null)
			{
				String[] data = line.split(",");
				float close = Float.parseFloat(data[1]);
				float open = Float.parseFloat(data[0]);
				float rOr = (close/open) - (float) 1.0;
				yearReturns.add(rOr*100);
			}
			String name = file.getName().split("/")[0].replace(".csv","");
			tickerReturns.put(stockList.indexOf(name), yearReturns);
		}

		return tickerReturns;
	}


	/*******************************************************************************
*                             STATIC METHODS                                   *
*******************************************************************************/

	public static void outputParameters(FileWriter output) throws java.io.IOException{


		output.write("Experiment ID                :  " + expID + "\n");
		output.write("Problem Type                 :  " + problemType + "\n");

		output.write("Data Input File Name         :  " + dataInputFileName + "\n");

		output.write("Number of Runs               :  " + numRuns + "\n");
		output.write("Generations per Run          :  " + generations + "\n");
		output.write("Population Size              :  " + popSize + "\n");

		output.write("Selection Method             :  " + selectType + "\n");
		output.write("Fitness Scaling Type         :  " + scaleType + "\n");
		output.write("Min or Max Fitness           :  " + minORmax + "\n");

		output.write("Crossover Type               :  " + xoverType + "\n");
		output.write("Crossover Rate               :  " + xoverRate + "\n");
		output.write("Mutation Type                :  " + mutationType + "\n");
		output.write("Mutation Rate                :  " + mutationRate + "\n");

		output.write("Random Number Seed           :  " + seed + "\n");
		output.write("Number of Genes/Points       :  " + numGenes + "\n");
		output.write("Size of Genes                :  " + geneSize + "\n");

		output.write("\n\n");

	}
}   // End of Parameters.java **************************************************