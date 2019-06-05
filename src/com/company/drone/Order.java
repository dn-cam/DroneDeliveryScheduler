/**
 * 
 */
package com.company.drone;

import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * @author Camellia Debnath 30-Apr-2019
 */
public class Order {

	private String orderID;
	private LocalDateTime orderTime;
	
	private Coordinates coords = new Coordinates();
	private int manhattanDistance;
	private Double euclideanDistance;
	private LocalDateTime droneDepartTime;
	private Double deliveryDelay;
	private LocalDateTime finishingTimes;
	
	//can be either 'N' for neutral, 'P' for Promoter ,or 'D' for detractor
	private char customerRating;



	// this is technically not required for calculations
	// but it should be part of an order
	private LocalDateTime deliveryTime;

	public Order(String orderID, LocalDateTime orderTime) {
		this.orderID = orderID;
		this.orderTime = orderTime;

	}

	/**
	 * @return the orderID
	 */
	public String getOrderID() {
		return orderID;
	}

	/**
	 * @param orderID the orderID to set
	 */
	public void setOrderID(String orderID) {
		this.orderID = orderID;
	}

	/**
	 * @return the orderTime
	 */
	public LocalDateTime getOrderTime() {
		return orderTime;
	}

	/**
	 * @param orderTime the orderTime to set
	 */
	public void setOrderTime(LocalDateTime orderTime) {
		this.orderTime = orderTime;
	}

	/**
	 * @return the deliveryTime
	 */
	public LocalDateTime getDeliveryTime() {
		return deliveryTime;
	}

	/**
	 * @param deliveryTime the deliveryTime to set
	 */
	public void setDeliveryTime(LocalDateTime deliveryTime) {
		this.deliveryTime = deliveryTime;
	}

	/**
	 * @return the coords
	 */
	public Coordinates getCoords() {
		return coords;
	}

	/**
	 * @param coords the coords to set
	 */
	public void setCoords(String yaxis, int y, String xaxis, int x) {
		if (yaxis.equalsIgnoreCase("N"))
			this.coords.setY(y);
		else
			this.coords.setY((-1) * y);

		if (xaxis.equalsIgnoreCase("E"))
			this.coords.setX(x);
		else
			this.coords.setX((-1) * x);

	}


	/**
	 * @return the manhattanDistance
	 */
	public int getManhattanDistance() {
		return manhattanDistance;
	}

	/**
	 * @param manhattanDistance the manhattanDistance to set
	 */
	public void setManhattanDistance(int manhattanDistance) {
		this.manhattanDistance = manhattanDistance;
	}

	/**
	 * @return the euclideanDistance
	 */
	public Double getEuclideanDistance() {
		return euclideanDistance;
	}

	/**
	 * @param euclideanDistance the euclideanDistance to set
	 */
	public void setEuclideanDistance(Double euclideanDistance) {
		this.euclideanDistance = euclideanDistance;
	}

	/**
	 * @return the droneDepartTime
	 */
	public LocalDateTime getDroneDepartTime() {
		return droneDepartTime;
	}

	/**
	 * @param droneDepartTime the droneDepartTime to set
	 */
	public void setDroneDepartTime(LocalDateTime droneDepartTime) {
		this.droneDepartTime = droneDepartTime;
	}

	/**
	 * @return the deliveryDelay
	 */
	public Double getDeliveryDelay() {
		return deliveryDelay;
	}

	/**
	 * @param deliveryDelay the deliveryDelay to set
	 */
	public void setDeliveryDelay(Double deliveryDelay) {
		this.deliveryDelay = deliveryDelay;
	}

	/**
	 * @return the customerRating
	 */
	public char getCustomerRating() {
		return customerRating;
	}

	/**
	 * @param customerRating the customerRating to set
	 */
	public void setCustomerRating(char customerRating) {
		this.customerRating = customerRating;
	}

	/**
	 * @return the finishingTimes
	 */
	public LocalDateTime getFinishingTimes() {
		return finishingTimes;
	}

	/**
	 * @param finishingTimes the finishingTimes to set
	 */
	public void setFinishingTimes(LocalDateTime finishingTimes) {
		this.finishingTimes = finishingTimes;
	}
	
	
	

}
