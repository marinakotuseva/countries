package ru.iteranet.repo;

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

@RunWith(SpringRunner.class)
@DataJpaTest
public class CityRepositoryTest {

    @Autowired
    private CityRepository cityRepository;
    @Autowired
    private CountryRepository countryRepository;

    @Test
    public void testCityRepository(){

        // Read all cities
        List<City> allCities = cityRepository.findAll();
        assertThat(allCities, hasSize(3));
        assertThat(allCities.get(0).getName(), equalTo("Москва"));
        assertThat(allCities.get(1).getName(), equalTo("Бразилиа"));
        assertThat(allCities.get(2).getName(), equalTo("Париж"));

        // Find city by id
        City city = cityRepository.findById((long)1).get();
        assertThat(city.getName(), equalTo("Москва"));

        // Find by name
        City city2 = cityRepository.findByName("Москва");
        assertThat(city2.getId(), equalTo((long)1));

        // Find by Country
        Country loadedCountry = countryRepository.findByName("Россия");
        Set<City> citiesByCountry = cityRepository.findByCountry(loadedCountry);
        assertThat(citiesByCountry, hasSize(1));
        assertThat(citiesByCountry.iterator().next().getName(), equalTo("Москва"));

        // Create new city
        Country createdCountry = countryRepository.save(new Country("Страна 1"));
        City createdCity = cityRepository.save(new City("Город 1", createdCountry));
        assertThat(createdCity.getName(), equalTo("Город 1"));

        // Check new amount
        List<City> citiesAfterAdding = cityRepository.findAll();
        assertThat(citiesAfterAdding, hasSize(4));

        // Update city
        createdCity.setName("Город 1.1");
        cityRepository.save(createdCity);

        // Check amount and name
        List<City> citiesAfterEditing = cityRepository.findAll();
        assertThat(citiesAfterEditing, hasSize(4));
        assertThat(citiesAfterEditing.get(3).getName(), equalTo("Город 1.1"));

        // Delete created city
        cityRepository.delete(createdCity);
        countryRepository.delete(createdCountry);

        // Check that no longer exists
        assertThat(countryRepository.findAll().contains(createdCity.getName()), equalTo(false));

    }


}
