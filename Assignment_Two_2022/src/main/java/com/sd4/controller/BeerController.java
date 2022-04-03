/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sd4.controller;
import com.sd4.model.Beer;
import com.sd4.model.Brewery;
import com.sd4.model.Category;
import com.sd4.model.Style;
import com.sd4.service.BeerService;
import com.sd4.service.BreweryService;
import com.sd4.service.CategoryService;
import com.sd4.service.StyleService;
import java.awt.print.Pageable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import net.minidev.json.JSONObject;
import org.apache.commons.io.IOUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import static org.springframework.hateoas.mediatype.alps.Alps.doc;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
/**
 *
 * @author Saoir
 */
@RestController
public class BeerController {
    @Autowired
    private BeerService beerService;
    
    @Autowired
    private BreweryService breweryService;
    
    @Autowired
    private CategoryService categoryService;
    
    @Autowired
    private StyleService styleService;
    
    @GetMapping(value = "/beers", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<List<Beer>> getAll() {
        List<Beer> beers = beerService.findAll();
        if(beers.isEmpty()) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        } else {
            for(Beer b : beers) {
                Link selfBeerLink = linkTo(methodOn(BeerController.class).getOne(b.getId())).withSelfRel();
                b.add(selfBeerLink);
                Link beerDetailsLink = linkTo(methodOn(BeerController.class).getBeerDetails(b.getId())).withRel("details");
                b.add(beerDetailsLink);
            }
            return ResponseEntity.ok(beers);
        }
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
    
    @GetMapping(value = "/beers/{id}/details", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<JSONObject> getBeerDetails(@PathVariable long id) {
       Optional<Beer> o =  beerService.findOne(id);
       if (!o.isPresent()) 
            return new ResponseEntity(HttpStatus.NOT_FOUND);
       else{
           JSONObject response = new JSONObject();
           Beer b = o.orElse(new Beer());
           response.appendField("name", b.getName());
           response.appendField("description", b.getDescription());
           Optional<Brewery> ob = breweryService.findOne(b.getBrewery_id());
           Brewery brewery = ob.orElse(new Brewery());
           response.appendField("brewery_name", brewery.getName());
           return ResponseEntity.ok(response);
       }
    }
    
    @GetMapping(value = "/beers/{id}/{size}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> getBeerImage(@PathVariable long id, @PathVariable String size) throws IOException, URISyntaxException {
        Optional<Beer> o =  beerService.findOne(id);
        if(!o.isPresent()) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        } else {
            Beer beer = o.orElse(new Beer());
            byte[] content = null;
            if(size.equalsIgnoreCase("large")) {
                content = Files.readAllBytes(Path.of(this.getClass().getClassLoader().getResource("static/assets/images/large/" + beer.getImage()).toURI()));
            } else if(size.equalsIgnoreCase("thumbnail")) {
                content = Files.readAllBytes(Path.of(this.getClass().getClassLoader().getResource("static/assets/images/thumbs/" + beer.getImage()).toURI()));
            }
            if(content == null) {
                return new ResponseEntity(HttpStatus.BAD_REQUEST);
            } else {
                return ResponseEntity.ok(content);
            }
        }
    }
    
    @GetMapping(value = "/beers/images", produces = "application/zip")
    public ResponseEntity<byte[]> getAllBeerImages() throws FileNotFoundException, IOException, URISyntaxException {
        File largeImgsFolder = new File(this.getClass().getClassLoader().getResource("static/assets/images/large").toURI());
        File[] largeImgs = largeImgsFolder.listFiles();
        File smallImgsFolder = new File(this.getClass().getClassLoader().getResource("static/assets/images/thumbs").toURI());
        File[] smallImgs = smallImgsFolder.listFiles();
        FileOutputStream fos = new FileOutputStream(new File(this.getClass().getClassLoader().getResource("static/assets/images/all.zip").toURI()));
        ZipOutputStream zos = new ZipOutputStream(fos);
        for(File f : largeImgs) {
            zos.putNextEntry(new ZipEntry("large/" + f.getName()));
            FileInputStream fileInputStream = new FileInputStream(f);
            fileInputStream.close();
            zos.closeEntry();
        }
        for(File f : smallImgs) {
            zos.putNextEntry(new ZipEntry("thumbs/" + f.getName()));
            FileInputStream fileInputStream = new FileInputStream(f);
            IOUtils.copy(fileInputStream, zos);
            fileInputStream.close();
            zos.closeEntry();
        }
        zos.finish();
        zos.flush();
        IOUtils.closeQuietly(zos);
        fos.close();
        return ResponseEntity.ok(Files.readAllBytes(Path.of(this.getClass().getClassLoader().getResource("static/assets/images/all.zip").toURI())));
    }
    
    @GetMapping(value = "/beers/{id}/pdf", produces = "application/pdf")
    public ResponseEntity<byte[]> getBeerBrochure(@PathVariable long id) throws IOException, URISyntaxException {
           Optional<Beer> o =  beerService.findOne(id);
           if(!o.isPresent()) {
               return new ResponseEntity(HttpStatus.NOT_FOUND);
           } else {
                Beer beer = o.orElse(new Beer());
                Optional<Brewery> ob = breweryService.findOne(beer.getBrewery_id());
                Brewery brewery = ob.orElse(new Brewery());
                Optional<Category> oc = categoryService.findOne(beer.getCat_id());
                Category category = oc.orElse(new Category());
                Optional<Style> os = styleService.findOne(beer.getStyle_id());
                Style style = os.orElse(new Style());
                PDDocument beerBrochure= new PDDocument();  
                beerBrochure.addPage(new PDPage());
                PDPage page = beerBrochure.getPage(0);
                PDPageContentStream contentStream = new PDPageContentStream(beerBrochure, page);
                PDImageXObject beerImage = PDImageXObject.createFromFileByContent(new File(this.getClass().getClassLoader().getResource("static/assets/images/large/" + beer.getImage()).toURI()), beerBrochure);
                contentStream.drawImage(beerImage, 70, 250);
                contentStream.close();
                beerBrochure.addPage(new PDPage());
                page = beerBrochure.getPage(1);
                contentStream = new PDPageContentStream(beerBrochure, page);
                contentStream.beginText();
                contentStream.newLineAtOffset(25, 700);
                contentStream.setLeading(14.5f);
                contentStream.setFont(PDType1Font.TIMES_ROMAN, 12);
                contentStream.showText("Name: " + beer.getName());
                contentStream.newLine();
                contentStream.showText("ABV: " + beer.getAbv().toString());
                contentStream.newLine();
                char[] charArray = beer.getDescription().toCharArray();
                int numCommas = 0;
                contentStream.showText("Description: ");
                for(char c : charArray) {
                    contentStream.showText(String.valueOf(c));
                    if(c == '.') {
                        contentStream.newLine();
                    } else if(c == ',') {
                        numCommas++;
                        if(numCommas >= 2) {
                            contentStream.newLine();
                            numCommas = 0;
                        }
                    }
                }
                contentStream.newLine();
                DecimalFormat df = new DecimalFormat("#.##");
                contentStream.showText("Price: €" + df.format(beer.getSell_price()));
                contentStream.newLine();
                contentStream.showText("Brewery: " + brewery.getName());
                contentStream.newLine();
                contentStream.showText("Website: " + brewery.getWebsite());
                contentStream.newLine();
                contentStream.showText("Category: " + category.getCat_name());
                contentStream.newLine();
                contentStream.showText("Style: " + style.getStyle_name());
                contentStream.endText();
                contentStream.close();
                beerBrochure.save(new File(this.getClass().getClassLoader().getResource("static/assets/brochures/" + beer.getId() + ".pdf").toURI()));
                beerBrochure.close();
                return ResponseEntity.ok(Files.readAllBytes(Path.of(this.getClass().getClassLoader().getResource("static/assets/brochures/" + beer.getId() + ".pdf").toURI())));
           }
    }
    
    @GetMapping("/beers/count")
    public long getCount() {
        return beerService.count();
    }
    
    @DeleteMapping("/beers/{id}/delete")
    public ResponseEntity delete(@PathVariable long id) {
        beerService.deleteByID(id);
        return new ResponseEntity(HttpStatus.OK);
    }
    
    @PostMapping(value = "/beers/add", consumes = "application/json")
    public ResponseEntity add(@RequestBody Beer b) {
        beerService.saveBeer(b);
        return new ResponseEntity(HttpStatus.CREATED);
    }
    
    @PutMapping(value = "/beers/edit", consumes = "application/json")
    public ResponseEntity edit(@RequestBody Beer b) {
        if(getOne(b.getId()) == new ResponseEntity(HttpStatus.NOT_FOUND))
        {
            return new ResponseEntity(HttpStatus.FOUND);
        }
        beerService.saveBeer(b);
        return new ResponseEntity(HttpStatus.OK);
    }
}
