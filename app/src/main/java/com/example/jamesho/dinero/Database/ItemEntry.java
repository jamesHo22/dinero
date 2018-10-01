package com.example.jamesho.dinero.Database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by jamesho on 2018-09-30.
 */

@Entity(tableName = "ItemsTable")
public class ItemEntry {

    @PrimaryKey(autoGenerate = true)
    public int id;
    public int restaurant_id;
    public int owner_id;
    public String course;
    public String description;
    public String name;
    public String price;

    @Ignore
    public ItemEntry(int restaurant_id, int owner_id, String course, String description, String name, String price) {
        this.restaurant_id = restaurant_id;
        this.owner_id = owner_id;
        this.name = name;
        this.course = course;
        this.description = description;
        this.price = price;
    }

    public ItemEntry(int id, int restaurant_id, int owner_id, String course, String description, String name, String price) {
        this.id = id;
        this.restaurant_id = restaurant_id;
        this.owner_id = owner_id;
        this.name = name;
        this.course = course;
        this.description = description;
        this.price = price;
    }
    public int getId() {
        return id;
    }

    public int getRestaurant_id() {
        return restaurant_id;
    }

    public int getOwner_id() {
        return owner_id;
    }

    public String getName() {
        return name;
    }

    public String getCourse() {
        return course;
    }

    public String getDescription() {
        return description;
    }

    public String getPrice() {
        return price;
    }
}
