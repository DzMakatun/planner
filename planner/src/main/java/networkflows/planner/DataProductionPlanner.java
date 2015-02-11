package networkflows.planner;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.jgrapht.*;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import org.jgrapht.ext.*;

import au.com.bytecode.opencsv.CSVReader;

public class DataProductionPlanner {
	//private Set<CompNode> nodes = new LinkedHashSet<CompNode>();
	//private Set<NetworkLink> links = new LinkedHashSet<NetworkLink>();
	
	//add logger
	private static final Logger logger = Logger.getLogger( DataProductionPlanner.class.getName() );
	private static FileHandler fh;
	private static SimpleFormatter formatter;
	private SimpleDirectedWeightedGraph<CompNode, NetworkLink> grid = new SimpleDirectedWeightedGraph<CompNode, NetworkLink>(NetworkLink.class);
	
	
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
			String [] nextLine;        
	        while ((nextLine = reader.readNext()) != null){ //Read one line at a time
	            if (nextLine[0].length()>0 && nextLine[0].charAt(0) != '#'){//skip commented and empty lines
	            		try {
			            	CompNode node = new CompNode(nextLine);
			            	if (this.grid.addVertex(node)){ //if node added successfully 
			            		DataProductionPlanner.logger.log( Level.FINEST, "Node red from file: "+node.toString());            					            		
			            	}else{ //if node adding failled
			            		DataProductionPlanner.logger.log( Level.WARNING, "Dublicated nodes in the nodes file (line skipped): "+node.toString());				            	
			            	}
	            		}catch (Exception e) {
	        	            //e.printStackTrace();
	        	            DataProductionPlanner.logger.log( Level.WARNING, "Failed to read node record from the nodes file (line skipped): " + nodesFileName + " " + e.getMessage());
	        	            continue;
	        	        }		            	
	            }	            
	        }
	        reader.close();
            DataProductionPlanner.logger.log( Level.INFO, "Reading nodes file is finished");
        }catch (Exception e){
			 e.printStackTrace();
	         DataProductionPlanner.logger.log( Level.SEVERE, "Failed to read nodes file: "+ nodesFileName);
		}	
    }

	//Read Links
	public void ReadLinksFromFile(String linksFileName){
		DataProductionPlanner.logger.log( Level.INFO, "Reading links from file: "+ linksFileName);
		CSVReader reader = null;
		CompNode bnode, enode; //temporary nodes for begin and end nodes of a link
		try {
            //Get the CSVReader instance with specifying the delimiter to be used
			reader = new CSVReader(new FileReader(linksFileName),',');
	        String [] nextLine;
	        //Read one line at a time
	        while ((nextLine = reader.readNext()) != null){
	            if (nextLine[0].length()>0 && nextLine[0].charAt(0) != '#'){//skip commented and empty lines
	            	try {
		            	NetworkLink link = new NetworkLink(nextLine); //throws exception if wrong string format
		            	bnode = getNode(link.getBeginNodeId());
            			enode = getNode(link.getEndNodeId());
		            	if (bnode!=null && enode!=null && this.grid.addEdge(bnode,enode,link)){ // this skips links with same id's or missing nodes
		            		//link is valid and unique        			
		            		 this.grid.setEdgeWeight(link, link.getBandwidth());
			            	 //this.links.add(link);
			            	 DataProductionPlanner.logger.log( Level.FINEST, "Link red from file: " + link.toString());		            		
		            	}else{
		            		 DataProductionPlanner.logger.log( Level.WARNING, "Duplicated link or missing nodes (line skipped): "+link.toString()); 
		            	}
	            	}catch (Exception e) {
	     	            //e.printStackTrace();
	     	            DataProductionPlanner.logger.log( Level.WARNING, "Error while reading the links file (line skipped): " + linksFileName + " " + e.getMessage());
	     	        }				
	            }
	        }
	        reader.close();
	        DataProductionPlanner.logger.log( Level.INFO, "Reading links file is finished");	       
        }catch (Exception e){ //if failed to read file
			 e.printStackTrace();
	         DataProductionPlanner.logger.log( Level.SEVERE, "Failed to read links file: "+ linksFileName);
		}	
    }
	
	public void PrintGridSetup(){
		System.out.println("--------------------GRID SETUP-------------------------");
		System.out.println("NODES: ");		
		for (CompNode node: this.grid.vertexSet()){
			System.out.println(node.toString());
		}
		System.out.println("");	
		System.out.println("Links: ");	
		for (NetworkLink link: this.grid.edgeSet()){
			System.out.println(link.toString());
		}
		System.out.println("-------------------------------------------------------");
		System.out.println("........................................................");
		System.out.println(this.GridSummarystring());
		System.out.println("........................................................");
	}
	
	public String GridSummarystring(){
		int numberOfNodes = this.grid.vertexSet().size();
		int numberOfLinks = this.grid.edgeSet().size();
		int numberOfInputSources = 0;
		int numberOfOutputDestinations = 0;
		int numberOfInputDestinations = 0;
		int numberOfOutputSources = 0;
		
		for (CompNode node: this.grid.vertexSet()){
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
		/*for (NetworkLink link: this.links){
			CompNode bnode = new CompNode(link.getBeginNodeId(), "Bsearch",false,false,false,false,1);
			CompNode enode = new CompNode(link.getEndNodeId(), "Bsearch",false,false,false,false,1);
			if ( !( this.nodes.contains(bnode) && this.nodes.contains(enode) ) ){
				DataProductionPlanner.logger.log( Level.SEVERE, "Unconsistent Gid setup, link from/to missing node: " + link.toString());
				return false;
			}		
		}*/		
		DataProductionPlanner.logger.log( Level.WARNING, "The method GridIsCostintent() is checking nothing so far");
		return true;
	}

	
	//search node by id
	private CompNode getNode(int id){
		for (CompNode node: this.grid.vertexSet()){
			if (node.getId() == id){
				return node;
			}
		}
		return null;
	}
	
	public void WriteGridODT(String outputFilename){
		NodeIdProvider nodeIds=new NodeIdProvider(); //node ids correspond to those in input file
		NodeNameProvider nodeNames=new NodeNameProvider(); //node names correspond to those in input file
		LinkNameProvider linkNames = new LinkNameProvider(); //link names are ids + name  from input file
		NodeAttributeProvider nodeAttributes = new NodeAttributeProvider(); //attributes for nodes
		LinkAttributeProvider linkAttributes = new LinkAttributeProvider(); //attributes for links
	    DOTExporter<CompNode, NetworkLink> export=new DOTExporter<CompNode, NetworkLink>(nodeIds, nodeNames, linkNames, nodeAttributes, linkAttributes);
	    
	    //DOTExporter<CompNode, NetworkLink> export=new DOTExporter<CompNode, NetworkLink>();
	    try {
	        export.export(new FileWriter(outputFilename), this.grid);
	        DataProductionPlanner.logger.log( Level.INFO, "Grid graph was written to file" + outputFilename);
	    }catch (IOException e){
	    	e.printStackTrace();
	    	DataProductionPlanner.logger.log( Level.WARNING, "Failed to write output file" + outputFilename + " " + e.getMessage());
	    }
	}
	
}
