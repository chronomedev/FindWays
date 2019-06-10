package com.hsd.findways;

public class tempat_destinasi {

    public static int indeks_pilihan;
    public static String[] place_id;
    public static String[] latitude;
    public static String[] longitude;
    public static String[] nama_tempat;
    public static String[] alamat;
    public static String[] link_ref_foto;



    public void createArray(int index){
        place_id = new String[index];
        latitude = new String[index];
        longitude = new String[index];
        nama_tempat = new String[index];
        alamat = new String[index];
        link_ref_foto = new String[index];

    }
}
