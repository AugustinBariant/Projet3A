import java.util.Iterator;

class UnaryIterator implements Iterable<Instruction> {
	private final Operator op;

	class Iter implements Iterator<Instruction> {
		Iterator<Register> regs = Register.iterator();

		@Override
		public boolean hasNext() {
			return regs.hasNext();
		}

		@Override
		public Instruction next() {
			return new UnaryInstruction(op, regs.next());
		}
	}

	UnaryIterator(Operator op) {
		assert (op.arity() == 1);
		this.op = op;
	}

	@Override
	public Iterator<Instruction> iterator() {
		return new Iter();
	}
}