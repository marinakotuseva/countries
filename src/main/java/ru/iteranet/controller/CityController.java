package ru.iteranet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.iteranet.entity.City;
import ru.iteranet.exceptions.IncorrectNameException;
import ru.iteranet.exceptions.RecordAlreadyExistsException;
import ru.iteranet.exceptions.RecordNotFoundException;
import ru.iteranet.exceptions.StringTooLongException;
import ru.iteranet.repo.CityRepository;
import ru.iteranet.repo.CountryRepository;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(path = "/api")
public class CityController {

    private static final int MAX_PARAM_NAME_LENGTH = 255;
    private CityRepository cityRepository;
    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    public CityController(CityRepository repository) {
        this.cityRepository = repository;
    }

    // Find all
    @GetMapping("/city")
    public List<City> findAll() {
        return cityRepository.findAll();
    }

    // Find by Name
    @GetMapping(value = "/city", params = "name")
    public City findByName(@RequestParam String name) {
        if (name == null){
            throw new IncorrectNameException();
        }
        return cityRepository.findByName(name);
    }

    // Find by Country
    @GetMapping(value = "/city", params = "country")
    public Set<City> findByCountry(@RequestParam String country) {
        if (country == null){
            throw new IncorrectNameException();
        }
        return cityRepository.findByCountry(countryRepository.findByName(country));
    }

    // Find by ID
    @GetMapping("/city/{id}")
    City findByID(@PathVariable Long id) {
        return cityRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(id));
    }

    // Create new
    @PostMapping("/city")
    City create(@RequestBody City city) throws StringTooLongException {
        String name = city.getName();
        if (name == null || name.equals("")) {
            throw new IncorrectNameException();
        }
        if (name.length() > MAX_PARAM_NAME_LENGTH) {
            throw new StringTooLongException(city.getName(), MAX_PARAM_NAME_LENGTH);
        }
        City existingCity = cityRepository.findByName(name);
        if (existingCity != null) {
            throw new RecordAlreadyExistsException(name);
        }
        return cityRepository.save(city);
    }

    // Update
    @PostMapping("/city/{id}")
    public City update(@RequestBody City city, @PathVariable Long id) {
        City existingCity = cityRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(id));
        existingCity.setName(city.getName());
        return cityRepository.save(existingCity);
    }

    // Delete
    @DeleteMapping("/city/{id}")
    public void delete(@PathVariable Long id) {
        cityRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(id));

        cityRepository.deleteById(id);
    }


}
