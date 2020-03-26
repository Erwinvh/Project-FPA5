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

	/**
	 * The getter for the stage capacity
	 * @return Stage capacity
	 */
	public int getCapacity() {
		return capacity;
	}

	/**
	 * The setter for the stage capacity
	 * @param capacity New stage capacity
	 */
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
	/**
	 * The getetr for the stage name
	 * @return Stage name
	 */
	public String getName() {
		return name;
	}

	/**
	 * The setter for the stage name
	 * @param name New stage name
	 */
	public void setName(String name) {
		this.name = name;
	}


}