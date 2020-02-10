package PlannerData;

import java.io.Serializable;

public class Stage implements Serializable {

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