package networkflows.planner;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.HashSet;
import java.util.Set;

import au.com.bytecode.opencsv.CSVReader;

public class DataProductionPlanner {
	private Set<CompNode> nodes = new HashSet<CompNode>();
	private Set<NetworkLink> links;
	
	//add logger
	private static final Logger logger = Logger.getLogger( DataProductionPlanner.class.getName() );
	private static FileHandler fh;
	private static SimpleFormatter formatter;
	
	//constructor
	public DataProductionPlanner() { 
		
        // Configure the logger with handler and formatter 
	    try {   
	    	DataProductionPlanner.fh = new FileHandler("log/DataProductionPlanner.log");  
	        DataProductionPlanner.logger.addHandler(DataProductionPlanner.fh);
	        DataProductionPlanner.formatter = new SimpleFormatter();  
	        DataProductionPlanner.fh.setFormatter(DataProductionPlanner.formatter);  
	        DataProductionPlanner.logger.setLevel(Level.FINE); 
	    } catch (SecurityException e) {  
	        e.printStackTrace();  
	    } catch (IOException e) {  
	        e.printStackTrace();  
	    }  		
	    DataProductionPlanner.logger.log( Level.FINE, "DataProductionPlanner Instantiated");
	}
	
	public void ReadNodesFromFile(String nodesFileName){
		CSVReader reader = null;
		//variables for further conversion
    	int id;
    	String name;
    	Boolean isInputSource;
    	Boolean isOutputDestination;
    	Boolean isInputDestination;
    	Boolean isOutputSource;
    	int inputWeight;
		try
        {
            //Get the CSVReader instance with specifying the delimiter to be used
			reader = new CSVReader(new FileReader(nodesFileName),',');
            String [] nextLine;
            //Read one line at a time
            while ((nextLine = reader.readNext()) != null)
            {
            	if (nextLine[0].charAt(0) != '#'){//skip commented lines
            	
	            	if (nextLine.length != 7) {  //check number of records for each node
	            		DataProductionPlanner.logger.log( Level.SEVERE, "Wrong nodes input file format: " + nodesFileName);
	            		throw new IOException();
	            	}
	            	//convert String to needed formats
	            	id = Integer.parseInt(nextLine[0]);
	            	name = nextLine[1];
	            	isInputSource = Boolean.valueOf(nextLine[2]);
	            	isOutputDestination = Boolean.valueOf(nextLine[3]);
	            	isInputDestination = Boolean.valueOf(nextLine[4]);
	            	isOutputSource = Boolean.valueOf(nextLine[5]);
	            	inputWeight = Integer.parseInt(nextLine[6]);
	            	
	            	
	            	
	            	CompNode node = new CompNode(id, name, isInputSource, isOutputDestination, isInputDestination,isOutputSource,inputWeight);
	            	this.nodes.add(node);
	            	System.out.println(node.toString());
            	}
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
		System.out.println(this.nodes.toString());
    }

	
	

}
