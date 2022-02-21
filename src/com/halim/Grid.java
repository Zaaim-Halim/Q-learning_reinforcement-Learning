package com.halim;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

// assumption that the grid X and Y start from 1 
// X axis == ROW 
// Y axis == COLLUMN
/**
 * @author HALIM ZAAIM
 *
 */
public class Grid {
	
	private List<List<Double>> grid;
	private Integer gridWidth;
	private Integer gridHeight;
	private Action actions;
	private Pair currentLocation;
	private Pair destiniationLocation;
	
	private List<Pair> notAllowedLoacations; 
	private List<Pair> bombLocations;
	
	//private Pair terminalLocation;
	
	private Double dangerReward;
	private Double reward;

	public Grid(Integer gridWidth, Integer gridHeight, Pair currentLocation, Pair destiniationLocation,
			List<Pair> notAllowedLoacations, List<Pair> bombLocations,Double reward, Double dangerReward) {
		super();
		this.gridWidth = gridWidth;
		this.gridHeight = gridHeight;
		this.currentLocation = currentLocation;
		this.destiniationLocation = destiniationLocation;
		this.notAllowedLoacations = notAllowedLoacations;
		this.bombLocations = bombLocations;
		this.dangerReward = dangerReward;
		this.reward = reward;
		initGrid();
		System.out.println("created Grid : ");
		for(List<Double> l: grid) {
			System.out.println("      "+l.toString());
		}
		System.out.println();
	}

	public void initGrid() {
		// init Grid
		grid = new ArrayList<>();
		Double [] initialisation = new Double[this.gridWidth];
		for(int i = 0 ; i < this.gridWidth;i++) {
			initialisation[i]= this.reward ;
		}
		for(int i = 0 ; i < this.gridHeight;i++) {
			List<Double> row = new ArrayList<>(Arrays.asList(initialisation));
			grid.add(row);
		}
		
		//set bomb states reward to danger-reward 
		for(Pair pair : bombLocations) {
			grid.get(pair.getX()-1).set(pair.getY()-1, this.dangerReward);
		}
		for(Pair pair : notAllowedLoacations) {
			grid.get(pair.getX()-1).set(pair.getY()-1, 0D);
		}
		
		// set destination reward 
		grid.get(destiniationLocation.getX()-1).set(destiniationLocation.getY()-1, -dangerReward);
		
	}
    
	// agent arrived to destination  
	public boolean isArraivedToDestination() {
		if(currentLocation.equals(destiniationLocation)) {
			return true;
		}
		return false;
	}
	// return reward based on each state 
	private Double getReward(Pair location ) {
		
		return grid.get(location.getX()-1).get(location.getY()-1);
		
	}
	
	// is agent can make a move to a certain state
	// agent can't make a move if the next state is a not allowed state or location 
	boolean isnotInNotAllowedLocation(Pair Location) {
		if(this.notAllowedLoacations.contains(Location)) {
			return false;
		}
		return true;
		
	}
	
	// make a move from current state to the next state 
	// the direction is based on the parameter "ACtion"
	public Double move(Action action) {
		Pair last_location = new Pair(this.currentLocation.getX(),this.currentLocation.getY());
		Double reward = null;
		switch (action) {
		case UP:
			if (last_location.getX() == 1) {

				reward = this.getReward(last_location);

			} else if (isnotInNotAllowedLocation(new Pair(last_location.getX() - 1, last_location.getY()))) {
				this.currentLocation.setX(this.currentLocation.getX() - 1);
				reward = this.getReward(this.currentLocation);
			}
			break;

		case DOWN:
			if (last_location.getX() == this.gridHeight) {
				reward = this.getReward(last_location);

			} else if (isnotInNotAllowedLocation(new Pair(last_location.getX() + 1, last_location.getY()))) {

				this.currentLocation.setX(this.currentLocation.getX() + 1);
				reward = this.getReward(this.currentLocation);

			}
			break;

		case LEFT:

			if (last_location.getY() == 1) {
				reward = this.getReward(last_location);

			} else if (isnotInNotAllowedLocation(new Pair(last_location.getX(), last_location.getY() - 1))) {
				this.currentLocation.setY(this.currentLocation.getY() - 1);
				reward = this.getReward(this.currentLocation);
			}
			break;

		case RIGHT:
			if (last_location.getY() == this.gridWidth) {
				reward = this.getReward(last_location);

			} else if (isnotInNotAllowedLocation(new Pair(last_location.getX(), last_location.getY() + 1))) {
				this.currentLocation.setY(this.currentLocation.getY() + 1);
				reward = this.getReward(this.currentLocation);
			}

			break;

		default:
			break;
		}

		if (reward == null) {
			// give the agent nothing 
			reward = 0.0;
		}

		return reward;

	}
	
	public List<List<Double>> getGrid() {
		return grid;
	}

	public Action getActions() {
		return actions;
	}

	public Pair getCurrentLocation() {
		return currentLocation;
	}
	
	
	public Integer getGridWidth() {
		return gridWidth;
	}

	public Integer getGridHeight() {
		return gridHeight;
	}

	public Pair getDestiniationLocation() {
		return destiniationLocation;
	}

	public List<Pair> getNotAllowedLoacations() {
		return notAllowedLoacations;
	}

	public boolean isCurrentStateTerminal() {
		if (this.bombLocations.contains(this.currentLocation)) {
			return true;
		}
		else return false;
	}
	
	// Randomly choose an action from the 4 possible actions "UP DOWN LEFT RIGHT
	public Action random_action() {
		Random random = new Random();
		int i = random.nextInt(4);
		Action action = null;
		switch (i) {
		case 0:
			action = Action.UP;
			break;

		case 1:
			action = Action.DOWN;
			break;
		case 2:
			action = Action.LEFT;

			break;

		case 3:
			action = Action.RIGHT;

			break;
		}

		return action;
		
	}
	
}
