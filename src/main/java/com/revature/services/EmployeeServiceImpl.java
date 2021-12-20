package com.revature.services;

import com.revature.beans.Bike;
import com.revature.data.BikeDAO;
import com.revature.data.postgres.BikePostgres;

public class EmployeeServiceImpl implements EmployeeService {
	private BikeDAO bikeDao = new BikePostgres();

	@Override
	public int addNewBike(Bike newBike) {
		return bikeDao.create(newBike);
	}

	@Override
	public Bike editBike(Bike bikeToEdit) {
		Bike bikeFromDatabase = bikeDao.getById(bikeToEdit.getId());
		if (bikeFromDatabase != null) {
			bikeDao.update(bikeToEdit);
			return bikeDao.getById(bikeToEdit.getId());
		}
		return null;
	}

	@Override
	public Bike getBikeById(int id) {
		return bikeDao.getById(id);
	}

}
