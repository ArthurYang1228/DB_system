package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class InventoryDAO {
private Connection conn;
	
	public InventoryDAO(Connection conn) {
		this.conn = conn;
	}
	
	public ArrayList<Inventory> getAllInventorys() {
		try {
			ArrayList<Inventory> inventorys = new ArrayList<Inventory>();
			
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(
				"SELECT id, name, quantity, safetyStock FROM Inventory"
			);
			while(rs.next()) {
				inventorys.add(
					new Inventory(
							rs.getInt("id"), rs.getString("name"), 
							rs.getInt("quantity"), rs.getInt("safetyStock")
					)
				);
			}
			return inventorys;
		}
		catch(SQLException e) {
			throw new ExceptionInInitializerError(e);
		} 
	}
	
	public Inventory getInventory(int id) {
		try {
			PreparedStatement stmt = conn.prepareStatement(
				"SELECT name, quantity, safetyStock FROM Inventory WHERE id=?"
			);
			stmt.setInt(1, id);
			ResultSet rs = stmt.executeQuery();
			if(rs.next())
				return new Inventory(id, rs.getString("name"), 
						rs.getInt("quantity"), rs.getInt("safetyStock"));
			else
				System.out.println("Records not found");
				return null;
		}
		catch(SQLException e) {
			throw new ExceptionInInitializerError(e);
		} 
	}
	
	public Inventory insert(Inventory inventory) {
		try {
			PreparedStatement stmt = conn.prepareStatement(
				"INSERT INTO Inventory (name, quantity, safetyStock) VALUES (?, ?, ?);", 
				Statement.RETURN_GENERATED_KEYS
			);
			stmt.setString(1, inventory.name);
			stmt.setInt(2, inventory.quantity);
			stmt.setInt(3, inventory.safetyStock);
			
			int insertedId = stmt.executeUpdate();
			inventory.id = insertedId;
			return inventory;
		}
		catch(SQLException e) {
			System.out.println("Exception: " + e.toString());
			throw new ExceptionInInitializerError(e);
		} 
	}
	
	public boolean update(Inventory inventory) {
		try {
			PreparedStatement stmt = conn.prepareStatement(
				"Update Inventory SET name=?, quantity=?, safetyStock=? WHERE id=?;"
			);
			stmt.setString(1, inventory.name);
			stmt.setInt(2, inventory.quantity);
			stmt.setInt(3, inventory.safetyStock);
			stmt.setInt(4, inventory.id);
			
			int affectedRows = stmt.executeUpdate();
			if(affectedRows == 1)
				return true;
			else
				return false;
		}
		catch(SQLException e) {
			System.out.println("Exception: " + e.toString());
			throw new ExceptionInInitializerError(e);
		}
	}
	
	public boolean delete(Inventory inventory) {
		try {
			PreparedStatement stmt = conn.prepareStatement(
				"DELETE FROM Inventory WHERE id=?;"
			);
			stmt.setInt(1, inventory.id);
			
			int affectedRows = stmt.executeUpdate();
			if(affectedRows == 1)
				return true;
			else
				return false;
		}
		catch(SQLException e) {
			System.out.println("Exception: " + e.toString());
			throw new ExceptionInInitializerError(e);
		}
	}
	
	public boolean delete(int inventoryId) {
		try {
			PreparedStatement stmt = conn.prepareStatement(
				"DELETE FROM Inventory WHERE id=?;"
			);
			stmt.setInt(1, inventoryId);
			
			int affectedRows = stmt.executeUpdate();
			if(affectedRows == 1)
				return true;
			else
				return false;
		}
		catch(SQLException e) {
			System.out.println("Exception: " + e.toString());
			throw new ExceptionInInitializerError(e);
		}
	}
	
	public ArrayList<Inventory> search(String inventoryName) {
		try {
			ArrayList<Inventory> inventorys = new ArrayList<Inventory>();
			PreparedStatement stmt = conn.prepareStatement(
				"SELECT id, name, quantity, safetyStock FROM Inventory WHERE name LIKE ?;"
			);
			stmt.setString(1, "%" + inventoryName + "%");
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				inventorys.add(
					new Inventory(
						rs.getInt("id"), rs.getString("name"), 
						rs.getInt("quantity"), rs.getInt("safetyStock")
					)
				);
			}
			return inventorys;
		}
		catch(SQLException e) {
			System.out.println("Exception: " + e.toString());
			throw new ExceptionInInitializerError(e);
		}
	}
}
