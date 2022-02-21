package com.halim;


/**
 * @author HALIM ZAAIM
 *
 */
public enum Action {
	UP(0),
	DOWN(1),
	LEFT(2),
	RIGHT(3);
	
	
	private final int ord;

	Action(int i) {
		this.ord = i;
	}
	public int getActionCode() {
        return this.ord;
    }

}
