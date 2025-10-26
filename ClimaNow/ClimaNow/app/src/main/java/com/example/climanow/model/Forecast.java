package com.example.climanow.model;

// Classe que representa a previsão do tempo para um dia
public class Forecast {

    private String date;        // Data da previsão (ex: "2025-10-18")
    private String description; // Descrição do clima (ex: "Ensolarado")
    private int max;            // Temperatura máxima do dia
    private int min;            // Temperatura mínima do dia

    // Retorna a data da previsão
    public String getDate() {
        return date;
    }

    // Retorna a descrição do clima
    public String getDescription() {
        return description;
    }

    // Retorna a temperatura máxima
    public int getMax() {
        return max;
    }

    // Retorna a temperatura mínima
    public int getMin() {
        return min;
    }
}
