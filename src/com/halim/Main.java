package com.halim;

import java.util.ArrayList;
import java.util.List;
// entry 

/**
 * @author HALIM ZAAIM
 *
 */
public class Main {
    public void test1() {
    	List<Pair> notAllowedLoacations = new ArrayList<>();
		notAllowedLoacations.add(new Pair(2,2));
		notAllowedLoacations.add(new Pair(4,2));
		notAllowedLoacations.add(new Pair(4,3));
		
		List<Pair> bombLocations = new ArrayList<>();
		bombLocations.add(new Pair(3,4));
		
		Pair destination = new Pair(1,3);
		Pair start = new Pair(5,1);
		Double reward = -1D;
		Double dangerReward = -5D;
		Grid env = new  Grid(4, 5, start, destination,notAllowedLoacations,bombLocations,reward,dangerReward);
		
		Double alpha = 0.1; // learning Rate
		Double gamma = 0.85; // discount 
		Double epsilon = 0.05; // greedy policy function ( exploration )
		Agent agent = new Agent(alpha, gamma, epsilon, env);
		
		Training training = new Training(agent, 500, 1000);
		training.train();

    }
    
	public static void main(String[] args) {
		new Main().test1();
	}

}
