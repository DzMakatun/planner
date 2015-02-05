package networkflows.planner;

import java.net.*;


/**
 * A simple introduction to using JGraphT.
 *
 * @author Barak Naveh
 * @since Jul 27, 2003
 */
public final class App
{
    

    private App()
    {
    } // ensure non-instantiability.

    

    /**
     * The starting point for the demo.
     *
     * @param args ignored.
     */
    public static void main(String [] args)
    {
        long startTime = System.currentTimeMillis();                
    	System.out.println("Starting Test");
    	
    	DataProductionPlanner planner = new DataProductionPlanner();
    	planner.ReadNodesFromFile("input/nodes.csv");
    	planner.ReadLinksFromFile("input/links.csv");


    	//measure execution time
        long stopTime = System.currentTimeMillis();
        long elapsedTime = stopTime - startTime;
        System.out.println("Elapsed time in ms: " + elapsedTime);
    	
    	

    	
    }
}


