package com.example.climanow.model;

import java.util.List;

// Classe que representa os resultados da API de clima
public class Results {

    private String city_name;          // Nome da cidade
    private int temp;                  // Temperatura atual
    private String description;        // Descrição do clima atual (ex: "Ensolarado")
    private List<Forecast> forecast;   // Lista de previsões futuras (classe Forecast)

    // Retorna o nome da cidade
    public String getCityName() {
        return city_name;
    }

    // Define o nome da cidade
    public void setCityName(String cityName) {
        this.city_name = cityName;
    }

    // Retorna a temperatura atual
    public int getTemp() {
        return temp;
    }

    // Define a temperatura atual
    public void setTemp(int temp) {
        this.temp = temp;
    }

    // Retorna a descrição do clima atual
    public String getDescription() {
        return description;
    }

    // Define a descrição do clima atual
    public void setDescription(String description) {
        this.description = description;
    }

    // Retorna a lista de previsões futuras
    public List<Forecast> getForecast() {
        return forecast;
    }

    // Define a lista de previsões futuras
    public void setForecast(List<Forecast> forecast) {
        this.forecast = forecast;
    }
}
