import java.util.Scanner;

public class BMI {

	public static void main(String[] args) {
		BMI bmi = new BMI();
		bmi.start();
	}

	void start() {
		System.out.println("Progam start");

		double height;
		double weight;
		// input Data
		Scanner scan = new Scanner(System.in);
		System.out.println("Please input height(cm)");
		height = scan.nextDouble();
		System.out.println("Please input weight(kg)");
		weight = scan.nextDouble();

		// calculate BMI
		double result = weight / ((height / 100) * (height / 100));

		// printout
		if(result > 35.0) {
			System.out.println("Advanced obesity");
		} else if(result > 25) {
			System.out.println("Obesity");
		} else if(result > 18.5) {
			System.out.println("Standard");
		} else {
			System.out.println("Skinny");
		}

		System.out.println("Program stop");
	}
}
