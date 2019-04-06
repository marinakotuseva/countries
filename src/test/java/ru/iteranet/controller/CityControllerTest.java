package ru.iteranet.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import ru.iteranet.entity.City;
import ru.iteranet.entity.Country;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class CityControllerTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    private String serverAddress = "http://localhost:8080";


    @Test
    public void testFindAllCities() {

        ResponseEntity<String> response = testRestTemplate.getForEntity("/api/city", String.class);

        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));


        Type listType = new TypeToken<ArrayList<Country>>() {
        }.getType();
        List<Country> cityList = new Gson().fromJson(response.getBody(), listType);

        assertThat(cityList, hasSize(3));
        assertThat(cityList.get(0).getName(), equalTo("Москва"));
        assertThat(cityList.get(1).getName(), equalTo("Бразилиа"));
        assertThat(cityList.get(2).getName(), equalTo("Париж"));
    }

    @Test
    public void testFindCityByName() {
        String cityName = "Москва";

        HttpEntity<?> entity = new HttpEntity<>(new HttpHeaders());

        ResponseEntity<String> response = testRestTemplate.exchange(
                serverAddress + "/api/city?name=" + cityName,
                HttpMethod.GET,
                entity,
                String.class);

        Type listType = new TypeToken<City>() {
        }.getType();
        City foundCity = new Gson().fromJson(response.getBody(), listType);

        assertThat(foundCity, notNullValue());
        assertThat(foundCity.getName(), equalTo(cityName));
    }

    @Test
    public void testFindCityByCountry() {
        String cityName = "Москва";
        String countryName = "Россия";

        HttpEntity<?> entity = new HttpEntity<>(new HttpHeaders());

        ResponseEntity<String> response = testRestTemplate.exchange(
                serverAddress + "/api/city?country=" + countryName,
                HttpMethod.GET,
                entity,
                String.class);

        Type listType = new TypeToken<Set<City>>() {
        }.getType();
        Set<City> foundCities = new Gson().fromJson(response.getBody(), listType);

        assertThat(foundCities, hasSize(1));
        assertThat(foundCities.iterator().next().getName(), equalTo(cityName));
    }

    @Test
    public void testFindCityByID() {
        String cityName = "Москва";
        long id = 1;

        ResponseEntity<String> response = testRestTemplate.getForEntity("/api/city/" + id, String.class);

        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));

        Type type = new TypeToken<City>() {
        }.getType();
        City foundCity = new Gson().fromJson(response.getBody(), type);

        assertThat(foundCity.getName(), equalTo(cityName));
    }

    @Test
    public void testCreateCity(){
        String cityName = "Город 1";
        long countryID = 1;

        // Check that this city do not exists
        HttpEntity<?> entity = new HttpEntity<>(new HttpHeaders());

        ResponseEntity<String> response = testRestTemplate.exchange(
                serverAddress + "/api/city?name=" + cityName,
                HttpMethod.GET,
                entity,
                String.class);

        Type listType = new TypeToken<City>() {
        }.getType();
        Country nonExistingCity = new Gson().fromJson(response.getBody(), listType);

        assertThat(nonExistingCity, nullValue());

        // Get Country
        ResponseEntity<String> countryResponse = testRestTemplate.getForEntity("/api/country/" + countryID, String.class);
        assertThat(countryResponse.getStatusCode(), equalTo(HttpStatus.OK));
        Type type = new TypeToken<Country>() {
        }.getType();
        Country foundCountry = new Gson().fromJson(countryResponse.getBody(), type);

        // Create new city
        City city = new City(cityName, foundCountry);
        ResponseEntity<String> responseAfterCreation = testRestTemplate.postForEntity("/api/city", city, String.class);

        assertThat(responseAfterCreation.getStatusCode(), equalTo(HttpStatus.OK));

        Type typeCity = new TypeToken<City>() {
        }.getType();
        City createdCity = new Gson().fromJson(responseAfterCreation.getBody(), typeCity);
        assertThat(createdCity.getName(), equalTo(city.getName()));

        // Delete created city
        ResponseEntity<City> responseDelete = testRestTemplate.exchange("/api/city/" + createdCity.getId(),
                HttpMethod.DELETE,
                HttpEntity.EMPTY,
                City.class);

        assertThat(responseDelete.getStatusCode(), equalTo(HttpStatus.OK));

    }


    @Test
    public void testCantCreateCityWithLongName(){
        String cityName = "11111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111112222222";
        long countryID = 1;

        // Check that this city do not exists
        HttpEntity<?> entity = new HttpEntity<>(new HttpHeaders());

        ResponseEntity<String> response = testRestTemplate.exchange(
                serverAddress + "/api/city?name=" + cityName,
                HttpMethod.GET,
                entity,
                String.class);

        Type listType = new TypeToken<City>() {
        }.getType();
        Country nonExistingCity = new Gson().fromJson(response.getBody(), listType);

        assertThat(nonExistingCity, nullValue());

        // Get Country
        ResponseEntity<String> countryResponse = testRestTemplate.getForEntity("/api/country/" + countryID, String.class);
        assertThat(countryResponse.getStatusCode(), equalTo(HttpStatus.OK));
        Type type = new TypeToken<Country>() {
        }.getType();
        Country foundCountry = new Gson().fromJson(countryResponse.getBody(), type);

        // Create new city
        City city = new City(cityName, foundCountry);
        ResponseEntity<String> responseAfterCreation = testRestTemplate.postForEntity("/api/city", city, String.class);

        assertThat(responseAfterCreation.getStatusCode(), equalTo(HttpStatus.INTERNAL_SERVER_ERROR));

    }

    @Test
    public void testDeleteCity() {
        String cityName = "Город_для_удаления";
        long countryID = 1;

        // Create new city to delete
        // Get country
        ResponseEntity<String> countryResponse = testRestTemplate.getForEntity("/api/country/" + countryID, String.class);
        assertThat(countryResponse.getStatusCode(), equalTo(HttpStatus.OK));
        Type type = new TypeToken<Country>() {
        }.getType();
        Country foundCountry = new Gson().fromJson(countryResponse.getBody(), type);

        // Create new city
        City city = new City(cityName, foundCountry);
        ResponseEntity<String> responseAfterCreation = testRestTemplate.postForEntity("/api/city", city, String.class);
        assertThat(responseAfterCreation.getStatusCode(), equalTo(HttpStatus.OK));

        Type typeCity = new TypeToken<City>() {
        }.getType();
        City createdCity = new Gson().fromJson(responseAfterCreation.getBody(), typeCity);
        assertThat(createdCity.getName(), equalTo(city.getName()));

        // Delete created city
        ResponseEntity<City> responseDelete = testRestTemplate.exchange("/api/city/" + createdCity.getId(),
                HttpMethod.DELETE,
                HttpEntity.EMPTY,
                City.class);

        assertThat(responseDelete.getStatusCode(), equalTo(HttpStatus.OK));

        // Check that no longer exists
        ResponseEntity<String> response = testRestTemplate.getForEntity("/api/country/" + createdCity.getId(), String.class);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.INTERNAL_SERVER_ERROR));

    }


    @Test
    public void testUpdateCity() {
        long id = 1;

        String url = serverAddress + "/api/city/" + id;
        String cityName = "Измененное_имя";

        // Check that city exists
        ResponseEntity<String> response = testRestTemplate.getForEntity("/api/city/" + id, String.class);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));

        Type type = new TypeToken<City>() {
        }.getType();
        City foundCity = new Gson().fromJson(response.getBody(), type);
        String oldName = foundCity.getName();
        assertThat(oldName, not(equalTo(cityName)));

        // Update found city
        foundCity.setName(cityName);

        ResponseEntity<String> responseAfterEditing = testRestTemplate.postForEntity("/api/city/"+id, foundCity, String.class);

        assertThat(responseAfterEditing.getStatusCode(), equalTo(HttpStatus.OK));

        Type typeCity = new TypeToken<City>() {
        }.getType();
        City editedCity = new Gson().fromJson(responseAfterEditing.getBody(), typeCity);
        assertThat(editedCity.getName(), equalTo(foundCity.getName()));

        // Change back
        foundCity.setName(oldName);

        ResponseEntity<String> responseAfterEditing2 = testRestTemplate.postForEntity("/api/city/"+id, foundCity, String.class);

        assertThat(responseAfterEditing2.getStatusCode(), equalTo(HttpStatus.OK));



    }



}
