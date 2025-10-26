package com.example.climanow.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

// ViewModel compartilhado entre fragments/activity
// Mantém o nome da cidade e permite que todos observem mudanças
public class SharedViewModel extends ViewModel {

    private final MutableLiveData<String> city = new MutableLiveData<>(); // Cidade atual

    // Atualiza a cidade
    // Use setValue() na thread principal, postValue() em background
    public void setCity(String newCity) {
        city.setValue(newCity);
    }

    // Retorna a cidade como LiveData para observação
    public LiveData<String> getCity() {
        return city;
    }
}
