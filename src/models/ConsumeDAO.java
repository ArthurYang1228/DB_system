package models;

import java.sql.*;
import java.util.ArrayList;

public class ConsumeDAO {
	private Connection conn;
	
	public ConsumeDAO(Connection conn) {
		this.conn = conn;
	}
	
	public int getQuantity(Product p, Inventory i) {
		try {
			PreparedStatement stmt = conn.prepareStatement(
					"SELECT quantity FROM Consume WHERE productId=? AND inventoryId=?"
				);
				stmt.setInt(1, p.id);
				stmt.setInt(2, i.id);
				ResultSet rs = stmt.executeQuery();
				if(rs.next())
					return rs.getInt("quantity");
				else
					return 0;
		}
		catch(SQLException e) {
			throw new ExceptionInInitializerError(e);
		}
	}
	
	public Consume getConsume(int consumeId) {
		try {
			PreparedStatement stmt = conn.prepareStatement(
				"SELECT id, quantity, productId, inventoryId FROM Consume WHERE id=?"
			);
			stmt.setInt(1, consumeId);
			ResultSet rs = stmt.executeQuery();
			if(rs.next())
				return new Consume(
						rs.getInt("id"),
						rs.getInt("quantity"),
						new ProductDAO(conn).getProduct(rs.getInt("productId")),
						new InventoryDAO(conn).getInventory(rs.getInt("inventoryId"))
					);
			else
				return null;
		}
		catch(SQLException e) {
			throw new ExceptionInInitializerError(e);
		}
	}
	
	public ArrayList<Consume> getAllConsumes() {
		ArrayList<Consume> consumes = new ArrayList<Consume>();
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(
				"SELECT id, quantity, productId, inventoryId FROM Consume"
			);
			while(rs.next()) {
				consumes.add(
					new Consume(
						rs.getInt("id"),
						rs.getInt("quantity"),
						new ProductDAO(conn).getProduct(rs.getInt("productId")),
						new InventoryDAO(conn).getInventory(rs.getInt("inventoryId"))
					)
				);
			}
			return consumes;
		}
		catch(SQLException e) {
			throw new ExceptionInInitializerError(e);
		}
	}
	
	public boolean inConsume(int i  ,int p){
		for(Consume cs : getAllConsumes()){
			if(i == cs.inventory.id && p == cs.product.id){
				
				return true;
			}
			
		}
		
		return false;
		
	}
	
	
	
	public Consume insert(Consume c) {
		try {
			PreparedStatement stmt = conn.prepareStatement(
				"INSERT INTO Consume (quantity, productId, inventoryId) VALUES (?, ?, ?)",
				Statement.RETURN_GENERATED_KEYS
			);
			stmt.setInt(1, c.quantity);
			stmt.setInt(2, c.product.id);
			stmt.setInt(3, c.inventory.id);
			stmt.executeUpdate();
			ResultSet rs = stmt.getGeneratedKeys();
			if (rs.next()){
			    c.id = rs.getInt(1);
			}
			return c;
		}
		catch(SQLException e) {
			throw new ExceptionInInitializerError(e);
		}
	}
	
	public boolean update(Consume c) {
		try {
			PreparedStatement stmt = conn.prepareStatement(
				"UPDATE Consume SET quantity=?, productId=?, inventoryId=? WHERE id=?"
			);
			stmt.setInt(1, c.quantity);
			stmt.setInt(2, c.product.id);
			stmt.setInt(3, c.inventory.id);
			stmt.setInt(4, c.id);
			int affectedRows = stmt.executeUpdate();
			if (affectedRows == 1)
				return true;
			else
				return false;
		}
		catch(SQLException e) {
			throw new ExceptionInInitializerError(e);
		}
	}
	
	public boolean delete(Consume c) {
		try {
			PreparedStatement stmt = conn.prepareStatement(
				"DELETE FROM Consume WHERE id=?"
			);
			stmt.setInt(1, c.id);
			int affectedRows = stmt.executeUpdate();
			if (affectedRows == 1)
				return true;
			else
				return false;
		}
		catch(SQLException e) {
			throw new ExceptionInInitializerError(e);
		}
	}
}
