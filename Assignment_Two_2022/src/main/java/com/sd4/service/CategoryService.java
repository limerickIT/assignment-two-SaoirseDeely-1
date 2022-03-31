/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sd4.service;

import com.sd4.model.Category;
import com.sd4.repository.CategoryRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Saoir
 */
@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepo;
    
    public Optional<Category> findOne(Long id) {
        return categoryRepo.findById(id);
    }

    public List<Category> findAll() {
        return (List<Category>) categoryRepo.findAll();
    }

    public long count() {
        return categoryRepo.count();
    }

    public void deleteByID(long categoryID) {
        categoryRepo.deleteById(categoryID);
    }

    public void saveBeer(Category c) {
        categoryRepo.save(c);
    }
    
    public void updateBeer(Category c) {
        if(categoryRepo.existsById(c.getId())) {
            categoryRepo.save(c);
        }
    }
}
