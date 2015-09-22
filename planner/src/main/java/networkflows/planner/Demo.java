package networkflows.planner;

import java.net.*;


/**
 * A simple introduction to using JGraphT.
 *
 * @author Barak Naveh
 * @since Jul 27, 2003
 */
public final class Demo
{
    

    private Demo()
    {
    } // ensure non-instantiability.

    

    /**
     * The starting point for the demo.
     *
     * @param args ignored.
     */
    public static void main(String [] args)
    {
        long startTime = System.currentTimeMillis();     	//measure execution time               
    	System.out.println("Starting Test");
    	
    	//initialization
    	DataProductionPlanner planner = new DataProductionPlanner("input/planner.conf");
    	planner.ReadNodesFromFile("input/nodes.csv");
    	planner.ReadLinksFromFile("input/links.csv");            
        
    	//solution (order of calls should be exactly like this)
        planner.CreateOutputNetwork();
        planner.SolveOutputProblem();
        planner.CreateInputNetwork();
        planner.SolveInputProblem();
        
        //generating output
        planner.CalculateNodeFlows();
        planner.PrintGridSetup();
        //ODT is standard graph format, compatible with Gephi, graphviz and etc.
        planner.WriteGridODT("output/grid.dot");
        //planner.WriteOutputNetworkODT("output/outputNetwork.dot");        
        //planner.WriteInputNetworkODT("output/inputNetwork.dot");
        
        //if (planner.GridIsCostintent()){
        //	System.out.println("Grid passed the consistency check");
        //}else{
        //	System.out.println("Grid is inconsistent");
        //}
        //planner.ConstructGrid();
        long stopTime = System.currentTimeMillis();
        long elapsedTime = stopTime - startTime;
        System.out.println("Elapsed time in ms: " + elapsedTime);
        
    	

    	
    }
}


