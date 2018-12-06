public class Not implements Operator {
	public int arity() {
		return 1;
	}
	public boolean run(boolean b[]) {
		assert(b.length == 1);
		return ! b[0];
	}
	public boolean isMove() {
		return false;
	}
	public boolean isNegate() {
		return true;
	}
}