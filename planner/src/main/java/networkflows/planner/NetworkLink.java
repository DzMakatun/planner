/**
 * 
 */
package networkflows.planner;

import java.io.IOException;
import org.jgrapht.graph.DefaultWeightedEdge;

/**
 * @author Dima
 *
 */
public class NetworkLink extends DefaultWeightedEdge{
	/**
	 * 
	 */
	
	private int id;
	private String name;	
	private int beginNodeId;
	private int endNodeId;	
	private int bandwidth;
	
	//constructor
	public NetworkLink(int id, String name, int beginNodeId, int endNodeId,
			int bandwidth) {
		super();
		this.id = id;
		this.name = name;
		this.beginNodeId = beginNodeId;
		this.endNodeId = endNodeId;
		this.bandwidth = bandwidth;
	}

	//constructor from string
	public NetworkLink(String [] row) throws IOException {
		super();
		if (row.length != 5) {  //check number of records for each link
    		throw new IOException("Wrong number of parameters (format missmatch) in a row.");
    	}
		this.id = Integer.parseInt(row[0]);
		this.name = row[1];
		this.beginNodeId = Integer.parseInt(row[2]);
		this.endNodeId = Integer.parseInt(row[3]);
		this.bandwidth = Integer.parseInt(row[4]);
	}

	@Override
	public String toString() {
		return "NetworkLink [id=" + id + ", name=" + name + ", beginNodeId="
				+ beginNodeId + ", endNodeId=" + endNodeId + ", bandwidth="
				+ bandwidth + "]";
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public int getBeginNodeId() {
		return beginNodeId;
	}

	public int getEndNodeId() {
		return endNodeId;
	}

	public int getBandwidth() {
		return bandwidth;
	}
	
	//comparison of two links
	@Override 
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final NetworkLink other = (NetworkLink) obj;
        if (this.id == other.id) //nodes has same id
            return true;
        return false;
    }
	
	@Override
    public int hashCode() {
        return this.id;
    }
	
}
