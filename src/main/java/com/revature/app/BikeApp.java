package com.revature.app;

import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.path;
import static io.javalin.apibuilder.ApiBuilder.post;
import static io.javalin.apibuilder.ApiBuilder.put;

import java.util.Set;

import org.eclipse.jetty.http.HttpStatus;

import com.revature.beans.Bike;
import com.revature.services.EmployeeService;
import com.revature.services.EmployeeServiceImpl;
import com.revature.services.UserService;
import com.revature.services.UserServiceImpl;

import io.javalin.Javalin;
import io.javalin.http.HttpCode;

public class BikeApp {
	private static UserService userServ = new UserServiceImpl();
	private static EmployeeService empServ = new EmployeeServiceImpl();;

	public static void main(String[] args) {
		Javalin app = Javalin.create();

		app.start();

		/*
		 * what end-points do we need? in other words, what actions would a user need to
		 * do and what address + HTTP method combo would represent each of those
		 * actions?
		 */
		app.routes(() -> {
			// local-host:8080/bikes
			path("/bikes", () -> {
				get(ctx -> {
					// checking if they did /bikes?brand= or /bikes?model=
					String brandSearch = ctx.queryParam("brand");
					String modelSearch = ctx.queryParam("model");
					// when using .equals with a String literal, put the
					// String literal first because it prevents null pointer exceptions
					if (brandSearch != null && !"".equals(brandSearch)) {
						Set<Bike> bikesFound = userServ.searchAllBikesByBrand(brandSearch);
						ctx.json(bikesFound);
					} else if (modelSearch != null && !"".equals(modelSearch)) {
						Set<Bike> bikesFound = userServ.searchAllBikesByModel(modelSearch);
						ctx.json(bikesFound);
						} else {
						// if they didn't put ?brand or ?model
						Set<Bike> availableBikes = userServ.viewAllBikes();
						ctx.json(availableBikes);
					}
				});
				// add a new bike
				post(ctx -> {
					// bodyAsClass turns JSON into a Java object based on the class you specify
					Bike newBike = ctx.bodyAsClass(Bike.class);
					if (newBike != null) {
						empServ.addNewBike(newBike);
						ctx.status(HttpStatus.CREATED_201);
					} else {
						ctx.status(HttpStatus.BAD_REQUEST_400);
					}
				});

				// local-host:8080/bikes/8
				path("/{id}", () -> {
					// get a bike by the id
					get(ctx -> {
						try {
							int bikeId = Integer.parseInt(ctx.pathParam("id")); // number format exception
							Bike bike = empServ.getBikeById(bikeId);
							if (bike != null)
								ctx.json(bike);
							else
								ctx.status(404);
						} catch (NumberFormatException e) {
							ctx.status(400);
							ctx.result("Bike ID must be a numeric value");
						}
					});
					//update a bike by the id
					put(ctx -> {
						try {
							int bikeId = Integer.parseInt(ctx.pathParam("id")); // number format exception
							Bike bikeToEdit = ctx.bodyAsClass(Bike.class);
							if (bikeToEdit != null && bikeToEdit.getId() == bikeId) {
								bikeToEdit = empServ.editBike(bikeToEdit);
								if (bikeToEdit != null)
									ctx.json(bikeToEdit);
								else
									ctx.status(404);
							} else {
								// conflict: the id doesn't match the id of the bike sent
								ctx.status(HttpCode.CONFLICT);
							}
						} catch (NumberFormatException e) {
							ctx.status(400);
							ctx.result("Bike ID must be a numeric value");
						}
					});

				});
			});
		});
	}
}
