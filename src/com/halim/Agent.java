package com.halim;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * @author HALIM ZAAIM
 *
 */
public class Agent {
	private Double alpha;
	private Double gamma;
	private Double epsilon;
	private Grid env;
	Map<Pair,Map<Action,Double>> qTable;

	public Agent(Double alpha, Double gamma, Double epsilon, Grid env) {
		super();
		this.alpha = alpha;
		this.gamma = gamma;
		this.epsilon = epsilon;
		this.env = env; // grid
		initqTable();
	}
    private Map<Action,Double> initInnerMap(){
    	Map<Action,Double> innerMap = new LinkedHashMap<>();
	    innerMap.put(Action.UP, 0.0);innerMap.put(Action.DOWN, 0.0);
	    innerMap.put(Action.LEFT, 0.0);innerMap.put(Action.RIGHT, 0.0);
	    return innerMap;
    }
	private void initqTable() {
		
		this.qTable = new LinkedHashMap<>();
		for(int x = 0 ; x<env.getGridHeight(); x++ ) {
			boolean bool = false;
			for(int y=0; y<env.getGridWidth();y++ ) {
				if(!this.getEnv().getNotAllowedLoacations().contains(new Pair(x+1,y+1))) {
					
					bool = true;
				}
				if(bool) {
					Pair pair = new Pair(x+1,y+1);
					this.qTable.put(pair, initInnerMap());
					bool = false; 
				}
				
			}
		}
			
	}
	
	public Action chooseAction() { // 
		Action nextAction = null; 
		Random rand = new Random();
		
		if(rand.nextDouble() < this.epsilon) {
			nextAction = env.random_action(); // no exploration 
		}else { // with exploration 
			    // chose the action with max reward 
			Map<Action,Double> qValuesOfState = this.qTable.get(env.getCurrentLocation());
			Double max = Collections.max(qValuesOfState.values());
			List<Action> possiblesActions= new ArrayList<>();
			for(Map.Entry<Action, Double> entry : qValuesOfState.entrySet()) {
				if(entry.getValue().equals(max)) {
					possiblesActions.add(entry.getKey());
				}
			}
			int index = rand.nextInt(possiblesActions.size());
			nextAction = possiblesActions.get(index);
		}
		/*
		 * if(this.epsilon > 0) {// decrement epsilon for stopping the exploration when
		 * we get an optimal policy this.epsilon -= 0.000001; } if(this.epsilon < 0) {
		 * this.epsilon = 0D; }
		 */
		return nextAction;
	}
	
	// learning and updating values in qTable
	public void learn(Pair newLocation, Pair oldLocation, Double reward, Action action) {
		Map<Action,Double> qValuesOfState_new = this.qTable.get(newLocation);
		Map<Action,Double> qValuesOfState_old = this.qTable.get(oldLocation);
		Double maxValue_newLocation = Collections.max(qValuesOfState_new.values());
		Double maxValue_oldLocation = qValuesOfState_old.get(action);
		
		// new value to the corresponding action 
		Double learned = (1 - this.alpha) * maxValue_oldLocation + this.alpha * (reward + this.gamma * maxValue_newLocation);
		
		qValuesOfState_old.replace(action, learned);
		this.qTable.replace(oldLocation, this.qTable.get(oldLocation), qValuesOfState_old);
	}
	
	public Pair getCurrentLocation() {
		return this.env.getCurrentLocation();
	}
	public Grid getEnv() {
		return env;
	}
	public Map<Pair, Map<Action, Double>> getqTable() {
		return qTable;
	}
	
}
