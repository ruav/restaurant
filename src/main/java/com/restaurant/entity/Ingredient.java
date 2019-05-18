package com.restaurant.entity;

import javax.persistence.*;

@Entity
@Table(name="ingredient")
public class Ingredient implements Data {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column
    private long id;

    @Column(unique = true, nullable = false)
    private String name;

    @OneToOne()
    @JoinTable(
            name="ingredientlogo",
            joinColumns = @JoinColumn(name = "ingredient_id"),
            inverseJoinColumns = @JoinColumn(name = "icon_id")

    )
    private Icon icon;

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

    public Icon getIcon() {
        return icon;
    }

    public void setIcon(Icon icon) {
        this.icon = icon;
    }

    @Override
    public String toString() {
        return "Ingredient{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", icon=" + icon +
                '}';
    }
}
