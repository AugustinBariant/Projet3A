import java.util.Iterator;

public class Not implements Instruction {
	final Register reg;

	Not(Register r1) {
		this.reg = r1;
	}

	@Override
	public boolean run(boolean[] in) {
		return !in[0];
	}

	@Override
	public boolean isNegate() {
		return true;
	}

	public Iterator<Instruction> iterator() {
		return new NotIterator();
	}

}

class NotIterator implements Iterator<Instruction> {

	Iterator<Register> regs = Register.iterator();

	@Override
	public boolean hasNext() {
		return regs.hasNext();
	}

	@Override
	public Instruction next() {
		return new Not(regs.next());
	}

}