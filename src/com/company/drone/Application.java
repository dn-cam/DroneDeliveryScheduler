/**
 * 
 */
package com.company.drone;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.apache.commons.cli.*;
import org.junit.runner.JUnitCore;


/**
 * @author Camellia Debnath
 * 30-Apr-2019
 */
public class Application {
	
	public static CommandLine getOptions(String[] args) {
	        Options options = new Options();

	        Option input = new Option("i", "input", true, "Input File Path");
	        input.setRequired(true);
	        options.addOption(input);
	        
	        Option output = new Option("o", "output", true, "Output File Path");
	        output.setRequired(true);
	        options.addOption(output);

	        
	        Option test = new Option("t", "test", true, "Run test cases");
	        test.setRequired(false);
	        options.addOption(test);

	        CommandLineParser parser = new DefaultParser();
	        HelpFormatter formatter = new HelpFormatter();
	        CommandLine cmd = null;

	        try {
	            cmd = parser.parse(options, args);
	        } catch (ParseException e) {
	            System.out.println(e.getMessage());
	            formatter.printHelp("Drone Scheduler", options);
	            System.exit(1);
	        }
	        return cmd;

	}
	
	public static void run(String[] args) {
		
        CommandLine cmd = getOptions(args);
        String inputFilePath = cmd.getOptionValue("input");
        String outputFilePath = cmd.getOptionValue("output");
        String testOption = cmd.getOptionValue("test");
		String filename = inputFilePath;
		DroneScheduler sched = new DroneScheduler();
		List<Order> orders = sched.readOrder(filename);
		List<Order> deliverySchedule = sched.schedule(orders);
		
		int nps = sched.getNPS();
		
		String outputFile = outputFilePath + LocalDateTime.now().toString();
		
		sched.printSchedule(outputFile, deliverySchedule, nps);
		System.out.println("output File path: "+outputFile);
		if(testOption != null && testOption.equalsIgnoreCase("y")) {
			System.out.println("\nRunning Test Cases:");
			JUnitCore.main("test.walmart.drone.DroneTests");
		}
	}
	
	public static void main(String args[]) {
		run(args);
	}
	
	
	

}
