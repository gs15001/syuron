package debugger;

abstract class Child extends Thread {
	private int age;

	public int getAge() {
		return age;
	}

	abstract boolean isBaby();

	public Child(String name) {
		super(name);
		age = 0;
		start();
	}

	public void growUp() {
		age++;
		if (isBaby()) {
			throw new RuntimeException("Oops, he/she is too young to speak!");
		}
	}

	public void run() {
		growUp();
		System.err.println("Hello! I'm " + getName() + ".");
	}
}

class CleverChild extends Child {
	public CleverChild(String name) {
		super(name);
	}

	public boolean isBaby() {
		return getAge() < 1;
	}
}

class CommonChild extends Child {
	public CommonChild(String name) {
		super(name);
	}

	public boolean isBaby() {
		return getAge() < 2;
	}
}

class DullChild extends Child {
	public DullChild(String name) {
		super(name);
	}

	public boolean isBaby() {
		return getAge() < 3;
	}
}