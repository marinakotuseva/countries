package ru.iteranet.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.iteranet.entity.City;
import ru.iteranet.entity.Country;

import java.util.List;

public interface CityRepository extends JpaRepository<City, Long> {
    @Query("SELECT c FROM City c  WHERE c.name=(:name) AND c.country= (:country)")
    List<City> findByNameAndCountry(@Param("name") String name, @Param("country") Country country);


    @Query("SELECT c FROM City c  WHERE c.name=(:name)")
    List<City> findByName(@Param("name") String name);


    @Query("SELECT c FROM City c  WHERE c.country= (:country)")
    List<City> findByCountry(@Param("country") String country);
}
