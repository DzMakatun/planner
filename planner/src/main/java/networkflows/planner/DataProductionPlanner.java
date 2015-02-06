package networkflows.planner;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import au.com.bytecode.opencsv.CSVReader;

public class DataProductionPlanner {
	private Set<CompNode> nodes = new LinkedHashSet<CompNode>();
	private Set<NetworkLink> links = new LinkedHashSet<NetworkLink>();
	
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
		            	if (this.nodes.contains(node)){
		            		DataProductionPlanner.logger.log( Level.WARNING, "Dublicated nodes (a row in the intup file is skipped): "+node.toString());
		            		
		            	}else{
		            		this.nodes.add(node);
			            	DataProductionPlanner.logger.log( Level.FINEST, "Node red from file: "+node.toString());
		            	}
		            	
	            	}
	            }
	            DataProductionPlanner.logger.log( Level.INFO, "Nodes were red successfully");
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
		            	if (this.links.contains(link)){ // skip links with same id's
		            		DataProductionPlanner.logger.log( Level.WARNING, "Dublicated links (a row in the intup file is skipped): "+link.toString());
		            		
		            	}else{
		            		if (!this.LinkIsValid(link)){// begin or end nodes are missing
		            			DataProductionPlanner.logger.log( Level.WARNING, "Begin/end nodes of a link are missing (a row in the intup file is skipped): "+link.toString());
		            		}else{ //link is valid and unique
			            		this.links.add(link);
			            		DataProductionPlanner.logger.log( Level.FINEST, "Link red from file: " + link.toString());
		            		}

		            	}
	            	}
	            }
	            DataProductionPlanner.logger.log( Level.INFO, "Links were red successfully");
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
	
	public void PrintGridSetup(){
		System.out.println("--------------------GRID SETUP-------------------------");
		System.out.println("NODES: ");		
		for (CompNode node: this.nodes){
			System.out.println(node.toString());
		}
		System.out.println("");	
		System.out.println("Links: ");	
		for (NetworkLink link: this.links){
			System.out.println(link.toString());
		}
		System.out.println("-------------------------------------------------------");
		System.out.println("........................................................");
		System.out.println(this.GridSummarystring());
		System.out.println("........................................................");
	}
	
	public String GridSummarystring(){
		int numberOfNodes = this.nodes.size();
		int numberOfLinks = this.links.size();
		int numberOfInputSources = 0;
		int numberOfOutputDestinations = 0;
		int numberOfInputDestinations = 0;
		int numberOfOutputSources = 0;
		
		for (CompNode node: this.nodes){
			if (node.isInputSource()){numberOfInputSources++;}
			if (node.isOutputDestination()){numberOfOutputDestinations++;}
			if (node.isInputDestination()){numberOfInputDestinations++;}
			if (node.isOutputSource()){numberOfOutputSources++;}
		}
		StringBuffer sb = new StringBuffer("GRID SUMMARY ");
		sb.append("[ Nodes: ");
		sb.append(numberOfNodes);
		sb.append(" Input Sources: ");
		sb.append(numberOfInputSources);
		sb.append(" Output Destinations: ");
		sb.append(numberOfOutputDestinations);
		sb.append(" Input Destinations: ");
		sb.append(numberOfInputDestinations);
		sb.append(" Output Sources: ");
		sb.append(numberOfOutputSources);
		sb.append("] Links: ");
		sb.append(numberOfLinks);
		return sb.toString();
	}
	
	public boolean GridIsCostintent(){
		for (NetworkLink link: this.links){
			CompNode bnode = new CompNode(link.getBeginNodeId(), "Bsearch",false,false,false,false,1);
			CompNode enode = new CompNode(link.getEndNodeId(), "Bsearch",false,false,false,false,1);
			if ( !( this.nodes.contains(bnode) && this.nodes.contains(enode) ) ){
				DataProductionPlanner.logger.log( Level.SEVERE, "Unconsistent Gid setup, link from/to missing node: " + link.toString());
				return false;
			}		
		}			
		return true;
	}

	private boolean LinkIsValid(NetworkLink link){
		CompNode bnode = new CompNode(link.getBeginNodeId(), "Bsearch",false,false,false,false,1);
		CompNode enode = new CompNode(link.getEndNodeId(), "Bsearch",false,false,false,false,1);
		if ( !( this.nodes.contains(bnode) && this.nodes.contains(enode) ) ){
			DataProductionPlanner.logger.log( Level.SEVERE, "Unconsistent Gid setup, link from/to missing node: " + link.toString());
			return false;
		}else{
			return true;
		}
	}

}
