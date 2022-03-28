/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sd4.controller;

import com.sd4.model.Beer;
import com.sd4.model.Brewery;
import com.sd4.service.BeerService;
import com.sd4.service.BreweryService;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Saoir
 */
@RestController
public class BreweryController {
    @Autowired
    private BreweryService breweryService;
    @Autowired
    private BeerService beerService;
    
    @GetMapping(value = "/breweries", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<List<Brewery>> getAll() {
        List<Brewery> breweries = breweryService.findAll();
        for(Brewery b : breweries) {
            Link selfBreweryLink = linkTo(methodOn(BreweryController.class).getOne(b.getId())).withSelfRel();
            b.add(selfBreweryLink);
        }
        return ResponseEntity.ok(breweries);
    }
    
    @GetMapping("/breweries/{id}")
    public ResponseEntity<Brewery> getOne(@PathVariable long id) {
       Optional<Brewery> o =  breweryService.findOne(id);
       
       if (!o.isPresent()) 
            return new ResponseEntity(HttpStatus.NOT_FOUND);
       else {
           Brewery brewery = o.orElse(new Brewery());
           List<Beer> beers = beerService.findAllByBrewery(brewery.getId());
           for(Beer b : beers) {
               Link beerLink = linkTo(methodOn(BeerController.class).getOne(b.getId())).withRel(b.getName());
                brewery.add(beerLink);
           }
           return ResponseEntity.ok(o.get());
       }
    }
    
    @GetMapping("/breweries/count")
    public long getCount() {
        return breweryService.count();
    }
    
    @DeleteMapping("/breweries/{id}")
    public ResponseEntity delete(@PathVariable long id) {
        breweryService.deleteByID(id);
        return new ResponseEntity(HttpStatus.OK);
    }
    
    @PostMapping("/breweries/")
    public ResponseEntity add(@RequestBody Brewery b) {
        breweryService.saveBrewery(b);
        return new ResponseEntity(HttpStatus.CREATED);
    }
    
    @PutMapping("/breweries/")
    public ResponseEntity edit(@RequestBody Brewery b) {
        if(getOne(b.getId()) == new ResponseEntity(HttpStatus.NOT_FOUND))
        {
            return new ResponseEntity(HttpStatus.FOUND);
        }
        breweryService.saveBrewery(b);
        return new ResponseEntity(HttpStatus.OK);
    }
}
