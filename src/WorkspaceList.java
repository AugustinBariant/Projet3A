
public class WorkspaceList {
	final int column;
	final BooleanTab value;
	final WorkspaceList tail;
	BooleanTab[] initial_workspace;
	
	WorkspaceList(int cardinalityLog){
		initial_workspace = new BooleanTab[cardinalityLog+1];
		for(int i =0; i<cardinalityLog+1;i++) {
			initial_workspace[i] = new BooleanTab();
			for(int k=0;k<1<<cardinalityLog;k++) {
				initial_workspace[i]= initial_workspace[i].set(k,1==(k>>i)%2);
			} 
		}
		column = 0;
		value = null;
		tail = null;
	}
	
	
	WorkspaceList(int col, BooleanTab val, WorkspaceList t){
		column = col;
		value = val;
		tail = t;
	}
	
	public WorkspaceList set(int col, BooleanTab val) {
		return new WorkspaceList(col,val,this);
	}
	
	
	public BooleanTab get(int col) {

		if(hasNext()) {
			if(column==col) {
				return value;
			}
			return tail.get(col);
		}else {
			return initial_workspace[col];
		}
	}
	
	public boolean hasNext() {
		return tail!=null;
	}
	
	
}
