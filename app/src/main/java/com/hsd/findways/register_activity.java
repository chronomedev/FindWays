package com.hsd.findways;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class register_activity extends AppCompatActivity {


    server_class kelasServer = new server_class();

    EditText textBox_nama, textBox_email, textBox_password, textBox_nomorHP;
    Button tombolDaftar;


    public void insertPelanggan(String server_pkm){
        RequestQueue antri = Volley.newRequestQueue(register_activity.this);
        StringRequest requestString = new StringRequest(Request.Method.POST, server_pkm, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject objekjson = new JSONObject(response);
                    String status_server = objekjson.getString("status");
                    if(status_server.equals("1")){
                        Toast.makeText(register_activity.this, "Anda berhasil membuat akun!", Toast.LENGTH_LONG).show();
                        finish();

                    } else if(status_server.equals("-1")){
                        Toast.makeText(getApplicationContext(), "E-mail anda atau password anda salah. Silahkan cek kembali!", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(register_activity.this, "Terjadi gangguan pada server. Silahkan ", Toast.LENGTH_SHORT).show();
                    }

                } catch(Exception e){
                    e.printStackTrace();
                    System.out.println("Gagal Fetch Data!");
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error);
                System.out.println("Koneksi gagal. Silahkan cek kembali internet anda!");

            }
        }){
            @Override
            public Map<String, String> getParams(){
                HashMap<String, String> parameter = new HashMap<>();
                parameter.put("nama_lengkap", textBox_nama.getText().toString());
                parameter.put("no_hp", textBox_nomorHP.getText().toString());
                parameter.put("alamat_email_daftar", textBox_email.getText().toString());
                parameter.put("password_daftar", textBox_password.getText().toString());
                return parameter;
            }
            @Override
            public Map<String, String>getHeaders(){
                HashMap<String, String> parameter = new HashMap<>();
                parameter.put("Content-Type", "application/x-www-form-urlencoded");
                return parameter;
            }
        };
        antri.getCache().clear();
        antri.add(requestString);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_activity);
        setTitle("Regristrasi Peserta");

        textBox_nama = (EditText)findViewById(R.id.field_nama_pengguna_daftar);
        textBox_email = (EditText)findViewById(R.id.field_email_daftar);
        textBox_password = (EditText)findViewById(R.id.field_password_daftar);
        textBox_nomorHP = (EditText)findViewById(R.id.field_nomor_telepon);


        tombolDaftar = (Button)findViewById(R.id.tombol_daftar);
        tombolDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertPelanggan(kelasServer.alamat_server+ "insert_pelanggan.php/");
            }
        });

    }
}
