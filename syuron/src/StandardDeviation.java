public class StandardDeviation {

	public static void main(String[] args) {

		// 5人分のデータ
		int data1 = 67;
		int data2 = 72;
		int data3 = 48;
		int data4 = 59;
		int data5 = 85;

		// 平均を求める
		int sum = data1 + data2 + data3 + data4 + data5;
		int average = sum / 5;

		// 5人それぞれ平均との差を求める
		int data1_sa = data1 - average;
		int data2_sa = data2 - average;
		int data3_sa = data3 - average;
		int data4_sa = data4 - average;
		int data5_sa = data5 - average;

		// 差を2乗する
		data1_sa = data1_sa * data1_sa;
		data2_sa = data2_sa * data2_sa;
		data3_sa = data3_sa * data3_sa;
		data4_sa = data4_sa * data4_sa;
		data5_sa = data5_sa * data5_sa;

		// 分散を求める
		int sa_sum = data1_sa + data2_sa + data3_sa + data4_sa + data5_sa;
		int bunsan = sa_sum / 5;

		// 標準偏差を求める
		int hensa = (int) Math.sqrt(bunsan);
		System.out.println("hensa : " + hensa);
	}

}
