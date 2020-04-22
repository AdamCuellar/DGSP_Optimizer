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

        int currentRank = ranks.get(pos);

        // exit if we've already calculated the fitness
        if(X.rawFitness > 0) {
            return;
        }

        X.rawFitness = 0;
        List<Integer> values;
        float numerator;
        float denom;
        if(currentRank == 1){
            values = new ArrayList<>(ranks.values());
            numerator = (values.size() - Collections.frequency(values, currentRank));
            denom = (Parameters.popSize);
            X.rawFitness = (numerator/(denom + 1.0));
            int i = 0;
        }
        else {
            double fitness = 0;
            for(int j = 0; j < ranks.size(); j++) {
                if(j == pos) {continue;}
                if(ranks.get(j) < currentRank) {
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
            X.rawFitness+=1;
//            X.rawFitness = 1.0/(X.rawFitness);
            int i = 0;
        }

        return;
    }


//  PRINT OUT AN INDIVIDUAL GENE TO THE SUMMARY FILE *********************************

        public void doPrintGenes (Chromo X, FileWriter output) throws java.io.IOException {
            	output.write("\n");
            	return;
        }

/*******************************************************************************
 *                             STATIC METHODS                                   *
 *******************************************************************************/

    }   // End of OneMax.java ******************************************************

