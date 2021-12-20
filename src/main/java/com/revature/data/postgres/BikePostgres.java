package com.revature.data.postgres;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

import com.revature.beans.Bike;
import com.revature.data.BikeDAO;
import com.revature.utils.ConnectionUtil;

public class BikePostgres implements BikeDAO {
	private ConnectionUtil connUtil = ConnectionUtil.getConnectionUtil();
	/******************************************************************************************************
	 * 
	 *  1. view all bikes
	 * 
	 ********************************************************/
	@Override
	public Set<Bike> getAll() {
		Set<Bike> allBikes = new HashSet<>();

		try (Connection conn = connUtil.getConnection()) {
			String sql = "select * from bike";
			Statement stmt = conn.createStatement();
			ResultSet resultSet = stmt.executeQuery(sql);
			// while the result set has another row
			while (resultSet.next()) {
				// create the Bike object
				Bike bike = new Bike();
				// pull the data from each row in the result set
				// and put it into the java object so that we can use it here
				bike.setId(resultSet.getInt("id"));
				bike.setBrand(resultSet.getString("brand"));
				bike.setModel(resultSet.getString("model"));

				allBikes.add(bike);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return allBikes;
	}
	/******************************************************************************************************
	 * 
	 *  2. add a new bike 
	 * 
	 **********************************************************/
	@Override
	public int create(Bike dataToAdd) {
		int generatedId = 0;
		// try-with-resources auto-closes resources
		try (Connection conn = connUtil.getConnection()) {
			// when you run DML statements, you want to manage the TCL
			conn.setAutoCommit(false);

			String sql = "insert into bike (id,brand,model)"
					+ "values (default, ?, ?)";
			String[] keys = { "id" }; // the name of the primary key column that will be auto-generated
			// creating the prepared statement
			PreparedStatement pStmt = conn.prepareStatement(sql, keys);
			// we need to set the values of the question marks
			pStmt.setString(1, dataToAdd.getBrand()); // question mark index starts at 1
			pStmt.setString(2, dataToAdd.getModel());

			// after setting the values, we can run the statement
			pStmt.executeUpdate();
			ResultSet resultSet = pStmt.getGeneratedKeys();

			if (resultSet.next()) { // "next" goes to the next row in the result set (or the first row)
				// getting the ID value from the result set
				generatedId = resultSet.getInt("id");
				conn.commit(); // running the TCL commit statement
			} else {
				conn.rollback();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return generatedId;
	}
	/**************************************************************************************************
	 * 
	 *  3. update a bike by id
	 * 
	 **************************************************************/
	@Override
	public void update(Bike dataToUpdate) {
		try (Connection conn = connUtil.getConnection()) {
			conn.setAutoCommit(false);
			
			String sql = "update bike set "
					+ "brand=?,model=? "
					+ "where id=?";
			PreparedStatement pStmt = conn.prepareStatement(sql);
			pStmt.setString(1, dataToUpdate.getBrand());
			pStmt.setString(2, dataToUpdate.getModel());
			pStmt.setInt(3, dataToUpdate.getId());
			
			int rowsAffected = pStmt.executeUpdate();
			
			if (rowsAffected==1) {
				conn.commit();
			} else {
				conn.rollback();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	/***************************************************************************************************
	 * 
	 *  NOT NEEDED YET
	 * 
	 ************************************************************/
	@Override
	public void delete(Bike dataToDelete) {
		// TODO Auto-generated method stub
	}
	/*************************************************************************************************
	 * 
	 *  4. view a bike by id
	 * 
	 ***********************************************************/
	@Override
	public Bike getById(int id) {
		Bike bike = null;
		try (Connection conn = connUtil.getConnection()) {
			String sql = "select * from bike where id=?";
			PreparedStatement pStmt = conn.prepareStatement(sql);
			pStmt.setInt(1, id);

			ResultSet resultSet = pStmt.executeQuery();

			if (resultSet.next()) {
				bike = new Bike();
				bike.setId(id);
				bike.setBrand(resultSet.getString("brand"));
				bike.setModel(resultSet.getString("model"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return bike;
	}
	/****************************************************************************************************
	 * 
	 *  5a. search bikes by brand
	 * 
	 *****************************************************/
	@Override
	public Set<Bike> getByBrand(String brand) {
		Set<Bike> allBikes = new HashSet<>();

		try (Connection conn = connUtil.getConnection()) {
			String sql = "select * from bike where brand=?";
			PreparedStatement pStmt = conn.prepareStatement(sql);
			pStmt.setString(1, brand);

			ResultSet resultSet = pStmt.executeQuery();

			while (resultSet.next()) {
				Bike bike = new Bike();
				bike.setId(resultSet.getInt("id"));
				bike.setBrand(resultSet.getString("brand"));
				bike.setModel(resultSet.getString("model"));
				
				allBikes.add(bike);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return allBikes;
	}
	/****************************************************************************************************
	 * 
	 *  5b. search bikes by model
	 * 
	 ***************************************************************/
	@Override
	public Set<Bike> getByModel(String model) {
		Set<Bike> allBikes = new HashSet<>();

		try (Connection conn = connUtil.getConnection()) {
			String sql = "select * from bike where model=?";
			PreparedStatement pStmt = conn.prepareStatement(sql);
			pStmt.setString(1, model);

			ResultSet resultSet = pStmt.executeQuery();

			while (resultSet.next()) {
				Bike bike = new Bike();
				bike.setId(resultSet.getInt("id"));
				bike.setBrand(resultSet.getString("brand"));
				bike.setModel(resultSet.getString("model"));
				
				allBikes.add(bike);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return allBikes;
	}
}
