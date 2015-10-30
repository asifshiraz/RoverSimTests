package world;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.Assert;
import masSim.schedule.AgentScheduleQualities;
import masSim.schedule.TaskScheduleQualities;
import net.sf.javailp.Linear;
import net.sf.javailp.OptType;
import net.sf.javailp.Problem;
import net.sf.javailp.Result;
import net.sf.javailp.Solver;
import net.sf.javailp.SolverFactory;
import net.sf.javailp.SolverFactoryLpSolve;
import raven.MeasureTime;
import masSim.schedule.BooleanOptimizationCalculator;
import masSim.schedule.MultipleTaskScheduleQualities;
import masSim.schedule.ScheduleQualities;

public class PseudoBooleanSolverTests {
	
	@Test
	public void CalculateBestAgent()
	{	
		//String s = "";
		//for(int i=1;i<=128;i++){s += "3 x" + i + " ";}
			
		
		DoBothCalculations(2,2);
		
		StringBuilder o = new StringBuilder();
		for(int agents=2; agents<=6; agents++)
		{
			for(int tasks=2; tasks<=4; tasks++)
			{
				//DoBothCalculations(agents, tasks);
			}
		}
		System.out.println(o.toString());
	}
	
	private int[] DoBothCalculations(int i, int j)
	{
		int[] timesTaken = new int[2]; 
		ArrayList<AgentScheduleQualities> input = CreateRamdomScheduleQualities(i,j);
		List<List<Integer>> taskCombinations = CreateCombinations(input);
		
		CalculatePlain(input, taskCombinations);
		
		BooleanOptimizationCalculator calc = new BooleanOptimizationCalculator();
		//int[] result1 = calc.SolveOptimizationProblem(input);
		Map<String, int[]> variableNameMappingToAgentTaskCombination = new HashMap<String, int[]>();
		
		String result = BuildOPBInput( input, variableNameMappingToAgentTaskCombination, taskCombinations, j );
		String filename = "E:\\EclipseWorkspace\\RoverSim\\TaskRepository\\problemDynamic.opb";
		try (Writer writer = new BufferedWriter(new OutputStreamWriter(
	        new FileOutputStream(filename), "US-ASCII"))) {
			writer.write(result);
		}
		catch(Exception ex)
		{
			System.out.print(ex);
		}
		int[] result2 = calc.Solve("E:\\EclipseWorkspace\\RoverSim\\TaskRepository\\problemDynamic.opb");
		List<Integer> resultList = new ArrayList<Integer>();
		System.out.println(Arrays.toString(resultList.toArray()));
		for(int r=0; r<result2.length; r++)
		{
			if (result2[r]>0) 
			{
				resultList.add(result2[r]);
				int[] agentCombination = variableNameMappingToAgentTaskCombination.get("x" + result2[r]);
				System.out.println( agentCombination[0] + " does " + taskCombinations.get(agentCombination[1]) );//mapping object's second array element is task combination index
			}
			
		}
		//System.out.print(Arrays.toString(resultList.toArray()));
		System.out.println("Done for " + i + " agents and " + j + " tasks");
		return null;
	}
	
	private List<List<Integer>> CreateCombinations(ArrayList<AgentScheduleQualities> input)
	{
		//Get all the unique task assignment combinations possible for a single agent. Since all agents share similar tasks,
		//take the first agent and use his task list to initialize variable for all task combinations. E.g. for two tasks, 0,1, this variable
		//should have 0,1,2,21
		List<List<Integer>> taskCombinations = new ArrayList<List<Integer>>();
		for(int i=0;i<input.get(0).TaskQualities.size();i++)
		{
			taskCombinations.add(input.get(0).TaskQualities.get(i).TaskIds);
		}
		return taskCombinations;
	}
	
	@Test
	public void IsAssignmentValidTests()
	{
		int agentsSize = 3;
		List<List<Integer>> taskCombinations = new ArrayList<List<Integer>>();
		ArrayList<Integer> a = new ArrayList<Integer>();
		ArrayList<Integer> b = new ArrayList<Integer>();
		ArrayList<Integer> c = new ArrayList<Integer>();
		
		taskCombinations.add(a);
		taskCombinations.add(b);
		taskCombinations.add(c);
		
		//Test
		a.add(1);
		b.add(2);
		b.add(3);
		c.add(0);
		
		int[] assignments = new int[]{0,1,2};
		Assert.assertEquals(true, IsAssignmentValid(assignments, agentsSize, taskCombinations) );
		
		b.add(2);Assert.assertEquals(false, IsAssignmentValid(assignments, agentsSize, taskCombinations) );
	}
	
