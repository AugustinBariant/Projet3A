
public enum Instruction {
	And(0,2),
	Or(1,2),
	Xor(2,2),
	Not(3,1),
	Mov(4,2);
	static String[] instr_names = {"And","Or","Xor","Not","Mov"};
	
	public int requiredColumns = 0;
	public int id = 0;
	Instruction(int i, int r){
		this.id = i;
		this.requiredColumns = r;		
	}
	
	
}
