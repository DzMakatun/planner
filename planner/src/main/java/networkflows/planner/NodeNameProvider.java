package networkflows.planner;

import org.jgrapht.ext.StringNameProvider;

public class NodeNameProvider extends StringNameProvider<CompNode> {
	public String getVertexName(CompNode node){
	    	if (node.isInputDestination()){
	    	    return node.getName();// + " " +  Integer.toString(node.getCpuN());
	    	}
	    	if (node.isOutputDestination()){
	    	    return "Tier-0";
	    	}
		return node.getName();
	}

}
