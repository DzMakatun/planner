/**
 * @author Dzmitry Makatun
 * e-mail: d.i.makatun@gmail.com
 * 2015 
 */
package networkflows.planner;

import java.io.IOException;
import java.util.logging.Level;


public class CompNode {
	private int id;
	private String name;
	private boolean isDummy;
	
	private boolean isInputSource; 			//true for the central storage
	private boolean isOutputDestination; 	//true for the central storage	
	private boolean isInputDestination;		//true for the processing nodes
	private boolean isOutputSource;			//true for the processing nodes
	
	//values to calculate capacities of dummy edges
	private long disk; //available storage spce
	private int cpuN; //number of cpus
	private float alpha; //time to process one unit of data
	private long minOut; //minimal amount of output data (reserved by running jobs)	
	private long initInputSize;
	private long initOutputSize;	
	
	private double inputWeight;				//weight (bandwidth) for the input transfer problem	
	private double outputWeight;				//weight (bandwidth) for the output transfer problem	
	
	private double inputCanProvide;				//for the central storage amount of input data that can be transferred
	private double outputCanStore;					//for the central storage amount of output data that can be accommodated	
	
	//to monitore solution quality
	private double incomingInputFlow;
	private double outgoingInputFlow;
	private double nettoInputFlow;
	private double outgoingOutputFlow;
	private double incomingOutputFlow;
	private double nettoOutputFlow;
	
	//constructor TO DO
	/*public CompNode(int id, String name, boolean isInputSource,
			boolean isOutputDestination, boolean isInputDestination,
			boolean isOutputSource, boolean isDummy) {
		this.id = id;
		this.name = name;
		this.isInputSource = isInputSource;
		this.isOutputDestination = isOutputDestination;
		this.isInputDestination = isInputDestination;
		this.isOutputSource = isOutputSource;
		this.isDummy = isDummy;		
		//TO DO		
		this.inputCanProvide = 0;				//for the central storage amount of input data that can be transferred
		this.outputCanStore = 0;			
	}*/

	//constructor to read from file
	public CompNode(String [] row) throws IOException {
		if (row.length != 14) {  //check number of records for each node
    		throw new IOException("Wrong number of parameters (format missmatch) in a row.");
    	}
		this.id = Integer.parseInt(row[0]);
		this.name = row[1];
		this.isInputSource = Boolean.parseBoolean(row[2]);
		this.isOutputDestination = Boolean.parseBoolean(row[3]);
		this.isInputDestination = Boolean.parseBoolean(row[4]);
		this.isOutputSource = Boolean.parseBoolean(row[5]);		
		this.disk = Long.parseLong(row[6]);
		this.cpuN = Integer.parseInt(row[7]);
		this.alpha = Float.parseFloat(row[8]);
		this.minOut = Long.parseLong(row[9]);
		this.initInputSize = Long.parseLong(row[10]);
		this.initOutputSize = Long.parseLong(row[11]);
		this.inputCanProvide =  Double.parseDouble(row[12]);
		this.outputCanStore =  Double.parseDouble(row[13]);
		
		this.isDummy = false; //nodes readen from a file are expected to be real	
	}
	
	// constructor
	public CompNode(int id, String name, boolean isDummy,
			boolean isInputSource, boolean isOutputDestination,
			boolean isInputDestination, boolean isOutputSource, long disk,
			int cpuN, float alpha, long minOut, long initInputSize,
			long initOutputSize, double inputCanProvide, double outputCanStore) {
		this.id = id;
		this.name = name;
		this.isDummy = isDummy;
		this.isInputSource = isInputSource;
		this.isOutputDestination = isOutputDestination;
		this.isInputDestination = isInputDestination;
		this.isOutputSource = isOutputSource;
		this.disk = disk;
		this.cpuN = cpuN;
		this.alpha = alpha;
		this.minOut = minOut;
		this.initInputSize = initInputSize;
		this.initOutputSize = initOutputSize;
		this.inputCanProvide = inputCanProvide;
		this.outputCanStore = outputCanStore;
	}
	
	

	public double getNettoOutputFlow() {
		return nettoOutputFlow;
	}

	public void setNettoOutputFlow(double nettoOutputFlow) {
		this.nettoOutputFlow = nettoOutputFlow;
	}

	
	
	public double getNettoInputFlow() {
		return nettoInputFlow;
	}

	public void setNettoInputFlow(double nettoInputFlow) {
		this.nettoInputFlow = nettoInputFlow;
	}

	private void CalculateOutputWeight(int deltaT, float beta){
		this.outputWeight = this.initOutputSize + (this.cpuN * beta * deltaT) / this.alpha - this.minOut;
	}
	
	private void CalculateInputWeight(int deltaT, float beta){
		this.inputWeight = this.disk - this.initInputSize - this.initOutputSize + ( (1 -beta) * this.cpuN * deltaT) /  this.alpha + this.nettoOutputFlow;
	}
	
	
	public double getOutputWeight(int deltaT,float beta) {
		this.CalculateOutputWeight(deltaT, beta);
		return outputWeight;
	}

