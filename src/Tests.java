import static org.junit.jupiter.api.Assertions.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

class Tests {
	static int[] s0 = {3, 8, 15, 1, 10, 6, 5, 11, 14, 13, 4, 2, 7, 0, 9, 12};
	static int[] s1 = {15, 12, 2, 7, 9, 0, 5, 10, 1, 11, 14, 8, 6, 13, 3, 4};
	static int[] s2 = {8, 6, 7, 9, 3, 12, 10, 15, 13, 1, 14, 4, 0, 11, 5, 2};
	static int[] s3 = {0, 15, 11, 8, 12, 9, 6, 3, 13, 1, 2, 4, 10, 7, 5, 14};
	static int[] s4 = {1, 15, 8, 3, 12, 0, 11, 6, 2, 5, 4, 10, 9, 14, 7, 13};
	static int[] s5 = {15, 5, 2, 11, 4, 10, 9, 12, 0, 3, 14, 8, 13, 6, 7, 1};
	static int[] s6 = {7, 2, 12, 5, 8, 4, 6, 11, 14, 9, 1, 15, 13, 3, 10, 0};
	static int[] s7 = {1, 13, 15, 0, 14, 8, 2, 11, 7, 4, 12, 10, 9, 3, 5, 6};
	static int[] permutation = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15 };
	static int[] permutation2 = {0,2,1,3,4,6,5,7,8,10,9,11,12,14,13,15};
	static int[][] serpent = {s0,s1,s2,s3,s4,s5,s6,s7,invert(s1),invert(s2),invert(s3),invert(s4),invert(s5),invert(s6),invert(s7)};
	static int[][] permutation_2 = {{0,1,2,3},{1,0}};
	static int[][] testingSet = {{1,0,3,2,5,4,7,6},{0,1,2,3,4,5,6,7},{0,1,3,2,4,5,7,6},{2,0,1,3,5,6,7,4},{2,0,1,3,5,4,7,6},{2,0,4,3,5,7,1,6},{0,3,1,2},{1,3,0,2},{2,0,1,3}};
	static List<FullInstruction>[] testingSetSolutions = new List[testingSet.length];
	
	public static void initializeTestingSetSolutions() {
		List<FullInstruction> l1 = new ArrayList<FullInstruction>();
		l1.add(new FullInstruction(3,0,0));
		testingSetSolutions[0]=l1;
		
		testingSetSolutions[1] = new ArrayList<FullInstruction>();
		
		List<FullInstruction> l2 = new ArrayList<FullInstruction>();
		l2.add(new FullInstruction(2,0,1));
		testingSetSolutions[2]=l2;
		
		List<FullInstruction> l3 = new ArrayList<FullInstruction>();
		l3.add(new FullInstruction(2,0,1));
		l3.add(new FullInstruction(2,0,2));
		l3.add(new FullInstruction(4,3,0));
		l3.add(new FullInstruction(0,0,2));
		l3.add(new FullInstruction(2,0,1));
		l3.add(new FullInstruction(3,3,0));
		testingSetSolutions[3]=l3;
		
		
		List<FullInstruction> l4 = new ArrayList<FullInstruction>();
		l4.add(new FullInstruction(2,1,0));
		l4.add(new FullInstruction(4,3,2));
		l4.add(new FullInstruction(3,2,0));
		l4.add(new FullInstruction(1,2,0));
		l4.add(new FullInstruction(2,2,1));
		l4.add(new FullInstruction(1,1,3));
		l4.add(new FullInstruction(2,0,1));
		testingSetSolutions[4]=l4;
		
		List<FullInstruction> l5 = new ArrayList<FullInstruction>();
		l5.add(new FullInstruction(3,0,0));
		l5.add(new FullInstruction(4,3,0));
		l5.add(new FullInstruction(0,0,1));
		l5.add(new FullInstruction(2,0,2));
		l5.add(new FullInstruction(1,2,1));
		l5.add(new FullInstruction(2,1,0));
		l5.add(new FullInstruction(2,2,3));
		testingSetSolutions[5]=l5;
		
		List<FullInstruction> l6 = new ArrayList<FullInstruction>();
		l6.add(new FullInstruction(2,1,0));
		testingSetSolutions[6]=l6;
		
		List<FullInstruction> l7 = new ArrayList<FullInstruction>();
		l7.add(new FullInstruction(3,1,0));
		testingSetSolutions[7]=l7;
		
		List<FullInstruction> l8 = new ArrayList<FullInstruction>();
		l8.add(new FullInstruction(2,0,1));
		l8.add(new FullInstruction(3,0,1));
		testingSetSolutions[8]=l8;
		
	}
	
	private static int[] invert(int[] t) {
		int[] a = new int[16];
		for(int i=0;i<16;i++) {
			a[t[i]]=i;
		}
		return a;
	}
	//
	@Test
	public static void testBasics() {
		initializeTestingSetSolutions();
		for(int i=0;i<testingSet.length;i++) {
			OptimizerSolver o = new OptimizerSolver(testingSet[i]);
			Optimizer obtainedOptimizer = o.solve();
			List<FullInstruction> obtained = obtainedOptimizer.operations;
			List<FullInstruction> expected = testingSetSolutions[i];// XXX: write here the desired output	
			testInstructionEquality(obtained,expected,testingSet[i]);
		}
		
	}
	
	
	public void printTime(Timestamp ts) {
		Timestamp ts2 = Timestamp.from(java.time.Clock.systemUTC().instant());
		long diff = ts2.getTime()-ts.getTime();
		System.out.print("\n Time: "+diff+" ms");
	}
	
	// teste si les deux listes d'instructions sont équivalentes, et si elles donnent bien la bonne permutation
	private static void testInstructionEquality(List<FullInstruction> instructionListToTest, List<FullInstruction> instructionListStandard, int[] permutation) {
		if(instructionListToTest.size()!=instructionListStandard.size()) {
			assert(false);
			return;
		}
		Optimizer op1 = new Optimizer(permutation);
		Optimizer op2 = new Optimizer(permutation);
		for(int i=0; i<instructionListToTest.size();i++) {
			op1.applyInstruction(instructionListToTest.get(i));
			op2.applyInstruction(instructionListStandard.get(i));
		}
		int[] permResult = op1.getPermutation();
		assert(java.util.Arrays.equals(permResult,permutation));
		assert(java.util.Arrays.equals(permResult,op2.getPermutation()));
		System.out.print(java.util.Arrays.equals(permResult,permutation));
		System.out.print(java.util.Arrays.equals(permResult,op2.getPermutation()));
		return;
	}
	@Test
	public void searchPermutation(int i) {
		Timestamp ts = Timestamp.from(java.time.Clock.systemUTC().instant());
		OptimizerSolver o = new OptimizerSolver(serpent[i]);
		Optimizer obtainedOptimizer = o.solve();
		int[] obtained = obtainedOptimizer.getPermutation();
		int[] expected = serpent[i];// XXX: write here the desired output	
		printTime(ts);
		assertEquals(expected, obtained, "permutation "+i);
	}
	
}
