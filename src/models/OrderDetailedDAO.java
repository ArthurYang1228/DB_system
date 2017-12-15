package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class OrderDetailedDAO {
	private Connection conn;
	
	public OrderDetailedDAO(Connection conn) {
		this.conn = conn;
	}
	
	public ArrayList<OrderDetailed> getAllOrderDetaileds() {
		try {
			ArrayList<OrderDetailed> orderDetaileds = new ArrayList<OrderDetailed>();
			
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(
				"SELECT id, sugar, ice, productId FROM OrderDetailed"
			);
			while(rs.next()) {
				orderDetaileds.add(
					new OrderDetailed(
						rs.getInt("id"), rs.getString("sugar"), rs.getString("ice"), 
						new ProductDAO(conn).getProduct(rs.getInt("productId"))
					)
				);
			}
			return orderDetaileds;
		}
		catch(SQLException e) {
			throw new ExceptionInInitializerError(e);
		}
	}
	
	public ArrayList<OrderDetailed> getAllByOrder(Order order) {
		try {
			ArrayList<OrderDetailed> orderDetaileds = new ArrayList<OrderDetailed>();
			
			PreparedStatement stmt = conn.prepareStatement(
				"SELECT id, sugar, ice, productId FROM OrderDetailed WHERE orderId=?"
			);
			stmt.setInt(1, order.id);
			ResultSet rs = stmt.executeQuery();
			
			while(rs.next()) {
				orderDetaileds.add(
					new OrderDetailed(
						rs.getInt("id"), rs.getString("sugar"), rs.getString("ice"), 
						new ProductDAO(conn).getProduct(rs.getInt("productId"))
					)
				);
			}
			return orderDetaileds;
		}
		catch(SQLException e) {
			throw new ExceptionInInitializerError(e);
		}
	}
	
	public OrderDetailed get(int id) {
		try {
			PreparedStatement stmt = conn.prepareStatement(
				"SELECT id, sugar, ice, productId FROM OrderDetailed WHERE id=?"
			);
			stmt.setInt(1, id);
			ResultSet rs = stmt.executeQuery();
			if(rs.next())
				return new OrderDetailed(
					rs.getInt("id"), rs.getString("sugar"), rs.getString("ice"), 
					new ProductDAO(conn).getProduct(rs.getInt("productId"))
				);
			else
				System.out.println("Records not found");
				return null;
		}
		catch(SQLException e) {
			throw new ExceptionInInitializerError(e);
		}
	}
	
	public OrderDetailed insert(OrderDetailed orderDetailed) {
		try {
			PreparedStatement stmt = conn.prepareStatement(
				"INSERT INTO OrderDetailed (sugar, ice, productId) VALUES (?, ?, ?);", 
				Statement.RETURN_GENERATED_KEYS
			);
			stmt.setString(1, orderDetailed.sugar);
			stmt.setString(2, orderDetailed.ice);
			stmt.setInt(3, orderDetailed.product.id);
			stmt.executeUpdate();
			
			ResultSet rs = stmt.getGeneratedKeys();
			if (rs.next()){
				orderDetailed.id = rs.getInt(1);
			}
			return orderDetailed;
		}
		catch(SQLException e) {
			System.out.println("Exception: " + e.toString());
			throw new ExceptionInInitializerError(e);
		} 
	}
	
	public OrderDetailed insertByOrder(OrderDetailed orderDetailed, Order order) {
		try {
			PreparedStatement stmt = conn.prepareStatement(
				"INSERT INTO OrderDetailed (sugar, ice, productId, orderId) VALUES (?, ?, ?, ?);", 
				Statement.RETURN_GENERATED_KEYS
			);
			stmt.setString(1, orderDetailed.sugar);
			stmt.setString(2, orderDetailed.ice);
			stmt.setInt(3, orderDetailed.product.id);
			stmt.setInt(4, order.id);
			
			stmt.executeUpdate();
			ResultSet rs = stmt.getGeneratedKeys();
			if (rs.next()){
				orderDetailed.id = rs.getInt(1);
			}
			return orderDetailed;
		}
		catch(SQLException e) {
			System.out.println("Exception: " + e.toString());
			throw new ExceptionInInitializerError(e);
		} 
	}
	
	public boolean update(OrderDetailed orderDetailed) {
		try {
			PreparedStatement stmt = conn.prepareStatement(
				"Update OrderDetailed SET sugar=?, ice=?, productId=? WHERE id=?;"
			);
			stmt.setString(1, orderDetailed.sugar);
			stmt.setString(2, orderDetailed.ice);
			stmt.setInt(3, orderDetailed.product.id);
			stmt.setInt(4, orderDetailed.id);
			
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
	
	public boolean delete(OrderDetailed orderDetailed) {
		try {
			PreparedStatement stmt = conn.prepareStatement(
				"DELETE FROM OrderDetailed WHERE id=?;"
			);
			stmt.setInt(1, orderDetailed.id);
			
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
	
	public boolean deleteByOrder(Order order) {
		try {
			PreparedStatement stmt = conn.prepareStatement(
				"DELETE FROM OrderDetailed WHERE orderId=?;"
			);
			stmt.setInt(1, order.id);
			
			int affectedRows = stmt.executeUpdate();
			if(affectedRows >= 1)
				return true;
			else
				return false;
		}
		catch(SQLException e) {
			System.out.println("Exception: " + e.toString());
			throw new ExceptionInInitializerError(e);
		}
	}
	
}
