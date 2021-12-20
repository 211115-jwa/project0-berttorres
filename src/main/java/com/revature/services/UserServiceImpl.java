package com.revature.services;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import com.revature.beans.Bike;
import com.revature.beans.Person;
import com.revature.data.BikeDAO;
import com.revature.data.PersonDAO;
import com.revature.data.postgres.BikePostgres;
import com.revature.data.postgres.PersonPostgres;

public class UserServiceImpl implements UserService {
	private PersonDAO personDao = new PersonPostgres();
	private BikeDAO bikeDao = new BikePostgres();

	@Override
	public Person register(Person newUser) {
		int newId = personDao.create(newUser);
		if (newId != 0) {
			newUser.setId(newId);
			return newUser;
		}
		return null;
	}

	@Override
	public Person logIn(String username, String password) {
		Person personFromDatabase = personDao.getByUsername(username);
		if (personFromDatabase != null && personFromDatabase.getPassword().equals(password)) {
			return personFromDatabase;
		}
		return null;
	}

	@Override
	public Person updateUser(Person userToUpdate) {
		if (personDao.getById(userToUpdate.getId()) != null) {
			personDao.update(userToUpdate);
			userToUpdate = personDao.getById(userToUpdate.getId());
			return userToUpdate;
		}
		return null;
	}

	@Override
	public Set<Bike> viewAllBikes() {
		return bikeDao.getAll();
	}

	@Override
	public Set<Bike> searchAllBikesByBrand(String brand) {
		
		Set<Bike> availableBikes = (Set<Bike>) bikeDao.getByBrand(brand);
		
		Set<Bike> bikesByBrand = new HashSet<>();
		for (Bike bike : availableBikes) {
			if(bike.getBrand().toLowerCase().contains(brand.toLowerCase())) {
				bikesByBrand.add(bike);
			}
		}
		availableBikes = bikesByBrand;
		return availableBikes;
	}

	@Override
	public Set<Bike> searchAllBikesByModel(String model) {

		Set<Bike> availableBikes = (Set<Bike>) bikeDao.getByModel(model);
		
		/* 
		   using a Stream to filter the bikes
		   "filter" takes in a Predicate (functional interface)
		   and iterates through each bike, adding the bike to the stream
		   if the predicate returns "true"
		*/
		availableBikes = availableBikes.stream()
				.filter(bike -> bike.getModel().toLowerCase().contains(model.toLowerCase()))
				.collect(Collectors.toSet());
		
		return availableBikes;
	}
}
