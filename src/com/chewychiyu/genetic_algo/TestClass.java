package com.chewychiyu.genetic_algo;

public class TestClass {
	public static void main(String[] args){
		new TestClass();
	}
	
	final String target_ = "abcedfghijklmnoprstuvwzyz";
	final int pool_size_ = 200;
	final double mutation_rate_ = 0.3;
	final int fitness_scale_ = 6;
	
	TestClass(){
		DataPool pool = new DataPool(pool_size_, target_, mutation_rate_,fitness_scale_);
		pool.cycle_.start();
	}
}
