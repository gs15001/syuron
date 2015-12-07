public class Quicksort {

	void quickSort(int[] arr, int left, int right) {
		if (left < right) {
			int p = (left + right) / 2;
			int l = left;
			int r = right;
			while (l <= r) {
				while (arr[l] < arr[p]) {
					l++;
				}
				while (arr[r] > arr[p]) {
					r--;
				}

				if (l <= r) {

					int tmp = arr[l];
					arr[l] = arr[r];
					arr[r] = tmp;
					l++;
					r--;
				}
			}
			System.out.println("left : " + left + " right : " + right);
			System.out.println("call sort " + left + "," + r);
			quickSort(arr, left, r);
			System.out.println("left : " + left + " right : " + right);
			System.out.println("call sort " + l + "," + right);
			quickSort(arr, l, right);
		}
	}

	public void sort(int[] a) {
		quickSort(a, 0, a.length - 1);
	}
}
