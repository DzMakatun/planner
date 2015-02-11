package networkflows.planner;

import java.util.LinkedHashMap;
import java.util.Map;

import org.jgrapht.ext.ComponentAttributeProvider;

public class LinkAttributeProvider implements ComponentAttributeProvider<NetworkLink>{
	public Map<String, String> getComponentAttributes(NetworkLink link) {
		Map<String, String> map =new LinkedHashMap<String, String>();
        map.put("weight", Integer.toString(link.getBandwidth()));
		return map;
	}

}
