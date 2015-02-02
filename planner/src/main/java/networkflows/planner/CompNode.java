/**
 * 
 */
package networkflows.planner;

/**
 * @author Dima
 *
 */
public class CompNode {
	private int id;
	private String name;
	
	private boolean isInputSource; 			//true for the central storage
	private boolean isOutputDestination; 	//true for the central storage
	
	
	private boolean isInputDestination;		//true for the processing nodes
	private boolean isOutputSource;			//true for the processing nodes

	private int inputWeight;				//weight for the input transfer problem	

}
