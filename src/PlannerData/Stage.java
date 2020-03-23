package PlannerData;

import java.io.Serializable;

/**
 * A class that saves the attributes of a stage
 */
public class Stage implements Serializable {

	private String name;
	private int capacity;

	/**
	 * Constructor of stage
	 * @param capacity the maximum amount of people the stage can fit
	 * @param name the name of the stage
	 */
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

	public void setName(String name) {
		this.name = name;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
}