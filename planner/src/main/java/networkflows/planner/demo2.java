package networkflows.planner;


public class demo2 {

    public static void main(String[] args) {
	String plannerLogFilename = "log/planner_demo2.log";
	int deltaT = 43200; //12 hours in seconds
	float beta = (float) 0.7;  
	// TODO Auto-generated method stub
	
	DataProductionPlanner solver = new DataProductionPlanner(plannerLogFilename, deltaT, beta);
	
	//create nodes
	
	//sources
	solver.addNode(new CompNode(12, "RCFstorage", false,
		true, true, false, false, 200000000, 0, 27, 0, 0,
		0, 100000000, 100000000 ));
	
	solver.addNode(new CompNode(22, "PDSFstorage", false,
		true, false, false, false, 200000000, 0, 27, 0, 0,
		0, 50000000, 100000000 ));
	//processing nodes
	
	solver.addNode(new CompNode(33, "RCF", false,
		false, false, true, true, 20000000,
		1000, 27, 1000000, 0,
		1000000, 0, 0));
	
	solver.addNode(new CompNode(43, "PDSF", false,
		false, false, true, true, 20000000,
		1000, 27, 1000000, 0,
		1000000, 0, 0));
	
	solver.addNode(new CompNode(53, "ANL", false,
		false, false, true, true, 20000000,
		1000, 27, 1000000, 0,
		1000000, 0, 0));
	
	solver.addNode(new CompNode(63, "KISTI", false,
		false, false, true, true, 20000000,
		1000, 27, 1000000, 0,
		1000000, 0, 0));
	
	//create edges
	solver.addLink(new NetworkLink(18, "RCFstorage->RCF", 12, 33, 11920, false) );
	solver.addLink(new NetworkLink(-18, "RCF->RCFstorage", 33, 12, 11920, false) );
	
	solver.addLink(new NetworkLink(28, "PDSFstorage->PDSF", 22, 43, 11920, false) );
	solver.addLink(new NetworkLink(-28, "PDSF->PDSFstorage", 43, 22, 11920, false) );
	
	solver.addLink(new NetworkLink(38, "RCF->PDSF", 33, 43, 23, false));
	solver.addLink(new NetworkLink(-38, "PDSF->RCF", 43, 33, 23, false));
	
	solver.addLink(new NetworkLink(48, "RCF->ANL", 33, 53, 23, false));
	solver.addLink(new NetworkLink(-48, "ANL->RCF", 53, 33, 23, false));
	
	solver.addLink(new NetworkLink(58, "PDSF->ANL", 43, 53, 23, false));
	solver.addLink(new NetworkLink(-58, "ANL->PDSF", 53, 43, 23, false));
	
	solver.addLink(new NetworkLink(68, "RCF->KISTI", 33, 63, 23, false));
	solver.addLink(new NetworkLink(-68, "KISTI->RCF", 63, 33, 23, false));
	
	solver.addLink(new NetworkLink(78, "PDSF->KISTI", 43, 63, 3, false));
	solver.addLink(new NetworkLink(-78, "KISTI->PDSF", 63, 43, 3, false));
	
	
	System.out.println("############################## OLD PLANNER #################################");
	//solver.PrintGridSetup();		
	
	System.out.println("Total flow: " + solver.solve() );
	
	//solver.clean();
	System.out.println("############################## NEW PLANNER #################################");
	solver.planInitialDataDistribution(200000000, 0.01f);
	//solver.WriteSolutionODT("output/demo_preplaning1.dot");
	System.out.println("Total flow: " + solver.solveWithCost() );
	//solver.WriteGridODT("output/demo_grid.dot");	
	//solver.WriteSolutionODT("output/demo_solution1.dot");	
	
    }

}
