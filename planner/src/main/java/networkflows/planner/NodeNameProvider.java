package networkflows.planner;

import org.jgrapht.ext.StringNameProvider;

public class NodeNameProvider extends StringNameProvider<CompNode> {
	public String getVertexName(CompNode node){
	    	if (node.isInputDestination()){
	    	    return Integer.toString(node.getCpuN());
	    	}
		return node.getName();
	}

}
