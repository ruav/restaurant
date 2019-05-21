package com.restaurant.dto;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

public class DishDto implements Serializable {

   @ApiModelProperty
   private long id;

   @ApiModelProperty
   private String name;

   @ApiModelProperty
   private String photo;

   @ApiModelProperty
   private float price;

   @ApiModelProperty
   private int weight;

   @ApiModelProperty
   private List<Long> proteins;

   @ApiModelProperty
   private List<Long> allergens;

   @ApiModelProperty
   private List<IngredientDto> ingredients;

   public long getId() {
      return id;
   }

   public void setId(long id) {
      this.id = id;
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getPhoto() {
      return photo;
   }

   public void setPhoto(String photo) {
      this.photo = photo;
   }

   public float getPrice() {
      return price;
   }

   public void setPrice(float price) {
      this.price = price;
   }

   public int getWeight() {
      return weight;
   }

   public void setWeight(int weight) {
      this.weight = weight;
   }

   public List<Long> getProteins() {
      return proteins;
   }

   public void setProteins(List<Long> proteins) {
      this.proteins = proteins;
   }

   public List<Long> getAllergens() {
      return allergens;
   }

   public void setAllergens(List<Long> allergens) {
      this.allergens = allergens;
   }

   public List<IngredientDto> getIngredients() {
      return ingredients;
   }

   public void setIngredients(List<IngredientDto> ingredients) {
      this.ingredients = ingredients;
   }
}
