package com.hsd.findways;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {


    //public String url_web_server = "http://192.168.100.6:4698/server_pkm/login.php";
    public String luar_status;
    server_class kelasServer = new server_class();
    data_pengguna_masuk passingID = new data_pengguna_masuk();
    EditText email_field, password_field;

    public void fungsiLogin(String url_web_server){

        RequestQueue antri = Volley.newRequestQueue(MainActivity.this);
        StringRequest requestString = new StringRequest(Request.Method.POST, url_web_server, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject objekjson = new JSONObject(response);
                    String status_operasi = objekjson.getString("status");
                    System.out.println("PRINT OBJEK JSON to variable: " + status_operasi);
                    if(email_field.getText().length() == 0){
                        email_field.setError("Email harus diisi!");
                    } else if(password_field.getText().length() == 0){
                        password_field.setError("Password harus diisi!");
                    } else {
                        System.out.println("Status Operasi::::" + status_operasi);
                        luar_status = status_operasi;
                        if(status_operasi.equals("1")){
                            System.out.println("MASUK IF OPERASI == 1");
                            passingID.id_pengguna = objekjson.getString("id");
                            passingID.nama_pengguna = objekjson.getString("username");
                            pindah = new Intent(getApplicationContext(), user_space.class);
                            startActivity(pindah);
                        } else if(status_operasi.equals("-1")){
                            Toast.makeText(MainActivity.this, "Silahkan cek kembali username anda dan password anda!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, "Sedang terjadi gangguan pada server. Tunggu beberapa saat lagi atau hubungi Customer Service kami!", Toast.LENGTH_LONG).show();
                        }
                    }



                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "Gagal Fetch data", Toast.LENGTH_LONG).show();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error);
                Toast.makeText(MainActivity.this, "Gagal melakukan koneksi. Silahkan cek koneksi anda", Toast.LENGTH_LONG).show();
                }
            }
        ){
            @Override
            public Map<String, String> getParams(){
                Map<String, String> parameter = new HashMap<>();
                parameter.put("email", email_field.getText().toString());
                parameter.put("password", password_field.getText().toString());
                return parameter;


            }
            @Override
            public Map<String, String> getHeaders(){
                Map<String, String>parameter = new HashMap<>();
                parameter.put("Content-Type", "application/x-www-form-urlencoded");
                return parameter;

            }

        };
        antri.getCache().clear();
        antri.add(requestString);

    }
    Intent pindah;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button tombol_masuk = (Button)findViewById(R.id.tombol_masuk);
        email_field = (EditText)findViewById(R.id.textBox_email);
        password_field = (EditText)findViewById(R.id.textBox_password);
        tombol_masuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fungsiLogin(kelasServer.alamat_server+"login.php/");
                //System.out.println("STATUS VARIABEL = " + passingID.nama_pengguna);

            }
        });

        TextView registerAkun = (TextView)findViewById(R.id.textView2);
        registerAkun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pindah = new Intent(MainActivity.this, register_activity.class);
                startActivity(pindah);
            }
        });
    }
}
