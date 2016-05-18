package networkflows.planner;

//Min cost max flow algorithm using an adjacency matrix.  If you
//want just regular max flow, setting all edge costs to 1 gives
//running time O(|E|^2 |V|).
//
//Running time: O(min(|V|^2 * totflow, |V|^3 * totcost))
//
//INPUT: cap -- a matrix such that cap[i][j] is the capacity of
//            a directed edge from node i to node j
//
//     cost -- a matrix such that cost[i][j] is the (positive)
//             cost of sending one unit of flow along a 
//             directed edge from node i to node j
//
//     source -- starting node
//     sink -- ending node
//
//OUTPUT: max flow and min cost; the matrix flow will contain
//      the actual flow values (note that unlike in the MaxFlow
//      code, you don't need to ignore negative flow values -- there
//      shouldn't be any)
//
//To use this, create a MinCostMaxFlow object, and call it like this:
//
//MinCostMaxFlow nf;
//int maxflow = nf.getMaxFlow(cap,cost,source,sink);
// algorithm from 

import java.util.*;

public class MinCostMaxFlow {
 boolean found[];
 int N,    //number of nodes
 cost[][], //cost
 dad[], //previous (ancestor) node on the s-t path
 dist[], //distance from source
 pi[]; //reduced cost
 
 double  cap[][],  //capacities
 flow[][]; //solution
 
 static final int INF = Integer.MAX_VALUE / 2 - 1;
 
 boolean search(int source, int sink) { //search a path
	Arrays.fill(found, false);
	Arrays.fill(dist, INF);
	dist[source] = 0;

	while (source != N) { //loop over all nodes
	    int best = N; //next hop?
	    found[source] = true;
	    for (int k = 0; k < N; k++) { //iteration over all nodes
		if (found[k]) continue;  //if node was "found" skip
		if (flow[k][source] != 0) { //if there is a reverce flow
		    int val = dist[source] + pi[source] - pi[k] - cost[k][source]; // - reduced cost ??
		    if (dist[k] > val) { //found shorter distance to source
			dist[k] = val;
			dad[k] = source;
		    }
		}
		if (flow[source][k] < cap[source][k]) { //if direct flow is not saturated
		    int val = dist[source] + pi[source] - pi[k] + cost[source][k]; //reduced cost
		    if (dist[k] > val) {//found shorter distance to the source
			dist[k] = val;
			dad[k] = source;
		    }
		}
		
		if (dist[k] < dist[best]) best = k;
	    }
	    source = best;
	}
	for (int k = 0; k < N; k++)
	    pi[k] = Math.min(pi[k] + dist[k], INF); //set all pi[k] to be leq than infinity
	return found[sink];
 }
 
 
 double[][] getMaxFlow(double cap[][], int cost[][], int source, int sink) {
	this.cap = cap;
	this.cost = cost;
	
	N = cap.length;
     found = new boolean[N];
     flow = new double[N][N];
     dist = new int[N+1];
     dad = new int[N];
     pi = new int[N];
	
	double totflow = 0, totcost = 0;
	while (search(source, sink)) { //search for unused path 
	    double amt = INF;
	    for (int x = sink; x != source; x = dad[x])// iterate over ancestors (find minimal value among ancestors)
		amt = Math.min(amt, flow[x][dad[x]] != 0 ? flow[x][dad[x]] :
                    cap[dad[x]][x] - flow[dad[x]][x]); // change flow by this value
	    for (int x = sink; x != source; x = dad[x]) {
		if (flow[x][dad[x]] != 0) { //if reverse flow
		    flow[x][dad[x]] -= amt;
		    totcost -= amt * cost[x][dad[x]];
		} else { //if normal flow
		    flow[dad[x]][x] += amt;
		    totcost += amt * cost[dad[x]][x];
		}
	    }
	    totflow += amt;
	}
	
	//return new int[]{ totflow, totcost };
	
	System.out.println("Capacity: " + Arrays.deepToString(cap));
	System.out.println("Cost: " + Arrays.deepToString(cost));
	System.out.println("Source: " + source + " Sink: " + sink);
	System.out.println("-------------------------------------------");
	System.out.println("Total flow: " + totflow + " Total cost: " + totcost);
	System.out.println("Flow: " + Arrays.deepToString(flow));
	System.out.println("\n");
	
	return flow;
 }

 public static void main (String args[]){
     MinCostMaxFlow flow = new MinCostMaxFlow();
     double[][] cap = {{0, 3000000000.0, 4000000000.0, 5000000000.0, 0},
                    {3000000000.0, 0, 2000000000.0, 0, 0},
                    {4000000000.0, 2000000000.0, 0, 4000000000.0, 1000000000.0},
                    {0, 0, 4000000000.0, 0, 10000000000.0},
                    {0, 0, 3000000000.0, 10000000000.0, 0}};

     int cost1[][] = {{0, 1, 0, 0, 0},
                      {0, 0, 0, 0, 0},
                      {0, 0, 0, 0, 0},
                      {0, 0, 0, 0, 0},
                      {0, 0, 0, 0, 0}};

     int cost2[][] = {{0, 0, 1, 0, 0},
                      {0, 0, 0, 0, 0},
                      {0, 0, 0, 0, 0},
                      {0, 0, 0, 0, 0},
                      {0, 0, 0, 0, 0}};
     
     // should print out:
     //   10 1
     //   10 3

     double[][] ret1 = flow.getMaxFlow(cap, cost1, 0, 4);
     double[][] ret2 = flow.getMaxFlow(cap, cost2, 0, 4);
     
     //System.out.println (ret1[0] + " " + ret1[1]);
     //System.out.println (ret2[0] + " " + ret2[1]);
 }
}
