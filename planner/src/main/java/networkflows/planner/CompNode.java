/**
 * 
 */
package networkflows.planner;

import java.io.IOException;
import java.util.logging.Level;

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

	public CompNode(String [] row) throws IOException {
		if (row.length != 7) {  //check number of records for each node
    		throw new IOException("Wrong number of parameters (format missmatch) in a row.");
    	}

		this.id = Integer.parseInt(row[0]);
		this.name = row[1];
		this.isInputSource = Boolean.valueOf(row[2]);
		this.isOutputDestination = Boolean.valueOf(row[3]);
		this.isInputDestination = Boolean.valueOf(row[4]);
		this.isOutputSource = Boolean.valueOf(row[5]);
		this.inputWeight = Integer.parseInt(row[6]);
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
