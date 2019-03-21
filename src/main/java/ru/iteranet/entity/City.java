package ru.iteranet.entity;

import javax.persistence.*;

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"name", "country_id"})})
public class City {

    private static int counter = 1;
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    public long id;
    @Column(length=255)
    private String name;
    @ManyToOne(cascade = CascadeType.ALL)
    //@JoinColumn(name = "city_id")
    private Country country;


    public City() {
        //this.id = counter++;
    }

    public City(String name) {
        //this.id = counter++;
        this.name = name;
    }

    public City(String name, Country country) {
        //this.id = counter++;
        this.name = name;
        this.country = country;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public String getCountry() {
        return country.getName();
    }

    @Override
    public String toString() {
        return name;
    }
}
