public class UnaryInstruction implements Instruction {
	final Operator op;
	final Register reg;
	
	UnaryInstruction(Operator op, Register r1) {
		assert(op.arity() == 2);
		this.op = op;
		this.reg = r1;
	}

	public Instruction not(Register r) {
		return new UnaryInstruction(new Not(), r);
	}
	
	@Override
	public void run(boolean[] in) {
		boolean[] args = { in[reg.register] };
		assert(args.length == op.arity());
		in[reg.register] = op.run(args);
	}

	@Override
	public boolean isNegate() {
		return op.isNegate();
	}
	
	@Override
	public boolean isMove() {
		return op.isMove();
	}
}