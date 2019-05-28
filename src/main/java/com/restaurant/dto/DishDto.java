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
   private int callories;

   @ApiModelProperty
   private int weight;

   @ApiModelProperty
   private List<Long> proteins;

   @ApiModelProperty
   private List<Long> allergens;

   @ApiModelProperty
   private List<IngredientDto> ingredients;

   private int fiber;
   @ApiModelProperty
   private int fats;
   @ApiModelProperty
   private int saturatedFats;
   @ApiModelProperty
   private int carbohydrates;
   @ApiModelProperty
   private int sugar;
   @ApiModelProperty
   private int cellulose;
   @ApiModelProperty
   private int salt;
   @ApiModelProperty
   private String link;

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

   public int getCallories() {
      return callories;
   }

   public void setCallories(int callories) {
      this.callories = callories;
   }

   public int getFiber() {
      return fiber;
   }

   public void setFiber(int fiber) {
      this.fiber = fiber;
   }

   public int getFats() {
      return fats;
   }

   public void setFats(int fats) {
      this.fats = fats;
   }

   public int getSaturatedFats() {
      return saturatedFats;
   }

   public void setSaturatedFats(int saturatedFats) {
      this.saturatedFats = saturatedFats;
   }

   public int getCarbohydrates() {
      return carbohydrates;
   }

   public void setCarbohydrates(int carbohydrates) {
      this.carbohydrates = carbohydrates;
   }

   public int getSugar() {
      return sugar;
   }

   public void setSugar(int sugar) {
      this.sugar = sugar;
   }

   public int getCellulose() {
      return cellulose;
   }

   public void setCellulose(int cellulose) {
      this.cellulose = cellulose;
   }

   public int getSalt() {
      return salt;
   }

   public void setSalt(int salt) {
      this.salt = salt;
   }

   public String getLink() {
      return link;
   }

   public void setLink(String link) {
      this.link = link;
   }
}
