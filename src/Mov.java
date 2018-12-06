public class Mov implements Operator {
	public int arity() {
		return 2;
	}
	public boolean run(boolean b[]) {
		assert(b.length == 2);
		return b[1];
	}
	public boolean isMove() {
		return true;
	}
	public boolean isNegate() {
		return false;
	}
}