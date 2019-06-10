package com.hsd.findways;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailTempat extends AppCompatActivity {

    Button gmaps;
    TextView judul;
    TextView alamat;
    ImageView gambar_destinasi;

    tempat_destinasi objekTravel = new tempat_destinasi();
    data_pengguna_masuk passing = new data_pengguna_masuk();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_tempat);
        setTitle("Detail Tempat");
        judul = (TextView)findViewById(R.id.textView8);
        alamat = (TextView)findViewById(R.id.textView9);
        gmaps = (Button)findViewById(R.id.id_gmap);
        gambar_destinasi = (ImageView)findViewById(R.id.gambar_detail);

        Picasso.with(getApplicationContext()).load(objekTravel.link_ref_foto[objekTravel.indeks_pilihan]).into(gambar_destinasi);

        judul.setText(objekTravel.nama_tempat[objekTravel.indeks_pilihan]);
        alamat.setText(objekTravel.alamat[objekTravel.indeks_pilihan]);



        gmaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pindah = new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?saddr="+passing.latitude_user+","+ passing.longitude_user+"&daddr="+objekTravel.latitude
                        [objekTravel.indeks_pilihan] + "," + objekTravel.longitude[objekTravel.indeks_pilihan]));
                startActivity(pindah);
            }
        });


    }
}
