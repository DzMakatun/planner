package networkflows.planner;

import java.util.LinkedHashMap;
import java.util.Map;

import org.jgrapht.ext.ComponentAttributeProvider;

public class NodeAttributeProvider implements ComponentAttributeProvider<CompNode>{
       public static final int BANDWIDTH = 0;
       public static final int SOLUTION = 1;
       
       private int type = 0;
       
       public NodeAttributeProvider(int type){	
	  this.type=type;
       }
	
    
    
	public Map<String, String> getComponentAttributes(CompNode node) {
        Map<String, String> map =new LinkedHashMap<String, String>();
        //map.put("isInputSource", Boolean.toString(node.isInputSource()));
        //map.put("isOutputDestination", Boolean.toString(node.isOutputDestination()));
        //map.put("isInputDestination", Boolean.toString(node.isInputDestination()));           
        //map.put("isOutputSource", Boolean.toString(node.isOutputSource()));
        //map.put("isDummy", Boolean.toString(node.isDummy()));
        
        String color = "black";
        if (node.isInputSource()){ color = "red"; }
        if (node.isOutputSource()){ color = "blue"; }
        if (node.isOutputSource() && node.isInputSource()){ color = "magenta"; }
        map.put("color", color);
        
        Double weight = (double) node.getCpuN();
        switch (this.type) {
          case BANDWIDTH:  weight = (double) node.getCpuN();
          		   if (weight == 1){weight = 6000.0;} 
          		   break;
          		   
          case SOLUTION:   weight = Math.abs(node.getNettoInputFlow()) + Math.abs(node.getNettoOutputFlow());
                           break;                           
                           
          default: weight = (double) node.getCpuN(); 
                   break;
       }              
        map.put("weight",String.format("%.0f",weight) );
        return map;
    }

}
