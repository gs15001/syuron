package sample;

public class Heapsort {

	public int[] heap;
	public int num;
	int target;

	public void insert(int a) {
		heap[num++] = a;
		// int i = num + 1, j = i / 2;// 無駄に+1：配列外参照
		// int i = num, j = i / 2 + 1;// 無駄に+1：問題なし
		// int i = num, j = num / 2 + 1;// num/2：問題なし
		int i = num, j = i / 2;// 正解パターン

		// System.out.println("i = " + i + ",j = " + j);// デバッグ用
		// while (heap[i - 1] < heap[j - 1]) {// i>1なし：配列外参照
		// while (i >= 1 && heap[i - 1] < heap[j - 1]) {// i>=1：配列外参照
		// while (i < 1 && heap[i - 1] < heap[j - 1]) {// 不等号ミス1：結果間違い
		// while (i > 1 && heap[i - 1] > heap[j - 1]) {// 不等号ミス2：結果間違い
		// while (i > 1 && heap[i] < heap[j]) {// -1なし：配列外参照
		while (i > 1 && heap[i - 1] < heap[j - 1]) {// 正解パターン
			// 入れ替え
			int t = heap[i - 1];
			// heap[j - 1] = heap[i - 1];// iとjの入れ替え：結果間違い
			heap[i - 1] = heap[j - 1];// 正解パターン
			heap[j - 1] = t;

			// iとjの更新
			i = j;
			j = i / 2;
		}
		// print(heap, "heap");
	}

	public int deletemin() {
		// 先頭取り出し
		int r = heap[0];

		// 最後を先頭に
		// heap[0] = heap[num--]; // ポストデクリメント：配列外参照
		// heap[--num] = heap[0]; // 順序逆：結果間違い
		heap[0] = heap[--num];// 正解パターン

		// int i = 0, j = i * 2;// +1忘れ：無限ループ
		int i = 0, j = i * 2 + 1;// 正解パターン

		// ヒープを再構築
		// while (j < num) { // =なし：問題なし
		// while (j >= num) { // 不等号逆：配列外参照
		// while (i <= num) { // iと比較：配列外参照
		while (j <= num) { // 正解パターン

			// j+1が最大値(num)内かつ子(jとj+1)で小さい方を選択
			// if (j + 1 <= num && heap[j] < heap[j + 1]) {// 不等号逆：結果間違い
			// if (j + 1 >= num && heap[j] > heap[j + 1]) {// j+1とnumの不等号逆：結果間違い
			// if (heap[j] > heap[j + 1]) {// j+1 <= numなし：結果間違い
			if (j + 1 <= num && heap[j] > heap[j + 1]) {// 正解パターン
				j++;
			}

			// 親と子で子のほうが小さければ入れ替え
			// if (heap[i] < heap[j]) {// 不等号逆：結果間違い
			if (heap[i] > heap[j]) {// 正解パターン
				int t = heap[i];
				heap[i] = heap[j];
				heap[j] = t;
			}
			i = j;
			j = i * 2;
		}
		// System.out.println("deletemin : " + r);
		return r;
	}

	public void sort(int[] a) {

		// 必要なヒープ用配列を確保します
		heap = new int[a.length];
		num = 0;

		Starter.print(heap, "heap");
		// ヒープに要素を追加します
		for (target = 0; target < a.length; target++) {
			// System.out.println(target);
			// print(heap,"heap");
			insert(a[target]);
		}
		Starter.print(heap, "heap");

		// ヒープから取り出しながら配列に格納します。
		for (target = 0; num > 0; target++) {
			a[target] = deletemin();
		}
		// print(a,"result");
	}

}
