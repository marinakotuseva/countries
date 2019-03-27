package ru.iteranet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.iteranet.entity.City;
import ru.iteranet.entity.Country;
import ru.iteranet.exceptions.IDIsNotInteger;
import ru.iteranet.exceptions.StringTooLongException;
import ru.iteranet.exceptions.NotFoundException;
import ru.iteranet.repo.CityRepository;

import javax.validation.Valid;
import java.util.*;

@RestController
public class CityController {

    @Autowired
    private CityRepository repository;
    private static final int MAX_PARAM_NAME_LENGTH = 255;



    @GetMapping("/city")
    List<City> findByParams(@RequestParam(required = false) String name, @RequestParam(required = false) Country country) {
        if (name.length() > MAX_PARAM_NAME_LENGTH) {
            throw new StringTooLongException(name, MAX_PARAM_NAME_LENGTH);
        }
        List<City> loadedCities;
        if (name != null && country != null) {
            loadedCities = repository.findByNameAndCountry(name, country.getName());
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

    // Find by ID
    @GetMapping("/city/{id}")
    City findOne(@PathVariable Long id) {
        System.out.println(id.getClass().toString());
        // TODO
        if (!(id instanceof Long)){
            throw new IDIsNotInteger(id);
        }
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException(id));
    }

    // Create new
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/city")
    City create(@Valid @RequestBody City cityJson) {

        // TODO check if Json
//        if (!(cityJson instanceof City)){
//            throw new RuntimeException ("Not city");
//        }
//        if (!cityJson.isNull("URL"))
//        {
//            // Note, not `getJSONArray` or any of that.
//            // This will give us whatever's at "URL", regardless of its type.
//            Object item = cityJson.get("URL");
//
//            // `instanceof` tells us whether the object can be cast to a specific type
//            if (item instanceof JSONArray)
//            {
//                // it's an array
//                JSONArray urlArray = (JSONArray) item;
//                // do all kinds of JSONArray'ish things with urlArray
//            }
//            else
//            {
//                // if you know it's either an array or an object, then it's an object
//                JSONObject urlObject = (JSONObject) item;
//                // do objecty stuff with urlObject
//            }
//        }
//        else
//        {
//            // URL is null/undefined
//            // oh noes
//        }

        if (cityJson.getName().length() > MAX_PARAM_NAME_LENGTH) {
            throw new StringTooLongException(cityJson.getName(), MAX_PARAM_NAME_LENGTH);
        }
        return repository.save(cityJson);
    }

    // Save or update
    @PutMapping("/city/{id}")
    City saveOrUpdate(@RequestBody City city, @PathVariable Long id) {
        // TODO check id format

        return repository.findById(id)
                .map(x -> {
                    x.setName(city.getName());
                    return repository.save(x);
                })
                .orElseGet(() -> {
                    return repository.save(city);
                });
    }

    @DeleteMapping("/city/{id}")
    void delete(@PathVariable Long id) {

        // TODO check id format
        // TODO check if country with city exists
        repository.deleteById(id);
    }
}
