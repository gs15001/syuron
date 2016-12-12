public class CalcDateMethod {

	public static void main(String[] args) {

		// 初期値
		int year = 2016;
		int month = 12;
		int day = 10;

		int isLeapYear = checkLeapYear(year);

		// 日付を日数に変換
		int nissu = convertNissu(month, day, isLeapYear);

		// 経過割合を計算
		int allDay = 364 + isLeapYear;
		double rate = (double) nissu / allDay * 100;
		// 四捨五入
		rate = rate * 10 + 0.5;
		int tmp = (int) rate;
		double result = tmp / 10.0;

		System.out.println("全体の" + result + "%経過しました");

	}

	// うるう年判定
	// うるう年だったら1を、そうでなかったら0を返す
	static int checkLeapYear(int year) {

		// 返り値用変数
		int isLeapYear = 0;
		// 4で割り切れるか
		if(year % 4 == 0) {
			// 100で割り切れるか
			if(year % 100 == 0) {
				// 400で割り切れるか
				if(year % 400 == 0) {
					isLeapYear = 1;
				} else {
					isLeapYear = 0;
				}
			} else {
				isLeapYear = 1;
			}
		}
		return isLeapYear;
	}

	// 日付から日数へ変換
	static int convertNissu(int month, int day, int isLeapYear) {
		// 各月の日数
		int[] dayOfMonth = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };

		// 日付を日数に変換
		int nissu = 0;
		// 3月以降ならうるう年を考慮
		if(month >= 3) {
			nissu += isLeapYear;
		}
		// 現在の月までの日数を計算
		for (int i = 0; i < month - 1; i++) {
			nissu += dayOfMonth[i];
		}
		// 現在の日付を加算
		nissu += day;

		return nissu;
	}
}
