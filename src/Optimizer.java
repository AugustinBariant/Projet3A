import java.util.List;
import java.util.ArrayList;
import java.util.Random;


public class Optimizer {
	public int[] permutation = new int[16];
	public int seed ;
	public List<FullInstruction> operations;
	public boolean[][] Workspace = new boolean[16][5];
	public boolean[] Negated = new boolean[5];
	public int NumberOfMatch = 0;
	
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
		UpdateNumberOfMatch();
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
	public void ApplyInstruction(FullInstruction f) {
		boolean[] L = new boolean[16];
		for(int i =0; i<16;i++) {
			L[i] = Workspace[i][f.column1];
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
		}

		return;
	}
	public boolean UpdateNumberOfMatch() {
		int a;
		int NbOfMatch = 0;
		for(int i =0; i<16;i++) {
			a=0;
			for(int k=0; k<4;k+=1){
				if(Workspace[i][k]) {
					a+=(1<<k);
				}
			}
			if(a == permutation[i]) {
				NbOfMatch+=1;
			}
		}
		if(NumberOfMatch>NbOfMatch) {
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

	public boolean Check() {
		boolean b;
		b= CheckPermutation();
		return b;
	}
}
