
public enum InstructionEnum {
	And(0,2),
	Or(1,2),
	Xor(2,2),
	Not(3,1),
	Mov(4,2);
	static String[] instr_names = {"And","Or","Xor","Not","Mov"};
	
	public int requiredColumns = 0;
	public int Id = 0;
	InstructionEnum(int i, int r){
		this.Id = i;
		this.requiredColumns = r;		
	}
	
	
}
