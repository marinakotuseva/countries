package ru.iteranet.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.iteranet.entity.City;
import ru.iteranet.entity.Country;

import java.util.List;

public interface CityRepository extends JpaRepository<City, Long> {
    //@Query("SELECT c FROM City c INNER JOIN Country cn ON c.country = cn.id WHERE c.name=(:name) AND cn.name= (:country)")
    List<City> findByNameAndCountry(@Param("name") String name, @Param("country") Country country);


    //@Query("SELECT c FROM City c  WHERE c.name=(:name)")
    List<City> findByName(String name);


    //@Query("SELECT c FROM City c INNER JOIN Country cn ON c.country = cn.id  WHERE cn.name= (:country)")
    List<City> findByCountry(Country country);
}
