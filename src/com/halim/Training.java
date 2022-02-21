package com.halim;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;


import java.util.ArrayList;
import java.util.Collections;

/**
 * @author Halim Zaaim 
 * 
 * 
 * class used to train the agent on a given env
 *
 */
public class Training {
	
	private boolean gameOver;
	private Agent agent;
	private int trials;
	private int maxStepsPerEpisode;
	
	public Training(Agent agent, int trials, int maxStepsPerEpisode) {
		super();
		this.agent = agent;
		this.trials = trials;
		this.maxStepsPerEpisode = maxStepsPerEpisode;
	}
	
	public void train() {
		System.out.println("=============== start training =============");
		Map<Integer,Double> rewardPerEpisod = new LinkedHashMap<>();
		int trial = 0;

		while(trial<trials) {
			int step = 0;
			this.gameOver = false;
			Double cumulativeReward = 0D;
			while(step < this.maxStepsPerEpisode && !gameOver) {
			
				Pair oldLocation = agent.getCurrentLocation();
				Action action = agent.chooseAction();
				Double reward = agent.getEnv().move(action);
				Pair newLocation = agent.getCurrentLocation();
				
				// learn 
				 agent.learn(newLocation, oldLocation, reward, action);
				 step++;
				 cumulativeReward += reward; 
				 if(this.agent.getEnv().isCurrentStateTerminal() ) {
					 this.gameOver = true;
					 this.agent.getEnv().initGrid();
				 }
				
				// System.out.println("ACTION SHOOSED : "+action +" , current state : "+ agent.getCurrentLocation());
					
			}
			trial++;
			rewardPerEpisod.put(trial, cumulativeReward);
			
		}
		
		System.out.println("=============== Done training ==============");
		printFinalQtable();
		plot(rewardPerEpisod);
		
		
	}
    private void plot(Map<Integer,Double> rewardPerEpisod) {
        Double[] dyData = new Double[rewardPerEpisod.values().size()];
        Double [] dxData = new Double[rewardPerEpisod.keySet().size()];
        List<Double> lis = new ArrayList();
        for(Integer in : rewardPerEpisod.keySet()) {
        	lis.add(new Double(in));
        }
        for(int i = 0 ; i < lis.size(); i++) {
        	dyData[i] = lis.get(i);
  
        }
        List<Double> dd = new ArrayList();
        for(Double d : rewardPerEpisod.values()) {
        	dd.add(d);
        }
        for(int i = 0 ; i < dd.size(); i++) {
        	dxData[i] = dd.get(i);
        }
       
        double[] yData = new double[lis.size()];
        double[] xData = new double[lis.size()];
        for(int i = 0 ; i < lis.size(); i++) {
        	yData[i] = dyData[i];
        	xData[i] = (double)dxData[i];
        }
       
        XYChart chart = QuickChart.getChart("acumulated reward ", "trial", "reward", "y(x)",  yData,xData);
     
        // Show it
        new SwingWrapper(chart).displayChart();
     
    }
	private void printLearnedPolicy() {
		StringBuilder builder = new StringBuilder();
		for(Map.Entry<Pair, Map<Action, Double>> entry : this.agent.getqTable().entrySet()) {
				Map<Action, Double> values = entry.getValue();
				Action key = Collections.max(values.entrySet(), Map.Entry.comparingByValue()).getKey();
				builder.append(key.toString() + " -->");
		
		}

		System.out.println(builder.toString());
	}

	private void printFinalQtable() {
		for(Map.Entry<Pair, Map<Action, Double>> entry : this.agent.getqTable().entrySet()) {
			Map<Action, Double> values = entry.getValue();
			System.out.print(entry.getKey().toString() +" :  ");
			StringBuilder builder  = new StringBuilder();
			for(Map.Entry<Action, Double> entry1 : values.entrySet()) {
				builder.append(entry1.getKey()+ " : "+entry1.getValue()+" , ");
				
			}
			System.out.print(builder.toString());
			System.out.print("\n");
		}
		
	}
	
	

}
