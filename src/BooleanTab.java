
public class BooleanTab {
	public int mainValue;
	BooleanTab(){
		mainValue=0;
	}
	BooleanTab(int value){
		mainValue=value;
	}
	
	public boolean get(int a) {
		return 1==((mainValue>>a) %2);
	}
	
	public void set(int a, boolean value) {
		if(((mainValue>>a)%2==1)^value) {
			mainValue^=(1<<a);
		}
		return;
	}
}
