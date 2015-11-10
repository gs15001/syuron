package debugger;

class Parent {
	public Parent() {
		Child alice = new CleverChild("Alice");
		Child bob = new CommonChild("Bob");
		Child charlie = new DullChild("Charlie");

		try {
			System.out.println("処理中");
			Thread.sleep(3000);
		} catch (InterruptedException ex) {
		}

		watch(alice);
		watch(bob);
		watch(charlie);
	}

	public void watch(Child child) {
		try {
			child.join();
		} catch (InterruptedException e) {
			// ignored.
		}
	}
}