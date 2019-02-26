import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;
import java.sql.Timestamp;

public class Optimizer {
	public static HashSet<WorkspaceKey> tree;
	private static int cardinalityLog;
	private static int[] permutation = new int[1<<cardinalityLog];
	private static int[] permutationLines; 
	public int numberOfMatch;
	private static int numberOfOperation;
	private static int[] returnColumns = new int[cardinalityLog+1];
	private static int postPoningCycles=3;
	public List<FullInstruction> operations;
	private BooleanTab[] workspace;
	private BooleanTab negated;
	private BooleanTab read;
	
	
	Optimizer() {
		
	}
	Optimizer(int[] perm) {
		cardinalityLog = 32-Integer.numberOfLeadingZeros(perm.length-1);;
		permutation = perm;
		operations = new ArrayList<FullInstruction>();
		workspace = new BooleanTab[cardinalityLog+1];
		for(int i =0; i<cardinalityLog+1;i++) {
			workspace[i] = new BooleanTab();
			for(int k=0;k<1<<cardinalityLog;k++) {
				workspace[i].set(k,1==(k>>i)%2);
			}
			 
		}
		negated = new BooleanTab();
		numberOfMatch=0;
		read = new BooleanTab();
		read.set(cardinalityLog, true);
		tree = new HashSet<WorkspaceKey>();
		permutationLines = new int[cardinalityLog];
		initializePermutation();
		try {
			updateNumberOfMatch();
		}catch(Exception e) {
			
		}
		
	}
	
	
	private void initializePermutation() {
		for(int k =0; k<cardinalityLog;k++) {
			permutationLines[k]=0;
			for(int i=0; i<(1<<cardinalityLog);i+=1){
				if((permutation[i]>>k) % 2 == 1) {
					permutationLines[k]+=(1<<i);
				}
			}
		}
	}
	// ApplyInstruction applies the instruction then checks if the instruction is valid in the current state of the workspace.
	public boolean applyInstruction(FullInstruction f) {
		int L1 = workspace[f.column1].mainValue;
		switch(f.instruct) {
			case And:
				workspace[f.column1].mainValue &= workspace[f.column2].mainValue;
				break;
			case Or:
				workspace[f.column1].mainValue |= workspace[f.column2].mainValue;
				break;
			case Xor:
				workspace[f.column1].mainValue ^= workspace[f.column2].mainValue;
				break;
			case Not:
				workspace[f.column1].mainValue =(~workspace[f.column1].mainValue+(1<<(1<<cardinalityLog)))%(1<<(1<<cardinalityLog));
				break;
			case Mov:
				workspace[f.column1].mainValue = workspace[f.column2].mainValue;
				break;
		}
		int L2 = workspace[f.column1].mainValue;
		operations.add(f);
		if(!check(f,L1,L2)) {
			//for(int i =0; i<16; i++) {
				//Workspace[i][f.column1]=L[i];
			//}
			return false;
		}
		
		return true;
	}
	private void updateNumberOfMatch() throws Exception {
		int a;
		int NbOfMatch = 0;
		List<Integer> s = new ArrayList<Integer>();
		int[] returnCols = new int[cardinalityLog];
		for(int k =0; k<cardinalityLog+1;k++) {
			a=workspace[k].mainValue;
			for(int j=0;j<cardinalityLog;j++) {
				if(!s.contains(j)) {
					if(a==permutationLines[j]) {
						s.add(j);
						returnCols[j]=k;
						break;
					}
				}
			}
		}
		NbOfMatch = s.size();
		if(NbOfMatch==cardinalityLog) {
			returnColumns = returnCols;
		}
		if(numberOfMatch>NbOfMatch) {
			throw new Exception();
		}
		numberOfMatch = NbOfMatch;
		return;
		/*
		if(numberOfOperation+postPoningCycles<=operations.size()+1) {
			if(numberOfMatch>NbOfMatch) {
				throw new Exception();
			}else {
				if(numberOfMatch==NbOfMatch) {
					return;
				}
				numberOfOperation = operations.size()+1;
				numberOfMatch = NbOfMatch;
				return;
			}
		}else {
			if(numberOfMatch<NbOfMatch) {
				numberOfOperation = operations.size()+1;
				numberOfMatch = NbOfMatch;
				return;
			}
			if(numberOfMatch-1>NbOfMatch) {
				throw new Exception();
			}
			return;
		}
		*/
		
	}
	private void updateNegateAndCheck(FullInstruction f) throws Exception {
		switch(f.instruct) {
			case And:
				negated.set(f.column1,false);
				negated.set(f.column2,false);
				break;
			case Or:
				negated.set(f.column1,false);
				negated.set(f.column2,false);
				break;
			case Xor:
				negated.set(f.column1,false);
				negated.set(f.column2,false);
				break;
			case Not:
				if(negated.get(f.column1)) {
					throw new Exception();
				}else {
					negated.set(f.column1,true);
				}
				break;
			case Mov:
				negated.set(f.column1,negated.get(f.column2));
				break;	
		}
		return;
	}
	private void checkCopy(FullInstruction f) throws Exception {
		if(f.instruct!=Instruction.Mov) {
			for(int i=0; i<cardinalityLog+1; i++) {
				if(i==f.column1) {
					continue;
				}
				if(workspace[i].mainValue==workspace[f.column1].mainValue) {
					throw new Exception();
				}
			}
		}
		return;
	}
	// Checks if every line is different
	private void checkPermutation() throws Exception{
		int[] L = new int[1<<(cardinalityLog+1)];
		int a;
		for(int i =0; i<(1<<cardinalityLog);i++) {
			a=0;
			for(int k=0; k<cardinalityLog+1;k+=1){
				if(workspace[k].get(i)) {
					a+=(1<<k);
				}
			}
			if(L[a]>=1) {
				throw new Exception();
			}else {
				L[a]+=1;
			}
		}
		return;
		
	}
	
