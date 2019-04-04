package ru.iteranet.repo;

import org.hamcrest.core.IsNot;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.iteranet.entity.City;
import ru.iteranet.entity.Country;

import java.util.List;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.IsNull.nullValue;

@RunWith(SpringRunner.class)
@DataJpaTest
public class CityRepositoryTest {

    @Autowired
    private CityRepository cityRepository;
    @Autowired
    private CountryRepository countryRepository;

    @Test
    public void testFindAllCities() {

        List<City> cities = cityRepository.findAll();
        assertThat(cities, hasSize(3));
        assertThat(cities.get(0).getName(), equalTo("Москва"));
        assertThat(cities.get(1).getName(), equalTo("Бразилиа"));
        assertThat(cities.get(2).getName(), equalTo("Париж"));
    }

    @Test
    public void testFindCityByID() {
        long id = 1;
        City city = cityRepository.findById(id).get();
        assertThat(city.getId(), equalTo(id));
    }

    @Test
    public void testFindCityByName() {
        String cityName = "Москва";
        City city = cityRepository.findByName(cityName);
        assertThat(city.getName(), equalTo(cityName));
    }


    @Test
    public void testFindCityByCountry() {
        String countryName = "Россия";
        Country loadedCountry = countryRepository.findByName(countryName);
        Set<City> cities = cityRepository.findByCountry(loadedCountry);
        assertThat(cities, hasSize(1));
        assertThat(cities.iterator().next().getName(), equalTo("Москва"));
    }

    @Test
    public void testCreateCity() {

        String cityName = "Город 1";
        String countryName = "Страна 1";

        // Check that this city not exists and get amount
        List<City> cities = cityRepository.findAll();
        City nonExistingCity = cityRepository.findByName(cityName);
        int citiesAmount = cities.size();
        assertThat(nonExistingCity, nullValue());

        // Add city
        Country country = new Country(countryName);
        City city = new City(cityName, country);
        Country createdCountry = countryRepository.save(country);
        City createdCity = cityRepository.save(city);
        assertThat(createdCity, equalTo(city));

        // Check new amount
        List<City> citiesAfterAdding = cityRepository.findAll();
        assertThat(citiesAfterAdding, hasSize(citiesAmount+1));

        // Delete created instances
        cityRepository.delete(createdCity);
        countryRepository.delete(createdCountry);
    }

    @Test
    public void testCantCreateCityWithLongName() {

        String cityName = "11111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111112222222";
        String countryName = "Страна 1";

        // Check that this city not exists and get amount
        List<City> cities = cityRepository.findAll();
        City nonExistingCity = cityRepository.findByName(cityName);
        int citiesAmount = cities.size();
        assertThat(nonExistingCity, nullValue());

        // Add city
        Country country = new Country(countryName);
        City city = new City(cityName, country);
        Country createdCountry = countryRepository.saveAndFlush(country);
        City createdCity = cityRepository.saveAndFlush(city);
        assertThat(createdCity, equalTo(city));

        // Check new amount
        List<City> citiesAfterAdding = cityRepository.findAll();
        assertThat(citiesAfterAdding, hasSize(citiesAmount+1));

        // Delete created instances
        cityRepository.delete(createdCity);
        countryRepository.delete(createdCountry);
    }

    @Test
    public void testDeleteCity(){
        String cityName = "Город 1";
        String countryName = "Страна 1";


        // Get initial amount
        List<City> cities = cityRepository.findAll();
        int citiesAmount = cities.size();

        // Create new city to delete
        Country country = new Country(countryName);
        City city = new City(cityName, country);
        Country createdCountry = countryRepository.save(country);
        City createdCity = cityRepository.save(city);
        assertThat(createdCity, equalTo(city));

        // Check new amount
        List<City> citiesAfterAdding = cityRepository.findAll();
        assertThat(citiesAfterAdding, hasSize(citiesAmount+1));

        // Delete
        cityRepository.delete(createdCity);
        countryRepository.delete(createdCountry);

        // Check new amount
        List<City> citiesAfterDeleting = cityRepository.findAll();
        assertThat(citiesAfterDeleting, hasSize(citiesAmount));

    }

    @Test
    public void testUpdateCity() {
        String cityName = "Город 1";
        String cityNewName = "Город 2";
        String countryName = "Страна 1";


        // Get initial amount
        List<City> cities = cityRepository.findAll();
        int citiesAmount = cities.size();

        // Create new city to update
        Country country = new Country(countryName);
        City city = new City(cityName, country);
        Country createdCountry = countryRepository.save(country);
        City createdCity = cityRepository.save(city);
        assertThat(createdCity, equalTo(city));

        // Update
        City loadedCity = cityRepository.findByName(cityName);
        loadedCity.setName(cityNewName);
        cityRepository.save(loadedCity);

        // Check that amount increased by 1 - after creation, and not by 2
        List<City> citiesAfterUpdate = cityRepository.findAll();
        assertThat(citiesAfterUpdate.size(), equalTo(citiesAmount+1));

        City cityAfterChangingName = cityRepository.findByName(cityNewName);
        assertThat(cityAfterChangingName.getName(), equalTo(cityNewName));

    }

}
