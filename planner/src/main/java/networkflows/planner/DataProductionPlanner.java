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
	private Set<NetworkLink> links = new HashSet<NetworkLink>();
	
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
	        DataProductionPlanner.logger.setLevel(Level.FINEST); 
	    } catch (Exception e) {  
	        e.printStackTrace();  
	    } 	
	    DataProductionPlanner.logger.log( Level.INFO, "DataProductionPlanner Instantiated");
	}
	
	//Read Nodes
	public void ReadNodesFromFile(String nodesFileName){
		DataProductionPlanner.logger.log( Level.INFO, "Reading nodes from file: "+ nodesFileName);
		CSVReader reader = null;
		try {
            //Get the CSVReader instance with specifying the delimiter to be used
			reader = new CSVReader(new FileReader(nodesFileName),',');
			try {
	            String [] nextLine;
	            //Read one line at a time
	            while ((nextLine = reader.readNext()) != null){
	            	if (nextLine[0].charAt(0) != '#'){//skip commented lines	            	
		            	CompNode node = new CompNode(nextLine);
		            	this.nodes.add(node);
		            	DataProductionPlanner.logger.log( Level.FINEST, "Node red from file: "+node.toString());
		            	System.out.println(node.toString());
	            	}
	            }
	            DataProductionPlanner.logger.log( Level.INFO, "Nodes were red successfully");
	    		System.out.println(this.nodes.toString());
	        }
	        catch (Exception e) {
	            e.printStackTrace();
	            DataProductionPlanner.logger.log( Level.SEVERE, "Error while reading the nodes file : " + nodesFileName + " " + e.getMessage());
	        }			
        }
		catch (Exception e){
			 e.printStackTrace();
	         DataProductionPlanner.logger.log( Level.SEVERE, "Failed to read nodes file: "+ nodesFileName);
		}	
    }

	//Read Links
	public void ReadLinksFromFile(String linksFileName){
		DataProductionPlanner.logger.log( Level.INFO, "Reading links from file: "+ linksFileName);
		CSVReader reader = null;
		try {
            //Get the CSVReader instance with specifying the delimiter to be used
			reader = new CSVReader(new FileReader(linksFileName),',');
			try {
	            String [] nextLine;
	            //Read one line at a time
	            while ((nextLine = reader.readNext()) != null){
	            	if (nextLine[0].charAt(0) != '#'){//skip commented lines	            	
		            	NetworkLink link = new NetworkLink(nextLine);
		            	this.links.add(link);
		            	DataProductionPlanner.logger.log( Level.FINEST, "Link red from file: " + link.toString());
		            	System.out.println(link.toString());
	            	}
	            }
	            DataProductionPlanner.logger.log( Level.INFO, "Links were red successfully");
	    		System.out.println(this.links.toString());
	        }
	        catch (Exception e) {
	            e.printStackTrace();
	            DataProductionPlanner.logger.log( Level.SEVERE, "Error while reading the links file : " + linksFileName + " " + e.getMessage());
	        }			
        }
		catch (Exception e){
			 e.printStackTrace();
	         DataProductionPlanner.logger.log( Level.SEVERE, "Failed to read links file: "+ linksFileName);
		}	
    }
	

}
