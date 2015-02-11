package networkflows.planner;
import org.jgrapht.ext.EdgeNameProvider;

public class LinkNameProvider implements EdgeNameProvider<NetworkLink>{
	public String getEdgeName(NetworkLink link){
		Integer id = link.getId();
		return id + " " + link.getName();
	}

}
