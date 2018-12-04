import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import java.util.TreeSet;
import java.util.HashSet;


public class Optimizer {
	public static HashSet<WorkspaceKey> tree;
	public static int[] permutation = new int[16];
	public static int[] permutation_lignes = new int[4]; 
	public int seed ;
	public List<FullInstruction> operations;
	public boolean[][] Workspace = new boolean[16][5];
	public boolean[] Negated = new boolean[5];
	public int NumberOfMatch;
	public static int[] return_columns = new int[4];
	public boolean[] Read = new boolean[5];
	Optimizer(int s) {
		s =seed;
	}
	Optimizer(int[] perm, int s) {
		permutation = perm;
		seed=s;
		operations = new ArrayList<FullInstruction>();
		for(int i =0; i<16;i++) {
			Workspace[i][0] = (i%2 == 1);
			Workspace[i][1] = (i>>1)%2 == 1;
			Workspace[i][2] = (i>>2)%2 == 1;
			Workspace[i][3] = (i>>3)%2 == 1;
			Workspace[i][4] = false;
		}
		for(int i =0;i<5;i++){
			Negated[i]=false;
		}
		NumberOfMatch=0;
		for(int i=0;i<4;i++) {
			Read[i]=false;
		}
		Read[4]=true;
		tree = new HashSet<WorkspaceKey>();
		InitializePermutation();
		UpdateNumberOfMatch();
		
	}
	public void InitializePermutation() {
		for(int k =0; k<4;k++) {
			permutation_lignes[k]=0;
			for(int i=0; i<16;i+=1){
				if((permutation[i]>>k) % 2 == 1) {
					permutation_lignes[k]+=(1<<i);
				}
			}
		}
	}
	public FullInstruction GetRandomInstruction() {
		Random generator = new Random(seed);
	    int id = (int) generator.nextDouble() * 5;
	    int c1 = (int) generator.nextDouble() * 5;
	    int c2 = (int) generator.nextDouble() * 5;
	    FullInstruction f = new FullInstruction(id,c1,c2);
	    return f;
	}
	// ApplyInstruction applies the instruction then checks if the instruction is valid in the current state of the workspace.
	public boolean ApplyInstruction(FullInstruction f) {
		boolean[] L1 = new boolean[16];
		boolean[] L2 = new boolean[16];
		for(int i =0; i<16;i++) {
			L1[i] = Workspace[i][f.column1];
			switch(f.instruct) {
				case And:
					Workspace[i][f.column1]&=Workspace[i][f.column2];
					break;
				case Or:
					Workspace[i][f.column1]|=Workspace[i][f.column2];
					break;
				case Xor:
					Workspace[i][f.column1]^=Workspace[i][f.column2];
					break;
				case Not:
					Workspace[i][f.column1]=!Workspace[i][f.column1];
					break;
				case Mov:
					Workspace[i][f.column1]=Workspace[i][f.column2];
					break;
			}
			L2[i] = Workspace[i][f.column1];
		}
		operations.add(f);
		if(!Check(f,L1,L2)) {
			//for(int i =0; i<16; i++) {
				//Workspace[i][f.column1]=L[i];
			//}
			return false;
		}
		
		return true;
	}
	public boolean UpdateNumberOfMatch() {
		int a;
		int NbOfMatch = 0;
		List<Integer> s = new ArrayList<Integer>();
		for(int k =0; k<5;k++) {
			a=0;
			for(int i=0; i<16;i+=1){
				if(Workspace[i][k]) {
					a+=(1<<i);
				}
			}
			for(int j=0;j<4;j++) {
				if(!s.contains(j)) {
					if(a==permutation_lignes[j]) {
						s.add(j);
						break;
					}
				}
			}
		}
		NbOfMatch = s.size();
		if(NbOfMatch==4) {
			for(int j=0;j<4;j++) {
				return_columns[j]=s.indexOf(j);
			}
		}
		if(NumberOfMatch>NbOfMatch) {
			NumberOfMatch = NbOfMatch;
			return false;
		}else {
			NumberOfMatch = NbOfMatch;
			return true;
		}
	}
	public boolean UpdateNegateAndCheck(FullInstruction f) {
		boolean isOk = true;
		switch(f.instruct) {
			case And:
				Negated[f.column1]=false;
				Negated[f.column2]=false;
				break;
			case Or:
				Negated[f.column1]=false;
				Negated[f.column2]=false;
				break;
			case Xor:
				Negated[f.column1]=false;
				Negated[f.column2]=false;
				break;
			case Not:
				if(Negated[f.column1]) {
					isOk=false;
				}else {
					Negated[f.column1]=true;
				}
				break;
			case Mov:
				Negated[f.column1]=false;
				Negated[f.column2]=false;
				break;	
		}
		return isOk;
	}
	public boolean CheckCopy(FullInstruction f) {
		boolean isOk= true;
		if(f.instruct!=Instruction.Mov) {
			for(int i=0; i<5; i++) {
				if(i==f.column1) {
					continue;
				}
				boolean isCopy = true;
				for(int a=0;a<16;a++) {
					if(Workspace[a][i]!=Workspace[a][f.column1]) {
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
	public boolean CheckPermutation() {
		int[] L = new int[32];
		int a;
		for(int i =0; i<16;i++) {
			a=0;
			for(int k=0; k<5;k+=1){
				if(Workspace[i][k]) {
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
	public boolean UpdateAndCheckRead(FullInstruction f) {
		boolean isOk = true;
		switch(f.instruct) {
			case Not:
				Read[f.column1]=false;
				break;
			default:
				if(!Read[f.column1]) {
					isOk = false;
				}
				Read[f.column1]=false;
				Read[f.column2]=true;
				break;	
		}
		return isOk;
	}
	public boolean CheckEqual(boolean[] L1, boolean[] L2) {
		boolean b = true;
		for(int i=0;i<L1.length;i++) {
			b&=(L1[i]==L2[i]);
		}
		return !b;
	}
	public boolean CheckTrueFalse(boolean[] L1) {
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
	public boolean CheckAndUpdateTree() {
		int[] L = new int[5];
		for(int k =0; k<5;k++) {
			L[k]=0;
			for(int i=0; i<16;i+=1){
				if(Workspace[i][k]) {
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
	
	public boolean Check(FullInstruction f, boolean[] L1, boolean[] L2) {
		boolean b;
		
		b= CheckPermutation();
		b&=UpdateNegateAndCheck(f);
		b&=CheckCopy(f);
		b&=UpdateNumberOfMatch();
		b&=UpdateAndCheckRead(f);
		b&=CheckEqual(L1,L2);
		b&=CheckTrueFalse(L2);
		if(b) {
			b&=CheckAndUpdateTree();
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
			ws[i]=Workspace[i].clone();
		}
		for(FullInstruction el : operations) {
			op.add(el);
		}
		Optimizer o = new Optimizer(seed);
		o.Read = Read.clone();
		o.operations = op;
		o.Workspace = ws;
		o.Negated=Negated.clone();
		o.NumberOfMatch=NumberOfMatch;
		return o;
	}

	public void PrintCurrentState() {
		System.out.print("Current Workspace : \n");
		for(int i =0;i<5;i++) {
			System.out.print("[");
			for(int j=0;j<15;j++) {
				System.out.print((Workspace[j][i]?1:0) +" ,");
			}
			System.out.print((Workspace[15][i]?1:0) +" ]\n");
			
		}
	}
	
	public List<FullInstruction> search(int[] permutation) {
		// TODO
		return null;
	}
	
	public static void main(String[] args){
		List<Optimizer> L = new ArrayList<Optimizer>();
		int[] permutation = {0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15};
		int[] permutation2 = {0,2,1,3,4,6,5,7,8,10,9,11,12,14,13,15};
		int[] permutation3 ={0,1,9,2,5,4,7,6,3,8,11,10,13,12,15,14};
		int[] s2 = {8,6,7,9,3,12,10,15,13,1,14,4,0,11,5,2};

		int seed = 100;
		L.add(new Optimizer(permutation3,seed));
		int m =0;
		boolean b = false;
		boolean c = true;
		//debug
		if(b) {
			FullInstruction f1 = new FullInstruction(4,4,0);
			FullInstruction f2 = new FullInstruction(4,0,1);
			FullInstruction f3 = new FullInstruction(4,1,4);
			System.out.print(L.get(0).Clone().ApplyInstruction(f1));
			L.get(0).ApplyInstruction(f1);
			//L.get(0).ApplyInstruction(f2);
			//L.get(0).ApplyInstruction(f3);
			//System.out.print(L.get(0).NumberOfMatch);
		}
		
		while(c) {
			if(L.isEmpty()) {
				System.out.print("Liste Vide");
				break;
			}
			Optimizer o = L.get(0);
			L.remove(0);
			if(o.NumberOfMatch==4 || o.operations.size()==10) {
				System.out.print("\nSolution trouv�e en " + o.operations.size() + " op�rations\n");
				int i =0;
				for(FullInstruction el:o.operations) {
					i+=1;
					System.out.print("Instruction "+  i+ " : " + el.StringToPrint());
				}
				break;
			}
			
			InstructionCycle cycle = new InstructionCycle();
			FullInstruction i = cycle.UpdateNewInstruction();
			//o.PrintCurrentState();
			while(i.isEnd) {
				Optimizer save = o.Clone();
				FullInstruction el = i;
				
				//System.out.print(Instruction.instr_names[el.instruct.Id] + "(" + el.column1 + "," + el.column2 +")\n");
				if(save.ApplyInstruction(i)) {
					//System.out.print("added instrct :" + i.StringToPrint());
					L.add(save);
				}
				i = cycle.UpdateNewInstruction();
			}
			m+=1;
			System.out.print("\nEtape " + m + " termin�e, L est de longueur "+ L.size() + ", il y a " + o.operations.size() + " op�rations, avec " + o.NumberOfMatch + " matchs\n tree est de longueur "+Optimizer.tree.size()+"\n");
			
		}
	}
}
