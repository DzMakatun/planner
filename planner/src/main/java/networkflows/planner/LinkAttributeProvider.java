package networkflows.planner;

import java.util.LinkedHashMap;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.ext.ComponentAttributeProvider;

public class LinkAttributeProvider implements ComponentAttributeProvider<NetworkLink>{
	private Graph<CompNode,NetworkLink> graph; 
	public LinkAttributeProvider(Graph<CompNode,NetworkLink> graph){
		this.graph = graph;
		
	}
	
	public Map<String, String> getComponentAttributes(NetworkLink link) {
		Map<String, String> map =new LinkedHashMap<String, String>();
        map.put("weight", Double.toString(this.graph.getEdgeWeight(link)));
        map.put("isDummy", Boolean.toString(link.isDummy()));
		return map;
	}

}
