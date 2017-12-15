package models;
public class Inventory {
	public int id;
	public String name;
	public int quantity;
	public int safetyStock;
	
	public Inventory(int id, String name, int quantity, int safetyStock) {
		this.id = id;
		this.name = name;
		this.quantity = quantity;
		this.safetyStock = safetyStock;
	}
	
	public Inventory(String name, int quantity, int safetyStock) {
		this.name = name;
		this.quantity = quantity;
		this.safetyStock = safetyStock;
	}
	
	@Override
	public String toString() {
		return String.format("id: %d, name: %s, quantity: %d, safetyStock: %d", 
				id, name, quantity, safetyStock);
	}
}
