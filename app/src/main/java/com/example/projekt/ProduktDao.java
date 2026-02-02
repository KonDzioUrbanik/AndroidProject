package com.example.projekt;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface ProduktDao {
    @Query("SELECT * FROM tabela_produktow ORDER BY uid DESC")
    List<Produkt> pobierzWszystkie();

    @Insert
    void dodajProdukt(Produkt produkt);

    @Delete
    void usunProdukt(Produkt produkt);
}