	@Test
	public void BaseConversionTest()
	{
		int[] arr = null;
		arr = ConvertBase10ToBaseNumberOfTasks(7,3,2);
		Assert.assertArrayEquals(ConvertBase10ToBaseNumberOfTasks(0,3,2), new int[]{0,0,0});
		Assert.assertArrayEquals(ConvertBase10ToBaseNumberOfTasks(1,3,2), new int[]{0,0,1});
		Assert.assertArrayEquals(ConvertBase10ToBaseNumberOfTasks(2,3,2), new int[]{0,1,0});
		Assert.assertArrayEquals(ConvertBase10ToBaseNumberOfTasks(3,3,2), new int[]{0,1,1});
		Assert.assertArrayEquals(ConvertBase10ToBaseNumberOfTasks(4,3,2), new int[]{1,0,0});
		Assert.assertArrayEquals(ConvertBase10ToBaseNumberOfTasks(5,3,2), new int[]{1,0,1});
		Assert.assertArrayEquals(ConvertBase10ToBaseNumberOfTasks(6,3,2), new int[]{1,1,0});
		Assert.assertArrayEquals(ConvertBase10ToBaseNumberOfTasks(7,3,2), new int[]{1,1,1});
		Assert.assertArrayEquals(ConvertBase10ToBaseNumberOfTasks(8,3,2), new int[]{0,0,0});
	}
	
	@Test
	public void IntegerCombinationTest()
	{
		List<Integer> arr = Arrays.asList(1,2,3,4);
		List<List<Integer>> result = GetArrayCombinations(arr);
		for(List<Integer> r : result)
		{
			System.out.println(r);
		}
	}
	
	protected List<Integer[]> permute(List<Integer> arr, int k){
        List<Integer[]> result = new ArrayList<Integer[]>();
        Integer[] arg = new Integer[0];
    	for(int i = k; i < arr.size(); i++){
            java.util.Collections.swap(arr, i, k);
            permute(arr, k+1);
            java.util.Collections.swap(arr, k, i);
        }
        if (k == arr.size() -1){
            result.add(arr.toArray(arg));
        }
		return result;
    }
	
	protected int GetRandomBaseQuality()
	{
		int min = 80;
		int max = 100;
		return (int)(Math.random() * (max - min) + min);
	}
	
	protected int GetRandomIncrementalQuality()
	{
		int min = 100;
		int max = 120;
		return (int)(Math.random() * (max - min) + min);
	}
	
	private ArrayList<AgentScheduleQualities> CreateRamdomScheduleQualities(int numberOfAgents, int numberOfTasks)
	{
		ArrayList<AgentScheduleQualities> scheduleQualities = new ArrayList<AgentScheduleQualities>();
		for(int i = 1; i<=numberOfAgents; i++)
		{
			AgentScheduleQualities a = new AgentScheduleQualities(100+i);
			a.TaskQualities = new ArrayList<MultipleTaskScheduleQualities>();
			List<Integer> taskIds = CreateRandomArrayOfTaskIds(numberOfTasks);
			for(int j = 1; j<=numberOfTasks; j++)
			{
				a.TaskIds = taskIds;
				a.TaskQualities = GetRandomMultipleTaskScheduleQualities(taskIds);
			}
			scheduleQualities.add(a);
		}
		return scheduleQualities;
	}
	
	private ArrayList<MultipleTaskScheduleQualities> GetRandomMultipleTaskScheduleQualities(List<Integer> taskIds)
	{
		ArrayList<MultipleTaskScheduleQualities> result = new ArrayList<MultipleTaskScheduleQualities>();
		int base = GetRandomBaseQuality();
		List<List<Integer>> taskCombinations = GetArrayCombinations(taskIds);
		for(List<Integer> taskCombination : taskCombinations)
		{
			result.add(new MultipleTaskScheduleQualities(taskCombination,base,GetRandomIncrementalQuality()));	
		}
		result.add(new MultipleTaskScheduleQualities(new ArrayList<Integer>(),base,base));	
		return result;
	}
	
	private List<Integer> Clone(List<Integer> arr)
	{
		List<Integer> copy = new ArrayList<Integer>();
		copy.addAll(arr);
		return copy;
	}
	
