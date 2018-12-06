import java.util.Iterator;

public class InstructionIterator implements Iterable<Instruction> {

	private final Operator op;
	
	InstructionIterator(Operator op){
		this.op = op;
	}
	
	@Override
	public Iterator<Instruction> iterator() {
		if (op.arity() == 1) {
			return (new UnaryIterator(op)).iterator();
		} else if (op.arity() == 2) {
			return (new BinaryIterator(op)).iterator();
		} else {
			throw new IllegalArgumentException("Invalid arity");
		}
	} 
	
}
