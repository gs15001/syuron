import java.util.Scanner;

public class Meat {

	public static void main(String[] args) {
		new Meat().main();
	}

	public void main() {
		System.out.println("Progam start");

		// input data
		Scanner scan = new Scanner(System.in);
		System.out.println("Please input Meat Name");
		String meatName = scan.nextLine();

		System.out.println("Please input Meat unit price(yen/100g)");
		int meatPrice = scan.nextInt();

		System.out.println("Please input Meat amount(g)");
		int meatAmount = scan.nextInt();

		// printout Value
		// int meatValue = (int) (meatAmount * (meatPrice / 100.0) * 1.08);
		int meatValue = (int) (meatAmount * (meatPrice / 100) * 1.08);
		System.out.println("meatValue = " + meatValue);

		System.out.println("please ");
	}
}
