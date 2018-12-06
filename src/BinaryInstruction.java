public class BinaryInstruction implements Instruction {
	final Operator op;
	final Register src, dst;

	BinaryInstruction(Operator op, Register r1, Register r2) {
		assert(op.arity() == 2);
		this.op = op;
		this.src = r1;
		this.dst = r2;
	}

	public static Instruction and(Register r1, Register r2) {
		return new BinaryInstruction(new And(), r1, r2);
	}
	
	public static Instruction or(Register r1, Register r2) {
		return new BinaryInstruction(new Or(), r1, r2);
	}
	
	public static Instruction xor(Register r1, Register r2) {
		return new BinaryInstruction(new Or(), r1, r2);
	}
	
	public static Instruction mov(Register r1, Register r2) {
		return new BinaryInstruction(new Mov(), r1, r2);
	}
	
	@Override
	public void run(boolean[] in) {
		boolean[] args = { in[src.register], in[dst.register] };
		assert(args.length == op.arity());
		in[dst.register] = op.run(args);
	}

	@Override
	public boolean isNegate() {
		return false;
	}

	@Override
	public boolean isMove() {
		return false;
	}
}