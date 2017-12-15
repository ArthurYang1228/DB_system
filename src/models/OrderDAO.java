package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;



public class OrderDAO {
	private Connection conn;
	
	public OrderDAO(Connection conn) {
		this.conn = conn;
	}
	
	public ArrayList<Order> getAllOrders() {
		try {
			ArrayList<Order> orders = new ArrayList<Order>();
			
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(
				"SELECT `Order`.id, `date`, `time`, employeeId, OrderDetailed.id, sugar, ice, orderId, productId "
				+ "FROM `Order`, OrderDetailed "
				+ "WHERE `Order`.id = orderId"
			);
			if(rs.next()) {
				int lastOrderId = rs.getInt("Order.Id");
				Order o = new Order(
						rs.getInt("Order.id"), rs.getString("date"), rs.getString("time"), 
						new EmployeeDAO(conn).getEmployee(rs.getInt("employeeId")));
				while(true) {
					if(rs.getInt("Order.Id") != lastOrderId){
						lastOrderId = rs.getInt("orderId");
						orders.add(o);
						o = new Order(
								rs.getInt("Order.id"), rs.getString("date"), rs.getString("time"), 
								new EmployeeDAO(conn).getEmployee(rs.getInt("employeeId")));
					}
					o.addOrderDetailed(new OrderDetailed(rs.getInt("OrderDetailed.id"), rs.getString("sugar"), rs.getString("ice"), new ProductDAO(conn).getProduct(rs.getInt("productId"))));
					if(!rs.next()){
						orders.add(o);
						break;
					}
				}
			}
			return orders;
		}
		catch(SQLException e) {
			throw new ExceptionInInitializerError(e);
		}
	}
	
	public Order get(int id) {
		try {
			Order order;
			
			PreparedStatement stmt = conn.prepareStatement(
				"SELECT id, `date`, `time`, employeeId FROM `Order` WHERE id=?"
			);
			stmt.setInt(1, id);
			
			ResultSet rs = stmt.executeQuery();
			if(rs.next()) {
				order = new Order(
					rs.getInt("id"), rs.getString("date"), rs.getString("time"),
					new EmployeeDAO(conn).getEmployee(rs.getInt("employeeId"))
				);
				
				OrderDetailedDAO oDDAO = new OrderDetailedDAO(conn);
				for(OrderDetailed od: oDDAO.getAllByOrder(order)) {
					order.addOrderDetailed(od);
				}
				
				return order;
			}
			else {
				System.out.println("Records not found");
				return null;
			}
		}
		catch(SQLException e) {
			throw new ExceptionInInitializerError(e);
		}
	}
	
	public Order insert(Order o) {
		try {
			// insert Order
			PreparedStatement stmt = conn.prepareStatement(
				"INSERT INTO `Order` (`date`, `time`, employeeId) VALUES (?, ?, ?);", 
				Statement.RETURN_GENERATED_KEYS
			);
			stmt.setString(1, o.date);
			stmt.setString(2, o.time);
			stmt.setInt(3, o.employee.id);
			
			stmt.executeUpdate();
			ResultSet rs = stmt.getGeneratedKeys();
			if (rs.next()){
			    o.id = rs.getInt(1);
			}
			
			// insert OrderDetailed
			OrderDetailedDAO orderDetailedDAO = new OrderDetailedDAO(conn);
			for(OrderDetailed od: o.orderDetailedList) {
				orderDetailedDAO.insertByOrder(od, o);
			}
			return o;
		}
		catch(SQLException e) {
			System.out.println("Exception: " + e.toString());
			throw new ExceptionInInitializerError(e);
		} 
	}
	
	public boolean update(Order o) {
		try {
			// update order
			PreparedStatement stmt = conn.prepareStatement(
				"Update `Order` SET `date`=?, `time`=?, employeeId=? WHERE id=?;"
			);
			stmt.setString(1, o.date);
			stmt.setString(2, o.time);
			stmt.setInt(3, o.employee.id);
			stmt.setInt(4, o.id);
			int affectedRows = stmt.executeUpdate();
			
			// update orderDetailed
			OrderDetailedDAO orderDetailedDAO = new OrderDetailedDAO(conn);
			
			// clean
			orderDetailedDAO.deleteByOrder(o);
			
			// insert
			for(OrderDetailed od: o.orderDetailedList) {
				orderDetailedDAO.insertByOrder(od, o);
			}
			
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
	
	public boolean delete(Order o) {
		try {
			// delete order
			PreparedStatement stmt = conn.prepareStatement(
				"DELETE FROM `Order` WHERE id=?;"
			);
			stmt.setInt(1, o.id);
			
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
	
	public ArrayList<Order> search(String orderDate) {
		try {
			ArrayList<Order> orders = new ArrayList<Order>();
			PreparedStatement stmt = conn.prepareStatement(
				"SELECT id, `time`, `date`, employeeId FROM `Order` WHERE `date` LIKE ?;"
			);
			stmt.setString(1, "%" + orderDate + "%");
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				Order order = new Order(
					rs.getInt("id"), rs.getString("time"), 
					rs.getString("date"),
					new EmployeeDAO(conn).getEmployee(rs.getInt("employeeId"))
				);
				order.orderDetailedList = new OrderDetailedDAO(conn).getAllByOrder(order);
				orders.add(order);
			}
			return orders;
		}
		catch(SQLException e) {
			System.out.println("Exception: " + e.toString());
			throw new ExceptionInInitializerError(e);
		}
	}
}