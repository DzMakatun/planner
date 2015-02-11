package networkflows.planner;

import org.jgrapht.ext.IntegerNameProvider;

public class NodeIdProvider extends IntegerNameProvider<CompNode>  {
	public String getVertexName(CompNode node){
		Integer id = node.getId();
		return id.toString();
	}

}
