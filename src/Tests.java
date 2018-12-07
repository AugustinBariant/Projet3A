import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

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
	
	private static int[] invert(int[] t) {
		int[] a = new int[16];
		for(int i=0;i<16;i++) {
			a[t[i]]=i;
		}
		return a;
	}
	
	@Test
	public void searchIdentity() {
		Optimizer o = new Optimizer(); // XXX: bogus object creation
		//List<FullInstruction> obtained = o.search(permutation);
		List<FullInstruction> expected = new ArrayList<>(); // XXX: write here the desired output

		//assertEquals(expected, obtained, "identity permutation");
	}

	@Test
	public void searchPermutation2() {
	    OptimizerSolver o = new OptimizerSolver(s2); // XXX: bogus object creation
		Optimizer obtainedOptimizer = o.solve();
		int[] obtained = obtainedOptimizer.getPermutation();
		int[] expected = s2;// XXX: write here the desired output

		assertEquals(expected, obtained, "permutation2");
	}
	
	
	public void searchSmallPermutation() {
	    OptimizerSolver o = new OptimizerSolver(permutation); // XXX: bogus object creation
		Optimizer obtainedOptimizer = o.solve();
		int[] obtained = obtainedOptimizer.getPermutation();
		int[] expected = permutation;// XXX: write here the desired output

		assertEquals(expected, obtained, "permutation2");
	}
}
