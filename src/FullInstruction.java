
public class FullInstruction {
	public Instruction instruct;
	public int column1;
	public int column2;
	public boolean isEnd;

	FullInstruction(boolean b){
		isEnd=b;
	}
	FullInstruction(int i, int c1, int c2){
		switch(i){
			case 0:
				instruct = Instruction.And;
				break;
			case 1:
				instruct = Instruction.Or;
				break;
			case 2:
				instruct = Instruction.Xor;
				break;
			case 3:
				instruct = Instruction.Not;
				break;
			case 4:
				instruct = Instruction.Mov;
				break;
			default: 
				System.out.print("Invalid Instruction Id");
			
		}		
		this.column1 = c1;
		this.column2 = c2;
		this.isEnd=false;
	}
	FullInstruction(int i, int c1){
		switch(i){
			case 0:
				instruct = Instruction.And;
				break;
			case 1:
				instruct = Instruction.Or;
				break;
			case 2:
				instruct = Instruction.Xor;
				break;
			case 3:
				instruct = Instruction.Not;
				break;
			case 4:
				instruct = Instruction.Mov;
				break;
			default: 
				System.out.print("Invalid Instruction Id");
			
		}
			
				
		this.column1 = c1;
		this.isEnd=false;
		return;
	}
	public String stringToPrint() {
		String s;
		switch(instruct.id){
		case 3:
			s=Instruction.instr_names[instruct.id] + "(" + column1 + ")\n";
			break;
		default:
			s=Instruction.instr_names[instruct.id] + "(" + column1 + "," + column2 +")\n";
			break;
		}
		return s;
	}

	
}
