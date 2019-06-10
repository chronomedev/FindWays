package com.hsd.findways;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class list_tempat_travel extends AppCompatActivity {

    data_pengguna_masuk passing = new data_pengguna_masuk();
    tempat_destinasi objekTravel = new tempat_destinasi();
    TextView testingNamaTempat;
    server_class kelasServer = new server_class();
    private FusedLocationProviderClient client;
    String latitude_user;
    String longitude_user;
    LinearLayout list_view_travel;
    ProgressDialog dialok_proses;


    private void requestPermission(){
        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION}, 1);

    }

    public String filterListTempat(String pilihan){
        if(pilihan.equalsIgnoreCase("museum")){
            return "museum";
        } else if(pilihan.equalsIgnoreCase("wisata taman")){
            return "park";
        } else if(pilihan.equalsIgnoreCase("wisata kuliner")){
            return "restaurant";
        } else if(pilihan.equalsIgnoreCase("wisata binatang")) {
            return "zoo";
        } else{
            return "department_store";
        }
    }
    public void kirimLokasiUser(String server_pkm){
        RequestQueue antri = Volley.newRequestQueue(getApplicationContext());
        StringRequest requestString = new StringRequest(Request.Method.POST, server_pkm, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                //dialok_proses.show();
                String status_operasi;
                try{

                    System.out.println("RESPON SERVER::::" + response);
                    JSONObject objekjson = new JSONObject(response);
                    status_operasi = objekjson.getString("status");
                    if(status_operasi.equals("1")){
                        JSONArray arrayListFetch = objekjson.getJSONArray("hasil");
                        objekTravel.createArray(arrayListFetch.length());
                        for(int i=0;i<arrayListFetch.length();i++){
                            final int ambil_indeks = i;
                            JSONObject subObjek = arrayListFetch.getJSONObject(i);
                            System.out.println("=============DEBUG WOI==================");
                            System.out.println(subObjek.getString("latitude"));
                            System.out.println(subObjek.getString("longitude"));
                            System.out.println(subObjek.getString("nama_tempat"));
                            System.out.println(subObjek.getString("alamat"));
                            System.out.println(subObjek.getString("link_ref_foto"));

                            objekTravel.place_id[i] = subObjek.getString("place_id");
                            objekTravel.latitude[i] = subObjek.getString("latitude");
                            objekTravel.longitude[i] = subObjek.getString("longitude");
                            objekTravel.nama_tempat[i] = subObjek.getString("nama_tempat");
                            objekTravel.alamat[i] = subObjek.getString("alamat");
                            objekTravel.link_ref_foto[i] = subObjek.getString("link_ref_foto");

                            //////inflate layout loop baris
                            View layout_baris = LayoutInflater.from(getApplicationContext()).inflate(R.layout.layout_baris, null);
                            TextView nama_travel = (TextView)layout_baris.findViewById(R.id.tampil_nama_travel);
                            TextView alamat_travel = (TextView)layout_baris.findViewById(R.id.tampil_alamat_travel);
                            ImageView gambar_travel = (ImageView)layout_baris.findViewById(R.id.tampil_gambar_travel);
                            Button tombol_detail = (Button)layout_baris.findViewById(R.id.tampil_button);
                            tombol_detail.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    objekTravel.indeks_pilihan = ambil_indeks;
                                    Intent pindah = new Intent(getApplicationContext(), DetailTempat.class);
                                    startActivity(pindah);
                                }
                            });

                            nama_travel.setText(subObjek.getString("nama_tempat"));
                            alamat_travel.setText(subObjek.getString("alamat"));
                            Picasso.with(getApplicationContext()).load(subObjek.getString("link_ref_foto")).into(gambar_travel);
                            list_view_travel.addView(layout_baris);


                        }
                        dialok_proses.dismiss();

                    } else {
                        Toast.makeText(list_tempat_travel.this, "TERJADI KESALAH PADA SERVER", Toast.LENGTH_SHORT).show();
                        dialok_proses.dismiss();
                        finish();
                    }


                } catch (Exception e){
                    dialok_proses.dismiss();
                    e.printStackTrace();
                    Toast.makeText(list_tempat_travel.this, "Gagal Fetch Data", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialok_proses.dismiss();
                System.out.println(error);
                System.out.println("MASUK ERROR!!!");
                finish();
            }
        }){
            @Override
            public Map<String, String> getParams(){
                HashMap<String, String> parameter = new HashMap<>();
                parameter.put("latitude", latitude_user);
                parameter.put("longitude", longitude_user);
                parameter.put("pilihan_kategori_travel", filterListTempat(passing.pilhian_kategori));
                String pilihan_jam = passing.pilihan_waktu[0] + ":" + passing.pilihan_waktu[1];
                Date sekarang = new Date();
                DateFormat formatjam = new SimpleDateFormat("hh:mm");
                String capture_jam = formatjam.format(sekarang);
                System.out.println("PILIHAN JAM USER:::::" + pilihan_jam);
                parameter.put("pilihan_jam", pilihan_jam);
                parameter.put("capture_jam",capture_jam);
                System.out.println("VALUE JAM SEKARANG:::::" + capture_jam);
                return parameter;
            }
            @Override
            public Map<String, String>getHeaders(){
                HashMap<String, String> parameter = new HashMap<>();
                parameter.put("Content-Type", "application/x-www-form-urlencoded");
                return parameter;
            }
        };

        requestString.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });


        antri.getCache().clear();
        antri.add(requestString);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_tempat_travel);
        setTitle("List tempat travel");
        dialok_proses = new ProgressDialog(list_tempat_travel.this);
        dialok_proses.setMessage("Harap Tunggu, melakukan pencarian...");
        list_view_travel = (LinearLayout)findViewById(R.id.kontener_list_travel);



        //request permission buat GPS////////////////////////
        requestPermission();
        client = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(list_tempat_travel.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getApplication(), "Untuk melakukan pencarian Silahkan mengaktifkan perizinan untuk GPS anda", Toast.LENGTH_LONG).show();
            return;
        }

        requestPermission();
        client.getLastLocation().addOnSuccessListener(list_tempat_travel.this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location!= null){
                    dialok_proses.show();
                    //String latitude = Double.toString(location.getLatitude());
                    latitude_user = Double.toString(location.getLatitude());
                    longitude_user = Double.toString(location.getLongitude());
                    passing.latitude_user = latitude_user;
                    passing.longitude_user = longitude_user;
                    System.out.println("LATITUDE USER = " + latitude_user);

                    kirimLokasiUser(kelasServer.alamat_server+"findways_calculation_engine.php/");

                }
            }
        });

//        Button tombolLihatDetail = (Button)findViewById(R.id.contoh_tombolid);
//        tombolLihatDetail.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(list_tempat_travel.this, DetailTempat.class);
//                startActivity(i);
//            }
//        });



    }
}
