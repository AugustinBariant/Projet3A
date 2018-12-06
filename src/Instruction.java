
public enum Instruction {
	And(0),
	Or(1),
	Xor(2),
	Not(3),
	Mov(4);
	static String[] instr_names = {"And","Or","Xor","Not","Mov"};
	
	public int id = 0;
	Instruction(int i){
		this.id = i;	
	}
	
	
}
