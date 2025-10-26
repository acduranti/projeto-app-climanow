package com.example.climanow.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.climanow.R;
import com.example.climanow.model.Forecast;

import java.util.List;
import java.util.Locale;

// Adapter do RecyclerView que mostra a previsão do tempo
// Recebe uma lista de Forecast e cria os cards correspondentes
public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder> {

    // Lista de previsões que vai popular os cards
    private List<Forecast> forecastList;

    // Construtor recebe a lista de previsões
    public WeatherAdapter(List<Forecast> forecastList) {
        this.forecastList = forecastList;
    }

    @NonNull
    @Override
    public WeatherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Cria uma nova view (infla o layout do item)
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_weather_card, parent, false);
        return new WeatherViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherViewHolder holder, int position) {
        // Pega o elemento da sua lista na posição e substitui o conteúdo da view
        Forecast forecast = forecastList.get(position);
        holder.textViewDate.setText(forecast.getDate()); // mostra a data
        holder.textViewDescription.setText(forecast.getDescription()); // descrição do clima
        holder.textViewMaxTemp.setText(String.format(Locale.getDefault(), "Max: %d°C", forecast.getMax())); // temperatura máxima
        holder.textViewMinTemp.setText(String.format(Locale.getDefault(), "Min: %d°C", forecast.getMin())); // temperatura mínima
    }

    @Override
    public int getItemCount() {
        // Quantos itens tem na lista
        return forecastList.size();
    }

    // ViewHolder que descreve a view do item e seus metadados
    public static class WeatherViewHolder extends RecyclerView.ViewHolder {
        TextView textViewDate, textViewDescription, textViewMaxTemp, textViewMinTemp;

        public WeatherViewHolder(@NonNull View itemView) {
            super(itemView);
            // Pega cada TextView do layout do card
            textViewDate = itemView.findViewById(R.id.textViewDate);
            textViewDescription = itemView.findViewById(R.id.textViewDescription);
            textViewMaxTemp = itemView.findViewById(R.id.textViewMaxTemp);
            textViewMinTemp = itemView.findViewById(R.id.textViewMinTemp);
        }
    }
}
