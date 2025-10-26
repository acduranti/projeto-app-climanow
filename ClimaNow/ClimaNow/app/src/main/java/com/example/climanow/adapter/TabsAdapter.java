package com.example.climanow.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.climanow.fragment.MapFragment;
import com.example.climanow.fragment.WeatherFragment;

// Adapter das abas do ViewPager2
public class TabsAdapter extends FragmentStateAdapter {

    // Cria o fragment certo dependendo da aba (Clima ou Mapa)
    public TabsAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
// Retorna WeatherFragment pra aba 0 e MapFragment pra aba 1
        switch (position) {
            case 0:
                return new WeatherFragment();
            case 1:
                return new MapFragment();
            default:
                return new WeatherFragment(); // Padrão
        }
    }

    @Override
    public int getItemCount() {
        return 2; // getItemCount retorna 2 porque só tem duas abas
    }
}
