package com.udacity.vehicles.api;

import com.udacity.vehicles.client.maps.MapsClient;
import com.udacity.vehicles.client.prices.PriceClient;
import com.udacity.vehicles.domain.Condition;
import com.udacity.vehicles.domain.Location;
import com.udacity.vehicles.domain.car.Car;
import com.udacity.vehicles.domain.car.Details;
import com.udacity.vehicles.domain.manufacturer.Manufacturer;
import com.udacity.vehicles.service.CarService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.net.URI;
import java.util.Collections;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Implements testing of the CarController class.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
public class CarControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JacksonTester<Car> json;

    @MockBean
    private CarService carService;

    @MockBean
    private PriceClient priceClient;

    @MockBean
    private MapsClient mapsClient;

    /**
     * Creates pre-requisites for testing, such as an example car.
     */
    @Before
    public void setup() {
        Car car = getCar();
        car.setId(1L);
        given(carService.save(any())).willReturn(car);
        given(carService.findById(any())).willReturn(car);
        given(carService.list()).willReturn(Collections.singletonList(car));
    }

    /**
     * Tests for successful creation of new car in the system
     *
     * @throws Exception when car creation fails in the system
     */
    @Test
    public void createCar() throws Exception {
        Car car = getCar();
        mvc.perform(
                post(new URI("/cars"))
                        .content(json.write(car).getJson())
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isCreated());
    }

    /**
     * Tests if the read operation appropriately returns a list of vehicles.
     *
     * @throws Exception if the read operation of the vehicle list fails
     */
    @Test
    public void listCars() throws Exception {
        checkCar("_embedded.carList[0].", mvc.perform(get(new URI("/cars")).accept(MediaType.APPLICATION_JSON_UTF8)).andDo(MockMvcResultHandlers.print()).andExpect(status().isOk()));
    }

    private void checkCar(String prefix, ResultActions resultActions) throws Exception {
        Car car = getCar();
        resultActions
                .andExpect(jsonPath(prefix + "details.body", is(car.getDetails().getBody())))
                .andExpect(jsonPath(prefix + "details.model", is(car.getDetails().getModel())))
                .andExpect(jsonPath(prefix + "details.numberOfDoors", is(car.getDetails().getNumberOfDoors())))
                .andExpect(jsonPath(prefix + "details.fuelType", is(car.getDetails().getFuelType())))
                .andExpect(jsonPath(prefix + "details.engine", is(car.getDetails().getEngine())))
                .andExpect(jsonPath(prefix + "details.mileage", is(car.getDetails().getMileage())))
                .andExpect(jsonPath(prefix + "details.modelYear", is(car.getDetails().getModelYear())))
                .andExpect(jsonPath(prefix + "details.productionYear", is(car.getDetails().getProductionYear())))
                .andExpect(jsonPath(prefix + "details.externalColor", is(car.getDetails().getExternalColor())))
                .andExpect(jsonPath(prefix + "location.lon", is(car.getLocation().getLon())))
                .andExpect(jsonPath(prefix + "location.lat", is(car.getLocation().getLat())));
    }

    /**
     * Tests the read operation for a single car by ID.
     *
     * @throws Exception if the read operation for a single car fails
     */
    @Test
    public void findCar() throws Exception {
        checkCar("", mvc.perform(get(new URI("/cars/1")).accept(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().isOk()));
    }

    /**
     * Tests the deletion of a single car by ID.
     *
     * @throws Exception if the delete operation of a vehicle fails
     */
    @Test
    public void deleteCar() throws Exception {
        mvc.perform(delete(new URI("/cars/1"))).andExpect(status().isNoContent()).andExpect(content().string(isEmptyOrNullString()));
    }

    /**
     * Creates an example Car object for use in testing.
     *
     * @return an example Car object
     */
    private Car getCar() {
        Car car = new Car();
        car.setLocation(new Location(40.730610, -73.935242));
        Details details = new Details();
        Manufacturer manufacturer = new Manufacturer(101, "Chevrolet");
        details.setManufacturer(manufacturer);
        details.setModel("Impala");
        details.setMileage(32280);
        details.setExternalColor("white");
        details.setBody("sedan");
        details.setEngine("3.6L V6");
        details.setFuelType("Gasoline");
        details.setModelYear(2018);
        details.setProductionYear(2018);
        details.setNumberOfDoors(4);
        car.setDetails(details);
        car.setCondition(Condition.USED);
        return car;
    }
}