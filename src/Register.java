import java.util.Iterator;

public class Register {
	final int register;
	
	Register(int regId){
		register = regId;
	}
	
	@Override
	public String toString() {
		return "r" + register;
	}
	
	public static Iterator<Register> iterator(){
		return new RegisterIterator(5);
	}
}

class RegisterIterator implements Iterator<Register> {
	private final int maxRegister;
	private int currRegister = 0;
	
	RegisterIterator(int numberOfRegisters){
		this.maxRegister = numberOfRegisters;
	}

	@Override
	public boolean hasNext() {
		return currRegister < maxRegister;
	}

	@Override
	public Register next() {
		return new Register(currRegister++);
	}
}
