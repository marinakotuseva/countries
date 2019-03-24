package ru.iteranet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.iteranet.entity.Country;
import ru.iteranet.exceptions.NotFoundException;
import ru.iteranet.repo.CountryRepository;

import java.util.List;

@RestController
public class CountryController {

    @Autowired
    private CountryRepository repository;



    @GetMapping("/country")
    List<Country> findAll() {
        return repository.findAll();
    }

    @GetMapping("/country/{id}")
    Country findOne(@PathVariable Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException(id));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/country")
    Country create(@RequestBody Country country) {
        return repository.save(country);
    }

    // Save or update
    @PutMapping("/country/{id}")
    Country saveOrUpdate(@RequestBody Country country, @PathVariable Long id) {

        return repository.findById(id)
                .map(x -> {
                    x.setName(country.getName());
                    //x.setAuthor(country.getAuthor());
                    //x.setPrice(country.getPrice());
                    return repository.save(x);
                })
                .orElseGet(() -> {
                    //country.setId(id);
                    return repository.save(country);
                });
    }

    @DeleteMapping("/country/{id}")
    void delete(@PathVariable Long id) {
        repository.deleteById(id);
    }
}
