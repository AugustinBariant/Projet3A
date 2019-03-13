import java.sql.Timestamp;
import java.util.List;
import java.util.ArrayList;

public class InstructionSequence {
	private InstructionCycle[] cycleList;
	private Optimizer[] opList;
	private int index;
	private int prof;
	public int idk=0;
	
	public InstructionSequence(int n, int[] permutation) {
		cycleList = new InstructionCycle[n];
		opList = new Optimizer[n+1];
		Optimizer op = new Optimizer(permutation);
		for(int i=0;i<n;i++) {
			cycleList[i]= new InstructionCycle(4);
			opList[i] = null;
		}
		opList[0]=op;
		index = 0;
		prof = n;
	}
	
	public Optimizer next() {
		if(cycleList[index].hasNext()) {
			Optimizer o = opList[index].applyInstruction(cycleList[index].updateNewInstruction());	
			if(o!=null) {
				if(index+1 < prof) {
					index+=1;
					opList[index] = o;
				}
				System.out.print(idk+"\n");
				return o;
			}
			idk+=1;
			return next();
			
		}else {
			cycleList[index]=new InstructionCycle(4);
			index -= 1;
			if(index==-1) {
				return null;
			}
			idk+=1;
			
			return next();
		}
	}
	
	
	
	public static void main(String[] args) {
		Timestamp ts = Timestamp.from(java.time.Clock.systemUTC().instant());
		int[] cardinality = {8, 6, 7, 9, 3, 12, 10, 15, 13, 1, 14, 4, 0, 11, 5, 2};
		InstructionSequence i = new InstructionSequence(4,cardinality);
		Optimizer p = new Optimizer(cardinality);
		int intt = 0;
		while(p!=null) {
			int k =0;
			for(FullInstruction el : p.operations.toListInstruction()) {
				k+=1;
				System.out.print("Instruction "+  k+ " : " + el.stringToPrint());
			}
			System.out.print(intt+"\n");
			i.idk = 0;
			p = i.next();
			intt+=1;
		}
		Timestamp ts2 = Timestamp.from(java.time.Clock.systemUTC().instant());
		long diff = ts2.getTime()-ts.getTime();
		System.out.print("\n Time: "+diff+" ms");
	}
}
