/******************************************************************************
*  A Teaching GA					  Developed by Hal Stringer & Annie Wu, UCF
*  Version 2, January 18, 2004
*******************************************************************************/

import java.io.*;
import java.util.*;
import java.text.*;

public class DGSP extends FitnessFunction{

/*******************************************************************************
*                            INSTANCE VARIABLES                                *
*******************************************************************************/


/*******************************************************************************
*                            STATIC VARIABLES                                  *
*******************************************************************************/


/*******************************************************************************
*                              CONSTRUCTORS                                    *
*******************************************************************************/

	public DGSP(){
		name = "DGSP Problem";
	}

/*******************************************************************************
*                                MEMBER METHODS                                *
*******************************************************************************/

//  COMPUTE A CHROMOSOME'S RAW FITNESS *************************************
//FIXHERE Calculate using dominated fronts calculated by rank in selection function. Fitness Function =>(depends on) Selection Function
	public void doRawFitness(Chromo X){

		double nonDominatedFitness = 0;
		//To be done, precondition according to the paper is a need for a sorted rank aquired in the selection function
		X.rawFitness = 0;

		for (int z = 0; z < Parameters.popSize; z++){
			if (X.chromo.getRank(z) == 1){//FIXHERE modify to appropriate problem representation and/or getter method
				X.rawFitness = (Parameters.popSize - 1)/(Parameters.popSize + 1);
			}
			for (int y= 1; y < Parameters.popSize  ; y++){
				//X.chromo.rawFitness = 1 + X.DominatedBy(X.chromo.getRank(z)"ProblemRepresentation")/(Parameters.popSize + 1)//FIXHERE Adjust names as well as details to domination front, how does an individual dominate another
					//nonDominatedFitness += X.rawFitness.getRank(z);//Cp must dominate cq overall must end up with f(Cq) = 1 + Sum of f(Cp)
					if (X.chromo.getRank(y) > 1){//modify to appropriate problem representation and/or getter method
				
						//make nonDominaters temp var
						X.rawFitness+= X.rawFitness;//FIX HERE doublecheck any mistakes,
				

				}
			}
			//if (X.chromo.charAt(z) == '1') X.rawFitness += 1;//old onemax fitness function

		}
	}

//  PRINT OUT AN INDIVIDUAL GENE TO THE SUMMARY FILE *********************************

	public void doPrintGenes(Chromo X, FileWriter output) throws java.io.IOException{

		//for (int i=0; i<Parameters.numGenes; i++){
	//		Hwrite.right(X.getGeneAlpha(i),11,output);
	//	}
	//	output.write("   RawFitness");
//		output.write("\n        ");
	//	for (int i=0; i<Parameters.numGenes; i++){
	//		Hwrite.right(X.getPosIntGeneValue(i),11,output);
	//	}
	//	Hwrite.right((int) X.rawFitness,13,output);
	//	output.write("\n\n");
	//	return;
	}

/*******************************************************************************
*                             STATIC METHODS                                   *
*******************************************************************************/

}   // End of OneMax.java ******************************************************

