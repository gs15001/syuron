public class Sample {

	public static void main(String[] args) {
		sort();
		sort2();
	}

	public static void sort() {
		int[] a = { 3, 6, 7, 2, 9, 1 };
		for (int i = 0; i < a.length - 1; i++) {
			for (int j = a.length - 1; j > i; j--) {
				if(a[j] < a[j - 1]) {
					int t = a[j];
					a[j] = a[j - 1];
					a[j - 1] = t;
				}
			}
		}
		for (int i : a) {
			System.out.print(i + ",");
		}
		System.out.println();
	}

	public static void sort2() {
		int[] a = { 3, 6, 7, 2, 9, 1 };
		for (int i = 0; i < a.length - 1; i++) {
			for (int j = a.length - 1; j > i; j--) {
				if(a[j] < a[j - 1]) {
					int t = a[j];
					a[j-1] = a[j];
					a[j - 1] = t;
				}
			}
		}

		for (int i : a) {
			System.out.print(i + ",");
		}
		System.out.println();
	}

}
