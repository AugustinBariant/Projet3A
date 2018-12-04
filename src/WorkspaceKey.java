import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class WorkspaceKey {
	List<Integer> tree;

	WorkspaceKey(int i1, int i2, int i3, int i4, int i5) {
		tree = new ArrayList<Integer>();
		tree.add(i1);
		tree.add(i2);
		tree.add(i3);
		tree.add(i4);
		tree.add(i5);
		tree.sort(Comparator.naturalOrder());
	}

	WorkspaceKey(int[] L) {
		tree = new ArrayList<Integer>();
		for (int i = 0; i < L.length; i++) {
			tree.add(L[i]);
		}
		tree.sort(Comparator.naturalOrder());
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof WorkspaceKey)) {
			return false;
		}
		WorkspaceKey t = (WorkspaceKey) o;
		return t.tree.equals(this.tree);
	}

	@Override
	public int hashCode() {
		return tree.hashCode();
	}

}
