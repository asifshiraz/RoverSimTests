package world;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

import masSim.taems.*;
import masSim.world.SimWorld;
import masSim.world.TaskRepository;
import sun.management.resources.agent_zh_TW;


public class BruteForceCalculation {

	ArrayList<IAgent> agents;
	Task[] tasks;
	UtilityPoint previousPoint;
	
	private Writer details = null;
	private Writer main = null;
	
	private void d(String s) throws IOException
	{
		details.write(s + System.lineSeparator());
	}
	private void m(String s) throws IOException
	{
		main.write(s + System.lineSeparator());
	}
	
	//Test
	public void CreateTestFiles()
	{
		int numberOfTasks = 10;
		String filenameDetails = "E:\\EclipseWorkspace\\RoverSim\\TaskRepository\\TasksDetails.xml";
		String filenameMain = "E:\\EclipseWorkspace\\RoverSim\\TaskRepository\\TasksMain.xml";
		try {
			details = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filenameDetails), "US-ASCII"));
			main = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filenameMain), "US-ASCII"));
			d("<Taems>");
			m("<Taems>");
			for(int i=1;i<=numberOfTasks;i++)
			{
				int x = (int) Math.round(Math.random()*800);
				int y = (int) Math.round(Math.random()*500);
				String task = "<Task id=\"T%1$s\" name=\"T%1$s\" qaf=\"SumAll\" ><Method id=\"M%1$s\" name=\"M%1$s\" Quality=\"500\" Duration=\"10\" XCoord=\"%2$s\" YCoord=\"%3$s\"></Method></Task>";
				d(String.format(task, i, x, y));
				m(String.format("<Task id=\"T%1$s\" name=\"%1$s\" qaf=\"SumAll\" />",i));
			}
			d("</Taems>");
			m("</Taems>");
			details.close();
			main.close();
		}
		catch(Exception ex)
		{
			System.out.print(ex);
		}
	}
	
	@Test
	public void ManualCalculationTest()
	{
		agents = SimWorld.CreateAgents(null);
		TaskRepository taskRepository = new TaskRepository();
		taskRepository.repositoryFolderPath = "E:\\EclipseWorkspace\\RoverSim\\TaskRepository\\";
		taskRepository.ReadTaskDescriptions("TasksDetails.xml");
		
		long start = System.currentTimeMillis();
		
		Task[] T = new Task[0];
		tasks = taskRepository.taskDefinitions.values().toArray(T);
		int tasksAndAgents = agents.size()+tasks.length;
		int[] permutations = new int[tasksAndAgents];
		for(int i=0;i<tasksAndAgents;i++)
		{
			permutations[i] = i;
		}
		String rows = "";
		int index = 1;
		ArrayList<ArrayList<Integer>> result = permute(permutations);
		for (ArrayList<Integer> ar : result){
			rows += getDisplayRow(ar, index++);
	    }
		
		long end = System.currentTimeMillis() - start;
		
		System.out.println(rows);
		System.out.println("Time Taken: " + end + " milliseconds");
	}
	
	private boolean IsAgentIndex(int i)
	{
		return i<agents.size();
	}
	
	public ArrayList<ArrayList<Integer>> permute(int[] num) {
		ArrayList<ArrayList<Integer>> result = new ArrayList<ArrayList<Integer>>();
	 
		//start from an empty list
		result.add(new ArrayList<Integer>());
	 
		for (int i = 0; i < num.length; i++) {
			//list of list in current iteration of the array num
			ArrayList<ArrayList<Integer>> current = new ArrayList<ArrayList<Integer>>();
	 
			for (ArrayList<Integer> l : result) {
				// # of locations to insert is largest index + 1
				for (int j = 0; j < l.size()+1; j++) {
					// + add num[i] to different locations
					l.add(j, num[i]);
	 
					ArrayList<Integer> temp = new ArrayList<Integer>(l);
					current.add(temp);
	 
					//System.out.println(temp);
	 
					// - remove num[i] add
					l.remove(j);
				}
			}
	 
			result = new ArrayList<ArrayList<Integer>>(current);
		}
	 
		return result;
	}
	
	public String getDisplayRow(ArrayList<Integer> p, int index)
	{
		double utility = 0;
		if (!IsAgentIndex(p.get(0)))
			return "";//ignore item as it begins with a task, not an agent, so is not a correct permutation we can use
		IAgent pAgent = agents.get(p.get(0));
		String result = index + ". " + pAgent.getName() + " does tasks: ";
		previousPoint = new UtilityPoint(pAgent.getPosition().x, pAgent.getPosition().y, 0, true, pAgent.getName());
		for(int j=1;j<p.size();j++)
		{
			int i = p.get(j);
			if ( IsAgentIndex(i) )
			{
				this.previousPoint = GetAgentUtilityPoint(i);
				result += ". " + previousPoint.name + " does tasks: ";
			}
			else
			{
				this.previousPoint = GetTaskUtilityPoint(i);
				utility += this.previousPoint.Utility;
				result += " " + this.previousPoint.name + " ";
			}
		}
		return result + ". Total Utility: " + utility + System.lineSeparator();
	}
	
	public UtilityPoint GetTaskUtilityPoint(int i)
	{
		UtilityPoint p = new UtilityPoint();
		Task task = this.tasks[ i - this.agents.size() ];
		for(Method m : task.GetMethods())
		{
			p.x = m.x;
			p.y = m.y;
			p.Utility += m.getOutcome().getQuality() - this.previousPoint.GetDistanceTo(m.x, m.y);
			p.name = task.label;
			this.previousPoint = new UtilityPoint(m.x, m.y, 0, false, task.label);
		}
		return p;
	}
	
	public UtilityPoint GetAgentUtilityPoint(int i)
	{
		IAgent agent = this.agents.get(i);
		UtilityPoint p = new UtilityPoint(agent.getPosition().x, agent.getPosition().y, 0, true, agent.getName());
		return p;
	}
	
    
	
}
