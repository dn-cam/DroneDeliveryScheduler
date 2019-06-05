/**
 * 
 */
package com.company.drone;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * @author Camellia Debnath 30-Apr-2019
 */
public class OrderInputFileParser {
	
    public static String WM = "WM(\\d+)";
    public static String COORD = "(S|N)(\\d+)(W|E)(\\d++)";
    public static String TIME = "(\\d+):(\\d+):(\\d+)";

	public List<Order> readOrder(String inputFile) {
		List<Order> orderList = new ArrayList<Order>();
		// WM001 N11W5 05:11:50
		try (Stream<String> stream = Files.lines(Paths.get(inputFile))) {

			for (Object line : stream.toArray()) {
				String[] orderString = ((String) line).split("\\s");

				Order order = createOrderPojo(orderString[0], orderString[2], orderString[1]);
				orderList.add(order);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return orderList;
	}

	public static Order createOrderPojo(String wmLabel, String timesInput, String coordsInput) {

		LocalDateTime now = LocalDateTime.now();
		ArrayList<Integer> timesInt = new ArrayList<>();
		String orderIndex = parseParts(WM, wmLabel).get(0);

		// Set times
		ArrayList<String> times = parseParts(TIME, timesInput);
		times.stream().forEach(t -> timesInt.add(Integer.parseInt(t)));
		LocalDateTime orderTime = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), timesInt.get(0),
				timesInt.get(1), timesInt.get(2));

		ArrayList<String> coords = parseParts(COORD, coordsInput);

		Order order = new Order(orderIndex, orderTime);
		order.setCoords(coords.get(0), Integer.parseInt(coords.get(1)), coords.get(2), Integer.parseInt(coords.get(3)));
		order.setManhattanDistance(Coordinates.manhattanDistance(order.getCoords().getX(), order.getCoords().getY()));
		order.setEuclideanDistance(Coordinates.euclideanDistance(order.getCoords().getX(), order.getCoords().getY()));
		return order;
	}

	public static ArrayList<String> parseParts(String regex, String target) {
		ArrayList<String> res = new ArrayList<>();
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(target);
		if (matcher.find()) {
			Stream<Integer> infiniteStream = Stream.iterate(1, i -> i + 1);
			infiniteStream.limit(matcher.groupCount()).forEach(n -> {
				res.add(matcher.group(n));
			});
		}
		return res;
	}
	
	
	public static void writeOrderSchedule(String filename, List<Order> orders, int NPS) throws IOException {
        FileWriter fileWriter = new FileWriter(filename);
        PrintWriter printWriter = new PrintWriter(fileWriter);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        for(int i = 0; i< orders.size(); i++) {
        	try {
        		
        		String orderid = "WM"+orders.get(i).getOrderID();
        		String timestamp = orders.get(i).getDroneDepartTime().format(formatter);
        		String line = orderid + " " + timestamp + "\n";
                printWriter.println(line);
            } catch (Exception ex) {
                System.out.println("error");
            }
        }
          
        printWriter.println("NPS "+NPS);
        fileWriter.close();
	}

}
