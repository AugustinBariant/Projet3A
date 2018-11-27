import java.lang.Comparable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;


public class WorkspaceComparator implements Comparable {
	List<Integer> tree;
	
	WorkspaceComparator(int i1,int i2,int i3,int i4,int i5){
		tree = new ArrayList<Integer>();
		tree.add(i1);
		tree.add(i2);
		tree.add(i3);
		tree.add(i4);
		tree.add(i5);
		tree.sort(Comparator.naturalOrder());
	}
	WorkspaceComparator(int[] L){
		tree = new ArrayList<Integer>();
		for(int i =0;i<L.length;i++) {
			tree.add(L[i]);
		}
		tree.sort(Comparator.naturalOrder());
	}
	
    public int compare(WorkspaceComparator o1, WorkspaceComparator o2) {
		int k =0;
		int[] L1 = new int[5];
		for(int i:o1.tree) {
			L1[k] = i;
			k+=1;
		}
		k=0;
		for(int i:o2.tree) {
			if(L1[k]>i) {
				return 1;
			}else if(L1[k]<i) {
				return -1;
			}
			k+=1;
		}
		/*
		String s = "Trees are equals : {";
		for(int i:o1.tree) {
			s+=i+",";
		}
		s+="} and {";
		for(int i:o2.tree) {
			s+=i+",";
		}
		s+="}\n";
		System.out.print(s);
		*/
		return 0;
    }
	@Override
	public boolean equals(Object o) { 
		if (o == this) { 
			return true; 
		} 
		if (!(o instanceof WorkspaceComparator)) { 
            return false; 
        } 
		WorkspaceComparator t = (WorkspaceComparator) o;
		return compare(t, this)==0;
	}
	@Override
	public int compareTo(Object o) {
		if (o == this) { 
			return 0; 
		} 
		if (!(o instanceof WorkspaceComparator)) { 
            return 0; 
        }
		return compare(this,(WorkspaceComparator) o);
	} 
	
}
