import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import java.util.TreeSet;
import java.util.HashSet;


public class Optimizer {
	public static HashSet<WorkspaceKey> tree;
	public static int[] permutation = new int[16];
	public static int[] permutationLines = new int[4]; 
	public List<FullInstruction> operations;
	public boolean[][] workspace = new boolean[16][5];
	public boolean[] negated = new boolean[5];
	public int numberOfMatch;
	public static int[] returnColumns = new int[4];
	public boolean[] read = new boolean[5];
	Optimizer() {
	}
	Optimizer(int[] perm) {
		permutation = perm;
		operations = new ArrayList<FullInstruction>();
		for(int i =0; i<16;i++) {
			workspace[i][0] = (i%2 == 1);
			workspace[i][1] = (i>>1)%2 == 1;
			workspace[i][2] = (i>>2)%2 == 1;
			workspace[i][3] = (i>>3)%2 == 1;
			workspace[i][4] = false;
		}
		for(int i =0;i<5;i++){
			negated[i]=false;
		}
		numberOfMatch=0;
		for(int i=0;i<4;i++) {
			read[i]=false;
		}
		read[4]=true;
		tree = new HashSet<WorkspaceKey>();
		initializePermutation();
		updateNumberOfMatch();
		
	}
	public void initializePermutation() {
		for(int k =0; k<4;k++) {
			permutationLines[k]=0;
			for(int i=0; i<16;i+=1){
				if((permutation[i]>>k) % 2 == 1) {
					permutationLines[k]+=(1<<i);
				}
			}
		}
	}
	public FullInstruction getRandomInstruction() {
		Random generator = new Random();
	    int id = (int) generator.nextDouble() * 5;
	    int c1 = (int) generator.nextDouble() * 5;
	    int c2 = (int) generator.nextDouble() * 5;
	    FullInstruction f = new FullInstruction(id,c1,c2);
	    return f;
	}
	// ApplyInstruction applies the instruction then checks if the instruction is valid in the current state of the workspace.
	public boolean applyInstruction(FullInstruction f) {
		boolean[] L1 = new boolean[16];
		boolean[] L2 = new boolean[16];
		for(int i =0; i<16;i++) {
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
	public boolean updateNumberOfMatch() {
		int a;
		int NbOfMatch = 0;
		List<Integer> s = new ArrayList<Integer>();
		for(int k =0; k<5;k++) {
			a=0;
			for(int i=0; i<16;i+=1){
				if(workspace[i][k]) {
					a+=(1<<i);
				}
			}
			for(int j=0;j<4;j++) {
				if(!s.contains(j)) {
					if(a==permutationLines[j]) {
						s.add(j);
						break;
					}
				}
			}
		}
		NbOfMatch = s.size();
		if(NbOfMatch==4) {
			for(int j=0;j<4;j++) {
				returnColumns[j]=s.indexOf(j);
			}
		}
		if(numberOfMatch>NbOfMatch) {
			numberOfMatch = NbOfMatch;
			return false;
		}else {
			numberOfMatch = NbOfMatch;
			return true;
		}
	}
	public boolean updateNegateAndCheck(FullInstruction f) {
		boolean isOk = true;
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
					isOk=false;
				}else {
					negated[f.column1]=true;
				}
				break;
			case Mov:
				negated[f.column1]=false;
				negated[f.column2]=false;
				break;	
		}
		return isOk;
	}
	public boolean checkCopy(FullInstruction f) {
		boolean isOk= true;
		if(f.instruct!=Instruction.Mov) {
			for(int i=0; i<5; i++) {
				if(i==f.column1) {
					continue;
				}
				boolean isCopy = true;
				for(int a=0;a<16;a++) {
					if(workspace[a][i]!=workspace[a][f.column1]) {
						isCopy=false;
						break;
					}
				}
				if(isCopy) {
					isOk = false;
				}
			}
		}
		return isOk;
	}
	public boolean checkPermutation() {
		int[] L = new int[32];
		int a;
		for(int i =0; i<16;i++) {
			a=0;
			for(int k=0; k<5;k+=1){
				if(workspace[i][k]) {
					a+=(1<<k);
				}
			}
			L[a]+=1;
		}
		boolean isOk = true;
		for(int i =0; i<32;i++) {
			if(L[i]>1) {
				isOk=false;
			}
		}
		return isOk;
		
	}
	public boolean updateAndCheckRead(FullInstruction f) {
		boolean isOk = true;
		switch(f.instruct) {
			case Not:
				read[f.column1]=false;
				break;
			default:
				if(!read[f.column1]) {
					isOk = false;
				}
				read[f.column1]=false;
				read[f.column2]=true;
				break;	
		}
		return isOk;
	}
	public boolean checkEqual(boolean[] L1, boolean[] L2) {
		boolean b = true;
		for(int i=0;i<L1.length;i++) {
			b&=(L1[i]==L2[i]);
		}
		return !b;
	}
	public boolean checkTrueFalse(boolean[] L1) {
		boolean b = true;
		boolean c = true;
		int i=0;
		while((b||c)&&i<16) {
			b&=(L1[i]==true);
			c&=(L1[i]==false);
			i+=1;
		}
		return (!b)&&(!c);
	}
	public boolean checkAndUpdateTree() {
		int[] L = new int[5];
		for(int k =0; k<5;k++) {
			L[k]=0;
			for(int i=0; i<16;i+=1){
				if(workspace[i][k]) {
					L[k]+=(1<<i);
				}
			}
			//System.out.print(L[k]+"\n");
		}
		WorkspaceKey t = new WorkspaceKey(L);
		if(tree.contains(t)) {
			return false;
		}
		tree.add(t);
		return true;
		
	}
	
