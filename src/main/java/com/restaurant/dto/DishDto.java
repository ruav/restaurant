package com.restaurant.dto;

import java.io.Serializable;
import java.util.List;

public class DishDto implements Serializable {

   private long id;
   private String name;
   private String photo;
   private float price;
   private int weight;
   private List<ProteinDto> proteins;
   private List<AllergenDto> allergens;
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

   public List<ProteinDto> getProteins() {
      return proteins;
   }

   public void setProteins(List<ProteinDto> proteins) {
      this.proteins = proteins;
   }

   public List<AllergenDto> getAllergens() {
      return allergens;
   }

   public void setAllergens(List<AllergenDto> allergens) {
      this.allergens = allergens;
   }

   public List<IngredientDto> getIngredients() {
      return ingredients;
   }

   public void setIngredients(List<IngredientDto> ingredients) {
      this.ingredients = ingredients;
   }
}
