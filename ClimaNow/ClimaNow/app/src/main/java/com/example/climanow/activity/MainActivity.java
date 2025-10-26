package com.example.climanow.activity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.climanow.R;
import com.example.climanow.adapter.TabsAdapter;
import com.example.climanow.fragment.MapFragment;
import com.example.climanow.fragment.WeatherFragment;
import com.example.climanow.viewmodel.SharedViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

// Activity principal do app, controla as telas de Clima e Mapa
// Configura ViewPager e abas, botão flutuante e scanner de QR Code

public class MainActivity extends AppCompatActivity {

    private TabsAdapter tabsAdapter;
    private ViewPager2 viewPager;
    private TabLayout tabLayout;

    // Esse é o ViewModel compartilhado pra todos os fragments dessa Activity
    private SharedViewModel sharedViewModel;

    // Launcher que vai pedir permissão de câmera e depois roda o QR scanner se liberar
    private final ActivityResultLauncher<String> cameraPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    scanQrCode(); // Se liberou a câmera, abre o scanner
                } else {
                    Toast.makeText(this, "Permissão da Câmera negada!", Toast.LENGTH_SHORT).show();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializa o SharedViewModel
        sharedViewModel = new ViewModelProvider(this).get(SharedViewModel.class);

        // Configura a Toolbar bonitinha lá em cima
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

// - Pega componentes da tela (ViewPager, TabLayout, FAB)
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);
        FloatingActionButton fab = findViewById(R.id.fab);

        // Configura o adapter das abas (Clima e Mapa)
        tabsAdapter = new TabsAdapter(this);
        viewPager.setAdapter(tabsAdapter);

        // Liga as tabs com o viewpager pra trocar de tela deslizando ou clicando
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            if (position == 0) {
                tab.setText("Clima");
            } else {
                tab.setText("Mapa");
            }
        }).attach();

        // Quando clicar no botão flutuante, pede permissão e abre scanner se tiver liberado
        fab.setOnClickListener(view -> requestCameraPermissionAndScan());
    }

    // Checa se a câmera já tá liberada, se sim abre scanner, se não pede permissão
    private void requestCameraPermissionAndScan() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            scanQrCode();
        } else {
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA);
        }
    }

    // Configura scanner de QR Code: prompt, bip, orientação travada, activity do scanner
    private void scanQrCode() {
        ScanOptions options = new ScanOptions();
        options.setPrompt("Aponte para o QR Code de uma cidade"); // mensagemzinha pro usuário
        options.setBeepEnabled(true); // faz um bip quando escaneia
        options.setOrientationLocked(true); // não deixa rotacionar a tela
        options.setCaptureActivity(com.journeyapps.barcodescanner.CaptureActivity.class);
        barcodeLauncher.launch(options);
    }

    // Resultado do QR Code:
    private final ActivityResultLauncher<ScanOptions> barcodeLauncher = registerForActivityResult(new ScanContract(),
            result -> {
                if (result.getContents() != null) {
                    String scannedCity = result.getContents().trim();
                    Toast.makeText(this, "Cidade escaneada: " + scannedCity, Toast.LENGTH_SHORT).show();

                    // Joga a cidade pro SharedViewModel, os fragments que tão observando vão receber
                    sharedViewModel.setCity(scannedCity);

                    // Atualiza os fragments diretamente também (opcional, pode só usar o ViewModel)
                    updateFragmentsWithNewCity(scannedCity);
                }
            });

    private void updateFragmentsWithNewCity(String city) {
        // Passa por todas as fragments abertas e atualiza elas
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            if (fragment instanceof WeatherFragment) {
                ((WeatherFragment) fragment).fetchWeatherData(city); // pede pro fragment de clima atualizar
            } else if (fragment instanceof MapFragment) {
                ((MapFragment) fragment).updateCity(city); // pede pro fragment do mapa atualizar
            }
        }

        // Vai pra aba de Clima pra mostrar o resultado pro usuário
        viewPager.setCurrentItem(0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Coloca o menu na AppBar
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Se clicar no "Sobre", abre a AboutActivity
        if (item.getItemId() == R.id.action_about) {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
