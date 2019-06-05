/**
 * 
 */
package com.company.drone;

/**
 * @author Camellia Debnath
 * 30-Apr-2019
 */
public class Coordinates {
	private int x;
	private int y;
	/**
	 * @return the x
	 */
	public int getX() {
		return x;
	}
	/**
	 * @param x the x to set
	 */
	public void setX(int x) {
		this.x = x;
	}
	/**
	 * @return the y
	 */
	public int getY() {
		return y;
	}
	/**
	 * @param y the y to set
	 */
	public void setY(int y) {
		this.y = y;
	}
	
	public static int manhattanDistance(int x, int y) {
		return (Math.abs(x) + Math.abs(y)); 
	}
	
	public static double euclideanDistance(int x, int y) {
		return (Math.sqrt(x*x + y*y));
		
	}

}
