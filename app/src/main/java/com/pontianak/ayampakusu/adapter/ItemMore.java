package com.pontianak.ayampakusu.adapter;

public class ItemMore {
    public String id_item,nama_item,harga_item,deskripsi_item,gambar_item;
    public int s_item;

    public ItemMore(String id_item, String nama_item, String harga_item, String deskripsi_item, String gambar_item, int s_item) {
        this.id_item = id_item;
        this.nama_item = nama_item;
        this.harga_item = harga_item;
        this.deskripsi_item = deskripsi_item;
        this.gambar_item = gambar_item;
        this.s_item = s_item;
    }

    public String getId_item() {
        return id_item;
    }
}
