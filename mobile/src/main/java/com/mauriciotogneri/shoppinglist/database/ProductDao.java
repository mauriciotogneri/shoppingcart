package com.mauriciotogneri.shoppinglist.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.content.Context;

import com.mauriciotogneri.shoppinglist.model.Product;

import java.util.List;

@Dao
public interface ProductDao
{
    @Query("SELECT * FROM Product WHERE inCart")
    List<Product> inCart();

    @Query("SELECT * FROM Product WHERE category=:category")
    List<Product> byCategory(String category);

    @Query("UPDATE Product SET category=:newCategory WHERE category=:oldCategory")
    void rename(String oldCategory, String newCategory);

    @Query("UPDATE Product SET selected=:selected WHERE id=:id")
    void setSelection(Integer id, Boolean selected);

    @Query("UPDATE Product SET inCart=:inCart WHERE id=:id")
    void moveToCart(Integer id, Boolean inCart);

    @Insert
    void insert(Product... products);

    @Delete
    void delete(Product product);

    static ProductDao instance(Context context)
    {
        return AppDatabase.instance(context).productDao();
    }
}