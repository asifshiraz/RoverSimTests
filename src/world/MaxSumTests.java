package world;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.AbstractMap.SimpleEntry;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import org.junit.Before;
import org.junit.Test;

import test.Main;

public class MaxSumTests {

	public MaxSumTests()
	{
		
	}
	
	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test() {
		Main jmax = new Main();
		String input = readLinesUsingFileReader();
		ArrayList<SimpleEntry<String, String>> result = jmax.CalculateMaxSumAssignments(input);
	}
	
	private String readLinesUsingFileReader() 
	{
		try
		{
			StringBuilder output = new StringBuilder();
		    File file = new File("D:\\EclipseWorkspace\\jmaxsum\\RoverCop.cop2");
		    FileReader fr = new FileReader(file);
		    BufferedReader br = new BufferedReader(fr);
		    String line;
		    while((line = br.readLine()) != null){
		        output.append(line).append("\n");
		    }
		    br.close();
		    fr.close();
		    return output.toString();
		}
		catch(Exception ex){
			return "";
		}
	}

}
