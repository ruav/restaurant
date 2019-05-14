package com.restaurant.entity;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "dish",uniqueConstraints=@UniqueConstraint(columnNames= {"category_id","name"}))
public class Dish implements Data{

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column
    private long id;

    @Column(name = "category_id", nullable = false)
    private long categoryId;

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


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
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

    @Override
    public String toString() {
        return "Dish{" +
                "categoryId=" + categoryId +
                ", name='" + name + '\'' +
                ", weight=" + weight +
                ", photos=" + photos +
                ", ingredients=" + ingredients +
                ", price=" + price +
                '}';
    }
}