	private void updateAndCheckRead(FullInstruction f) throws Exception{
		switch(f.instruct) {
			case Not:
				break;
			case Mov:
				if(!read.get(f.column1)) {
					throw new Exception();
				}
				read.set(f.column1, false);
				read.set(f.column2, false);
				break;
			default:
				read.set(f.column1, false);
				read.set(f.column2, true);
				break;	
		}
		return;
	}
	private void checkEqual(int L1, int L2) throws Exception{
		if(L1==L2) {
			throw new Exception();
		}
		return;
	}
	// Check if the new line is either entirely true or entirely false
	private void checkTrueFalse(int L1) throws Exception{
		if(L1==0 || (~L1)%(1<<(1<<cardinalityLog))==0) {
			throw new Exception();
		}
		return;
	}
	private void checkAndUpdateTree() throws Exception{
		int[] L = new int[cardinalityLog+1];
		for(int k =0; k<cardinalityLog+1;k++) {
			L[k]=workspace[k].mainValue;
		}
		WorkspaceKey t = new WorkspaceKey(L);
		if(tree.contains(t)) {
			throw new Exception();
		}
		tree.add(t);
		return ;
		
	}
	
	private boolean check(FullInstruction f, int L1, int L2) {
		try {
			checkPermutation();
			updateNegateAndCheck(f);
			checkCopy(f);
			updateNumberOfMatch();
			updateAndCheckRead(f);
			checkEqual(L1,L2);
			checkTrueFalse(L2);
			checkAndUpdateTree();
			return true ;
		}catch(Exception e) {
			return false;
		}
		
	}
	public Optimizer copy() {
		List<FullInstruction> op = new ArrayList<FullInstruction>();
		BooleanTab[] ws = new BooleanTab[cardinalityLog+1];
		for(int i=0;i<(cardinalityLog+1);i++) {
			ws[i]=new BooleanTab(workspace[i].mainValue);
		}
		for(FullInstruction el : operations) {
			op.add(el);
		}
		Optimizer o = new Optimizer();
		o.read = new BooleanTab(read.mainValue);
		o.operations = op;
		o.workspace = ws;
		o.negated=new BooleanTab(negated.mainValue);
		//o.numberOfMatch=numberOfMatch;
		return o;
	}

	public void printCurrentState() {
		System.out.print("Current Workspace : \n");
		for(int i =0;i<cardinalityLog+1;i++) {
			System.out.print("[");
			for(int j=0;j<(1<<cardinalityLog)-1;j++) {
				System.out.print((workspace[i].get(j)?1:0) +" ,");
			}
			System.out.print((workspace[i].get((1<<cardinalityLog)-1)?1:0) +" ]");
			System.out.print(" " + workspace[i].mainValue +"\n");
		}
	}
	
	public int[] getPermutation() {
		int[] tab = new int[1<<cardinalityLog];
		for(int i =0;i<(1<<cardinalityLog);i++) {
			for(int k=0;k<cardinalityLog;k++) {
				if(workspace[returnColumns[k]].get(i)) {
					tab[i]+=(1<<k);
				}
			}
			
		}
		return tab;
	} 
	
	public static void main(String[] args){
		Timestamp ts = Timestamp.from(java.time.Clock.systemUTC().instant());
		int[] permutation = {0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15};
		int[] permutation2 = {0,2,1,3,4,6,5,7,8,10,9,11,12,13,14,15};
		int[] s2 = {8, 6, 7, 9, 3, 12, 10, 15, 13, 1, 14, 4, 0, 11, 5, 2};
		int[] s1 = {15, 12, 2, 7, 9, 0, 5, 10, 1, 11, 14, 8, 6, 13, 3, 4};
		int[] cardinality = {2,0,4,3,5,7,1,6};
		//int[] permutation3 ={0,1,9,2,5,4,7,6,3,8,11,10,13,12,15,14};
		/*
		OptimizerSolver o = new OptimizerSolver(s2); // XXX: bogus object creation
		Optimizer obtainedOptimizer = o.solve();
		int[] obtained = obtainedOptimizer.getPermutation();
		
		*/
		Timestamp ts2 = Timestamp.from(java.time.Clock.systemUTC().instant());
		long diff = ts2.getTime()-ts.getTime();
		System.out.print("\n Time: "+diff+" ms");
		Tests.test9();
		
		//Tests.testBasics();
		int[] expected = permutation;// XXX: write here the desired output

	}
	
}
