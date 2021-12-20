package com.revature.data;

import java.util.Set;

import com.revature.beans.Bike;

public interface BikeDAO extends GenericDAO<Bike> {
	// here, we could add any additional behaviors that are
	// unique to accessing Bike data (not just basic CRUD)
	
	public Set<Bike> getByBrand(String brand);

	public Set<Bike> getByModel(String model);
}
