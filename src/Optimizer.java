import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;
import java.sql.Timestamp;

public class Optimizer {
	public static HashSet<WorkspaceKey> tree;
	private static int cardinalityLog;
	private static int[] permutation = new int[1<<cardinalityLog];
	private static int[] permutationLines; 
	public List<FullInstruction> operations;
	private boolean[][] workspace;
	private boolean[] negated;
	public int numberOfMatch;
	private static int[] returnColumns = new int[cardinalityLog+1];
	private boolean[] read;
	
	
	Optimizer() {
		
	}
	Optimizer(int[] perm, int cardLog) {
		cardinalityLog=cardLog;
		permutation = perm;
		operations = new ArrayList<FullInstruction>();
		workspace = new boolean[1<<cardinalityLog][cardinalityLog+1];
		for(int i =0; i<(1<<cardinalityLog);i++) {
			for(int k=0;k<cardinalityLog;k++) {
				workspace[i][k] = ((i>>k)%2==1);
			}
			workspace[i][cardinalityLog] = false;
		}
		negated = new boolean[cardinalityLog+1];
		for(int i =0;i<cardinalityLog+1;i++){
			negated[i]=false;
		}
		numberOfMatch=0;
		read = new boolean[cardinalityLog+1];
		for(int i=0;i<cardinalityLog+1;i++) {
			read[i]=false;
		}
		read[cardinalityLog]=true;
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
		boolean[] L1 = new boolean[1<<cardinalityLog];
		boolean[] L2 = new boolean[1<<cardinalityLog];
		for(int i =0; i<(1<<cardinalityLog);i++) {
			L1[i] = workspace[i][f.column1];
			switch(f.instruct) {
				case And:
					workspace[i][f.column1]&=workspace[i][f.column2];
					break;
				case Or:
					workspace[i][f.column1]|=workspace[i][f.column2];
					break;
				case Xor:
					workspace[i][f.column1]^=workspace[i][f.column2];
					break;
				case Not:
					workspace[i][f.column1]=!workspace[i][f.column1];
					break;
				case Mov:
					workspace[i][f.column1]=workspace[i][f.column2];
					break;
			}
			L2[i] = workspace[i][f.column1];
		}
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
			a=0;
			for(int i=0; i<(1<<cardinalityLog);i+=1){
				if(workspace[i][k]) {
					a+=(1<<i);
				}
			}
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
		}else {
			numberOfMatch = NbOfMatch;
			return;
		}
	}
	private void updateNegateAndCheck(FullInstruction f) throws Exception {
		switch(f.instruct) {
			case And:
				negated[f.column1]=false;
				negated[f.column2]=false;
				break;
			case Or:
				negated[f.column1]=false;
				negated[f.column2]=false;
				break;
			case Xor:
				negated[f.column1]=false;
				negated[f.column2]=false;
				break;
			case Not:
				if(negated[f.column1]) {
					throw new Exception();
				}else {
					negated[f.column1]=true;
				}
				break;
			case Mov:
				negated[f.column1]=negated[f.column2];
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
				boolean isCopy = true;
				for(int a=0;a<(1<<cardinalityLog);a++) {
					if(workspace[a][i]!=workspace[a][f.column1]) {
						isCopy=false;
						break;
					}
				}
				if(isCopy) {
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
				if(workspace[i][k]) {
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
				if(!read[f.column1]) {
					throw new Exception();
				}
				read[f.column1]=false;
				read[f.column2]=true;
				break;
			default:
				read[f.column1]=false;
				read[f.column2]=true;
				break;	
		}
		return;
	}
	private void checkEqual(boolean[] L1, boolean[] L2) throws Exception{
		for(int i=0;i<L1.length;i++) {
			if(L1[i]!=L2[i]) {
				return;
			}
		}
		throw new Exception();
	}
	// Check if the new line is either entirely true or entirely false
	private void checkTrueFalse(boolean[] L1) throws Exception{
		boolean b = true;
		boolean c = true;
		int i=0;
		while((b||c)&&i<(1<<cardinalityLog)) {
			b&=L1[i];
			c&=!L1[i];
			i+=1;
		}
		if(b||c) {
			throw new Exception();
		}
		return;
	}
	private void checkAndUpdateTree() throws Exception{
		int[] L = new int[cardinalityLog+1];
		for(int k =0; k<cardinalityLog+1;k++) {
			L[k]=0;
			for(int i=0; i<(1<<cardinalityLog);i+=1){
				if(workspace[i][k]) {
					L[k]+=(1<<i);
				}
			}
			//System.out.print(L[k]+"\n");
		}
		WorkspaceKey t = new WorkspaceKey(L);
		if(tree.contains(t)) {
			throw new Exception();
		}
		tree.add(t);
		return ;
		
	}
	
	private boolean check(FullInstruction f, boolean[] L1, boolean[] L2) {
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
		boolean[][] ws = new boolean[1<<cardinalityLog][cardinalityLog+1];
		for(int i=0;i<(1<<cardinalityLog);i++) {
			ws[i]=workspace[i].clone();
		}
		for(FullInstruction el : operations) {
			op.add(el);
		}
		Optimizer o = new Optimizer();
		o.read = read.clone();
		o.operations = op;
		o.workspace = ws;
		o.negated=negated.clone();
		o.numberOfMatch=numberOfMatch;
		return o;
	}

	public void printCurrentState() {
		System.out.print("Current Workspace : \n");
		for(int i =0;i<cardinalityLog+1;i++) {
			System.out.print("[");
			for(int j=0;j<(1<<cardinalityLog)-1;j++) {
				System.out.print((workspace[j][i]?1:0) +" ,");
			}
			System.out.print((workspace[cardinalityLog-1][i]?1:0) +" ]\n");
			
		}
	}
	
	public int[] getPermutation() {
		int[] tab = new int[1<<cardinalityLog];
		for(int i =0;i<(1<<cardinalityLog);i++) {
			for(int k=0;k<cardinalityLog;k++) {
				if(workspace[i][returnColumns[k]]) {
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
		int[] cardinality = {3,1,0,2,7,5,6,4};
		//int[] permutation3 ={0,1,9,2,5,4,7,6,3,8,11,10,13,12,15,14};
		OptimizerSolver o = new OptimizerSolver(cardinality,3); // XXX: bogus object creation
		Optimizer obtainedOptimizer = o.solve();
		int[] obtained = obtainedOptimizer.getPermutation();
		for(int i=0;i<(1<<cardinalityLog);i++) {
			System.out.print(obtained[i]+" ");
		}
		System.out.print("\n");
		Timestamp ts2 = Timestamp.from(java.time.Clock.systemUTC().instant());
		long diff = ts2.getTime()-ts.getTime();
		System.out.print("\n Time: "+diff+" ms");
		
		int[] expected = permutation;// XXX: write here the desired output

	}
	
}
