package sample;

public class Heapsort {

	public int[] heap; // データの配列
	public int num; // 現在の要素数
	int target; // 現在の着目ライン

	public static void main(String[] args) {
		int[] original = { 3, 7, 2, 6, 4, 5 };
		Heapsort hs = new Heapsort();

		hs.print(original, "original");
		hs.sort(original);
		hs.print(original, "original");
	}

	/*
	 * 挿入
	 */
	public void insert(int a) {
		heap[num++] = a;
		//int i = num + 1, j = i / 2;//無駄に+1
		//int i = num, j = i / 2 + 1;//無駄に+1
		//int i = num, j = num / 2 + 1;//num/2
		int i = num, j = i / 2;//正解パターン
		//while (heap[i - 1] < heap[j - 1]) {i>1なし
		//while (i >= 1 && heap[i - 1] < heap[j - 1]) {i>=1
		//while (i < 1 && heap[i - 1] < heap[j - 1]) {不等号ミス1
		//while (i > 1 && heap[i - 1] > heap[j - 1]) {不等号ミス2
		//while (i > 1 && heap[i] < heap[j]) {-1なし
		while (i > 1 && heap[i - 1] < heap[j - 1]) {//正解パターン
			//入れ替え
			int t = heap[i - 1];
			//heap[j - 1] = heap[i - 1];//iとjの入れ替え
			heap[i - 1] = heap[j - 1];//正解パターン
			heap[j - 1] = t;

			//iとjの更新
			i = j;
			j = i / 2;
		}
		//print(heap, "heap");
	}

	/*
	 * 先頭の要素を取り除き、返す
	 */
	public int deletemin() {
		//先頭取り出し
		int r = heap[0];
		
		//最後を先頭にしてヒープを再構築
		heap[0] = heap[--num];
		int i = 1, j = i * 2;
		while (j <= num) {
			if (j + 1 <= num && heap[j - 1] > heap[j])
				j++;
			if (heap[i - 1] > heap[j - 1]) {
				int t = heap[i - 1];
				heap[i - 1] = heap[j - 1];
				heap[j - 1] = t;
			}
			i = j;
			j = i * 2;
		}
		//System.out.println("deletemin : " + r);
		return r;
	}

	/*
	 * ソート
	 */
	public void sort(int[] a) {

		// 必要なヒープ用配列を確保します
		heap = new int[a.length];
		num = 0;

		//print(heap,"heap");
		// ヒープに要素を追加します
		for (target = 0; target < a.length; target++) {
			insert(a[target]);
		}
		//print(heap,"heap");

		// ヒープから取り出しながら配列に格納します。
		for (target = 0; num > 0; target++) {
			a[target] = deletemin();
		}
		//print(a,"result");
	}

	public void print(int[] a, String t) {
		System.out.print(t + " : ");
		for (int e : a) {
			System.out.print(e + " ");
		}
		System.out.println();
	}

}
