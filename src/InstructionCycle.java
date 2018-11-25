
public class InstructionCycle {

	public int i;
	public int j;
	public int k;
	public boolean is_first_try;
	InstructionCycle(){
		i=0;
		j=0;
		k=0;
		is_first_try=false;
	}
	public FullInstruction NewInstruction() {
		if(k==j && i!=3) {return UpdateNewInstruction();}
		if((i==3) && (k!=0)) {return UpdateNewInstruction();}//si l'opération est not
		return new FullInstruction(i,j,k);
	}
	public FullInstruction UpdateNewInstruction() {
		if(is_first_try) {
			is_first_try=false;
			return NewInstruction();
		}
		if(k==4 && j==4 && i==4) {
			return new FullInstruction(false);
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
		return NewInstruction();
	}
	
}
