package models;

import java.util.ArrayList;

public class Purchase {
	public int id;
	public String date;
	public Employee employee;
	public ArrayList<PurchaseDetailed> purchaseDetailedList = new ArrayList<PurchaseDetailed>();
	
	public Purchase(int id, String date, Employee employee) {
		this.id = id;
		this.date = date;
		this.employee = employee;
	}
	
	public Purchase(String date, Employee employee) {
		this.date = date;
		this.employee = employee;
	}
	
	public void addPurchaseDetailed(PurchaseDetailed p) {
		purchaseDetailedList.add(p);
	}
	
	public void removePurchaseDetailed(PurchaseDetailed p2) {
		for(PurchaseDetailed p: purchaseDetailedList)
			if(p.equals(p2)) {
				purchaseDetailedList.remove(p);
				break;
			}
	}
	
	public String toString() {
		String s = String.format("id: %d, date: %s, employee: %s",
				id, date, employee.toString());
		for(PurchaseDetailed pd: purchaseDetailedList)
			s += String.format("\n    %s", pd.toString());
		return s;
	}
}