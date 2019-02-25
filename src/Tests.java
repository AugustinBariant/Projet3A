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
	
	@Test
	public static void test1() {
		List<FullInstruction> expected = new ArrayList<FullInstruction>();
		expected.add(new FullInstruction(3,0,0));
		int[] permutation = {1,0,3,2,5,4,7,6};
		OptimizerSolver o = new OptimizerSolver(permutation);
		Optimizer obtainedOptimizer = o.solve();
		List<FullInstruction> obtained = obtainedOptimizer.operations;	
		testInstructionEquality(obtained,expected,permutation);
	}
	@Test
	public static void test2() {
		List<FullInstruction> expected = new ArrayList<FullInstruction>();
		int[] permutation = {0,1,2,3,4,5,6,7};
		OptimizerSolver o = new OptimizerSolver(permutation);
		Optimizer obtainedOptimizer = o.solve();
		List<FullInstruction> obtained = obtainedOptimizer.operations;	
		testInstructionEquality(obtained,expected,permutation);
	}
	@Test
	public static void test3() {
		List<FullInstruction> expected = new ArrayList<FullInstruction>();
		expected.add(new FullInstruction(2,0,1));
		int[] permutation = {0,1,3,2,4,5,7,6};
		OptimizerSolver o = new OptimizerSolver(permutation);
		Optimizer obtainedOptimizer = o.solve();
		List<FullInstruction> obtained = obtainedOptimizer.operations;	
		testInstructionEquality(obtained,expected,permutation);
	}
	@Test
	public static void test4() {
		List<FullInstruction> expected = new ArrayList<FullInstruction>();
		expected.add(new FullInstruction(2,0,1));
		expected.add(new FullInstruction(2,0,2));
		expected.add(new FullInstruction(4,3,0));
		expected.add(new FullInstruction(0,0,2));
		expected.add(new FullInstruction(2,0,1));
		expected.add(new FullInstruction(3,3,0));
		int[] permutation = {2,0,1,3,5,6,7,4};
		OptimizerSolver o = new OptimizerSolver(permutation);
		Optimizer obtainedOptimizer = o.solve();
		List<FullInstruction> obtained = obtainedOptimizer.operations;	
		testInstructionEquality(obtained,expected,permutation);
	}
	@Test
	public static void test5() {
		List<FullInstruction> expected = new ArrayList<FullInstruction>();
		expected.add(new FullInstruction(2,1,0));
		expected.add(new FullInstruction(4,3,2));
		expected.add(new FullInstruction(3,2,0));
		expected.add(new FullInstruction(1,2,0));
		expected.add(new FullInstruction(2,2,1));
		expected.add(new FullInstruction(1,1,3));
		expected.add(new FullInstruction(2,0,1));
		int[] permutation = {2,0,1,3,5,4,7,6};
		OptimizerSolver o = new OptimizerSolver(permutation);
		Optimizer obtainedOptimizer = o.solve();
		List<FullInstruction> obtained = obtainedOptimizer.operations;	
		testInstructionEquality(obtained,expected,permutation);
	}
	@Test
	public static void test6() {
		List<FullInstruction> expected = new ArrayList<FullInstruction>();
		expected.add(new FullInstruction(3,0,0));
		expected.add(new FullInstruction(4,3,0));
		expected.add(new FullInstruction(0,0,1));
		expected.add(new FullInstruction(2,0,2));
		expected.add(new FullInstruction(1,2,1));
		expected.add(new FullInstruction(2,1,0));
		expected.add(new FullInstruction(2,2,3));
		int[] permutation = {2,0,4,3,5,7,1,6};
		OptimizerSolver o = new OptimizerSolver(permutation);
		Optimizer obtainedOptimizer = o.solve();
		List<FullInstruction> obtained = obtainedOptimizer.operations;	
		testInstructionEquality(obtained,expected,permutation);
	}
	@Test
	public static void test7() {
		List<FullInstruction> expected = new ArrayList<FullInstruction>();
		expected.add(new FullInstruction(2,1,0));
		int[] permutation = {0,3,1,2};
		OptimizerSolver o = new OptimizerSolver(permutation);
		Optimizer obtainedOptimizer = o.solve();
		List<FullInstruction> obtained = obtainedOptimizer.operations;	
		testInstructionEquality(obtained,expected,permutation);
	}
	@Test
	public static void test8() {
		List<FullInstruction> expected = new ArrayList<FullInstruction>();
		expected.add(new FullInstruction(3,1,0));
		int[] permutation = {1,3,0,2};
		OptimizerSolver o = new OptimizerSolver(permutation);
		Optimizer obtainedOptimizer = o.solve();
		List<FullInstruction> obtained = obtainedOptimizer.operations;	
		testInstructionEquality(obtained,expected,permutation);
	}
	@Test
	public static void test9() {
		List<FullInstruction> expected = new ArrayList<FullInstruction>();
		expected.add(new FullInstruction(2,0,1));
		expected.add(new FullInstruction(3,0,1));
		int[] permutation = {2,0,1,3};
		OptimizerSolver o = new OptimizerSolver(permutation);
		Optimizer obtainedOptimizer = o.solve();
		List<FullInstruction> obtained = obtainedOptimizer.operations;	
		testInstructionEquality(obtained,expected,permutation);
	}
	
	@Test
	public static void test4x4Permutation() {
		for(int i=0; i<256;i++) {
			int a = ((i>>6) % 4) ;
			int b = ((i>>4) % 4) ;
			int c = ((i>>2) % 4) ;
			int d = (i % 4) ;
			if((a+2)*(b+2)*(c+2)*(d+2)==120) {
				int[] permutation = {a,b,c,d};
				OptimizerSolver o = new OptimizerSolver(permutation);
				Optimizer obtainedOptimizer = o.solve();
				List<FullInstruction> obtained = obtainedOptimizer.operations;	
				Optimizer toTest = new Optimizer(permutation);
				for(int k =0;k<obtained.size();k++) {
					toTest.applyInstruction(obtained.get(k));
				}
				assertTrue(Arrays.equals(permutation,toTest.getPermutation()),"Permutation " + a + b + c + d + " has failed the test");
			}
		}
	}
	
	private static int[] invert(int[] t) {
		int[] a = new int[16];
		for(int i=0;i<16;i++) {
			a[t[i]]=i;
		}
		return a;
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
		assertTrue(Arrays.equals(permResult,permutation), "La permutation n'est pas égal au résultat trouvé après la méthode .solve()\n");
		assertTrue(Arrays.equals(permResult,op2.getPermutation()),"La permutation n'est pas retrouvée avec la liste d'instruction de réference\n");
		System.out.print(java.util.Arrays.equals(permResult,permutation) && java.util.Arrays.equals(permResult,op2.getPermutation()));
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
