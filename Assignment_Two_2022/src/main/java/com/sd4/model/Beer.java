/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sd4.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Id;
import javax.persistence.Lob;
import javax.validation.constraints.NotBlank;
import org.springframework.hateoas.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class Beer extends RepresentationModel<Beer> implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @NotBlank(message = "Beer Brewery ID must not be empty")
    private long brewery_id;
    @NotBlank(message = "Beer Name must not be empty")
    private String name;
    @NotBlank(message = "Beer Category ID must not be empty")
    private long cat_id;
    @NotBlank(message = "Beer Style ID must not be empty")
    private long style_id;
    @NotBlank(message = "Beer ABV must not be empty")
    private Double abv;
    @NotBlank(message = "Beer IBU must not be empty")
    private Double ibu;
    @NotBlank(message = "Beer SRM must not be empty")
    private Double srm;
    
    @NotBlank(message = "Beer Description must not be empty")
    @Lob 
    private String description;
    private Integer add_user;

    @Temporal(TemporalType.TIMESTAMP)
    private Date last_mod;

    @NotBlank(message = "Beer Image must not be empty")
    private String image;
    @NotBlank(message = "Beer Buy Price must not be empty")
    private Double buy_price;
    @NotBlank(message = "Beer Sell Price must not be empty")
    private Double sell_price;

}
