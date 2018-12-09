
public class InstructionCycle {

	private int i;
	private int j;
	private int k;
	private boolean isFirstTry;
	private int cardinalityLog;
	InstructionCycle(int cardLog){
		i=0;
		j=0;
		k=0;
		isFirstTry=false;
		cardinalityLog = cardLog;
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
		if(k==cardinalityLog && j==cardinalityLog && i==4) {
			return new FullInstruction(true);
		}
		if(k!=cardinalityLog) {
			k+=1;
		}else {
			if(j!=cardinalityLog) {
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
