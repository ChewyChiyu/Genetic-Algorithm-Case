package com.chewychiyu.genetic_algo;

import com.chewychiyu.random.Random;

/* An instance of this object ( new Data ) is used to store the fitness according to
 *  the current evolutionary status. Will be used to store to mutate and breed in random and
 *  specified order. 
 */
public class Data {

	double fitness_;

	String observable_;

	/*  Constructor of Data object, initialize variables
	 *  Using Random._ascii_string for random gen 0 observable of length
	 */
	public Data(int _data_length){
		fitness_ = 0;
		observable_ = Random._ascii_string(_data_length);
	}

	/* Constructor of Data object passing in preset string */
	public Data(String _data){
		observable_ = _data;
	}


	/*
	 *  Mutation function to get random variation of observable
	 *  string, using Random._integer_inclusive for random 
	 *  elements of string and changing it to _ascii_string element.
	 *  Only returns if passes _rate test
	 */
	public void _mutate(double _rate){
			if(Math.random() <= _rate){
				int _index = Random._integer_inclusive(0, observable_.length()-1);
				observable_ = observable_.substring(0, _index) + Random._ascii_string(1) + observable_.substring(_index+1);
			}
	}


	/*
	 * Calculating the fitness if the observable scaled to pow (exponential)
	 * Passing in the target and returns the fitness calculated
	 * for each character same in corrent index fitness_++;
	 */
	public double _calc_fitness(String target_, int _pow){
		int _temp_fit = 0;
		for(int _i = 0; _i < observable_.length(); _i++){
			if(observable_.charAt(_i)==target_.charAt(_i)){
				_temp_fit++;
			}
		}
		fitness_ = (double) Math.pow(_pow, _temp_fit);
		return fitness_;
	}


	/* Merge two data together ( cropping half and half ), used for 
	 * selection process , look for similarities to target, if none found pick
	 * random 
	 */
	public Data _merge_with(Data _d, String _target){
		int _split_index = Random._integer_inclusive(0, observable_.length());
		return new Data(observable_.substring(0, _split_index) + _d.observable_.substring(_split_index));
	}

	/* Basic to String method */
	public String toString(){
		return " fit : " + fitness_ + " observe : " + observable_;
	}

}
