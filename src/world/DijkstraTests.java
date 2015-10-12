package world;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


import java.util.concurrent.TimeUnit;

import masSim.schedule.DijkstraAlgorithm;
import masSim.schedule.Graph;
import masSim.schedule.MethodTransition;
import masSim.taems.Method;

import org.junit.Before;
import org.junit.Test;

public class DijkstraTests {

	private ExecutorService schedulerPool;
	private ArrayList<Method> nodes = new ArrayList<Method>();
	private ArrayList<MethodTransition> edges = new ArrayList<MethodTransition>();
	
	public DijkstraTests()
	{
	}
	
	@Before
	public void setUp() throws Exception {
	}
	

	@Test
	public void dijkstraTest1() {
		Method o = new Method(Method.StartingPoint,0,0,0);
		Method a = new Method("A",10,5,5);
		Method b = new Method("B",50,5,10);
		Method c = new Method("C",20,8,8);
		Method f = new Method(Method.FinalPoint,0,0,0);
		nodes.add(o);
		nodes.add(a);
		nodes.add(b);
		nodes.add(c);
		nodes.add(f);
		
		MethodTransition oa = new MethodTransition("oa",o,a);
		MethodTransition ab = new MethodTransition("ab",a,b);
		MethodTransition bc = new MethodTransition("bc",b,c);
		MethodTransition oc = new MethodTransition("oc",o,c);
		MethodTransition cf = new MethodTransition("bc",b,c);
		edges.add(oa);
		edges.add(ab);
		edges.add(bc);
		edges.add(oc);
		edges.add(cf);
		
		Graph graph = new Graph(nodes, edges);
		DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(graph);
	    dijkstra.execute(o);
	    LinkedList<Method> path = dijkstra.getPath(f);
	}
	

}
