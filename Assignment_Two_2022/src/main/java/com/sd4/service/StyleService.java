/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sd4.service;

import com.sd4.model.Style;
import com.sd4.repository.StyleRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Saoir
 */
@Service
public class StyleService {
    @Autowired
    private StyleRepository styleRepo;
    
    public Optional<Style> findOne(Long id) {
        return styleRepo.findById(id);
    }

    public List<Style> findAll() {
        return (List<Style>) styleRepo.findAll();
    }

    public long count() {
        return styleRepo.count();
    }

    public void deleteByID(long styleID) {
        styleRepo.deleteById(styleID);
    }

    public void saveBeer(Style s) {
        styleRepo.save(s);
    }
    
    public void updateBeer(Style s) {
        if(styleRepo.existsById(s.getId())) {
            styleRepo.save(s);
        }
    }
}
