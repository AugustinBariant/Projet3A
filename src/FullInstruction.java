
public class FullInstruction {
	public InstructionEnum instruct;
	public int column1;
	public int column2;
	public boolean isEnd;

	FullInstruction(boolean b){
		isEnd=b;
	}
	FullInstruction(int i, int c1, int c2){
		switch(i){
			case 0:
				instruct = InstructionEnum.And;
				break;
			case 1:
				instruct = InstructionEnum.Or;
				break;
			case 2:
				instruct = InstructionEnum.Xor;
				break;
			case 3:
				instruct = InstructionEnum.Not;
				break;
			case 4:
				instruct = InstructionEnum.Mov;
				break;
			default: 
				System.out.print("Invalid Instruction Id");
			
		}		
		this.column1 = c1;
		this.column2 = c2;
		this.isEnd=true;
	}
	FullInstruction(int i, int c1){
		switch(i){
			case 0:
				instruct = InstructionEnum.And;
				break;
			case 1:
				instruct = InstructionEnum.Or;
				break;
			case 2:
				instruct = InstructionEnum.Xor;
				break;
			case 3:
				instruct = InstructionEnum.Not;
				break;
			case 4:
				instruct = InstructionEnum.Mov;
				break;
			default: 
				System.out.print("Invalid Instruction Id");
			
		}
			
				
		this.column1 = c1;
		this.isEnd=true;
		return;
	}
	public String StringToPrint() {
		String s;
		switch(instruct.Id){
		case 3:
			s=InstructionEnum.instr_names[instruct.Id] + "(" + column1 + ")\n";
			break;
		default:
			s=InstructionEnum.instr_names[instruct.Id] + "(" + column1 + "," + column2 +")\n";
			break;
		}
		return s;
	}

	
}
