public interface Instruction {
	String toString();
	void run(boolean[] in);
	boolean isMove();
	boolean isNegate();
}
