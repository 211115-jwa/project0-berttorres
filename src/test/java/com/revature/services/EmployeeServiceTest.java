package com.revature.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.revature.beans.Bike;
import com.revature.data.BikeDAO;

@ExtendWith(MockitoExtension.class)

public class EmployeeServiceTest {
	@Mock
	private BikeDAO bikeDao;
	
	@InjectMocks
	private EmployeeService empServ = new EmployeeServiceImpl();
	
	@Test
	public void addNewBikeSuccessfully() {
		Bike bike = new Bike();
		
		when(bikeDao.create(bike)).thenReturn(10);
		
		int newId = empServ.addNewBike(bike);
		
		assertNotEquals(0, newId);
	}
	
	@Test
	public void addNewBikeSomethingWrong() {
		Bike bike = new Bike();
		
		when(bikeDao.create(bike)).thenReturn(0);
		
		int newId = empServ.addNewBike(bike);
		
		assertEquals(0,newId);
	}
	
	@Test
	public void editBikeSuccessfully() {
		Bike editedBike = new Bike();
		editedBike.setId(2);
		editedBike.setBrand("Schwinn");
		
		when(bikeDao.getById(2)).thenReturn(editedBike);
		doNothing().when(bikeDao).update(Mockito.any(Bike.class));
		
		Bike actualBike = empServ.editBike(editedBike);
		
		assertEquals(editedBike, actualBike);
	}
	
	@Test
	public void editBikeSomethingWrong() {
		Bike mockBike = new Bike();
		mockBike.setId(2);
		
		when(bikeDao.getById(2)).thenReturn(mockBike);
		doNothing().when(bikeDao).update(Mockito.any(Bike.class));
		
		Bike editedBike = new Bike();
		editedBike.setId(2);
		editedBike.setBrand("Schwinn");
		
		Bike actualBike = empServ.editBike(editedBike);
		
		assertNotEquals(editedBike, actualBike);
	}
	
	@Test
	public void editBikeDoesNotExist() {
		when(bikeDao.getById(2)).thenReturn(null);
		
		Bike editedBike = new Bike();
		editedBike.setId(2);
		editedBike.setBrand("Schwinn");
		
		Bike actualBike = empServ.editBike(editedBike);
		
		assertNull(actualBike);
		verify(bikeDao, times(0)).update(Mockito.any(Bike.class));
	}
	
	@Test
	public void getByIdBikeExists() {
		Bike bike = new Bike();
		bike.setId(2);
		
		when(bikeDao.getById(2)).thenReturn(bike);
		
		Bike actualBike = empServ.getBikeById(2);
		assertEquals(bike, actualBike);
	}
	
	@Test
	public void getByIdBikeDoesNotExist() {
		when(bikeDao.getById(2)).thenReturn(null);
		
		Bike actualBike = empServ.getBikeById(2);
		assertNull(actualBike);
	}
}
