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

        // POPRAWKA: Dodano .fallbackToDestructiveMigration()
        // To naprawia błąd, gdy na telefonie jest stara wersja bazy i blokuje dodawanie
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
        if (nazwa == null || nazwa.isEmpty()) return; // Zabezpieczenie przed pustym

        CompletableFuture.runAsync(() -> {
            // Dodawanie w tle
            db.produktDao().dodajProdukt(new Produkt(nazwa, polka));
        }).thenRun(() -> {
            // Po dodaniu od razu odśwież
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
                    // postValue wysyła dane z tła do głównego wątku (UI)
                    produktyLive.postValue(lista);
                });
    }
}