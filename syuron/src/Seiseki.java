public class Seiseki {

	// ｛No,点数,性別(0=男,1=女)｝のデータ 男性の点数にtop3を表示

	public static void main(String[] args) {
		// 入力
		int[][] data = input();

		// 性別判定
		int manNum = 0;
		int[][] manData = new int[9][3];
		for (int i = 0; i < data.length; i++) {
			if(data[i][2] == 0) {
				// 男だったら別の配列にコピー
				manData[manNum][0] = data[i][0];
				manData[manNum][1] = data[i][1];
				manData[manNum][2] = data[i][2];
				manNum++;
			}
		}

		// 対象以外のデータをフィルタリング
		int targetNum = 0;
		int[][] targetData = new int[9][3];
		for (int i = 0; i < manNum; i++) {
			if(manData[i][1] >= 0) {
				// 対象データだったら別の配列にコピー
				targetData[targetNum][0] = manData[i][0];
				targetData[targetNum][1] = manData[i][1];
				targetData[targetNum][2] = manData[i][2];
				targetNum++;
			}
		}

		// 対象データから標準偏差を算出
		// 合計と平均を算出
		int sum = 0;
		int average = 0;
		for (int i = 0; i < targetNum; i++) {
			sum += targetData[i][1];
		}
		average = sum / targetNum;

		// 分散と標準偏差を算出
		int bunsan = 0;
		for (int i = 0; i < targetNum; i++) {
			int sa = targetData[i][1] - average;
			bunsan += sa;
		}
		bunsan = bunsan / targetNum;

		int hensa = (int) Math.sqrt(bunsan);
		System.out.println("標準偏差:" + hensa);
	}

	// 今回は直接入力
	public static int[][] input() {
		// 74 80 69 78
		//@formatter:off
		int[][] data = {
				{ 0, 74, 0 },
				{ 1, 59, 1 },
				{ 2, 75, 1 },
				{ 3, 80, 0 },
				{ 4, 65, 1 },
				{ 5, 69, 0 },
				{ 6, 78, 0 },
				{ 7, 91, 1 },
				{ 8, 83, 1 },
				{ 9, -1, 0 }
				};
		//@formatter:on
		return data;
	}
}
