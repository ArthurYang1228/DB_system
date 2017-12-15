package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class EmployeeDAO {
	private Connection conn;
	
	public EmployeeDAO(Connection conn) {
		this.conn = conn;
	}
	
	public ArrayList<Employee> getAllEmployees() {
		try {
			ArrayList<Employee> employees = new ArrayList<Employee>();
			
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(
				"SELECT id, name, workingHours, hourlySalary FROM Employee"
			);
			while(rs.next()) {
				employees.add(
					new Employee(
							rs.getInt("id"), rs.getString("name"), 
							rs.getInt("workingHours"), rs.getInt("hourlySalary")
					)
				);
			}
			return employees;
		}
		catch(SQLException e) {
			throw new ExceptionInInitializerError(e);
		} 
	}
	
	public Employee getEmployee(int id) {
		try {
			PreparedStatement stmt = conn.prepareStatement(
				"SELECT name, workingHours, hourlySalary FROM Employee WHERE id=?"
			);
			stmt.setInt(1, id);
			ResultSet rs = stmt.executeQuery();
			if(rs.next())
				return new Employee(id, rs.getString("name"), 
						rs.getInt("workingHours"), rs.getInt("hourlySalary"));
			else
				System.out.println("Records not found");
				return null;
		}
		catch(SQLException e) {
			throw new ExceptionInInitializerError(e);
		} 
	}
	
	public Employee insert(Employee employee) {
		try {
			PreparedStatement stmt = conn.prepareStatement(
				"INSERT INTO Employee (name, workingHours, hourlySalary) VALUES (?, ?, ?);", 
				Statement.RETURN_GENERATED_KEYS
			);
			stmt.setString(1, employee.name);
			stmt.setInt(2, employee.workingHours);
			stmt.setInt(3, employee.hourlySalary);
			
			int insertedId = stmt.executeUpdate();
			employee.id = insertedId;
			return employee;
		}
		catch(SQLException x) {
			System.out.println("Exception: " + x.toString());
			return null;
		} 
	}
	
	public boolean update(Employee employee) {
		try {
			PreparedStatement stmt = conn.prepareStatement(
				"Update Employee SET name=?, workingHours=?, hourlySalary=? WHERE id=?;"
			);
			stmt.setString(1, employee.name);
			stmt.setInt(2, employee.workingHours);
			stmt.setInt(3, employee.hourlySalary);
			stmt.setInt(4, employee.id);
			
			int affectedRows = stmt.executeUpdate();
			if(affectedRows == 1)
				return true;
			else
				return false;
		}
		catch(SQLException x) {
			System.out.println("Exception: " + x.toString());
			return false;
		}
	}
	
	public boolean delete(Employee employee) {
		try {
			PreparedStatement stmt = conn.prepareStatement(
				"DELETE FROM Employee WHERE id=?;"
			);
			stmt.setInt(1, employee.id);
			
			int affectedRows = stmt.executeUpdate();
			if(affectedRows == 1)
				return true;
			else
				return false;
		}
		catch(SQLException x) {
			System.out.println("Exception: " + x.toString());
			return false;
		}
	}
	
	public ArrayList<Employee> search(String employeeName) {
		try {
			ArrayList<Employee> employees = new ArrayList<Employee>();
			PreparedStatement stmt = conn.prepareStatement(
				"SELECT id, name, workingHours, hourlySalary FROM Employee WHERE name LIKE ?;"
			);
			stmt.setString(1, "%" + employeeName + "%");
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				employees.add(
					new Employee(
						rs.getInt("id"), rs.getString("name"), 
						rs.getInt("workingHours"), rs.getInt("hourlySalary")
					)
				);
			}
			return employees;
		}
		catch(SQLException e) {
			System.out.println("Exception: " + e.toString());
			throw new ExceptionInInitializerError(e);
		}
	}
}