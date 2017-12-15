package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class ProductDAO {
	private Connection conn;
	
	public ProductDAO(Connection conn) {
		this.conn = conn;
	}
	
	public ArrayList<Product> getAllProducts() {
		try {
			ArrayList<Product> products = new ArrayList<Product>();
			
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(
				"SELECT id, name, size, price FROM Product"
			);
			while(rs.next()) {
				products.add(
					new Product(
							rs.getInt("id"), rs.getString("name"), 
							rs.getString("size"), rs.getInt("price")
					)
				);
			}
			return products;
		}
		catch(SQLException e) {
			throw new ExceptionInInitializerError(e);
		} 
	}
	
	public Product getProduct(int id) {
		try {
			PreparedStatement stmt = conn.prepareStatement(
				"SELECT name, size, price FROM Product WHERE id=?"
			);
			stmt.setInt(1, id);
			ResultSet rs = stmt.executeQuery();
			if(rs.next())
				return new Product(id, rs.getString("name"), 
						rs.getString("size"), rs.getInt("price"));
			else
				System.out.println("Records not found");
				return null;
		}
		catch(SQLException e) {
			throw new ExceptionInInitializerError(e);
		} 
	}
	
	public Product insert(Product product) {
		try {
			PreparedStatement stmt = conn.prepareStatement(
				"INSERT INTO Product (name, size, price) VALUES (?, ?, ?);", 
				Statement.RETURN_GENERATED_KEYS
			);
			stmt.setString(1, product.name);
			stmt.setString(2, product.size);
			stmt.setInt(3, product.price);
			
			int insertedId = stmt.executeUpdate();
			product.id = insertedId;
			return product;
		}
		catch(SQLException e) {
			System.out.println("Exception: " + e.toString());
			throw new ExceptionInInitializerError(e);
		} 
	}
	
	public boolean update(Product product) {
		try {
			PreparedStatement stmt = conn.prepareStatement(
				"Update Product SET name=?, size=?, price=? WHERE id=?;"
			);
			stmt.setString(1, product.name);
			stmt.setString(2, product.size);
			stmt.setInt(3, product.price);
			stmt.setInt(4, product.id);
			
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
	
	public boolean delete(Product product) {
		try {
			PreparedStatement stmt = conn.prepareStatement(
				"DELETE FROM Product WHERE id=?;"
			);
			stmt.setInt(1, product.id);
			
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
	
	public ArrayList<Product> search(String productName) {
		try {
			ArrayList<Product> products = new ArrayList<Product>();
			PreparedStatement stmt = conn.prepareStatement(
				"SELECT id, name, size, price FROM Product WHERE name LIKE ?;"
			);
			stmt.setString(1, "%" + productName + "%");
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				products.add(
					new Product(
						rs.getInt("id"), rs.getString("name"), 
						rs.getString("size"),rs.getInt("price")						
					)
				);
			}
			return products;
		}
		catch(SQLException e) {
			System.out.println("Exception: " + e.toString());
			throw new ExceptionInInitializerError(e);
		}
	}
}