	protected List<List<Integer>> GetArrayCombinations(List<Integer> arr)
	{
		List<List<Integer>> result = new ArrayList<List<Integer>>();
		if (arr.size()<=1)
		{ 
			result.add(Clone(arr));
		}
		else
		{
			int firstInteger = arr.get(0);
			List<Integer> subArrayWithOneLessItem = arr.subList(1, arr.size());
			List<List<Integer>> combinations = GetArrayCombinations(subArrayWithOneLessItem);
			for(List<Integer> aCombination : combinations)
			{
				result.add(aCombination);
				List<Integer> aCombinationWithFirstIntegerAdded = Clone(aCombination);
				aCombinationWithFirstIntegerAdded.add(firstInteger);
				result.add(aCombinationWithFirstIntegerAdded);
			}
			List<Integer> aCombinationWithOnlFirstInteger = new ArrayList<Integer>();
			aCombinationWithOnlFirstInteger.add(firstInteger);
			result.add(aCombinationWithOnlFirstInteger);
		}
		
		return result;
	}
	
	private List<Integer> CreateRandomArrayOfTaskIds(int numberOfTasks)
	{
		List<Integer> tasks = new ArrayList<Integer>();
		for(int i=0; i<numberOfTasks; i++)
		{
			tasks.add(1000 +i);
		}
		return tasks;
	}
	
	
	
	private void AddPBVariableToConstraintsList(Map<Integer,List<String>> constraintsPerTask, String variable, List<Integer> tasksForThisVariable)
	{
		for(Integer _int : tasksForThisVariable)
		{
			if (constraintsPerTask.containsKey(_int))
			{
				constraintsPerTask.get(_int).add(variable);
			}
			else
			{
				List<String> arr = new ArrayList<String>();
				arr.add(variable);
				constraintsPerTask.put(_int, arr);
			}
		}
	}
	
	public String BuildOPBInput(ArrayList<AgentScheduleQualities> input, Map<String, int[]> variableNameMappingToAgentTastCombination, List<List<Integer>> taskCombinations, int numberOfTasks)
	{	
		StringBuilder opb = new StringBuilder();
				
		int taskCombinationsSize = taskCombinations.size();
		
		//Get all unique agents
		List<Integer> agentIds = new ArrayList<Integer>();
		for(int i=0;i<input.size();i++)
		{
			agentIds.add(input.get(i).AgentVariableId);
		}
		int agentsSize = agentIds.size();
				
		Map<Integer,List<String>> constraintsPerTask = new HashMap<Integer, List<String>>();
		
		opb.append(  String.format("* #variable= %1$s #constraint= %2$s%3$s", agentsSize * taskCombinationsSize, numberOfTasks, System.lineSeparator()));
		opb.append("min:");
		int i = 1;
		String variableMappingCommentBlock = "* ";
		for(int agent=0; agent<agentsSize; agent++)
		{
			for(int j=0; j<taskCombinationsSize; j++)
			{
				int [] mapping = new int[2];
				mapping[0] = input.get(agent).AgentVariableId;
				mapping[1] = j;
				String variableName = "x" + i++;
				variableNameMappingToAgentTastCombination.put(variableName,mapping);
				
				//Calculate quality for this combination
				List<Integer> t = taskCombinations.get(j);
				int quality = this.GetQualityIfAgentDoes(t, input.get(agent));
				if (quality>0)//- sign to convert min function to max
				{	
					opb.append(" -" + quality + " " + variableName);
				}
				else if (quality<0)
					opb.append(" " + Math.abs(quality) + " " + variableName);
				else
					opb.append(" " + quality + " " + variableName);
				variableMappingCommentBlock += variableName + "=" + Arrays.toString(t.toArray()) + " ";
				AddPBVariableToConstraintsList(constraintsPerTask, variableName, t);
			}
		}
		opb.append(";" + System.lineSeparator());
		opb.append(variableMappingCommentBlock + System.lineSeparator());
		//Create constraints
		
		/*String[] constraints = new String[taskCombinationsSize];
		for(int j=1;j<=taskCombinationsSize;j++)
		{
			constraints[j-1] = "";
		}
		int index = 1;
		for(int k=1;k<=agentsSize;k++)
		{
			for(int j=1;j<=taskCombinationsSize;j++)
			{
				constraints[j-1] += "1 x"+index++ + " ";
			}	
		}
		for(int j=1;j<=taskCombinationsSize;j++)
		{
			if (constraints[j-1].length()!=0)
				opb.append(constraints[j-1] + "= 1;" + System.lineSeparator());
		}*/
		for(List<String> constraintsForATask : constraintsPerTask.values())
		{
			for(String str : constraintsForATask)
			{
				opb.append( "1 " + str + " "  );
			}
			opb.append( "= 1;" + System.lineSeparator() );
		}
		return opb.toString();
	}
	
