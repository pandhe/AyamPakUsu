package com.pontianak.ayampakusu.adapter;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class VoucherPelanggan implements Parcelable  {
    @SerializedName("id_voucher_pelanggan")
    public String id_voucher_pelanggan;
    @SerializedName("id_pelanggan")
    public String id_pelanggan;
    @SerializedName("id_voucher")
    public String id_voucher;
    @SerializedName("is_used_voucher_pelanggan")
    public String is_used_voucher_pelanggan;
    @SerializedName("datetime_used_voucher_pelanggan")
    public String datetime_used_voucher_pelanggan;
    @SerializedName("id_code")
    public String id_code;
    @SerializedName("datetime_kadaluwarsa_voucher_pelanggan")
    public String datetime_kadaluwarsa_voucher_pelanggan;
    @SerializedName("datetime_voucher_pelanggan_add")
    public String datetime_voucher_pelanggan_add;
    @SerializedName("kode_voucher")
    public String kode_voucher;
    @SerializedName("nama_voucher")
    public String nama_voucher;
    @SerializedName("besaran_voucher")
    public String besaran_voucher;
    @SerializedName("datetime_mulai_berlaku_voucher")
    public String datetime_mulai_berlaku_voucher;
    @SerializedName("datetime_stop_voucher")
    public String datetime_stop_voucher;
    @SerializedName("image_voucher")
    public String image_voucher;
    @SerializedName("prefix_voucher")
    public String prefix_voucher;
    @SerializedName("indexed_to_pelanggan")
    public String indexed_to_pelanggan;
    @SerializedName("status_voucher")
    public String status_voucher;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id_voucher_pelanggan);
        dest.writeString(this.id_pelanggan);
        dest.writeString(this.id_voucher);
        dest.writeString(this.is_used_voucher_pelanggan);
        dest.writeString(this.datetime_used_voucher_pelanggan);
        dest.writeString(this.id_code);
        dest.writeString(this.datetime_kadaluwarsa_voucher_pelanggan);
        dest.writeString(this.datetime_voucher_pelanggan_add);
        dest.writeString(this.kode_voucher);
        dest.writeString(this.nama_voucher);
        dest.writeString(this.besaran_voucher);
        dest.writeString(this.datetime_mulai_berlaku_voucher);
        dest.writeString(this.datetime_stop_voucher);
        dest.writeString(this.image_voucher);
        dest.writeString(this.prefix_voucher);
        dest.writeString(this.indexed_to_pelanggan);
        dest.writeString(this.status_voucher);
    }

    public VoucherPelanggan() {
    }

    protected VoucherPelanggan(Parcel in) {
        this.id_voucher_pelanggan = in.readString();
        this.id_pelanggan = in.readString();
        this.id_voucher = in.readString();
        this.is_used_voucher_pelanggan = in.readString();
        this.datetime_used_voucher_pelanggan = in.readString();
        this.id_code = in.readString();
        this.datetime_kadaluwarsa_voucher_pelanggan = in.readString();
        this.datetime_voucher_pelanggan_add = in.readString();
        this.kode_voucher = in.readString();
        this.nama_voucher = in.readString();
        this.besaran_voucher = in.readString();
        this.datetime_mulai_berlaku_voucher = in.readString();
        this.datetime_stop_voucher = in.readString();
        this.image_voucher = in.readString();
        this.prefix_voucher = in.readString();
        this.indexed_to_pelanggan = in.readString();
        this.status_voucher = in.readString();
    }

    public static final Creator<VoucherPelanggan> CREATOR = new Creator<VoucherPelanggan>() {
        @Override
        public VoucherPelanggan createFromParcel(Parcel source) {
            return new VoucherPelanggan(source);
        }

        @Override
        public VoucherPelanggan[] newArray(int size) {
            return new VoucherPelanggan[size];
        }
    };
}

