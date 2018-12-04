public interface Instruction extends Iterable<Instruction> {
	String toString();
	boolean run(boolean[] in);
	boolean isNegate();
}
