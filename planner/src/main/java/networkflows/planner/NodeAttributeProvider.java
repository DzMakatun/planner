package networkflows.planner;

import java.util.LinkedHashMap;
import java.util.Map;

import org.jgrapht.ext.ComponentAttributeProvider;

public class NodeAttributeProvider implements ComponentAttributeProvider<CompNode>{
	public Map<String, String> getComponentAttributes(CompNode node) {
        Map<String, String> map =new LinkedHashMap<String, String>();
        map.put("isInputSource", Boolean.toString(node.isInputSource()));
        map.put("isOutputDestination", Boolean.toString(node.isOutputDestination()));
        map.put("isInputDestination", Boolean.toString(node.isInputDestination()));           
        map.put("isOutputSource", Boolean.toString(node.isOutputSource()));
        map.put("isDummy", Boolean.toString(node.isDummy()));
        return map;
    }

}
