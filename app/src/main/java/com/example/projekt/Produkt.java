package com.example.projekt;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tabela_produktow")
public class Produkt {
    @PrimaryKey(autoGenerate = true)
    public int uid;

    @ColumnInfo(name = "nazwa")
    public String nazwa;

    @ColumnInfo(name = "polka")
    public String polka;


    public Produkt() {}


    public Produkt(String nazwa, String polka) {
        this.nazwa = nazwa;
        this.polka = polka;
    }
}