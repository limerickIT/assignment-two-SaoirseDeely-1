/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sd4.service;

import com.sd4.model.Beer;
import com.sd4.repository.BeerRepository;
import java.awt.print.Pageable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Saoir
 */
@Service
public class BeerService {
    @Autowired
    private BeerRepository beerRepo;

    public Optional<Beer> findOne(Long id) {
        return beerRepo.findById(id);
    }

    public List<Beer> findAll() {
        return (List<Beer>) beerRepo.findAll();
    }
    
    public List<Beer> findAllByBrewery(Long id) {
        List<Beer> allBeers = (List<Beer>) beerRepo.findAll();
        List<Beer> breweryBeers = new ArrayList();
        for(Beer b : allBeers) {
            if(b.getBrewery_id() == id)
                breweryBeers.add(b);
        }
        return breweryBeers;
    }

    public long count() {
        return beerRepo.count();
    }

    public void deleteByID(long beerID) {
        beerRepo.deleteById(beerID);
    }

    public void saveBeer(Beer b) {
        beerRepo.save(b);
    }
    
    public void updateBeer(Beer b) {
        if(beerRepo.existsById(b.getId())) {
            beerRepo.save(b);
        }
    }
}
