package networkflows.planner;

import org.jgrapht.ext.StringNameProvider;

public class NodeNameProvider extends StringNameProvider<CompNode> {
	public String getVertexName(CompNode node){
		return node.getName();
	}

}
