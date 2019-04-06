package ru.iteranet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.iteranet.entity.City;
import ru.iteranet.entity.Country;
import ru.iteranet.exceptions.*;
import ru.iteranet.repo.CityRepository;
import ru.iteranet.repo.CountryRepository;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(path = "/api")
public class CountryController {

    private CountryRepository countryRepository;
    private CityRepository cityRepository;
    private static final int MAX_PARAM_NAME_LENGTH = 255;

    @Autowired
    public CountryController(CountryRepository countryRepository, CityRepository cityRepository) {
        this.countryRepository = countryRepository;
        this.cityRepository = cityRepository;
    }

    @GetMapping("/country")
    public List<Country> findAll() {
        return countryRepository.findAll();
    }

    @GetMapping(value = "/country", params = "name")
    public Country findByName(@RequestParam String name) {
        if (name == null){
            throw new IncorrectNameException();
        }
        return countryRepository.findByName(name);
    }

    @GetMapping("/country/{id}")
    public Country findByID(@PathVariable Long id) {
        return countryRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(id));
    }

    @PostMapping("/country")
    public Country create(@RequestBody Country country) throws StringTooLongException {
        String name = country.getName();
        if (name == null || name.equals("")) {
            throw new IncorrectNameException();
        }
        if (name.length() > MAX_PARAM_NAME_LENGTH) {
            throw new StringTooLongException(country.getName(), MAX_PARAM_NAME_LENGTH);
        }
        Country existingCountry = countryRepository.findByName(name);
        if (existingCountry != null) {
            throw new RecordAlreadyExistsException(name);
        }
        return countryRepository.save(country);
    }

    @PostMapping("/country/{id}")
    public Country update(@RequestBody Country country, @PathVariable Long id) {
        Country existingCountry = countryRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(id));
        existingCountry.setName(country.getName());
        countryRepository.save(existingCountry);
        return existingCountry;
    }

    @DeleteMapping("/country/{id}")
    public void delete(@PathVariable Long id) throws CantDeleteCountryInUseException {
        // Check country exists
        Country country = countryRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(id));
        // Check it has no cities
        Set<City> cities = cityRepository.findByCountry(country);
        System.out.println(cities);
        if (cities.size() > 0) {
            throw new CantDeleteCountryInUseException(country.getName());
        }
        countryRepository.deleteById(id);
    }
}
