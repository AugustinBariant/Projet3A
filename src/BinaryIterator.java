import java.util.Iterator;

class BinaryIterator implements Iterable<Instruction> {
	private final Operator op;

	class Iter implements Iterator<Instruction> {
		Iterator<Register> srcs = Register.iterator();
		Iterator<Register> dsts = Register.iterator();
		Register currentDst = dsts.next();

		@Override
		public boolean hasNext() {
			return srcs.hasNext() || dsts.hasNext();
		}

		@Override
		public Instruction next() {
			// Cartesian product of srcs and dsts
			// excluding the diagonal (in which case it would be a NOP)
			if (srcs.hasNext()) {
				Register currentSrc = srcs.next();
				if (currentSrc.equals(currentDst)) {
					return next();
				} else {
					return new BinaryInstruction(op, currentSrc, currentDst);
				}
			} else {
				srcs = Register.iterator();
				currentDst = dsts.next();
				return next();
			}
		}
	}

	BinaryIterator(Operator op) {
		assert(op.arity() == 2);
		this.op = op;
	}

	@Override
	public Iterator<Instruction> iterator() {
		return new Iter();
	}	
}