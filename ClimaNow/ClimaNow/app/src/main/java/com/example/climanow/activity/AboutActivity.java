package com.example.climanow.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.climanow.R;

// Activity que mostra a tela "Sobre" do app
// Só configura a interface quando a Activity é criada
// layout activity_about é carregado aqui

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
    }
}
