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
	public InstructionList operations;
	public final WorkspaceList workspace;
	private final BooleanTab negated;
	private final BooleanTab read;
	
	
	Optimizer() {
		operations = null;
		negated = null;
		read =null;
		workspace = null;
		numberOfMatch = 0;
	}
	Optimizer(int[] perm) {
		cardinalityLog = 32-Integer.numberOfLeadingZeros(perm.length-1);;
		permutation = perm;
		operations = new InstructionList();
		workspace = new WorkspaceList(cardinalityLog);
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
	Optimizer(BooleanTab r, BooleanTab neg, WorkspaceList ws, InstructionList op, int nOfMatch){
		operations = op;
		read = r;
		negated  = neg;
		workspace = ws;
		numberOfMatch = nOfMatch;
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
	public Optimizer applyInstruction(FullInstruction f) {
		int L1 = workspace.get(f.column1).mainValue;
		WorkspaceList newWorkSpace;
		InstructionList newOperations;
		System.out.print("\n" + f.stringToPrint() + " "+ workspace.get(f.column1).mainValue);
		switch(f.instruct) {
			case And:
				newWorkSpace = workspace.set(f.column1, workspace.get(f.column1).and(workspace.get(f.column2)));
				break;
			case Or:
				newWorkSpace = workspace.set(f.column1, workspace.get(f.column1).or(workspace.get(f.column2)));
				break;
			case Xor:
				newWorkSpace = workspace.set(f.column1, workspace.get(f.column1).xor(workspace.get(f.column2)));
				break;
			case Not:
				newWorkSpace = workspace.set(f.column1, workspace.get(f.column1).not(cardinalityLog));
				break;
			case Mov:
				newWorkSpace = workspace.set(f.column1, workspace.get(f.column2).mov());
				break;
			default:
				newWorkSpace = new WorkspaceList(cardinalityLog);
		}
		System.out.print("\n" + f.stringToPrint() + " "+ newWorkSpace.get(f.column1).mainValue);
		int L2 = newWorkSpace.get(f.column1).mainValue;
		return check(f,L1,L2,newWorkSpace);
	}
	private int updateNumberOfMatch() throws Exception {
		int a;
		int NbOfMatch = 0;
		List<Integer> s = new ArrayList<Integer>();
		int[] returnCols = new int[cardinalityLog];
		for(int k =0; k<cardinalityLog+1;k++) {
			a=workspace.get(k).mainValue;
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
		return NbOfMatch;
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
	private BooleanTab updateNegateAndCheck(FullInstruction f) throws Exception {
		BooleanTab newNegated;
		switch(f.instruct) {
			case And:
				newNegated = negated.set(f.column1,false).set(f.column2,false);
				break;
			case Or:
				newNegated = negated.set(f.column1,false).set(f.column2,false);
				break;
			case Xor:
				newNegated = negated.set(f.column1,false).set(f.column2,false);
				break;
			case Not:
				if(negated.get(f.column1)) {
					throw new Exception();
				}else {
					newNegated = negated.set(f.column1,true);
				}
				break;
			case Mov:
				newNegated = negated.set(f.column1,negated.get(f.column2));
				break;	
			default:
				newNegated = new BooleanTab();
				break;
		}
		return newNegated;
	}
	private void checkCopy(FullInstruction f) throws Exception {
		if(f.instruct!=Instruction.Mov) {
			for(int i=0; i<cardinalityLog+1; i++) {
				if(i==f.column1) {
					continue;
				}
				if(workspace.get(i).mainValue==workspace.get(f.column1).mainValue) {
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
				if(workspace.get(k).get(i)) {
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
	
	private BooleanTab updateAndCheckRead(FullInstruction f) throws Exception{
		BooleanTab newRead;
		switch(f.instruct) {
			case Not:
				newRead = new BooleanTab(read.mainValue);
				break;
			case Mov:
				if(!read.get(f.column1)) {
					throw new Exception();
				}
				newRead  = read.set(f.column1, false).set(f.column2, false);
				break;
			default:
				newRead = read.set(f.column1, false).set(f.column2, true);
				break;	
		}
		return newRead;
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
			L[k]=workspace.get(k).mainValue;
		}
		WorkspaceKey t = new WorkspaceKey(L);
		if(tree.contains(t)) {
			throw new Exception();
		}
		tree.add(t);
		return ;
		
	}
	
	private Optimizer check(FullInstruction f, int L1, int L2, WorkspaceList ws) {
		BooleanTab r;
		BooleanTab n;
		int nOfMatch;
		InstructionList newOperations = new InstructionList(f,operations);
		try {
			checkPermutation();
			n = updateNegateAndCheck(f);
			checkCopy(f);
			nOfMatch = updateNumberOfMatch();
			r = updateAndCheckRead(f);
			checkEqual(L1,L2);
			checkTrueFalse(L2);
			checkAndUpdateTree();
			return new Optimizer(r,n,ws,newOperations,nOfMatch) ;
		}catch(Exception e) {
			return null;
		}
		
	}
	/*
	public Optimizer copy() {
		List<FullInstruction> op = new ArrayList<FullInstruction>();
		BooleanTab[] ws = new BooleanTab[cardinalityLog+1];
		for(int i=0;i<(cardinalityLog+1);i++) {
			ws[i]=new BooleanTab(workspace.get(i).mainValue);
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
	*/

	public void printCurrentState() {
		System.out.print("Current Workspace : \n");
		for(int i =0;i<cardinalityLog+1;i++) {
			System.out.print("[");
			for(int j=0;j<(1<<cardinalityLog)-1;j++) {
				System.out.print((workspace.get(i).get(j)?1:0) +" ,");
			}
			System.out.print((workspace.get(i).get((1<<cardinalityLog)-1)?1:0) +" ]");
			System.out.print(" " + workspace.get(i).mainValue +"\n");
		}
	}
	
	public int[] getPermutation() {
		int[] tab = new int[1<<cardinalityLog];
		for(int i =0;i<(1<<cardinalityLog);i++) {
			for(int k=0;k<cardinalityLog;k++) {
				if(workspace.get(returnColumns[k]).get(i)) {
					tab[i]+=(1<<k);
				}
			}
			
		}
		return tab;
	} 
	
	public Optimizer applyInstructions(InstructionList l) {
		Optimizer op = this;
		while(l!=null && l.node!=null) {
			op = op.applyInstruction(l.node);
			l = l.tail;
		}
		return op;
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
