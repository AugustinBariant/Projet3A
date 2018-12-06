
public class InstructionCycle {

	private int i;
	private int j;
	private int k;
	private boolean isFirstTry;
	InstructionCycle(){
		i=0;
		j=0;
		k=0;
		isFirstTry=false;
	}
	private FullInstruction newInstruction() {
		if(k==j && i!=3) {return updateNewInstruction();}
		if((i==3) && (k!=0)) {return updateNewInstruction();}//si l'opération est not
		return new FullInstruction(i,j,k);
	}
	public FullInstruction updateNewInstruction() {
		if(isFirstTry) {
			isFirstTry=false;
			return newInstruction();
		}
		if(k==4 && j==4 && i==4) {
			return new FullInstruction(true);
		}
		if(k!=4) {
			k+=1;
		}else {
			if(j!=4) {
				j+=1;
				k=0;
			}else {
				i+=1;
				k=0;
				j=0;
			}
		}
		return newInstruction();
	}
	
}
