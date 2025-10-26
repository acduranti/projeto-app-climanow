package com.example.climanow.model;

// Classe que representa a resposta completa da API de clima
public class WeatherResponse {

    private Results results; // Resultado principal da API, contém cidade, clima atual e previsão

    // Retorna os resultados da API
    public Results getResults() {
        return results;
    }

    // Define os resultados da API
    public void setResults(Results results) {
        this.results = results;
    }
}
