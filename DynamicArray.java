public class DynamicArray {
	int[] arr;
	int cursor;

	public DynamicArray() {
		this.arr = new int[1];
		this.cursor = 0;
	}

	public DynamicArray(int value) {
		this.arr = new int[] {value};
		this.cursor = 1;
	}

	public void append(int value) {
		if (cursor == arr.length) {
			this.resize();
		}
		arr[cursor++] = value;
	}

	public int pop() {
		int val = arr[--cursor];
		arr[cursor] = 0;
		return val;
	}

	public int get(int index) {
		if (index >= arr.length || index < 0 || arr[index] == -1)
			return -1;
		return arr[index];
	}

	public int top() {
		if (cursor == 0)
			return -1;
		return arr[cursor-1];
	}

	public boolean contains(int value) {
		for (int i = 0; i < cursor; i++) {
			if (arr[i] == value)
				return true;
		} return false;
	}

	public boolean isEmpty() {
		return cursor==0;
	}

	private void resize() {
		int[] copy = arr;
		arr = new int[arr.length * 2];
		for (int i = 0; i < cursor; i++) {
			arr[i] = copy[i];
		}
	}

	public void wipe() {
		arr = new int[1];
		cursor = 0;
	}
}
