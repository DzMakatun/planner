/**
 * @author Dzmitry Makatun
 * e-mail: d.i.makatun@gmail.com
 * 2015 
 */
package networkflows.planner;

import java.io.IOException;

import org.jgrapht.graph.DefaultWeightedEdge;


public class NetworkLink extends DefaultWeightedEdge{
	//inherits double private weight from superclass 
	private int id;
	private String name;	
	private boolean isDummy;
	private int beginNodeId;
	private int endNodeId;	
	private double bandwidth;
	
	private double inputFlow;
	private double outputFlow;
	
	
	
	//constructor
	public NetworkLink(int id, String name, int beginNodeId, int endNodeId,
			double bandwidth, boolean isDummy) {
		super();
		this.id = id;
		this.name = name;
		this.beginNodeId = beginNodeId;
		this.endNodeId = endNodeId;
		this.bandwidth = bandwidth;
		this.isDummy = isDummy;
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
		this.bandwidth = Double.parseDouble(row[4]);
		this.isDummy = false;
	}

	@Override
	public String toString() {
		return "NetworkLink [id=" + id + " name=" + name + " isDummy=" + isDummy + " beginNodeId="
				+ beginNodeId + " endNodeId=" + endNodeId + " bandwidth="
				+ bandwidth + " inputFlow=" + inputFlow + " outputFlow=" + outputFlow + " weight=" + super.getWeight()+ "]";
	}
	
	
	public String toFormatedString() {
		StringBuffer sb = new StringBuffer();
		sb.append( String.format("%10d %15s ",id,name) );
		if (this.isDummy)
			sb.append("dummy ");
		else
			sb.append("real  ");
		sb.append( String.format("%10d -> %-10d ",beginNodeId,endNodeId) );//(beginNodeId + "->"+ endNodeId + " ");
		sb.append( String.format("bandwidth=%1.0f ",bandwidth) );
		sb.append( String.format("inputFlow=%1.0f ",inputFlow) );
		sb.append( String.format("outputFlow=%1.0f ",outputFlow) );
		sb.append( String.format("weight=%1.0f",super.getWeight()) );
		return sb.toString();
	}

	//to get weight from the superclass
	@Override
	public double getWeight(){
		return super.getWeight();
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
	
	public double getInputFlow() {
		return inputFlow;
	}

	public void setInputFlow(double inputFlow) {
		this.inputFlow = inputFlow;
	}

	public double getOutputFlow() {
		return outputFlow;
	}

	public void setOutputFlow(double outputFlow) {
		this.outputFlow = outputFlow;
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

	public double getBandwidth() {
		return bandwidth;
	}
	
	public double getOutputWeight(int deltaT) { //edge weight for output problem
		return this.bandwidth * deltaT;
	}
	
	public double getInputWeight(int deltaT) {//edge weight for input problem
		return this.bandwidth * deltaT - this.outputFlow; //decrease bandwidth by value used by output flow
	}
	
	public boolean isDummy() {
		// TODO Auto-generated method stub
		return this.isDummy;
	}
	

	
}
