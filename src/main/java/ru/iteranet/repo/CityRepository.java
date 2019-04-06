package ru.iteranet.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.iteranet.entity.City;
import ru.iteranet.entity.Country;

import java.util.Set;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {

    City findByName(String name);
    Set<City> findByCountry(Country country);
}
