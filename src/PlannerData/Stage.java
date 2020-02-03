package PlannerData;

public class Stage {

	private int capacity;
	private String name;

	public Stage(int capacity, String name) {
		this.capacity = capacity;
		this.name = name;
	}

	public int getCapacity() {
		return capacity;
	}

	public String getName() {
		return name;
	}
}