package com.revature.services;

import com.revature.beans.Bike;

public interface EmployeeService {
	// services represent business logic - actual user activities.
		// what can an employee do?
		public int addNewBike(Bike newBike);
		public Bike editBike(Bike bikeToEdit);
		public Bike getBikeById(int id);

}
