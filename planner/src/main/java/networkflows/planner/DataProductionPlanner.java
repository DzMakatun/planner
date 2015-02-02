package networkflows.planner;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.Set;

public class DataProductionPlanner {
	private Set<CompNode> nodes;
	private Set<NetworkLink> links;
	
	//add logger
	private static final Logger logger = Logger.getLogger( DataProductionPlanner.class.getName() );
	private static FileHandler fh;
	private static SimpleFormatter formatter;
	
	//constructor
	public DataProductionPlanner() { 
		
        // This block configure the logger with handler and formatter 
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
	
	

}
