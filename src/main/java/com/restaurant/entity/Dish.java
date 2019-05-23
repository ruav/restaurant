package com.restaurant.entity;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "dish",uniqueConstraints=@UniqueConstraint(columnNames= {"subcategory_id","name"}))
public class Dish implements Data{

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

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name="dish_photo",
            joinColumns = @JoinColumn(name = "dish_id"),
            inverseJoinColumns = @JoinColumn(name = "photo_id")

    )
    private Set<Photo> photos;

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
    private int callories;

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

    public Set<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(Set<Photo> photos) {
        this.photos = photos;
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

    public int getCallories() {
        return callories;
    }

    public void setCallories(int callories) {
        this.callories = callories;
    }

    @Override
    public String toString() {
        return "Dish{" +
                "subcategoryId=" + subCategoryId +
                ", name='" + name + '\'' +
                ", weight=" + weight +
                ", photos=" + photos +
                ", callories=" + callories +
                ", ingredients=" + ingredients +
                ", price=" + price +
                '}';
    }
}