	private int GetQualityIfAgentDoes(List<Integer> tasks, AgentScheduleQualities asql)
	{
		for(MultipleTaskScheduleQualities tq: asql.TaskQualities)
		{
			if (tq.IsMatch(tasks))
			{
				return tq.diff();
			}
		}
		return 0;
	}
	
	private int[] ConvertBase10ToBaseNumberOfTasks(double number, int numAgents, int numTasksCombinations)
	{
		int[] result = new int[numAgents+1];
		double remainder = number;
		for(int i=numAgents; i>=0; i--)
		{
			double currentBase = Math.pow(numTasksCombinations,i);
			result[numAgents-i] = (int)(remainder / currentBase);
			remainder = remainder % currentBase;
		}
		return Arrays.copyOfRange(result, 1, result.length);
	}
	
	private void CalculatePlain(ArrayList<AgentScheduleQualities> input, List<List<Integer>> taskCombinations)
	{
		MeasureTime.Timer2 = new MeasureTime();
		MeasureTime.Timer2.Start();
	
		int taskCombinationsSize = taskCombinations.size();
		
		//Get all unique agents
		List<Integer> agentIds = new ArrayList<Integer>();
		for(int i=0;i<input.size();i++)
		{
			agentIds.add(input.get(i).AgentVariableId);
		}
		int agentsSize = agentIds.size();
		
		//Variable to hold all possible combination of tasks for any agent
		Map<Integer[], Integer> agentTaskCombinationQualities = new HashMap<Integer[],Integer>();
		
		double totalPossibleValues = Math.pow(taskCombinationsSize, agentsSize);
		//Populate all combinations
		
		int bestQuality = -9999999;
		int[] bestCombination = new int[0];
		
		int[] lastValidAssignment = new int[agentsSize];
		int[] nextAssignment = new int[agentsSize];
		
		for(int i=0; i<totalPossibleValues; i++)
		{
			nextAssignment = ConvertBase10ToBaseNumberOfTasks(i,agentsSize, taskCombinationsSize);
			//System.out.println(Arrays.toString(assignments));
			
			if (!IsAssignmentValid(nextAssignment, agentsSize, taskCombinations))
			{
				continue;
			}
			lastValidAssignment = nextAssignment;
			//System.out.println(RemapAssignmentToAgentTasks(lastValidAssignment, taskCombinations));
			int tempQuality = 0;
			//Find combination quality
			for(int agentIndex=0; agentIndex<agentsSize; agentIndex++)
			{
				int inn = lastValidAssignment[agentIndex];
				try
				{
					List<Integer> tasksSet = taskCombinations.get( inn );
					tempQuality += GetQualityIfAgentDoes( tasksSet, input.get(agentIndex)  );
				}
				catch(Exception ex)
				{
					System.out.println(inn + " " + ex);
				}
				
			}
			//If this is best, assign
			if (tempQuality>bestQuality)
			{
				bestQuality = tempQuality;
				bestCombination = lastValidAssignment;
			}
		}
		
		MeasureTime.Timer2.Stop();
		System.out.println("Plain Calculation Took " + MeasureTime.Timer2.GetTotal());
		System.out.println(RemapToAgentTasksFromTaskCombinations(bestCombination, taskCombinations, input));
		
	}
	
	private String RemapToAgentTasksFromTaskCombinations(int[] chosenAssignment, List<List<Integer>> taskCombinations, ArrayList<AgentScheduleQualities> input)
	{
		String r = "Best Combination: " + System.lineSeparator();
		for(int i=0;i<chosenAssignment.length;i++)
		{
			r += input.get(i).AgentVariableId + " does tasks ";
			List<Integer> tasks = taskCombinations.get(chosenAssignment[i]);
			for(Integer in : tasks)
			{
				r += " " + in + " " + System.lineSeparator();
			}
		}
		return r;
	}
	
	
	
	private boolean IsAssignmentValid(int[] assignments, int agentsSize, List<List<Integer>> taskCombinations)
	{
		boolean result = true;
		List<Integer> tasksAlreadyTakenUpByAnAgent = new ArrayList<Integer>();
		
		//List<Integer> tasksSetPrevious = null;
		for(int agentIndex=0; agentIndex<agentsSize; agentIndex++)
		{
			int inn = assignments[agentIndex];
			try
			{
				List<Integer> tasksSet = taskCombinations.get( inn );
				for(Integer task : tasksSet)
				{
					if (tasksAlreadyTakenUpByAnAgent.contains(task))
					{
						return false;
					}
					else
					{
						tasksAlreadyTakenUpByAnAgent.add(task);
					}
				}
			}
			catch(Exception ex)
			{
				System.out.println(inn + " " + ex);
			}	
		}
		return result;
	}
	
	
}
