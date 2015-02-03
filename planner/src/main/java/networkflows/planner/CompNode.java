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

	private int inputWeight;				//weight (bandwidth) for the input transfer problem	

	//constructor
	public CompNode(int id, String name, boolean isInputSource,
			boolean isOutputDestination, boolean isInputDestination,
			boolean isOutputSource, int inputWeight) {
		this.id = id;
		this.name = name;
		this.isInputSource = isInputSource;
		this.isOutputDestination = isOutputDestination;
		this.isInputDestination = isInputDestination;
		this.isOutputSource = isOutputSource;
		this.inputWeight = inputWeight;
	}

	@Override
	public String toString() {
		return "CompNode [id=" + id + ", name=" + name + ", isInputSource="
				+ isInputSource + ", isOutputDestination="
				+ isOutputDestination + ", isInputDestination="
				+ isInputDestination + ", isOutputSource=" + isOutputSource
				+ ", inputWeight=" + inputWeight + "]";
	}
	
	

}
