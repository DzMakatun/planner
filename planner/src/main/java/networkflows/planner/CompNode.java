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
	private int index;


	private String name;
	private boolean isDummy;
	
	private boolean isInputSource; 			//true for the central storage
	private boolean isOutputDestination; 	//true for the central storage	
	private boolean isInputDestination;		//true for the processing nodes
	private boolean isOutputSource;			//true for the processing nodes
	
	//values to calculate capacities of dummy edges
	private long disk; //available storage space
	private int cpuN; //number of cpus
	/**
	 * @return the disk
	 */
	public long getDisk() {
	    return disk;
	}

	/**
	 * @return the cpuN
	 */
	public int getCpuN() {
	    return cpuN;
	}

	/**
	 * @return the alpha
	 */
	public float getAlpha() {
	    return alpha;
	}

	private float alpha; //time to process one unit of data
	private long minOut; //minimal amount of output data (reserved by running jobs)	
	private long waitingInputSize;
	private long readyOutputSize;	
	private long reservedOutputSize;
	private long submittedInputSize;
	private int busyCPUs;
	private long currentFreeSpace;
	private double highMark = 0.75;
	private double averageInputFileSize = 6000; //MB
	private double processedInput = 0;
	private double createdOutput = 0;
	
	private double inputWeight;				//weight (bandwidth) for the input transfer problem	
	private double outputWeight;				//weight (bandwidth) for the output transfer problem	
	
	private double inputCanProvide;				//for the central storage amount of input data that can be transferred
	private double outputCanStore;				//for the central storage amount of output data that can be accommodated	
	
	//to monitore solution quality
	private double incomingInputFlow;
	private double outgoingInputFlow;
	private double nettoInputFlow;
	private double outgoingOutputFlow;
	private double incomingOutputFlow;
	private double nettoOutputFlow;
	private double localProcessingFlow;
	private int inputSourceCost = 0;
	
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
		this.waitingInputSize = Long.parseLong(row[10]);
		this.readyOutputSize = Long.parseLong(row[11]);
		this.inputCanProvide =  Double.parseDouble(row[12]);
		this.outputCanStore =  Double.parseDouble(row[13]);
		
		this.isDummy = false; //nodes readen from a file are expected to be real	
	}
	
	// constructor
	public CompNode(int id, String name, boolean isDummy,
			boolean isInputSource, boolean isOutputDestination,
			boolean isInputDestination, boolean isOutputSource, long disk,
			int cpuN, float alpha, long minOut, long waitingInputSize,
			long readyOutputSize, double inputCanProvide, double outputCanStore) {
		this.id = id;
		this.name = name;
		this.isDummy = isDummy;
		this.isInputSource = true; //isInputSource;
		this.isOutputDestination = isOutputDestination;
		this.isInputDestination = isInputDestination;
		this.isOutputSource = isOutputSource;
		this.disk = disk;
		this.cpuN = cpuN;
		this.alpha = alpha;
		this.minOut = minOut;
		this.waitingInputSize = waitingInputSize;
		this.readyOutputSize = readyOutputSize;
		this.inputCanProvide = inputCanProvide;
		this.outputCanStore = outputCanStore;
		
		this.currentFreeSpace = disk - waitingInputSize - readyOutputSize; //fix it
		this.nettoInputFlow = 0;
		this.nettoOutputFlow = 0;
	}
	
	/**
	 * @return the index
	 */
	public int getIndex() {
	    return index;
	}

	/**
	 * @param index the index to set
	 */
	public void setIndex(int index) {
	    this.index = index;
	}
	

	public double getNettoOutputFlow() {
		return this.nettoOutputFlow;
	}

	public void addOutputFlow(double outputFlow) {
		this.nettoOutputFlow += outputFlow;
	}

	
	
	public double getNettoInputFlow() {
		return this.nettoInputFlow;
	}

	public void addInputFlow(double inputFlow) {
		this.nettoInputFlow += inputFlow;
	}

	private void CalculateOutputWeight(int deltaT, float beta){
	    	/*if (this.busyCPUs == this.cpuN){ //saturated mode
	    	    this.outputWeight = this.readyOutputSize + (this.cpuN * beta * deltaT) / this.alpha;
	    	}else{
	    	     this.outputWeight = this.readyOutputSize + this.reservedOutputSize;
	    	}*/
	    	//this.outputWeight = this.readyOutputSize + this.reservedOutputSize;
	    double maxPossible = this.readyOutputSize + this.reservedOutputSize;
	    double estimatedFlow = this.readyOutputSize + (this.cpuN * beta * deltaT) / this.alpha;
	    this.outputWeight = Math.min(estimatedFlow, maxPossible);
	    this.outputWeight = Math.max(this.outputWeight, this.createdOutput);//transfer not less then was created during last iteration
	    
	}
	
	private void CalculateInputWeight(int deltaT, float beta){
	       double maxInputDataCanAccomodate = //this.disk - this.waitingInputSize - this.readyOutputSize //initial free space
		       currentFreeSpace - disk * ( 1 - highMark); // disk * ( 1 - highMark) - minimal reserved space
		       //+ ( (1 -beta) * this.cpuN * deltaT) /  this.alpha //free space due to deleted data
		       //+ this.nettoOutputFlow; //free space due to transferred outputfiles
	       
	       if (maxInputDataCanAccomodate < 0){ maxInputDataCanAccomodate = 0; }
	       
	       double dataCanProcess;
	       if (this.busyCPUs < this.cpuN && this.currentFreeSpace > 0){ //unsaturated regime
		   maxInputDataCanAccomodate = ( currentFreeSpace - disk * ( 1 - highMark) ) / ( 1 + beta) ;
		   dataCanProcess =   (this.cpuN * averageInputFileSize * 1.1) - this.waitingInputSize; // if there are free CPU - transfer as much data as possible;
		   
	       }else{//saturated regime
		   //dataCanProcess = (this.cpuN * deltaT) /  this.alpha; //input data that can be processed during the time interval
		   dataCanProcess = this.submittedInputSize - this.waitingInputSize;		   
	       }
	       if (dataCanProcess < 0){ dataCanProcess = 0; }
	       //the final weight is limited by the value that we can actually accommodate 
	       this.inputWeight = Math.min(maxInputDataCanAccomodate, dataCanProcess);//Math.min(maxInputDataCanAccomodate, dataCanProcess);
	       this.inputWeight = Math.max(this.processedInput, this.inputWeight); //transfer not less then was processed during last iteration
	       if (this.inputWeight < 0){this.inputWeight = 0;}	
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
				+ ", minOut=" + minOut + ", waitingInputSize=" + waitingInputSize
				+ ", readyOutputSize=" + readyOutputSize + ", inputWeight="
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
		sb.append( String.format("waitingInputSize=%d ",waitingInputSize) );
		sb.append( String.format("readyOutputSize=%d ",readyOutputSize) );
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
	
	public static String getFormatedHeader(){
	        //String patterm = "%12s ";
		StringBuffer sb = new StringBuffer();
		sb.append( String.format("%10s %20s ","id","name") );
		sb.append("type  ");
		sb.append("role ");				
		sb.append( "      disk " );
		sb.append( "      cpuN " );
		sb.append( "     alpha " );
		sb.append( "     minOut " );
		sb.append( "  waitingIn " );
		sb.append( "   readyOut " );
		sb.append( "   inWeight " );
		sb.append( "  outWeight " );
		sb.append( "  inProvide " );
		sb.append( " outCanStor " );		
		sb.append( " nettoInFlow " );
		sb.append( "nettoOutFlow " );	
			
		return sb.toString();
	}
	
	public String toFormatedString2() {
		StringBuffer sb = new StringBuffer();
		sb.append( String.format("%10d %20s ",id,name) );
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
				
		sb.append( String.format(" %10d ",disk) );
		sb.append( String.format("%10d ",cpuN) );
		sb.append( String.format("%10f ",alpha) );
		sb.append( String.format("%11d ",minOut) );
		sb.append( String.format("%11d ",waitingInputSize) );
		sb.append( String.format("%11d ",readyOutputSize) );
		sb.append( String.format("%11.0f ",inputWeight) );
		sb.append( String.format("%11.0f ",outputWeight) );
		sb.append( String.format("%11.0f ",inputCanProvide) );
		sb.append( String.format("%11.0f ",outputCanStore) );		
		sb.append( String.format("%12.0f ",nettoInputFlow) );
		sb.append( String.format("%12.0f ",nettoOutputFlow) );	
			
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
    
    /**
     * cleans status and solution data from the nodes
     */
    public void clean(){
	waitingInputSize = 0;
	readyOutputSize = 0;
	inputWeight = 0;
	outputWeight =0;
	inputCanProvide = 0;
	outputCanStore = 0;
	
	incomingInputFlow = 0;
	outgoingInputFlow = 0;
	nettoInputFlow = 0;
	outgoingOutputFlow = 0;
	incomingOutputFlow = 0;
	nettoOutputFlow = 0;	
	inputSourceCost=0;
    }
	
    
    public void update(long waitingInputSize, long readyOutputSize, double inputCanProvide, double outputCanStore,
	    int busyCPUs, long currentFreeSpace, long submittedInputSize, long reservedOutputSize, double processedInput, double createdOutput){
	clean();
	this.waitingInputSize = waitingInputSize;
	this.readyOutputSize = readyOutputSize;
	this.inputCanProvide = inputCanProvide;
	this.outputCanStore = outputCanStore;
	this.busyCPUs = busyCPUs;
	this.currentFreeSpace = currentFreeSpace;
	this.reservedOutputSize = reservedOutputSize;
	this.submittedInputSize = submittedInputSize;
	this.processedInput = processedInput;
	this.createdOutput = createdOutput;
    }

    public double getLocalProcessingFlow() {
	return localProcessingFlow;
    }

    public void setLocalProcessingFlow(double localProcessingFlow) {
	this.localProcessingFlow = localProcessingFlow;
    }

    public void setInputSourceCost(int inputSourceCost) {
	this.inputSourceCost = inputSourceCost;
	
    }

    public int getInputSourceCost() {
	return this.inputSourceCost;	
    }
    
    public int getInputDestCost() {
    	if (this.cpuN > this.busyCPUs){
    		return 0;
    	}
    	return 10;
    }
    
    public double getEstimatedProcessingThroughput(int time){
	if (this.isInputDestination){
	    return time * (double)this.cpuN /this.alpha;
	}
	return 0;
    }


}
