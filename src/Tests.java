import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

class Tests {
	static int[] permutation = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15 };
	static int[] permutation2 = {0,2,1,3,4,6,5,7,8,10,9,11,12,14,13,15};
	
	@Test
	public void searchIdentity() {
		Optimizer o = new Optimizer(0); // XXX: bogus object creation
		List<FullInstruction> obtained = o.search(permutation);
		List<FullInstruction> expected = new ArrayList<>(); // XXX: write here the desired output

		assertEquals(expected, obtained, "identity permutation");
	}

	@Test
	public void searchPermutation2() {
	    Optimizer o = new Optimizer(0); // XXX: bogus object creation
		List<FullInstruction> obtained = o.search(permutation2);
		List<FullInstruction> expected = new ArrayList<>(); // XXX: write here the desired output

		assertEquals(expected, obtained, "permutation2");
	}
	
}
