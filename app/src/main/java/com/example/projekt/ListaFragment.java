package com.example.projekt;

import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.projekt.databinding.FragmentListaBinding;

import java.util.ArrayList; // Dodano import dla ArrayList

public class ListaFragment extends Fragment {

    private FragmentListaBinding binding;
    private LodowkaViewModel viewModel;
    private LodowkaAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Użycie ViewBinding do napompowania widoku
        binding = FragmentListaBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Inicjalizacja ViewModel (Instrukcja 14)
        viewModel = new ViewModelProvider(requireActivity()).get(LodowkaViewModel.class);

        // Inicjalizacja Adaptera i listy (Instrukcja 12)
        adapter = new LodowkaAdapter(requireContext(), new ArrayList<>());
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(adapter);

        // Obserwowanie zmian w bazie danych (LiveData)
        viewModel.getProdukty().observe(getViewLifecycleOwner(), produkty -> {
            adapter.setDane(produkty);
        });

        // KLIKNIĘCIE 1: Otwieranie szczegółów (Instrukcja 4)
        adapter.setOnItemClickListener(produkt -> {
            Intent intent = new Intent(requireContext(), DetailsActivity.class);
            intent.putExtra("NAZWA", produkt.nazwa);
            intent.putExtra("POLKA", produkt.polka);
            startActivity(intent);
        });

        // KLIKNIĘCIE 2: Usuwanie produktu (NOWOŚĆ)
        adapter.setOnDeleteClickListener(produkt -> {
            // Wywołujemy metodę usuwania z ViewModelu
            viewModel.usunProdukt(produkt);
        });

        // Obsługa przycisku dodawania (FAB)
        binding.fabDodaj.setOnClickListener(v -> pokazDialogDodawania());

        // Obsługa przycisku ustawień
        binding.btnUstawienia.setOnClickListener(v -> {
            startActivity(new Intent(requireContext(), SettingsActivity.class));
        });

        // Utworzenie kanału powiadomień (wymagane dla Androida 8+)
        createNotificationChannel();
    }

    // Metoda wyświetlająca Dialog (Instrukcja 10) ze Spinnerem (Instrukcja 13)
    private void pokazDialogDodawania() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Nowy produkt");

        // Własny layout dialogu
        View view = getLayoutInflater().inflate(R.layout.dialog_dodaj, null);
        final EditText etNazwa = view.findViewById(R.id.etNazwaDialog);
        final Spinner spinnerPolka = view.findViewById(R.id.spinnerPolka);

        // Konfiguracja Spinnera
        String[] polki = {"Górna", "Środkowa", "Dolna", "Szuflada"};
        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item, polki);
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPolka.setAdapter(adapterSpinner);

        builder.setView(view);

        builder.setPositiveButton("Dodaj", (dialog, which) -> {
            String nazwa = etNazwa.getText().toString();
            String polka = spinnerPolka.getSelectedItem().toString();

            // Dodanie do bazy
            viewModel.dodajProdukt(nazwa, polka);

            // Wysłanie powiadomienia (jeśli włączone w ustawieniach)
            wyslijPowiadomienie(nazwa);
        });
        builder.setNegativeButton("Anuluj", null);
        builder.show();
    }

    // Obsługa powiadomień (Instrukcja 11)
    private void wyslijPowiadomienie(String nazwaProduktu) {
        // Sprawdź czy użytkownik chce powiadomienia (SharedPreferences - Instrukcja 6)
        SharedPreferences prefs = requireContext().getSharedPreferences("UstawieniaLodowki", Context.MODE_PRIVATE);
        boolean czyPowiadamiac = prefs.getBoolean("powiadomienia", true);

        if (!czyPowiadamiac) return;

        // Sprawdzenie uprawnień (Android 13+)
        if (ActivityCompat.checkSelfPermission(requireContext(), android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // W prawdziwej aplikacji tutaj poprosilibyśmy o uprawnienia
            return;
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(requireContext(), "LODOWKA_CHANNEL")
                .setSmallIcon(android.R.drawable.ic_input_add)
                .setContentTitle("Dodano produkt")
                .setContentText("Włożyłeś do lodówki: " + nazwaProduktu)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat.from(requireContext()).notify(1, builder.build());
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("LODOWKA_CHANNEL", "Powiadomienia Lodówki", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = requireContext().getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }
}