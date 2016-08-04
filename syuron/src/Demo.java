public class Demo {

	public static void main(String[] args) {
		new Demo().sort();
	}

	public void sort() {
		int[] a = { 3, 6, 7, 2, 9, 1 };
		for (int i = 0; i < a.length - 1; i++) {
			for (int j = a.length - 1; j > i; j--) {
				if(a[j] < a[j - 1]) {
					int t = a[j];
					a[j - 1] = a[j];
					a[j - 1] = t;
				}
				printArray(a);
			}
			System.out.println();
		}
	}

	public void printArray(int[] a) {
		for (int i = 0; i < a.length; i++) {
			System.out.print(a[i] + ",");
		}
		System.out.println();
	}

}
