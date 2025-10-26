package com.example.climanow.fragment;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.climanow.R;
import com.example.climanow.viewmodel.SharedViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// Fragment que mostra o mapa com a cidade atual
// Usa Google Maps e geocoding pra colocar marcador na cidade
public class MapFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap; // Guarda a referência do mapa
    private String currentCity = "Dois Vizinhos"; // Cidade padrão
    private SharedViewModel sharedViewModel; // ViewModel compartilhado entre fragments e activity
    private final ExecutorService geocodeExecutor = Executors.newSingleThreadExecutor(); // Executor para rodar Geocoder em background e não travar UI

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false); // Cria a view do fragment

        // Pega o SupportMapFragment que está no XML
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            // Configura o mapa para chamar onMapReady quando estiver pronto
            mapFragment.getMapAsync(this);
        }

        // Inicializa o ViewModel compartilhado
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        // Observa mudanças de cidade
        sharedViewModel.getCity().observe(getViewLifecycleOwner(), city -> {
            if (city == null || city.trim().isEmpty()) return;
            // Atualiza o mapa usando geocoding em background
            updateCityInternal(city.trim());
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Encerra o executor quando o fragment for destruído para não vazar memória
        geocodeExecutor.shutdownNow();
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        // Recebe a referência do mapa
        mMap = googleMap;
        // Opcional: habilitar controles de UI
        mMap.getUiSettings().setZoomControlsEnabled(true);
        // Se já tivermos uma cidade definida (padrão ou por updateCity), centraliza o mapa
        if (currentCity != null && !currentCity.trim().isEmpty()) {
            updateCityInternal(currentCity);
        }
    }

    // Atualiza a cidade do mapa, pode ser chamado pela Activity ou outro fragment
    public void updateCity(String newCity) {
        if (newCity == null) return;
        currentCity = newCity.trim();
        // publica também no ViewModel para que outros observadores fiquem sincronizados (opcional)
        if (sharedViewModel != null) {
            sharedViewModel.setCity(currentCity);
        }
        // Atualiza o mapa (em background)
        updateCityInternal(currentCity);
    }

    // Lógica interna que faz geocoding em background e atualiza o mapa na UI thread
    private void updateCityInternal(String cityName) {
        if (cityName == null || cityName.trim().isEmpty()) return;

        geocodeExecutor.execute(() -> {
            if (getContext() == null) return;
            // Cria Geocoder para transformar nome da cidade em latitude/longitude
            Geocoder geocoder = new Geocoder(requireContext(), Locale.getDefault());
            try {
                // Pega apenas o primeiro resultado da busca
                List<Address> addresses = geocoder.getFromLocationName(cityName, 1);
                if (addresses != null && !addresses.isEmpty()) {
                    Address address = addresses.get(0);
                    final LatLng cityLocation = new LatLng(address.getLatitude(), address.getLongitude());
                    // Atualiza a UI thread para mexer no mapa
                    requireActivity().runOnUiThread(() -> {
                        if (mMap != null) {
                            // Limpa marcadores antigos
                            mMap.clear();
                            // Adiciona marcador na cidade
                            mMap.addMarker(new MarkerOptions().position(cityLocation).title(cityName));
                            // Move a câmera para a cidade
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(cityLocation, 12f));
                        }
                    });
                } else {
                    // Mostra mensagem se não encontrar a cidade
                    requireActivity().runOnUiThread(() ->
                            Toast.makeText(getContext(), "Cidade não encontrada no mapa: " + cityName, Toast.LENGTH_SHORT).show()
                    );
                }
            } catch (IOException e) {
                e.printStackTrace();
                // Mostra mensagem de erro
                requireActivity().runOnUiThread(() ->
                        Toast.makeText(getContext(), "Erro ao buscar localização: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
            }
        });
    }
}