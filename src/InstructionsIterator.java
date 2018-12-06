import java.util.Iterator;

public class InstructionsIterator implements Iterator<Instruction> {

	Iterator<Operator> operators = (new Operators ()).iterator();
	Iterator<Instruction> currentInstructions = (new InstructionIterator(operators.next())).iterator();
	
	@Override
	public boolean hasNext() {
		return operators.hasNext() || currentInstructions.hasNext();
	}

	@Override
	public Instruction next() {
		if (currentInstructions.hasNext()) {
			return currentInstructions.next();
		} else {
			currentInstructions = (new InstructionIterator(operators.next())).iterator();
			return next();
		}
	}

}