	public boolean check(FullInstruction f, boolean[] L1, boolean[] L2) {
		boolean b;
		
		b= checkPermutation();
		b&=updateNegateAndCheck(f);
		b&=checkCopy(f);
		b&=updateNumberOfMatch();
		b&=updateAndCheckRead(f);
		b&=checkEqual(L1,L2);
		b&=checkTrueFalse(L2);
		if(b) {
			b&=checkAndUpdateTree();
			if(!b) {
				//System.out.print("Not added instrct :" + f.StringToPrint());
			}
		}
		return b;
	}
	public Optimizer Clone() {
		List<FullInstruction> op = new ArrayList<FullInstruction>();
		boolean[][] ws = new boolean[16][5];
		for(int i=0;i<16;i++) {
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
		for(int i =0;i<5;i++) {
			System.out.print("[");
			for(int j=0;j<15;j++) {
				System.out.print((workspace[j][i]?1:0) +" ,");
			}
			System.out.print((workspace[15][i]?1:0) +" ]\n");
			
		}
	}
	
	public static List<FullInstruction> search(int[] permutation) {
		List<Optimizer> L = new ArrayList<Optimizer>();
		L.add(new Optimizer(permutation));
		int m=0;
		while(true) {
			if(L.isEmpty()) {
				System.out.print("Liste Vide");
				break;
			}
			Optimizer o = L.get(0);
			L.remove(0);
			if(o.numberOfMatch==4 || o.operations.size()==10) {
				System.out.print("\nSolution trouvée en " + o.operations.size() + " opérations\n");
				int i =0;
				for(FullInstruction el:o.operations) {
					i+=1;
					System.out.print("Instruction "+  i+ " : " + el.stringToPrint());
				}
				break;
			}
			
			InstructionCycle cycle = new InstructionCycle();
			FullInstruction i = cycle.updateNewInstruction();
			//o.PrintCurrentState();
			while(!i.isEnd) {
				Optimizer save = o.Clone();
				FullInstruction el = i;
				//System.out.print(Instruction.instr_names[el.instruct.Id] + "(" + el.column1 + "," + el.column2 +")\n");
				if(save.applyInstruction(i)) {
					//System.out.print("added instrct :" + i.StringToPrint());
					L.add(save);
				}
				i = cycle.updateNewInstruction();
			}
			m+=1;
			System.out.print("\nEtape " + m + " terminée, L est de longueur "+ L.size() + ", il y a " + o.operations.size() + " opérations, avec " + o.numberOfMatch + " matchs\n tree est de longueur "+Optimizer.tree.size()+"\n");
			
		}
		return null;
	}
	
	public static void main(String[] args){
		int[] permutation = {0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15};
		int[] permutation2 = {0,2,1,3,4,6,5,7,8,10,9,11,12,14,13,15};
		int[] permutation3 ={0,1,9,2,5,4,7,6,3,8,11,10,13,12,15,14};
		int[] s2 = {8,6,7,9,3,12,10,15,13,1,14,4,0,11,5,2};
		Optimizer.search(s2);

	}
}
