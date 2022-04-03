/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sd4.model;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author Alan.Ryan
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class Breweries_Geocode implements Serializable {
    
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   long id ;
   @NotBlank(message = "Breweries Geocode Brewery ID must not be empty")
   long brewery_id;
   @NotBlank(message = "Breweries Geocode Latitude must not be empty")
   Double latitude;
   @NotBlank(message = "Breweries Geocode Longitude must not be empty")
   Double longitude;
    
}
