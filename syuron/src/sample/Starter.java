package sample;

public class Starter {
	
	public static void main(String[] args) {
		int[] original = { 3, 7, 5, 6, 4, 2};
		Heapsort hs = new Heapsort();
		Quicksort qs = new Quicksort();

		print(original, "original");
		// hs.sort(original);
		qs.sort(original);
		print(original, "original");
	}

	public static void print(int[] a, String t) {
		System.out.print(t + " : ");
		for (int e : a) {
			System.out.print(e + " ");
		}
		System.out.println();
	}
}
