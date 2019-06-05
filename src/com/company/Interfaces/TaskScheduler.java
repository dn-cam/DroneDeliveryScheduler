/**
 * 
 */
package com.company.Interfaces;

import java.util.List;

import com.company.drone.Order;

/**
 * @author Camellia Debnath
 * 30-Apr-2019
 */

public interface TaskScheduler {
	
	/**
	 * Reads the input file for orders for the scheduler 
	 * Returns an order POJO
	 */	
	public List<Order> readOrder(String inputfile);
	
	
	/**
	 * The primary method of a scheduler
	 * creates a schedule of drone departure times for deliveries
	 * returns a Schedule POJO 
	 */
	public List<Order> schedule(List<Order> orderList);
	
	/**
	 * Writes the final schedule to a file 
	 */	
	public void printSchedule(String filename, List<Order> orderList, int nps);

	/**
	 * calculates the net promoter score 
	 */	
	public int calculateNPS(List<Order> deliveredOrders, List<Order> failedOrders);

}
