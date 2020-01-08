package com.pontianak.ayampakusu.adapter;



import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class VoucherTukar implements  Parcelable {

    @SerializedName("id_voucher")
    public String id_voucher;
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
        dest.writeString(this.id_voucher);
        dest.writeString(this.nama_voucher);
        dest.writeString(this.besaran_voucher);
        dest.writeString(this.datetime_mulai_berlaku_voucher);
        dest.writeString(this.datetime_stop_voucher);
        dest.writeString(this.image_voucher);
        dest.writeString(this.image_voucher);
    }


    public VoucherTukar() {
    }



    // Perhatikan method yang dipanggil pada objek in
    protected VoucherTukar(Parcel in) {
        this.id_voucher = in.readString();
        this.nama_voucher = in.readString();
        this.besaran_voucher = in.readString();
        this.datetime_mulai_berlaku_voucher = in.readString();
        this.datetime_stop_voucher=in.readString();
        this.image_voucher=in.readString();
    }

    public static final Parcelable.Creator<ParcelVoucherTukar> CREATOR = new Parcelable.Creator<ParcelVoucherTukar>() {
        @Override
        public ParcelVoucherTukar createFromParcel(Parcel source) {
            return new ParcelVoucherTukar(source);
        }

        @Override
        public ParcelVoucherTukar[] newArray(int size) {
            return new ParcelVoucherTukar[size];
        }
    };
}


