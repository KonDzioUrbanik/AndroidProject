package com.example.projekt;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Room;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class LodowkaViewModel extends AndroidViewModel {

    private BazaLodowka db;
    private MutableLiveData<List<Produkt>> produktyLive = new MutableLiveData<>();

    public LodowkaViewModel(@NonNull Application application) {
        super(application);


        db = Room.databaseBuilder(application,
                        BazaLodowka.class, "baza-lodowka")
                .fallbackToDestructiveMigration()
                .build();

        odswiezListe();
    }

    public LiveData<List<Produkt>> getProdukty() {
        return produktyLive;
    }

    public void dodajProdukt(String nazwa, String polka) {
        if (nazwa == null || nazwa.isEmpty()) return;

        CompletableFuture.runAsync(() -> {

            db.produktDao().dodajProdukt(new Produkt(nazwa, polka));
        }).thenRun(() -> {

            odswiezListe();
        });
    }

    public void usunProdukt(Produkt p) {
        CompletableFuture.runAsync(() -> {
            db.produktDao().usunProdukt(p);
        }).thenRun(this::odswiezListe);
    }

    private void odswiezListe() {
        CompletableFuture.supplyAsync(() -> db.produktDao().pobierzWszystkie())
                .thenAccept(lista -> {

                    produktyLive.postValue(lista);
                });
    }
}