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
   private String description;

   @ApiModelProperty
   private String photo;

   @ApiModelProperty
   private Float price;

   @ApiModelProperty
   private Integer calories;

   @ApiModelProperty
   private Integer weight;

   @ApiModelProperty
   private List<Long> proteins;

   @ApiModelProperty
   private List<Long> allergens;

   @ApiModelProperty
   private List<IngredientDto> ingredients;

   @ApiModelProperty
   private Integer fiber;
   @ApiModelProperty
   private Integer fats;
   @ApiModelProperty
   private Integer saturatedFats;
   @ApiModelProperty
   private Integer carbohydrates;
   @ApiModelProperty
   private Integer sugar;
   @ApiModelProperty
   private Integer cellulose;
   @ApiModelProperty
   private Integer salt;
   @ApiModelProperty
   private String video;

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

   public String getDescription() { return description; }

   public void setDescription(String description) { this.description = description; }

   public String getPhoto() {
      return photo;
   }

   public void setPhoto(String photo) {
      this.photo = photo;
   }

   public Float getPrice() {
      return price;
   }

   public void setPrice(Float price) {
      this.price = price;
   }

   public Integer getCalories() {
      return calories;
   }

   public void setCalories(Integer calories) {
      this.calories = calories;
   }

   public Integer getWeight() {
      return weight;
   }

   public void setWeight(Integer weight) {
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

   public Integer getFiber() {
      return fiber;
   }

   public void setFiber(Integer fiber) {
      this.fiber = fiber;
   }

   public Integer getFats() {
      return fats;
   }

   public void setFats(Integer fats) {
      this.fats = fats;
   }

   public Integer getSaturatedFats() {
      return saturatedFats;
   }

   public void setSaturatedFats(Integer saturatedFats) {
      this.saturatedFats = saturatedFats;
   }

   public Integer getCarbohydrates() {
      return carbohydrates;
   }

   public void setCarbohydrates(Integer carbohydrates) {
      this.carbohydrates = carbohydrates;
   }

   public Integer getSugar() {
      return sugar;
   }

   public void setSugar(Integer sugar) {
      this.sugar = sugar;
   }

   public Integer getCellulose() {
      return cellulose;
   }

   public void setCellulose(Integer cellulose) {
      this.cellulose = cellulose;
   }

   public Integer getSalt() {
      return salt;
   }

   public void setSalt(Integer salt) {
      this.salt = salt;
   }

   public String getVideo() {
      return video;
   }

   public void setVideo(String video) {
      this.video = video;
   }
}
