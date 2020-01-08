package com.pontianak.ayampakusu.adapter;

public class Promo {
    String id_promo;
    String nama_promo;
    String gambar_promo;
    String url_action;
    String subtitle_promo;

    public Promo(String id_promo, String nama_promo, String gambar_promo, String url_action) {
        this.id_promo = id_promo;
        this.nama_promo = nama_promo;
        this.gambar_promo = gambar_promo;
        this.url_action = url_action;
        this.subtitle_promo=nama_promo;
    }

    public Promo(String id_promo, String nama_promo, String gambar_promo, String url_action, String subtitle_promo) {
        this.id_promo = id_promo;
        this.nama_promo = nama_promo;
        this.gambar_promo = gambar_promo;
        this.url_action = url_action;
        this.subtitle_promo = subtitle_promo;
    }
}
