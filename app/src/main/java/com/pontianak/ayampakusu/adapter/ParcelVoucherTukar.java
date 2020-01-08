package com.pontianak.ayampakusu.adapter;

import android.os.Parcel;
import android.os.Parcelable;

public class ParcelVoucherTukar implements Parcelable {

    public String id_voucher;
     public String nama_voucher;
    public String besaran_voucher;
    public String datetime_mulai_berlaku_voucher;
   public String datetime_stop_voucher;
     public String image_voucher;
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


    public ParcelVoucherTukar() {
    }

    public ParcelVoucherTukar(String id_voucher, String nama_voucher, String besaran_voucher, String datetime_mulai_berlaku_voucher, String datetime_stop_voucher, String image_voucher) {
        this.id_voucher = id_voucher;
        this.nama_voucher = nama_voucher;
        this.besaran_voucher = besaran_voucher;
        this.datetime_mulai_berlaku_voucher = datetime_mulai_berlaku_voucher;
        this.datetime_stop_voucher = datetime_stop_voucher;
        this.image_voucher = image_voucher;
    }

    // Perhatikan method yang dipanggil pada objek in
    protected ParcelVoucherTukar(Parcel in) {
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
