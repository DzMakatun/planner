package networkflows.planner;

import java.util.LinkedHashMap;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.ext.ComponentAttributeProvider;

public class LinkAttributeProvider implements ComponentAttributeProvider<NetworkLink>{
    public static final int BANDWIDTH = 0;
    public static final int SOLUTION = 1;
    public static final int COST = 2;
    public static final int CAPACITY = 3;
    public static final int INPUT = 4;
    public static final int OUTPUT = 5;
    public static final int EXCESS = 6;
    public static final int WEIGHT = 7;    
    private int type = 0;
    
	public LinkAttributeProvider(int type){	
	    this.type=type;
	}
	
	public Map<String, String> getComponentAttributes(NetworkLink link) {
		Map<String, String> map =new LinkedHashMap<String, String>();
	//map.put("splines", "curved");
        map.put("isDummy", Boolean.toString(link.isDummy()));
        map.put("bandwidth", Double.toString(link.getBandwidth()));
        map.put("inputFlow", Double.toString(link.getInputFlow()));
        map.put("outputFlow", Double.toString(link.getOutputFlow()));
        map.put("capacity", Double.toString(link.getCapacity()));
        
        map.put("dir", "forward");
        map.put("arrowType", "normal");
        map.put("arrowhead", "normal");
        map.put("style", "dashed");
        


        
        String color;
        double weight;
        switch (this.type) {
          case BANDWIDTH:  weight =link.getBandwidth();
          		   color = "black";
                           break;
          case SOLUTION:   if (link.getInputFlow() > 0){
                               if (link.getOutputFlow() > 0){
                        	   color = "magenta";
                               }else{
                        	   color = "red";
                               }              
                           }else{
                               if (link.getOutputFlow() > 0){
                        	   color = "blue";
                               }else{
                        	   color = "grey";
                               }  
                           }
                           weight = link.getInputFlow() + link.getOutputFlow();
                           if (weight == 0 ){ weight = 1.0;}
                           break;

                 
          default: weight = link.getBandwidth();  
          	   color = "black";
                   break;
        }
        
        map.put("weight", Double.toString(weight));
        map.put("color", color);
		return map;
	}

}
