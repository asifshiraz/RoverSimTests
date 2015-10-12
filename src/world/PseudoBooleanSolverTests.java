package world;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import org.junit.Test;
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
import masSim.schedule.ScheduleQualities;

public class PseudoBooleanSolverTests {
	
	private int GetRandomBaseQuality()
	{
		int min = 10;
		int max = 100;
		return (int)(Math.random() * (max - min) + min);
	}
	
	private int GetRandomIncrementalQuality()
	{
		int min = 20;
		int max = 50;
		return (int)(Math.random() * (max - min) + min);
	}
	
	private ArrayList<AgentScheduleQualities> CreateRamdomScheduleQualities(int numberOfAgents, int numberOfTasks)
	{
		int base = GetRandomBaseQuality();
		ArrayList<AgentScheduleQualities> scheduleQualities = new ArrayList<AgentScheduleQualities>();
		for(int i = 1; i<=numberOfAgents; i++)
		{
			AgentScheduleQualities a = new AgentScheduleQualities(1000+i);
			a.TaskQualities = new ArrayList<TaskScheduleQualities>();
			for(int j = 1; j<=numberOfTasks; j++)
			{
				a.TaskQualities.add(new TaskScheduleQualities(2000+j,base,GetRandomIncrementalQuality()));
			}
			scheduleQualities.add(a);
		}
		return scheduleQualities;
	}
	
	
	
	@Test
	public void CalculateBestAgent()
	{
		ArrayList<AgentScheduleQualities> input = CreateRamdomScheduleQualities(2,2);
		CalculatePlain(input);
		
		
		
		BooleanOptimizationCalculator calc = new BooleanOptimizationCalculator();
		
		int[] result1 = calc.SolveOptimizationProblem(input).model();
		
		
		String result = calc.BuildOPBInput( input );
		String filename = "E:\\EclipseWorkspace\\RoverSim\\TaskRepository\\problemDynamic.opb";
		try (Writer writer = new BufferedWriter(new OutputStreamWriter(
	        new FileOutputStream(filename), "US-ASCII"))) {
			writer.write(result);
		}
		catch(Exception ex)
		{
			System.out.print(ex);
		}
		int selection = calc.Solve("E:\\EclipseWorkspace\\RoverSim\\TaskRepository\\problemDynamic.opb");
		//*/
	}
	
	private void CalculatePlain(ArrayList<AgentScheduleQualities> input)
	{
		MeasureTime.Timer2.Start();
		int totalTasks = input.get(0).TaskQualities.size();
		int i = 0;
		int maxImprovement = -9999999;
		String result = "";
		for(int t = 0; t<totalTasks; t++)
		{
			for(AgentScheduleQualities aql : input)
			{
				TaskScheduleQualities ql = aql.TaskQualities.get(t);
				int improvement = ql.incremental - ql.base;
				if (improvement > maxImprovement)
				{
					maxImprovement = improvement;
					result = aql.AgentVariableId + " does " + ql.TaskId;
				}
			}
			System.out.println(result);
		}
		MeasureTime.Timer2.Stop();
		System.out.println("Plain Calculation Took " + MeasureTime.Timer2.GetTotal());
	}
	
}
