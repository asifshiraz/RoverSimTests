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

import masSim.schedule.MaxSumCalculator;

import org.junit.Before;
import org.junit.Test;

import test.Main;

public class MaxSumCalculatorTests {

	public MaxSumCalculatorTests()
	{
		
	}
	
	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test1() {
		MaxSumCalculator calc = new MaxSumCalculator("task1",3);
		calc.AddCostData("a", 10, 18);
		calc.AddCostData("b", 10, 11);
		calc.AddCostData("c", 10, 15);
		String best = calc.GetBestAgent();
		String selected = best;
		assertEquals(selected,"a");
	}
	
	@Test
	public void test2() {
		MaxSumCalculator calc = new MaxSumCalculator("task1",3);
		calc.AddCostData("a", 10, 11);
		calc.AddCostData("b", 10, 18);
		String best = calc.GetBestAgent();
		String selected = best;
		assertEquals(selected,"b");
	}
	
	@Test
	public void test3() {
		MaxSumCalculator calc = new MaxSumCalculator("task1",3);
		calc.AddCostData("a", 10, 11);
		String best = calc.GetBestAgent();
		String selected = best;
		assertEquals(selected,"a");
	}
	
	@Test
	public void test4() {
		MaxSumCalculator calc = new MaxSumCalculator("task1",3);
		calc.AddCostData("a", 0, 11);
		String best = calc.GetBestAgent();
		String selected = best;
		assertEquals(selected,"a");
	}
	

	

}
