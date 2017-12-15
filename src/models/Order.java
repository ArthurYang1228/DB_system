package models;

import java.util.ArrayList;

public class Order {
	public int id;
	public String date;
	public String time;
	public Employee employee;
	public ArrayList<OrderDetailed> orderDetailedList = new ArrayList<OrderDetailed>();
	
	public Order(int id, String date, String time, Employee employee) {
		this.id = id;
		this.date = date;
		this.time = time;
		this.employee = employee;
	}
	
	public Order(String date, String time, Employee employee) {
		this.date = date;
		this.time = time;
		this.employee = employee;
	}
	
	public void addOrderDetailed(OrderDetailed o) {
		orderDetailedList.add(o);
	}
	
	public void removeOrderDetailed(OrderDetailed o2) {
		for(OrderDetailed o: orderDetailedList)
			if(o.equals(o2)) {
				orderDetailedList.remove(o);
				break;
			}
	}
	
	public String toString() {
		String s = String.format("id: %d, time: %s, date: %s, employee: %s",
				id, time, date, employee.toString());
		for(OrderDetailed od: orderDetailedList)
			s += String.format("\n    %s", od.toString());
		return s;
	}
}