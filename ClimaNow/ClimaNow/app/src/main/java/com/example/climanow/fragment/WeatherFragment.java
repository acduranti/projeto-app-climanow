package com.example.climanow.fragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.lifecycle.ViewModelProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.climanow.R;
import com.example.climanow.adapter.WeatherAdapter;
import com.example.climanow.model.Forecast;
import com.example.climanow.model.WeatherResponse;
import com.example.climanow.viewmodel.SharedViewModel;
import com.google.gson.Gson;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

// Fragment que exibe o tempo atual e previsão da cidade
public class WeatherFragment extends Fragment {

    private RecyclerView recyclerView; // Lista de previsão do tempo
    private WeatherAdapter weatherAdapter; // Adapter da lista
    private List<Forecast> forecastList; // Lista de previsões
    private RequestQueue requestQueue; // Para requisições HTTP

    // TextViews para mostrar informações do tempo atual
    private TextView textViewCityName, textViewCurrentTemp, textViewCurrentDescription;

    private final String API_KEY = "8d9f54b3"; // Chave da API de clima

    // ViewModel compartilhado com outros fragments/activity
    private SharedViewModel sharedViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Cria a view do fragment
        View view = inflater.inflate(R.layout.fragment_weather, container, false);

        // Conecta as TextViews da interface
        textViewCityName = view.findViewById(R.id.textViewCityName);
        textViewCurrentTemp = view.findViewById(R.id.textViewCurrentTemp);
        textViewCurrentDescription = view.findViewById(R.id.textViewCurrentDescription);

        // Configura o RecyclerView para mostrar a previsão
        recyclerView = view.findViewById(R.id.recyclerViewWeather);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext())); // Lista vertical
        forecastList = new ArrayList<>();
        weatherAdapter = new WeatherAdapter(forecastList);
        recyclerView.setAdapter(weatherAdapter);

        // Inicializa a fila de requisições
        requestQueue = Volley.newRequestQueue(requireContext());

        // Inicializa o SharedViewModel (compartilhado com a Activity)
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        // Observa mudanças na cidade para atualizar automaticamente
        sharedViewModel.getCity().observe(getViewLifecycleOwner(), city -> {
            if (city != null && !city.trim().isEmpty()) {
                fetchWeatherData(city.trim()); // Puxa dados do clima quando a cidade muda
            }
        });

        // Carrega dados iniciais da cidade padrão
        fetchWeatherData("Dois Vizinhos");

        return view;
    }

    // Metodo que busca dados do clima via API
    public void fetchWeatherData(String city) {
        if (city == null || city.trim().isEmpty()) return;

        try {
            // Codifica o nome da cidade para URL
            String encodedCity = URLEncoder.encode(city, "UTF-8");
            String url = "https://api.hgbrasil.com/weather?key=" + API_KEY + "&city_name=" + encodedCity;

            Log.d("WeatherFragment", "Requesting weather for: " + city + " -> " + url);

            // Cria a requisição HTTP GET
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    response -> {
                        try {
                            // Converte JSON em objeto Java usando Gson
                            Gson gson = new Gson();
                            WeatherResponse weatherResponse = gson.fromJson(response, WeatherResponse.class);

                            if (weatherResponse != null && weatherResponse.getResults() != null) {
                                // Atualiza tempo atual
                                textViewCityName.setText(weatherResponse.getResults().getCityName());
                                textViewCurrentTemp.setText(String.format(Locale.getDefault(), "%d°", weatherResponse.getResults().getTemp()));
                                textViewCurrentDescription.setText(weatherResponse.getResults().getDescription());

                                // Atualiza a lista de previsão
                                forecastList.clear();
                                if (weatherResponse.getResults().getForecast() != null) {
                                    forecastList.addAll(weatherResponse.getResults().getForecast());
                                }
                                weatherAdapter.notifyDataSetChanged(); // Notifica o adapter
                            } else {
                                // API respondeu de forma inesperada
                                Toast.makeText(getContext(), "Resposta inválida da API.", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Log.e("WeatherFragment", "Erro JSON", e);
                            Toast.makeText(getContext(), "Erro ao processar dados.", Toast.LENGTH_SHORT).show();
                        }
                    },
                    error -> {
                        Log.e("WeatherFragment", "Erro na requisição", error);
                        Toast.makeText(getContext(), "Erro ao buscar dados.", Toast.LENGTH_SHORT).show();
                    });

            // Adiciona requisição na fila
            requestQueue.add(stringRequest);
        } catch (Exception e) {
            Log.e("WeatherFragment", "Erro ao codificar nome da cidade", e);
            Toast.makeText(getContext(), "Nome da cidade inválido.", Toast.LENGTH_SHORT).show();
        }
    }
}
