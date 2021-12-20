package com.revature.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.revature.beans.Person;
import com.revature.beans.Bike;
import com.revature.data.PersonDAO;
import com.revature.data.BikeDAO;

//tell JUnit that we're using Mockito
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
	// tell Mockito which classes/interfaces that we'll be mocking
	@Mock
	private BikeDAO bikeDao;

	@Mock
	private PersonDAO personDao;

	// tell Mockito to override the regular DAOs with our mock DAOs
	@InjectMocks
	private UserService userServ = new UserServiceImpl();

	private static Set<Bike> mockAvailableBikes;

	@BeforeAll
	public static void mockAvailableBikesSetup() {
		mockAvailableBikes = new HashSet<>();

		for (int i = 1; i <= 5; i++) {
			Bike bike = new Bike();
			bike.setId(i);
			if (i < 3)
				bike.setBrand("schwin");
			mockAvailableBikes.add(bike);
		}
	}

	@Test
	public void logInSuccessfully() {
		// input setup
		String username = "qwertyuiop";
		String password = "pass";

		// set up the mocking
		Person mockPerson = new Person();
		mockPerson.setUsername(username);
		mockPerson.setPassword(password);
		when(personDao.getByUsername(username)).thenReturn(mockPerson);

		// call the method we're testing
		Person actualPerson = userServ.logIn(username, password);

		// assert the expected behavior/output
		assertEquals(mockPerson, actualPerson);
	}

	@Test
	public void logInIncorrectPassword() {
		String username = "qwertyuiop";
		String password = "12345";

		Person mockPerson = new Person();
		mockPerson.setUsername(username);
		mockPerson.setPassword("pass");
		when(personDao.getByUsername(username)).thenReturn(mockPerson);

		Person actualPerson = userServ.logIn(username, password);
		assertNull(actualPerson);
	}

	@Test
	public void logInUsernameDoesNotExist() {
		String username = "asdfghjkl";
		String password = "pass";

		when(personDao.getByUsername(username)).thenReturn(null);

		Person actualPerson = userServ.logIn(username, password);
		assertNull(actualPerson);
	}

	@Test
	public void registerPersonSuccessfully() {
		Person person = new Person();

		when(personDao.create(person)).thenReturn(10);

		Person actualPerson = userServ.register(person);
		assertEquals(10, actualPerson.getId());
	}

	@Test
	public void registerPersonSomethingWrong() {
		Person person = new Person();
		when(personDao.create(person)).thenReturn(0);
		Person actualPerson = userServ.register(person);
		assertNull(actualPerson);
	}

	@Test
	public void searchByBrandExists() {
		String brand = "trek";

		when(bikeDao.getByBrand(brand)).thenReturn(mockAvailableBikes);

		Set<Bike> actualBrand = userServ.searchAllBikesByBrand(brand);
		boolean onlyBrand = true;
		for (Bike bike : actualBrand) {
			if (!bike.getBrand().equals(brand))
				onlyBrand = false;
		}

		assertTrue(onlyBrand);
	}

	@Test
	public void searchByBrandDoesNotExist() {
		String brand = "qwertyuiop";

		when(bikeDao.getByBrand(brand)).thenReturn(mockAvailableBikes);

		Set<Bike> actualBrand = userServ.searchAllBikesByBrand(brand);
		assertTrue(actualBrand.isEmpty());
	}
	@Test
	public void searchByModelExists() {
		String model = "10 speed";

		when(bikeDao.getByModel(model)).thenReturn(mockAvailableBikes);

		Set<Bike> actualModel = userServ.searchAllBikesByModel(model);
		boolean onlyModelExists = true;
		for (Bike bike : actualModel) {
			if (!bike.getModel().equals(model))
				onlyModelExists = false;
		}

		assertTrue(onlyModelExists);
	}

	@Test
	public void searchByModelDoesNotExist() {
		String model = "qwertyuiop";

		when(bikeDao.getByModel(model)).thenReturn(mockAvailableBikes);

		Set<Bike> actualModel = userServ.searchAllBikesByModel(model);
		assertTrue(actualModel.isEmpty());
	}
}
