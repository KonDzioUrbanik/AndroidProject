package com.example.projekt;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import com.example.projekt.databinding.ActivityDetailsBinding;
import androidx.appcompat.app.AppCompatActivity;

public class DetailsActivity extends AppCompatActivity {

    private ActivityDetailsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        String nazwa = getIntent().getStringExtra("NAZWA");
        String polka = getIntent().getStringExtra("POLKA");

        binding.tvDetaleNazwa.setText(nazwa);
        binding.tvDetalePolka.setText("Leży na półce: " + polka);


        ObjectAnimator animator = ObjectAnimator.ofFloat(binding.ivIkona, "rotation", 0f, 360f);
        animator.setDuration(1000);
        animator.start();

        binding.btnUdostepnij.setOnClickListener(v -> {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "Kup: " + nazwa);
            sendIntent.setType("text/plain");

            Intent shareIntent = Intent.createChooser(sendIntent, null);
            startActivity(shareIntent);
        });
    }
}