package ru.iteranet.entity;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Country {
    private static int counter = 0;
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    public long id;
    @Column(length=255,unique=true)
    private String name;

    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "country")
    Set<City> cities;

    public Country() {
        //this.id = counter++;
    }

    public Country (String name){
        //this.id = counter++;
        this.name = name;
    }
    public void setCities(Set<City> cities) {
        this.cities = cities;
    }

    @Override
    public String toString() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
