package sample;

public class Test2 {

	int x;
	int y;
	double result;

	public static void main(String[] args) {
		Test2 t = new Test2();
		t.calc();
		System.out.println(t.result);
	}

	public Test2() {
		x = 10;
		y = 20;
	}

	public void calc() {
		int t = x * x;
		int t2 = y * y;
		result = Math.sqrt(t + t2);
	}
}
