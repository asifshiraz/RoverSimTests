package world;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import scpsolver.constraints.LinearBiggerThanEqualsConstraint;
import scpsolver.constraints.LinearSmallerThanEqualsConstraint;
import scpsolver.lpsolver.LinearProgramSolver;
import scpsolver.lpsolver.SolverFactory;
import scpsolver.problems.LPWizard;
import scpsolver.problems.LinearProgram;

public class InequalitiesTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test() {
		LinearProgram lp = new LinearProgram(new double[]{5.0,10.0}); 
		lp.addConstraint(new LinearBiggerThanEqualsConstraint(new double[]{3.0,1.0}, 8.0, "c1")); 
		lp.addConstraint(new LinearBiggerThanEqualsConstraint(new double[]{0.0,4.0}, 4.0, "c2")); 
		lp.addConstraint(new LinearSmallerThanEqualsConstraint(new double[]{2.0,0.0}, 2.0, "c3")); 
		lp.setMinProblem(true); 
		LinearProgramSolver solver  = SolverFactory.newDefault(); 
		double[] sol = solver.solve(lp);
		
		LPWizard lpw = new LPWizard(); 
		lpw.plus("x1",5.0).plus("x2",10.0); 
		lpw.addConstraint("c1",8,"<=").plus("x1",3.0).plus("x2",1.0); 
		lpw.addConstraint("c2",4,"<=").plus("x2",4.0); 
		lpw.addConstraint("c3", 2, ">=").plus("x1",2.0); 
	}

}
