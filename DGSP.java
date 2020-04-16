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
			if (X(z).getRank() == 1){//FIXHERE modify to appropriate problem representation and/or getter method
				X.rawFitness = (Parameters.popSize - 1)/(Parameters.popSize + 1);
			}
		
				
					//Cp must dominate cq overall must end up with f(Cq) = 1 + Sum of f(Cp)
					
					else if (X(z).getRank() > 1){//modify to appropriate problem representation and/or getter method
						
						for (int y= 0; y < Parameters.popSize  ; y++){

							int dominatorCount = 1;//keep count of all individuals that dominate x(i)
							
							X(z).rawFitness+= X(dominatorCount-1).rawFitness;//Accumulate all fitness values of individuals that dominate x(i)
							
							dominatorCount++;

						}
						
						X(i).rawFitness += 1;

					}

			//CLEANUP if (X.chromo.charAt(z) == '1') X.rawFitness += 1;//old onemax fitness function

//		}
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

