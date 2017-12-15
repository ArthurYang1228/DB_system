package models;

public class Employee {
	public int id;
	public String name;
	public int workingHours;
	public int hourlySalary;
	
	public Employee(int id, String name, int workingHours, int hourlySalary) {
		this.id = id;
		this.name = name;
		this.workingHours = workingHours;
		this.hourlySalary = hourlySalary;
	}
	
	public Employee(String name, int workingHours, int hourlySalary) {
		this.name = name;
		this.workingHours = workingHours;
		this.hourlySalary = hourlySalary;
	}
	
	@Override
	public String toString() {
		return String.format("id:%d, name:%s, workingHours:%d, hourlySalary:%d", 
				id, name, workingHours, hourlySalary);
	}
}
