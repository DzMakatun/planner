package networkflows.planner;
import org.jgrapht.ext.EdgeNameProvider;

public class LinkNameProvider implements EdgeNameProvider<NetworkLink>{
	public String getEdgeName(NetworkLink link){
		Integer id = link.getId();
		return String.format("%.0f", link.getBandwidth() * 8.0 * 1024.0 * 1024.0 / 1000000.0) + "";//id + "_" + link.getName();
		//return String.format("%.2f", link.getBandwidth()*1000 ) + " Mbps";
	}

}
