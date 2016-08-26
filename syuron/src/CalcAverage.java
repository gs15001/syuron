public class CalcAverage {

	public static void main(String[] args) {

		int[] data = {550,630,480,720,-1,515,420,-1,755,675};
		int sum = 0;
		int count = 0;

		for (int i = 0; i < data.length; i++) {
			if(data[i] > -1) {
				sum = data[i];
				count++;
			}
		}

		int average = sum / count;
		System.out.println("average : " + average);
	}

}
