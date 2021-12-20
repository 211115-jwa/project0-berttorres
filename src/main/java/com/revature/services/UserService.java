package com.revature.services;

import java.util.Set;

import com.revature.beans.Bike;
import com.revature.beans.Person;

public interface UserService {
	// services represent business logic - actual user activities.
		// what can a user do?
		public Person register(Person newUser);
		public Person logIn(String username, String password);
		public Person updateUser(Person userToUpdate);
		
		public Set<Bike> viewAllBikes();
		public Set<Bike> searchAllBikesByBrand(String brand);
		public Set<Bike> searchAllBikesByModel(String model);

}