	public double getInputWeight(int deltaT, float beta) {
		this.CalculateInputWeight(deltaT, beta);
		return inputWeight;
	}
	
	public double getInputCanProvide() {
		return inputCanProvide;
	}

	public double getOutputCanStore() {
		return outputCanStore;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public boolean isInputSource() {
		return isInputSource;
	}

	public boolean isOutputDestination() {
		return isOutputDestination;
	}

	public boolean isInputDestination() {
		return isInputDestination;
	}

	public boolean isOutputSource() {
		return isOutputSource;
	}
	
	public double getIncomingInputFlow() {
		return incomingInputFlow;
	}

	public void setIncomingInputFlow(double incomingInputFlow) {
		this.incomingInputFlow = incomingInputFlow;
	}

	public double getOutgoingInputFlow() {
		return outgoingInputFlow;
	}

	public void setOutgoingInputFlow(double outgoingInputFlow) {
		this.outgoingInputFlow = outgoingInputFlow;
	}

	public double getOutgoingOutputFlow() {
		return outgoingOutputFlow;
	}

	public void setOutgoingOutputFlow(double outgoingOutputFlow) {
		this.outgoingOutputFlow = outgoingOutputFlow;
	}

	public double getIncomingOutputFlow() {
		return incomingOutputFlow;
	}

	public void setIncomingOutputFlow(double incomingOutputFlow) {
		this.incomingOutputFlow = incomingOutputFlow;
	}

	@Override
	public String toString() {
		return "CompNode [id=" + id + ", name=" + name + ", isDummy=" + isDummy
				+ ", isInputSource=" + isInputSource + ", isOutputDestination="
				+ isOutputDestination + ", isInputDestination="
				+ isInputDestination + ", isOutputSource=" + isOutputSource
				+ ", disk=" + disk + ", cpuN=" + cpuN + ", alpha=" + alpha
				+ ", minOut=" + minOut + ", initInputSize=" + initInputSize
				+ ", initOutputSize=" + initOutputSize + ", inputWeight="
				+ inputWeight + ", outputWeight=" + outputWeight
				+ ", inputCanProvide=" + inputCanProvide + ", outputCanStore="
				+ outputCanStore + ", incomingInputFlow=" + incomingInputFlow
				+ ", outgoingInputFlow=" + outgoingInputFlow
				+ ", nettoInputFlow=" + nettoInputFlow
				+ ", outgoingOutputFlow=" + outgoingOutputFlow
				+ ", incomingOutputFlow=" + incomingOutputFlow
				+ ", nettoOutputFlow=" + nettoOutputFlow + "]";
	}
	
	public String toFormatedString() {
		StringBuffer sb = new StringBuffer();
		sb.append( String.format("%10d %8s ",id,name) );
		if (this.isDummy)
			sb.append("dummy ");
		else			
			sb.append("real  ");
		
		if (this.isInputSource)
			sb.append("1");
		else			
			sb.append("0");
		if (this.isOutputDestination)
			sb.append("1");
		else			
			sb.append("0");
		if (this.isInputDestination)
			sb.append("1");
		else			
			sb.append("0");
		if (this.isOutputSource)
			sb.append("1");
		else			
			sb.append("0");
				
		sb.append( String.format(" disk=%d ",disk) );
		sb.append( String.format("cpuN=%d ",cpuN) );
		sb.append( String.format("alpha=%.3f ",alpha) );
		sb.append( String.format("minOut=%d ",minOut) );
		sb.append( String.format("initInputSize=%d ",initInputSize) );
		sb.append( String.format("initOutputSize=%d ",initOutputSize) );
		sb.append( String.format("inputWeight=%.0f ",inputWeight) );
		sb.append( String.format("outputWeight=%.0f ",outputWeight) );
		sb.append( String.format("inputCanProvide=%.0f ",inputCanProvide) );
		sb.append( String.format("outputCanStore=%.0f ",outputCanStore) );		
		sb.append( String.format("incomingInputFlow=%.0f ",incomingInputFlow) );
		sb.append( String.format("outgoingInputFlow=%.0f ",outgoingInputFlow) );
		sb.append( String.format("nettoInputFlow=%.0f ",nettoInputFlow) );
		sb.append( String.format("outgoingOutputFlow=%.0f ",outgoingOutputFlow) );
		sb.append( String.format("incomingOutputFlow=%.0f ",incomingOutputFlow) );
		sb.append( String.format("nettoOutputFlow=%.0f ",nettoOutputFlow) );	
			
		return sb.toString();
	}

	//comparison of two nodes
	@Override 
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final CompNode other = (CompNode) obj;
        if (this.id == other.id) //nodes has same id
            return true;
        return false;
    }
	
	@Override
    public int hashCode() {
        return this.id;
    }

	public boolean isDummy() {
		// TODO Auto-generated method stub
		return this.isDummy;
	}


}
