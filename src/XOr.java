public class XOr implements Operator {
	public int arity() {
		return 2;
	}
	public boolean run(boolean b[]) {
		assert(b.length == arity());
		return b[0] ^ b[1];
	}
	public boolean isMove() {
		return false;
	}
	public boolean isNegate() {
		return false;
	}
}
