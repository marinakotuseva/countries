package ru.iteranet;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.iteranet.entity.City;
import ru.iteranet.entity.Country;
import ru.iteranet.repo.CityRepository;

import java.util.List;

import static junit.framework.TestCase.assertEquals;

@RunWith(SpringRunner.class)
@DataJpaTest
public class CityRepositoryTest {

    @Autowired
    private CityRepository repository;

    @Test
    public void can_find_city_after_creation() {
        Country russia = new Country("Russia");
        City city = new City("Volgograd", russia);
        repository.save(city);

        List<City> cities = repository.findAll();

        assertEquals(1, cities.size());
        assertEquals("Volgograd", cities.get(0).getName());
        assertEquals("Russia", cities.get(0).getCountry());

    }

    @Test
    public void can_delete_city_after_creation() {

        Country russia = new Country("Russia");
        City city = new City("Volgograd", russia);
        repository.save(city);

        List<City> cities = repository.findAll();

        repository.delete(cities.get(0));

        List<City> citiesAfterDeletion = repository.findAll();
        assertEquals(0, citiesAfterDeletion.size());

    }

    @Test
    public void can_update_city_after_creation() {

        Country russia = new Country("Russia");
        City city = new City("Volgograd", russia);
        repository.save(city);

        city.setName("Kiev");
        repository.save(city);

        List<City> cities = repository.findAll();
        assertEquals(1, cities.size());
        assertEquals("Kiev", cities.get(0).getName());
    }

    @Test
    public void can_find_cities_by_name_and_country() {

        Country russia = new Country("Russia");
        Country ukraine = new Country("Ukraine");
        City city = new City("Volgograd", russia);
        City city2 = new City("Samara", russia);
        City city3 = new City("Kiev", ukraine);
        repository.save(city);
        repository.save(city2);
        repository.save(city3);

        List<City> citiesVolgogradInRussia = repository.findByNameAndCountry("Volgograd", russia.getName());
        List<City> citiesVolgogradInUkraine = repository.findByNameAndCountry("Volgograd", ukraine.getName());

        assertEquals(1, citiesVolgogradInRussia.size());
        City foundVolgogradInRussia = citiesVolgogradInRussia.get(0);
        assertEquals("Volgograd", foundVolgogradInRussia.getName());
        assertEquals("Russia", foundVolgogradInRussia.getCountry());
        assertEquals(0, citiesVolgogradInUkraine.size());
    }

    @Test
    public void can_find_cities_by_name() {

        Country russia = new Country("Russia");
        Country ukraine = new Country("Ukraine");
        City city = new City("Volgograd", russia);
        City city2 = new City("Samara", russia);
        City city3 = new City("Volgograd", ukraine);
        repository.save(city);
        repository.save(city2);
        repository.save(city3);

        List<City> citiesVolgograd = repository.findByName("Volgograd");

        assertEquals(2, citiesVolgograd.size());
        assertEquals("Volgograd", citiesVolgograd.get(0).getName());
        assertEquals("Volgograd", citiesVolgograd.get(1).getName());
    }

    @Test
    public void can_find_cities_by_country() {

        Country russia = new Country("Russia");
        Country ukraine = new Country("Ukraine");
        City city = new City("Volgograd", russia);
        City city2 = new City("Samara", russia);
        City city3 = new City("Volgograd", ukraine);
        repository.save(city);
        repository.save(city2);
        repository.save(city3);

        List<City> citiesInRussia = repository.findByCountry(russia.getName());

        assertEquals(2, citiesInRussia.size());
        assertEquals("Volgograd", citiesInRussia.get(0).getName());
        assertEquals("Samara", citiesInRussia.get(1).getName());
    }

    //TODO: exceptions
}
