package com.restaurant.entity;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "dish",uniqueConstraints=@UniqueConstraint(columnNames= {"subcategory_id","name"}))
public class Dish implements DataWithLogo<Photo>{

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column
    private long id;

    @Column(name = "subcategory_id", nullable = false)
    private long subCategoryId;

    @Column(nullable = false)
    private String name;

    @Column
    private int weight;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name="dish_photo",
            joinColumns = @JoinColumn(name = "dish_id"),
            inverseJoinColumns = @JoinColumn(name = "photo_id")

    )
    private Photo logo;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name="dish_ingredients",
            joinColumns = @JoinColumn(name = "dish_id"),
            inverseJoinColumns = @JoinColumn(name = "ingredient_id")

    )
    private Set<Ingredient> ingredients;

    @Column
    private float price;

    @Column
    private int calories;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name="dish_proteins",
            joinColumns = @JoinColumn(name = "dish_id"),
            inverseJoinColumns = @JoinColumn(name = "protein_id")

    )
    private Set<Protein> proteins;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name="dish_allergens",
            joinColumns = @JoinColumn(name = "dish_id"),
            inverseJoinColumns = @JoinColumn(name = "allergen_id")

    )
    private Set<Allergen> allergens;

    @Column
    private Integer fiber;
    @Column
    private Integer fats;
    @Column
    private Integer saturatedFats;
    @Column
    private Integer carbohydrates;
    @Column
    private Integer sugar;
    @Column
    private Integer cellulose;
    @Column
    private Integer salt;
    @Column
    private String video;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getSubCategoryId() {
        return subCategoryId;
    }

    public void setSubCategoryId(long subCategoryId) {
        this.subCategoryId = subCategoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public Set<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(Set<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    @Override
    public Photo getLogo() {
        return logo;
    }

    @Override
    public void setLogo(Photo logo) {
        this.logo = logo;
    }

    public Set<Protein> getProteins() {
        return proteins;
    }

    public void setProteins(Set<Protein> proteins) {
        this.proteins = proteins;
    }

    public Set<Allergen> getAllergens() {
        return allergens;
    }

    public void setAllergens(Set<Allergen> allergens) {
        this.allergens = allergens;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
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

    @Override
    public String toString() {
        return "Dish{" +
                "subcategoryId=" + subCategoryId +
                ", name='" + name + '\'' +
                ", weight=" + weight +
                ", logo=" + logo +
                ", callories=" + calories +
                ", ingredients=" + ingredients +
                ", price=" + price +
                '}';
    }
}
