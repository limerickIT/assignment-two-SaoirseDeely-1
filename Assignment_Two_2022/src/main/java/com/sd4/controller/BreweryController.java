/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sd4.controller;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.sd4.model.Beer;
import com.sd4.model.Breweries_Geocode;
import com.sd4.model.Brewery;
import com.sd4.service.BeerService;
import com.sd4.service.Breweries_GeocodeService;
import com.sd4.service.BreweryService;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import javax.validation.constraints.Positive;
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
import org.springframework.http.MediaType;


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
    @Autowired
    private Breweries_GeocodeService breweriesGeocodeService;
    
    @GetMapping(value = "/breweries", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<List<Brewery>> getAll() {
        List<Brewery> breweries = breweryService.findAll();
        if(breweries.isEmpty()) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        } else {
            for(Brewery b : breweries) {
                Link selfBreweryLink = linkTo(methodOn(BreweryController.class).getOne(b.getId())).withSelfRel();
                b.add(selfBreweryLink);
            }
            return ResponseEntity.ok(breweries);
        }
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
    
    @GetMapping("/breweries/{id}/map")
    public ResponseEntity<String> getBreweryMap(@PathVariable long id) throws MalformedURLException, IOException {
       Optional<Brewery> o =  breweryService.findOne(id);
       
       if (!o.isPresent()) 
            return new ResponseEntity(HttpStatus.NOT_FOUND);
       else {
           Brewery brewery = o.orElse(new Brewery());
           Breweries_Geocode bg = breweriesGeocodeService.findOneByBreweryID(brewery.getId());
           String url = "https://dev.virtualearth.net/REST/v1/Imagery/Map/Road/" + bg.getLatitude() + ',' + bg.getLongitude() + "/18?mapSize=500,500&mapLayer=Basemap,Buildings&pushpin=" + bg.getLatitude() + ',' + bg.getLongitude() + ";89&key=AviDzozBZePHU-0qym2Wvn0_yXtVE641qbKHgZ2jeGtUjLEnZkQc7x0uQo0MPz6B";
           return ResponseEntity.ok("<html><body><h1>" + brewery.getName() + "</h1><h2>" + brewery.getAddress1() + "</h2><h2>" +  brewery.getAddress2() + "</h2><h2>" + brewery.getCity() + "</h2><h2>" + brewery.getState() + "</h2><h2>" + brewery.getCountry() + "</h2>" + "<img src=\"" + url + "\"" + "</body></html>");
       }
    }
    
    @GetMapping(value = "/breweries/{id}/qr", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> getBreweryQR(@PathVariable long id) throws WriterException, IOException {
        Optional<Brewery> o =  breweryService.findOne(id);
        if(!o.isPresent()) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        } else {
            Brewery b = o.orElse(new Brewery());
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            String data = "BEGIN:VCARD\nVERSION:3.0\nCOM:" + b.getName()  + "\nADR:" + b.getAddress1() + "\n" + b.getAddress2() + "\nTEL:" + b.getPhone() + "\nEMAIL:" + b.getEmail() + "\nURL:" + b.getWebsite() + "\nEND:VCARD";
            BitMatrix bitMatrix = qrCodeWriter.encode(data, BarcodeFormat.QR_CODE, 256, 256);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", bos);
            return ResponseEntity.ok(bos.toByteArray());
        }
    }
    
    @GetMapping("/breweries/count")
    public long getCount() {
        return breweryService.count();
    }
    
    @DeleteMapping("/breweries/{id}/delete")
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
