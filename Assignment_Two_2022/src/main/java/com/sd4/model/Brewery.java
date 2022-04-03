/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sd4.model;

import java.io.Serializable;
import java.sql.Clob;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.hateoas.RepresentationModel;

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
public class Brewery extends RepresentationModel<Beer> implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotBlank(message = "Brewery Name must not be empty")
    private String name;
    @NotBlank(message = "Brewery Address must not be empty")
    private String address1;
    private String address2;
    @NotBlank(message = "Brewery City must not be empty")
    private String city;
    @NotBlank(message = "Brewery State must not be empty")
    private String state;
    @NotBlank(message = "Brewery Code must not be empty")
    private String code;
    @NotBlank(message = "Brewery Country must not be empty")
    private String country;
    @NotBlank(message = "Brewery Phone must not be empty")
    private String phone;
    @NotBlank(message = "Brewery Website must not be empty")
    private String website;
    @NotBlank(message = "Brewery Image must not be empty")
    private String image;
    
    @Lob
    @NotBlank(message = "Brewery Description must not be empty")
    private String description;
    
    private Integer add_user;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date last_mod;
    
    @NotBlank(message = "Brewery Credit Limit must not be empty")
    private Double credit_limit;
    @NotBlank(message = "Brewery Email must not be empty")
    private String email;
}
