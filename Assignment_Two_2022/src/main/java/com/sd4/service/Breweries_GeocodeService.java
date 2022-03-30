/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sd4.service;

import com.sd4.model.Breweries_Geocode;
import com.sd4.repository.Breweries_GeocodeRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Saoir
 */
@Service
public class Breweries_GeocodeService {
    @Autowired
    private Breweries_GeocodeRepository breweriesGeocodeRepo;

    public Optional<Breweries_Geocode> findOne(Long id) {
        return breweriesGeocodeRepo.findById(id);
    }
    
    public Breweries_Geocode findOneByBreweryID(Long id) {
        List<Breweries_Geocode> list =  (List<Breweries_Geocode>) breweriesGeocodeRepo.findAll();
        for(Breweries_Geocode bg : list) {
            if(bg.getBrewery_id() == id) {
                return bg;
            }
        }
        return new Breweries_Geocode();
    }

    public List<Breweries_Geocode> findAll() {
        return (List<Breweries_Geocode>) breweriesGeocodeRepo.findAll();
    }

    public long count() {
        return breweriesGeocodeRepo.count();
    }

    public void deleteByID(long breweryID) {
        breweriesGeocodeRepo.deleteById(breweryID);
    }

    public void saveBreweriesGeocode(Breweries_Geocode b) {
        breweriesGeocodeRepo.save(b);
    }
}
