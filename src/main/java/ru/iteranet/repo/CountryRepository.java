package ru.iteranet.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.iteranet.entity.Country;

public interface CountryRepository extends JpaRepository<Country, Long> {
}
