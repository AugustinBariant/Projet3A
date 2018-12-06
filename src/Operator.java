public interface Operator {
	int arity();
	boolean run(boolean b[]);
	boolean isMove();
	boolean isNegate();
}
