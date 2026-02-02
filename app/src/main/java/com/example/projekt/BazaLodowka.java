package com.example.projekt;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Produkt.class}, version = 2, exportSchema = false)
public abstract class BazaLodowka extends RoomDatabase {
    public abstract ProduktDao produktDao();
}