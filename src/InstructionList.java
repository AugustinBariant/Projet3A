import java.util.List;
import java.util.ArrayList;

public class InstructionList {
	final FullInstruction node;
	final InstructionList tail;
	InstructionList(){
		node = null;
		tail = null;
	}
	InstructionList(FullInstruction n, InstructionList t){
		node = n;
		tail = t;
	}
	public boolean hasNext() {
		return tail!=null&&(tail.node!=null);
	}
	
	public int size() {
		if(!hasNext()) {
			return node!=null?1:0;
		}else {
			return tail.size()+1;
		}
	}
	
	public static InstructionList fromListInstruction(List<FullInstruction> lList) {
		InstructionList l = new InstructionList();
		for(int i =0;i<lList.size();i++) {
			l = new InstructionList(lList.get(i),l);
		}
		return l;
	}
	public List<FullInstruction> toListInstruction(){
		List<FullInstruction> l;
		if(!hasNext()) {
			l = new ArrayList<FullInstruction>();
			if(node!=null) {
				l.add(node);
			}
		}else {
			l = tail.toListInstruction();
			l.add(node);
		}
		return l;
	}
	
}
