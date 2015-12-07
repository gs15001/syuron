public class Test {

	int x;
	int y;
	double result;

	public static void main(String[] args) {
		Test t = new Test();
		t.calc();
		System.out.println(t.result);
	}

	public Test() {
		x = 10;
		y = 20;
	}

	public void calc() {
		int t = x * x;
		int t2 = y * y;
		result = Math.sqrt(t + t2);
	}
}
