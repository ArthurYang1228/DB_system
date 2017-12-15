package models;

import java.sql.*;

public class Product {
	public int id;
	public String name;
	public String size;
	public int price;
	
	public Product(int id, String name, String size, int price) {
		this.id = id;
		this.name = name;
		this.size = size;
		this.price = price;
	}
	
	public Product(String name, String size, int price) {
		this.name = name;
		this.size = size;
		this.price = price;
	}
	
	public void consume(Connection conn) {
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(
				"SELECT quantity, inventoryId FROM Consume WHERE productId=" + id);
			while(rs.next()) {
				InventoryDAO iDAO = new InventoryDAO(conn);
				Inventory i = iDAO.getInventory(rs.getInt("inventoryId"));
				i.quantity -= rs.getInt("quantity");
				iDAO.update(i);
			}
		}
		catch(SQLException e) {
			throw new ExceptionInInitializerError(e);
		} 
	}
	public void consume(Connection conn, int i) {
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(
				"SELECT quantity, inventoryId FROM Consume WHERE productId=" + id);
			while(rs.next()) {
				InventoryDAO iDAO = new InventoryDAO(conn);
				Inventory in = iDAO.getInventory(rs.getInt("inventoryId"));
				in.quantity -= i * rs.getInt("quantity");
				iDAO.update(in);
			}
		}
		catch(SQLException e) {
			throw new ExceptionInInitializerError(e);
		} 
	}
	@Override
	public String toString() {
		return String.format("id: %d, name: %s, size: %s, price: %d", 
				id, name, size, price);
	}
}

