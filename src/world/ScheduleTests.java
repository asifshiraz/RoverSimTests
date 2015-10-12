package world;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;

public class ScheduleTests {

	private ExecutorService schedulerPool;

	public ScheduleTests()
	{
		schedulerPool = Executors.newFixedThreadPool(5);
	}
	
	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test() {
		try
		{
			Future<Integer> test = getExpectedScheduleQuality();
			int i = test.get(11,TimeUnit.SECONDS);
			System.out.println(i);
			assertEquals(i, 10);
		}
		catch(Exception ex)
		{
			fail(ex.toString());
		}
	}
	
	public Future<Integer> getExpectedScheduleQuality() throws IOException {
		return schedulerPool.submit(new Callable<Integer>() {
			@Override
			public Integer call() throws Exception {
				Thread.sleep(10000);
				return 10;
			}
		});
	}

}
