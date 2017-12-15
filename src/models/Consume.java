package models;

public class Consume {
	public int id;
	public int quantity;
	public Product product;
	public Inventory inventory;
	
	public Consume(int id, int quantity, Product product, Inventory inventory) {
		this.id = id;
		this.quantity = quantity;
		this.product = product;
		this.inventory = inventory;
	}
	
	public Consume(int quantity, Product product, Inventory inventory) {
		this.quantity = quantity;
		this.product = product;
		this.inventory = inventory;
	}
}
