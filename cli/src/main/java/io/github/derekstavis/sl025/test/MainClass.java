package io.github.derekstavis.sl025.test;

import org.apache.commons.cli.*;

public class MainClass {

	private static final String OPTION_PORT = "port";
	private static final String OPTION_PORT_ARGUMENT = "PORT";
	
	public static void main(String[] args) {
        Options options = new Options();
        CommandLineParser parser = new GnuParser();
		
        CommandLine commandLine;
		generateOptions(options);
	
        try
        {
            commandLine = parser.parse(options, args);

            if (commandLine.hasOption(OPTION_PORT))
            {
            	String port = commandLine.getOptionValue(OPTION_PORT);
            	
            	Sl025ModuleTest.onlineTest(port);
                
            } else {
            	
            	Sl025ModuleTest.offlineTest();
            
            }
            
        } catch (ParseException exception) {
        	 displayHelp(options);
        }
        
	}
	
	@SuppressWarnings("static-access")
	private static void generateOptions(Options options) {
		Option optionPort = 
        		OptionBuilder.withArgName(OPTION_PORT_ARGUMENT)
        		              .hasArg()
        		              .withDescription("Starts the app on this serial port")
        		              .create(OPTION_PORT);
        
		options.addOption(optionPort);
        
	}
	
	private static void displayHelp(Options options) {
		String header = "Runs the main class from SL025m Mifare Reader\n\n";
		 String footer = "Please report issues at github.com/derekstavis/sl025m-java";
		 
		 HelpFormatter formatter = new HelpFormatter();
		 formatter.printHelp("sl025m-java", header, options, footer, true);
	}
	
}