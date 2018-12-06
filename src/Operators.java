import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class Operators implements Iterable<Operator> {

	ArrayList<Operator> ops = new ArrayList<>(Arrays.asList(new And(), new Or(), new XOr(), new Not(), new Mov()));
	
	@Override
	public Iterator<Operator> iterator() {
		return ops.iterator();
	}
	
	
}
