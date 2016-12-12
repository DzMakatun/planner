package networkflows.planner;

import org.jgrapht.ext.IntegerNameProvider;
import org.jgrapht.ext.VertexNameProvider;

public class NodeIdProvider extends IntegerNameProvider<CompNode> implements VertexNameProvider<CompNode>  {
	public String getVertexName(CompNode node){
		Integer id = node.getId();
		return id.toString();
	}

}
