package com.chewychiyu.genetic_algo;

import com.chewychiyu.random.Random;

/*
 * DataPool is used to store Data objects to handle them with
 * genome algorithm. Selection and processing is done here with
 * cycle_ thread and sequence_ runnable
 */
public class DataPool{

	/* Data Object array */
	Data[] data_array_;

	/* target object */
	String target_;

	/* Generation */
	float generation_;

	/* global mutation rate */
	double mutation_rate_;

	/* sequence cycle and loop */
	Thread cycle_;
	Runnable sequence_;

	/* cycle sleep ( final ) */
	//final int CYCLE_SLEEP_ = 1;

	/* cycle running bool */
	boolean run_cycle_;

	/* max fitness , linked to target */
	final double MAX_FITNESS_;

	/* scale of fitness */
	final int fitness_scale_;

	/* best data selection */
	Data _best_of_set = null;
	
	/* basic constructor */
	public DataPool(int _size, String _target, double _mutation_rate, int _fitness_scale){
		data_array_ = new Data[_size];
		target_ = _target;
		fitness_scale_ = _fitness_scale;
		MAX_FITNESS_ = (double) Math.pow(_fitness_scale,target_.length());
		mutation_rate_ = _mutation_rate;
		generation_ = 0f;
		_fill_array();
		sequence_ = () -> _sequence();
		run_cycle_ = true;
		cycle_ = new Thread(sequence_);
	}

	/* This is where the sequence of the genome is tested and apply mutation and fitness
	 * Sequence : manage fitness -> selection -> mutation 
	 * control the time of cycles with CYCLE_SLEEP_  	 
	 */
	public void _sequence(){
		while(run_cycle_){
			_selection_process();
			_mutate_array();
			_calc_fit();
			_verbose();
			generation_++;
			//try{Thread.sleep(CYCLE_SLEEP_);}catch(Exception e) { }	
		}
	}

	/*
	 * The core feature of the genetic algro. Selecting the data fitness
	 * at random with applied weights with a higher probability with higher fitness
	 * Calculating weight array from data_array_ fitness elements and
	 * repopulating new geneation with two selected (Cropping half and half)
	 */
	public void _selection_process(){
		double[] _weight_array = new double[data_array_.length];
		int[] _index_array = new int[data_array_.length];
		for(int _i = 0; _i < _weight_array.length; _i++){
			_weight_array[_i] = ((double) data_array_[_i].fitness_) / _total_fitness();
			_index_array[_i] = _i;
		}
		Data _select_a = data_array_[Random._pick_weighted(_index_array, _weight_array)];
		Data _select_b = data_array_[Random._pick_weighted(_index_array, _weight_array)];	
		_fill_array(_select_a,_select_b);
	}


	/* Get total fitness of data pool */
	public float _total_fitness(){
		float _sum = 0;
		for(Data _d : data_array_){
			_sum+=_d.fitness_;
		}
		return _sum;
	}

	/*
	 * Calculating the fitness of each data object 
	 * Breaking the cycle if there is an element with complete fitness 
	 *  ( target has been reached ) 
	 */
	public void _calc_fit(){
		for(Data _d : data_array_){
			if(_best_of_set == null || _d._calc_fitness(target_,fitness_scale_) > _best_of_set.fitness_){
				_best_of_set = _d;
			}
			if(_d._calc_fitness(target_,fitness_scale_) == MAX_FITNESS_){
				run_cycle_ = false;
			}
		}
	}


	/* fill data array */
	public void _fill_array(){
		for(int _i = 0; _i < data_array_.length; _i++){
			data_array_[_i] = new Data(target_.length());
		}
	}
	/* fill data array with preset data */
	public void _fill_array(Data _a, Data _b){
		for(int _i = 0; _i < data_array_.length; _i++){
			Data _next_gen = _a._merge_with(_b, target_);
			data_array_[_i] = new Data(_next_gen.observable_);
		}
	}

	/* check for mutation of data array */
	public void _mutate_array(){
		for(Data _d : data_array_){
			_d._mutate(mutation_rate_);
		}
	}

	/* verbose of data pool */
	public void _verbose(){
		//for(Data _d : data_array_){
		//	System.out.println(_d);
		//}
		System.out.println("gen : " + generation_ + " pool size: " + data_array_.length + ", target: " + target_ + "\n" + " best : " + _best_of_set + "\n");
	}
}
