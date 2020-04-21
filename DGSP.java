/******************************************************************************
 *  A Teaching GA					  Developed by Hal Stringer & Annie Wu, UCF
 *  Version 2, January 18, 2004
 *******************************************************************************/

import java.io.*;
import java.util.*;
import java.text.*;

public class DGSP extends FitnessFunction {

/*******************************************************************************
 *                            INSTANCE VARIABLES                                *
 *******************************************************************************/

/*******************************************************************************
 *                            STATIC VARIABLES                                  *
 *******************************************************************************/

    /*******************************************************************************
     *                              CONSTRUCTORS                                    *
     *******************************************************************************/

    public DGSP() {
        name = "DGSP Problem";
    }

    /*******************************************************************************
     *                                MEMBER METHODS                                *
     *******************************************************************************/

//  COMPUTE A CHROMOSOME'S RAW FITNESS *************************************
//FIXHERE Calculate using dominated fronts calculated by rank in selection function. Fitness Function =>(depends on) Selection Function
    public void doRawFitness(Chromo X, Map<Integer, Integer> ranks, int pos) {

        List<Integer> values = new ArrayList<>(ranks.values());
        int currentRank = ranks.get(pos);

        // exit if we've already calculated the fitness
        if(X.rawFitness > 0) {
            return;
        }

        X.rawFitness = 0;

        if(currentRank == 1){
            float numerator = (values.size() - Collections.frequency(values, currentRank));
            float denom = (Parameters.popSize + 1);
            X.rawFitness += (numerator/denom);
        }
        else {
            double fitness = 0;
            for(int j = 0; j < ranks.size(); j++) {
                if(j == pos) {continue;}
                if(ranks.get(j) <= (currentRank - 1)) {
                    if(Search.member[j].rawFitness > 0){
                        fitness = Search.member[j].rawFitness;
                        X.rawFitness += fitness;
                    }
                    else {
                        doRawFitness(Search.member[j], ranks, j);
                        fitness = Search.member[j].rawFitness;
                        X.rawFitness += fitness;
                    }
                }
            }

            X.rawFitness += 1;
        }

        return;
    }


//  PRINT OUT AN INDIVIDUAL GENE TO THE SUMMARY FILE *********************************

//        public void doPrintGenes (Chromo X, FileWriter output) throws java.io.IOException {
//
//            for (int i=0; i<Parameters.numGenes; i++){
//            		Hwrite.right(X.getGeneAlpha(i),11,output);
//            	}
//            	output.write("   RawFitness");
//		output.write("\n        ");
//            	for (int i=0; i<Parameters.numGenes; i++){
//            		Hwrite.right(X.getPosIntGeneValue(i),11,output);
//            	}
//            	Hwrite.right((int) X.rawFitness,13,output);
//            	output.write("\n\n");
//            	return;
//        }

/*******************************************************************************
 *                             STATIC METHODS                                   *
 *******************************************************************************/

    }   // End of OneMax.java ******************************************************

