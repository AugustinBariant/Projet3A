import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class InstructionIterator implements Iterator<Instruction> {

	List<Iterator<Instruction>> instructionIterator = new ArrayList<Iterator<Instruction>>(
			Arrays.asList(new AndIterator(), new OrIterator(), new XOrIterator(), new NotIterator()));
	ListIterator<Iterator<Instruction>> instructions = instructionIterator.listIterator();
	Iterator<Instruction> currentInstruction = instructions.next();
	
	@Override
	public boolean hasNext() {
		return instructions.hasNext() || currentInstruction.hasNext();
	}

	@Override
	public Instruction next() {
		if (currentInstruction.hasNext()) {
			return currentInstruction.next();
		} else {
			currentInstruction = instructions.next();
			return next();
		}
	}

}