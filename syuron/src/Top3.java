public class Top3 {

	// ｛No,点数,性別(0=男,1=女)｝のデータ 男性の点数にtop3を表示

	public static void main(String[] args) {
		Top3 top3 = new Top3();
		top3.main();
	}

	public void main() {
		// 入力
		int[][] data = input();

		// デバッグプリント
		// for (int i = 0; i < data.length; i++) {
		// System.out.println(data[i][0] + "," + data[i][1] + "," + data[i][2]);
		// }

		int manNum = 0;
		int[][] manData = new int[9][3];
		// フィルター
		for (int i = 0; i < data.length; i++) {
			// System.out.println("この出力の数を数える");
			// 性別判定
			if(data[i][2] == 0) {
				// 男だったら別の配列にコピー
				manData[manNum][0] = data[i][0];
				manData[manNum][1] = data[i][1];
				manData[manNum][2] = data[i][2];
				manNum++;
			}
		}

		// デバッグプリント
		// for (int i = 0; i < manNum; i++) {
		// System.out.println(manData[i][0] + "," + manData[i][1] + "," + manData[i][2]);
		// }
		// System.out.println("manNum = " + manNum);

		// バブルソート
		for (int i = 0; i < manNum; i++) {
			// System.out.println("外側ループ" + i + "回目");
			for (int j = 0; j < manNum - i - 1; j++) {
				// System.out.println("この出力の数を数える");
				// System.out.println(j + "," + (j + 1));
				System.out.println(manData[i][1] + "と" + manData[j][1] + "を比較");
				if(manData[i][1] < manData[j][1]) {
					// System.out.println(manData[i][1] + "と" + manData[j][1] + "を交換");
					// 交換
					int tmp;
					tmp = manData[i][0];
					manData[i][0] = manData[j][0];
					manData[j][0] = tmp;

					tmp = manData[i][1];
					manData[i][1] = manData[j][1];
					manData[j][1] = tmp;

					tmp = manData[i][2];
					manData[i][2] = manData[j][2];
					manData[j][2] = tmp;
				}
				// for (int k = 0; k < manNum; k++) {
				// System.out.println(manData[k][0] + "," + manData[k][1] + "," + manData[k][2]);
				// }
			}
		}

		// デバッグプリント
		// for (int i = 0; i < manNum; i++) {
		// System.out.println(manData[i][0] + "," + manData[i][1] + "," + manData[i][2]);
		// }

		// 出力
		System.out.println("First : " + manData[0][1] + " No." + manData[0][0]);
		System.out.println("Second : " + manData[1][1] + " No." + manData[1][0]);
		System.out.println("Third : " + manData[2][1] + " No." + manData[2][0]);

	}

	// 今回は直接入力
	public int[][] input() {
		int[][] data = { { 0, 74, 0 }, { 1, 59, 1 }, { 2, 75, 1 }, { 3, 80, 0 }, { 4, 65, 1 }, { 5, 69, 0 },
				{ 6, 78, 0 }, { 7, 91, 1 }, { 8, 83, 1 }, { 9, 98, 0 } };
		return data;
	}
}
