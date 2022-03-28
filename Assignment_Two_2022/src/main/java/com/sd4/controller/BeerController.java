/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sd4.controller;
import com.google.gson.Gson;
import com.sd4.model.Beer;
import com.sd4.service.BeerService;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
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
public class BeerController {
    @Autowired
    private BeerService beerService;
    
    @GetMapping("/beers")
    public ResponseEntity getAll() {
        return new ResponseEntity(new Gson().toJson(beerService.findAll()), HttpStatus.OK);
    }
    
    @GetMapping(value = "/beers/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<Beer> getOne(@PathVariable long id) {
       Optional<Beer> o =  beerService.findOne(id);
       
       if (!o.isPresent()) 
            return new ResponseEntity(HttpStatus.NOT_FOUND);
       else{
           Link allBeersLink = linkTo(methodOn(BeerController.class).getAll()).withSelfRel();
           o.get().add(allBeersLink);
           return ResponseEntity.ok(o.get());
       }
    }
    
    @GetMapping("/beers/count")
    public long getCount() {
        return beerService.count();
    }
    
    @DeleteMapping("/beers/{id}")
    public ResponseEntity delete(@PathVariable long id) {
        beerService.deleteByID(id);
        return new ResponseEntity(HttpStatus.OK);
    }
    
    @PostMapping("/beers/")
    public ResponseEntity add(@RequestBody Beer b) {
        beerService.saveBeer(b);
        return new ResponseEntity(HttpStatus.CREATED);
    }
    
    @PutMapping("/beers/")
    public ResponseEntity edit(@RequestBody Beer b) {
        if(getOne(b.getId()) == new ResponseEntity(HttpStatus.NOT_FOUND))
        {
            return new ResponseEntity(HttpStatus.FOUND);
        }
        beerService.saveBeer(b);
        return new ResponseEntity(HttpStatus.OK);
    }
}
