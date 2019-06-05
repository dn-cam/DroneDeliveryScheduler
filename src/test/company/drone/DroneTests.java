/**
 * 
 */
package test.company.drone;

import java.util.*;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Assert.*;

import com.company.drone.DroneScheduler;
import com.company.drone.Order;
import com.company.drone.OrderInputFileParser;

import org.junit.Test;

/**
 * @author Camellia Debnath 01-May-2019
 */
public class DroneTests {
	

	/**
	 * Should be a promoter
	 */
	@Test
	public void testPromoter() {
		Order order = OrderInputFileParser.createOrderPojo("WM001", "9:00:00", "N3E5");
		List<Order> orders = new ArrayList<Order>();
		orders.add(order);
		DroneScheduler sch = new DroneScheduler();
		sch.schedule(orders);
		assertTrue(orders.get(0).getCustomerRating() == 'P');
	}
	
	/**
	 * Should be a detractor since it comes after the deadline
	 */
	@Test
	public void testDetractor() {
		Order order = OrderInputFileParser.createOrderPojo("WM001", "23:00:00", "N3E5");
		List<Order> orders = new ArrayList<Order>();
		orders.add(order);
		DroneScheduler sch = new DroneScheduler();
		sch.schedule(orders);
		assertTrue(orders.get(0).getCustomerRating() == 'D');
	}
	
	/**
	 * All orders should be promoters
	 */
	@Test
	public void testNormalOrders() {
		List<Order> orders = new ArrayList<Order>();
		orders.add(OrderInputFileParser.createOrderPojo("WM001", "6:40:00", "N8E8"));
		orders.add(OrderInputFileParser.createOrderPojo("WM002", "6:40:01", "N10E10"));
		orders.add(OrderInputFileParser.createOrderPojo("WM003", "6:40:03", "N9E10"));
		
		DroneScheduler sch = new DroneScheduler();
		
		sch.schedule(orders);
		assertTrue(orders.get(0).getCustomerRating() == 'P' && orders.get(1).getCustomerRating() == 'P' && orders.get(2).getCustomerRating() == 'P');
	}
	
	
	/**
	 * Order comes in earlier than the last order, but is not promoter since it's further 
	 */
	@Test
	public void testOneNeutral() {
		List<Order> orders = new ArrayList<Order>();
		orders.add(OrderInputFileParser.createOrderPojo("WM001", "6:40:00", "N8E8"));
		orders.add(OrderInputFileParser.createOrderPojo("WM002", "6:40:01", "N99E100"));
		orders.add(OrderInputFileParser.createOrderPojo("WM003", "6:40:03", "N9E10"));
		
		DroneScheduler sch = new DroneScheduler();
		
		sch.schedule(orders);
		assertTrue(orders.get(0).getCustomerRating() == 'P' && orders.get(1).getCustomerRating() == 'N' && orders.get(2).getCustomerRating() == 'P');
	}
	
	
	/**
	 * Order comes in earlier than the last order, but is not delivered since it's outside radius 
	 */
	@Test
	public void testOneNotDelivered() {
		List<Order> orders = new ArrayList<Order>();
		orders.add(OrderInputFileParser.createOrderPojo("WM001", "6:40:00", "N8E8"));
		orders.add(OrderInputFileParser.createOrderPojo("WM002", "6:40:01", "N1000E100"));
		orders.add(OrderInputFileParser.createOrderPojo("WM003", "6:40:03", "N9E10"));
		
		DroneScheduler sch = new DroneScheduler();
		
		sch.schedule(orders);
		assertTrue(orders.get(0).getCustomerRating() == 'N' && orders.get(1).getCustomerRating() == 'O' && orders.get(2).getCustomerRating() == 'P');
	}

}
