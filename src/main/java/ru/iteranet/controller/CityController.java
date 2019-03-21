package ru.iteranet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.iteranet.entity.City;
import ru.iteranet.entity.Country;
import ru.iteranet.exceptions.NotFoundException;
import ru.iteranet.repo.CityRepository;

import java.util.*;

@RestController
public class CityController {

    @Autowired
    private CityRepository repository;



    @GetMapping("/city")
    List<City> findByParams(@RequestParam(required = false) String name, @RequestParam(required = false) Country country) {
        //return repository.findAll();
        List<City> loadedCities;
        if (name != null && country != null) {
            loadedCities = repository.findByNameAndCountry(name, country);
        } else {
            if (name != null){
                loadedCities = repository.findByName(name);
            } else {
                loadedCities = repository.findByCountry(country.getName());
            }
            // TODO: сюда не доходит
            if (name == null && country == null){
                throw new NotFoundException();
            }
        }
        return loadedCities;
    }

    @GetMapping("/city/{id}")
    City findOne(@PathVariable Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException());
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/city")
    City create(@RequestBody City city) {
        return repository.save(city);
    }

    // Save or update
    @PutMapping("/city/{id}")
    City saveOrUpdate(@RequestBody City city, @PathVariable Long id) {

        return repository.findById(id)
                .map(x -> {
                    x.setName(city.getName());
                    //x.setAuthor(city.getAuthor());
                    //x.setPrice(city.getPrice());
                    return repository.save(x);
                })
                .orElseGet(() -> {
                    //city.setId(id);
                    return repository.save(city);
                });
    }

    @DeleteMapping("/city/{id}")
    void delete(@PathVariable Long id) {
        repository.deleteById(id);
    }
}
