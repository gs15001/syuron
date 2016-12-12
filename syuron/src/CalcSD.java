public class CalcSD {

	// ｛学籍番号,点数}のデータ　奇数組・偶数組それぞれの標準偏差を求める

	public static void main(String[] args) {
		// 入力
		int[][] data = { { 1, 74 }, { 2, 59 }, { 3, 75 }, { 4, 80 }, { 6, 54 }, { 7, 69 }, { 8, 78 }, { 9, 91 },
				{ 10, 83 }, { 12, 61 } };

		// 奇数偶数判定
		int[][] kisuData = new int[10][2];
		int kisuCount = 0;
		int[][] gusuData = new int[10][2];
		int gusuCount = 0;
		for (int i = 0; i < data.length; i++) {
			if(data[i][0] % 2 == 0) {
				// 偶数だったら偶数用の配列にコピー
				gusuData[gusuCount][0] = data[i][0];
				gusuData[gusuCount][1] = data[i][1];
				gusuCount++;
			} else {
				kisuData[kisuCount][0] = data[i][0];
				kisuData[kisuCount][1] = data[i][1];
				kisuCount++;
			}
		}

		// 奇数組のデータから標準偏差を算出
		// 合計と平均を算出
		int sum = 0;
		int average = 0;
		for (int i = 0; i < kisuCount; i++) {
			sum += kisuData[i][1];
		}
		average = sum / kisuCount;
		// 分散と標準偏差を算出
		int bunsan = 0;
		for (int i = 0; i < kisuCount; i++) {
			int sa = kisuData[i][1] - average;
			bunsan += sa * sa;
		}
		bunsan = bunsan / kisuCount;
		double kisuHensa = Math.sqrt(bunsan);
		// 四捨五入
		kisuHensa = kisuHensa * 10 + 0.5;
		int tmp = (int) kisuHensa;
		double kisuResult = tmp / 10.0;

		// 偶数組のデータから標準偏差を算出
		sum = 0;
		average = 0;
		for (int i = 0; i < gusuCount; i++) {
			sum += gusuData[i][1];
		}
		average = sum / gusuCount;
		// 分散と標準偏差を算出
		bunsan = 0;
		for (int i = 0; i < gusuCount; i++) {
			int sa = gusuData[i][1] - average;
			bunsan += sa * sa;
		}
		bunsan = bunsan / gusuCount;
		double gusuHensa = Math.sqrt(bunsan);
		// 四捨五入
		gusuHensa = gusuHensa * 10 + 0.5;
		tmp = (int) gusuHensa;
		double gusuResult = tmp / 10.0;

		System.out.println("奇数組の標準偏差:" + kisuResult);
		System.out.println("偶数組の標準偏差:" + gusuResult);
	}
}
