package com.pontianak.ayampakusu.adapter;

public class Voucher {
    public String id_voucher,nama_voucher,nominal_voucher,tanggal_berlaku,tanggal_habis,is_active,image_voucher;

    public Voucher(String id_voucher, String nama_voucher, String nominal_voucher, String tanggal_berlaku, String tanggal_habis, String is_active,String image_voucher) {
        this.id_voucher = id_voucher;
        this.nama_voucher = nama_voucher;
        this.nominal_voucher = nominal_voucher;
        this.tanggal_berlaku = tanggal_berlaku;
        this.tanggal_habis = tanggal_habis;
        this.is_active = is_active;
        this.image_voucher=image_voucher;
    }
}
