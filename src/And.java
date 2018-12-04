import java.util.Iterator;

public class And implements Instruction {
	final Register src, dst;

	And(Register r1, Register r2) {
		this.src = r1;
		this.dst = r2;
	}

	@Override
	public boolean run(boolean[] in) {
		return in[0] && in[1];
	}

	@Override
	public boolean isNegate() {
		return false;
	}

	public Iterator<Instruction> iterator() {
		return new AndIterator();
	}

}

class AndIterator implements Iterator<Instruction> {

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
				return new And(currentSrc, currentDst);
			}
		} else {
			srcs = Register.iterator();
			currentDst = dsts.next();
			return next();
		}
	}

}