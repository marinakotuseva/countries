package ru.iteranet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

//    // init bean to insert 3 books into h2 database.
//    @Bean
//    CommandLineRunner initDatabase(CityRepository repository) {
//        return args -> {
//            Country c = new Country("Country1");
//            City city = new City("City1", c );
//            repository.save(city);
////            repository.save(new City("City2", new Country("Country2")));
////            repository.save(new City("City3", new Country("Country3")));
//        };
//    }

}

