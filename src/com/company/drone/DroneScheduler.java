/**
 * 
 */
package com.company.drone;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

import com.company.Interfaces.TaskScheduler;

/**
 * @author Camellia Debnath 30-Apr-2019
 */
public class DroneScheduler implements TaskScheduler {
	public static final LocalDateTime START_TIME = LocalDate.now().atTime(6, 0, 0);
	public static final LocalDateTime END_TIME = LocalDate.now().atTime(22, 0, 0);
	
	//delivery radius 
	public static final int MAX_X = 100;
	public static final int MAX_Y = 100;
	private int NPS = 0;

	/**
	 * Read the inputfile populate orderlist return orderlist to be used by schedule
	 * method
	 */
	@Override
	public List<Order> readOrder(String inputfile) {
		OrderInputFileParser parser = new OrderInputFileParser();
		List<Order> orderList = parser.readOrder(inputfile);
		return orderList;

	}

	@Override
	public List<Order> schedule(List<Order> orderList) {

		List<Order> deliveredOrders = new ArrayList<Order>();
		List<Order> failedOrders = new ArrayList<Order>();
		
		
		
		//Calculate finishing times
		for (int k = 0; k < orderList.size(); k++) {
			Order x = orderList.get(k);
			LocalDateTime clockval = x.getOrderTime();
			double doubleVal = 2 * x.getEuclideanDistance();
			int totalmins = (int) doubleVal;
			int hours = totalmins / 60;
			int mins = totalmins % 60;
			int secs = (int) ((doubleVal - totalmins) * 60);

			clockval = clockval.plusHours(hours);
			clockval = clockval.plusMinutes(mins);
			clockval = clockval.plusSeconds(secs);
			
			x.setFinishingTimes(clockval);
		}
		
		List<Order> sortedOrderlist = orderList.stream()
				.sorted((a, b) -> a.getFinishingTimes().compareTo(b.getFinishingTimes()))
				.collect(Collectors.toList());

		LocalDateTime clock = START_TIME;
		
		for (int k = 0; k < sortedOrderlist.size(); k++) {
			Order x = sortedOrderlist.get(k);
			LocalDateTime clockval = clock;
			x.setDeliveryDelay(2 * x.getEuclideanDistance());
			double doubleVal = x.getDeliveryDelay();
			int totalmins = (int) doubleVal;
			int hours = totalmins / 60;
			int mins = totalmins % 60;
			int secs = (int) ((doubleVal - totalmins) * 60);

			if(clock.isAfter(x.getOrderTime())) {
				clock = clock.plusHours(hours);
				clock = clock.plusMinutes(mins);
				clock = clock.plusSeconds(secs);
			} else {
				clock = x.getOrderTime();
				clock = clock.plusHours(hours);
				clock = clock.plusMinutes(mins);
				clock = clock.plusSeconds(secs);
			}
			
			if (clock.isBefore(END_TIME)  && x.getCoords().getX() <= MAX_X && x.getCoords().getY() <= MAX_Y) {
				x.setDroneDepartTime(clockval);
				deliveredOrders.add(x);
			}

			else if(x.getCoords().getX() <= MAX_X && x.getCoords().getY() <= MAX_Y) {
				failedOrders.add(x);
			}
			
			else { // when order is outside delivery radius
				x.setCustomerRating('O');
			}

		}
		
		//call function to take care of prev days orders 
		String failedOrdersFile = "FailedOrders_" + LocalDateTime.now().toString();
		
		if(failedOrders.size() > 0) {
			try {
				writeFailedOrdersToFile(failedOrders, failedOrdersFile);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		this.setNPS(this.calculateNPS(deliveredOrders, failedOrders));
		return deliveredOrders;
		
	}

	@Override
	public void printSchedule(String filename, List<Order> orderList, int nps) {
		OrderInputFileParser parser = new OrderInputFileParser();
		try {
			parser.writeOrderSchedule(filename, orderList, nps);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.walmart.drone.WalmartScheduler#calculateNPS(java.util.List)
	 */
	@Override
	public int calculateNPS(List<Order> deliveredOrders, List<Order> failedOrders) {
		int promoters = 0;
		int detractors = 0;
		int neutral = 0;
		
		for(int i = 0; i<deliveredOrders.size(); i++) {
			double doubleVal = deliveredOrders.get(i).getEuclideanDistance();
			int totalmins = (int) doubleVal;
			int hours = totalmins / 60;
			int mins = totalmins % 60;
			int secs = (int) ((doubleVal - totalmins) * 60);
			LocalDateTime orderDeliveryTime = deliveredOrders.get(i).getDroneDepartTime().plusHours(hours);
			
			orderDeliveryTime = orderDeliveryTime.plusMinutes(mins);
			orderDeliveryTime = orderDeliveryTime.plusSeconds(secs);
			deliveredOrders.get(i).setDeliveryTime(orderDeliveryTime);

			//System.out.println(doubleVal);
			long timdiff = LocalDateTime.from(deliveredOrders.get(i).getOrderTime()).until(orderDeliveryTime, ChronoUnit.HOURS);
			if(timdiff <= 1) {
				promoters++;
				deliveredOrders.get(i).setCustomerRating('P');
			} else if (timdiff <= 3  && timdiff > 1) {
				neutral++;
				deliveredOrders.get(i).setCustomerRating('N');
			} else {
				detractors++;
				deliveredOrders.get(i).setCustomerRating('D');
			}
		}
		
		failedOrders.forEach(x -> x.setCustomerRating('D'));
		
		detractors += failedOrders.size();
		double total = promoters + detractors + neutral;
		double nps = 100*((promoters - detractors)/total);
		
		if(nps >= 0)
			return (int) (nps);
		else return 0;
	}
	
	
	public void writeFailedOrdersToFile(List<Order> failedOrders, String failedOrdersFile) throws IOException {
        FileWriter fileWriter = new FileWriter(failedOrdersFile);
        PrintWriter printWriter = new PrintWriter(fileWriter);

        for(int i = 0; i< failedOrders.size(); i++) {
        	try {
        		
        		String orderid = "WM"+failedOrders.get(i).getOrderID();
        		failedOrders.get(i).setOrderTime(failedOrders.get(i).getOrderTime().plusDays(1));
        		String timestamp = failedOrders.get(i).getOrderTime().toString();
        		int location_x = failedOrders.get(i).getCoords().getX();
        		int location_y = failedOrders.get(i).getCoords().getY();
        		String location;
        		
        		if(location_y >= 0)
        			location = "N"+location_y;
        		else location = "S"+location_y;
        		
        		if(location_x >= 0)
        			location = location+"E"+location_x;
        		else location = location+"W"+location_x;
        		
        		String line = orderid + " " + location + " " + timestamp + "\n";
        		//System.out.println("Delivery couldn't be completed: "+line);
        		
                printWriter.println(line);
            } catch (Exception ex) {
                System.out.println("error");
            }
        }
          
        fileWriter.close();
	}

	/**
	 * @return the nPS
	 */
	public int getNPS() {
		return NPS;
	}

	/**
	 * @param nPS the nPS to set
	 */
	public void setNPS(int nPS) {
		NPS = nPS;
	}

	

}
