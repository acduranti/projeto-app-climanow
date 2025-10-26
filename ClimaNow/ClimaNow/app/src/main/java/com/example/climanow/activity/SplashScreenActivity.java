package com.example.climanow.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.example.climanow.R;

// Tela de entrada do app (Splash Screen)
public class SplashScreenActivity extends AppCompatActivity {

    private static final int SPLASH_TIME_OUT = 3000; // Mostra o "logo" por 3 segundos

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        // Handler para atrasar a transição para a MainActivity
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Intent i = new Intent(SplashScreenActivity.this, MainActivity.class);
            startActivity(i);
            // Depois abre MainActivity e fecha essa tela pra não voltar pra ela
            finish();
        }, SPLASH_TIME_OUT);
    }
}
