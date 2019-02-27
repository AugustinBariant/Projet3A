
public class BooleanTab {
	public final int mainValue;
	BooleanTab(){
		mainValue=0;
	}
	BooleanTab(int value){
		mainValue=value;
	}
	
	public boolean get(int a) {
		return 1==((mainValue>>a) %2);
	}
	
	public BooleanTab set(int a, boolean value) {
		if(((mainValue>>a)%2==1)^value) {
			return new BooleanTab(mainValue^(1<<a));
		}
		return new BooleanTab(mainValue);
	}
	
	public BooleanTab and(BooleanTab t) {
		return new BooleanTab(mainValue&t.mainValue);
	}
	public BooleanTab or(BooleanTab t) {
		return new BooleanTab(mainValue|t.mainValue);
	}
	public BooleanTab xor(BooleanTab t) {
		return new BooleanTab(mainValue^t.mainValue);
	}
	public BooleanTab not(int cardinalityLog) {
		return new BooleanTab((~mainValue+(1<<(1<<cardinalityLog)))%(1<<(1<<cardinalityLog)));
	}
	public BooleanTab mov() {
		return new BooleanTab(mainValue);
	}
}
