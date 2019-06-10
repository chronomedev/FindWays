package com.hsd.findways;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;


import com.google.android.gms.location.FusedLocationProviderClient;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class user_space extends AppCompatActivity {

    private FusedLocationProviderClient client;
    ImageButton tombolCari;
    Spinner jam;
    Spinner menit_puluhan;
    Spinner comboBox_kategori;

    data_pengguna_masuk passing = new data_pengguna_masuk();




    public void isiJam(ArrayList<String> isiComboBox, Spinner comboboxMasukan){

        ArrayAdapter pengisi = new ArrayAdapter(getApplicationContext(), R.layout.support_simple_spinner_dropdown_item, isiComboBox);
        comboboxMasukan.setAdapter(pengisi);
    }

    public void isiKategoriTravel(Context konteks, Spinner comboBoxMasukan){
        ArrayList<String> list_kategori = new ArrayList<String>();
        list_kategori.add("Museum");
        list_kategori.add("Wisata Taman");
        list_kategori.add("Wisata Kuliner");
        list_kategori.add("Wisata Binatang");
        list_kategori.add("Gaya Hidup");

        ArrayAdapter adapterArray = new ArrayAdapter(konteks, R.layout.support_simple_spinner_dropdown_item, list_kategori);

        comboBoxMasukan.setAdapter(adapterArray);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_space);

        TextView nama_pengguna = (TextView)findViewById(R.id.textview_nama_pengguna);
        nama_pengguna.setText("Hallo " + passing.nama_pengguna);
        jam = (Spinner)findViewById(R.id.jam);
        menit_puluhan = (Spinner)findViewById(R.id.menit_puluhan);

        comboBox_kategori = (Spinner)findViewById(R.id.kategori_wisata);

        isiKategoriTravel(this, comboBox_kategori);
        comboBox_kategori.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                passing.pilhian_kategori = comboBox_kategori.getSelectedItem().toString();
                System.out.println("FILTER YANG DIPILIH::::" + passing.pilhian_kategori);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




        ArrayList<String> arrayJam = new ArrayList<String>();
        ArrayList<String> arrayMenit = new ArrayList<String>();
        System.out.println("ID MASUKAN:::" + passing.id_pengguna);
        for(int i = 1;i<=12;i++){
            String rubahAngka = String.valueOf(i);
            arrayJam.add(rubahAngka);
        }

        jam.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("PILIHAN JAM::::" + jam.getSelectedItem());
                passing.pilihan_waktu[0] = jam.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        menit_puluhan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                passing.pilihan_waktu[1] = menit_puluhan.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        arrayMenit.add("0");
        arrayMenit.add("30");

        isiJam(arrayJam, jam);
        isiJam(arrayMenit, menit_puluhan);

        tombolCari = (ImageButton) findViewById(R.id.imageButton);
        tombolCari.setOnHoverListener(new View.OnHoverListener() {
            @Override
            public boolean onHover(View v, MotionEvent event) {
                tombolCari.setBackgroundColor(Color.GREEN);
                return false;
            }
        });
        tombolCari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent Pindah = new Intent(user_space.this, list_tempat_travel.class);
                startActivity(Pindah);
            }
        });

    }
}
