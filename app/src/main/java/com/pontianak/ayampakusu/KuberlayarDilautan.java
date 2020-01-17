package com.pontianak.ayampakusu;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;



import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class KuberlayarDilautan extends AppCompatActivity {
    public int PICTURE_RESULT = 1;
   public AlertDialog.Builder builder_loading;
    public AlertDialog dialog_loading;
    View mydialog ;
    LayoutInflater gabung2;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gabung2 =getLayoutInflater();
        mydialog = gabung2.inflate(R.layout.include_popup_loading, null);
        builder_loading = new AlertDialog.Builder(this);
        builder_loading.setView(mydialog);
        dialog_loading=builder_loading.create();
        dialog_loading.setCancelable(false);



    }

    String namaactivitynye=this.getClass().getSimpleName();
    private ProgressDialog mProgressDialog;
    /*public void goabsen(View view){
        startActivity(new Intent(this, AbsentOnlineActivity.class));
    }
    public void mypulsa(View view){
        Intent intee=new Intent(this, BeliPulsaActivity.class);
        intee.putExtra("aksi","myPulsa");
        startActivity(intee);
    }
    public void myvoucher(View view){
        Intent intee=new Intent(this, MyVoucherActivity.class);
        intee.putExtra("aksi","myVoucher");
        startActivity(intee);
    }
    public void mytagihan(View view){

        Intent intee=new Intent(this, TagihanActivity.class);
        intee.putExtra("aksi","My Tagihan");
        startActivity(intee);
    }
    public void myMenu(View view){
        Intent intee=new Intent(this, FiturLinkajaActivity.class);
        intee.putExtra("aksi","My Transfer");
        startActivity(intee);
    }
    public void myDonasi(View view){
        Intent intee=new Intent(this, FiturLinkajaActivity.class);
        intee.putExtra("aksi","My Tix");
        startActivity(intee);
    }
    public void myLazisMU(View view){
        Intent intee=new Intent(this, FiturLinkajaActivity.class);
        intee.putExtra("aksi","My Jek");
        startActivity(intee);
    }
    public void myBayar(View view){
        Intent intee=new Intent(this, BayarActivity.class);
        intee.putExtra("aksi","Bayar");
        //startActivityForResult(intee,1003);
        startActivity(intee);
    }
    public void mylainnya(View view){
        Intent intee=new Intent(this, HomeDeposit.class);
        intee.putExtra("aksi","My Cash");
        startActivity(intee);
    }
    public void myDeposit(View view){
        Intent intee=new Intent(this, DepositAtivity.class);
        intee.putExtra("aksi","My Cash");
        startActivity(intee);
    }
    public void myWithdraw(View view){
        Intent intee=new Intent(this, WithdrawActivity.class);
        intee.putExtra("aksi","My Cash");
        startActivity(intee);
    }
    public void myprofil(View view){
        if(!namaactivitynye.equals("ProfileActivity")) {
            Intent intee = new Intent(this, ProfileActivity.class);
            intee.putExtra("aksi", "Lainnya");
            startActivityForResult(intee, 1003);
            //startActivity(intee);
        }
    }
    public void myTransferPulsa(View view){
        Intent intee=new Intent(this, TransferPulsaActivity.class);
        intee.putExtra("aksi","Bayar");
        //startActivityForResult(intee,1003);
        startActivity(intee);
    }
    public void myTransferDepostit(View view){
        Intent intee=new Intent(this, TransferDepositActivity.class);
        intee.putExtra("aksi","Bayar");
        //startActivityForResult(intee,1003);
        startActivity(intee);
    }
    public void myphotoprofil(View view){
        if(!namaactivitynye.equals("PhotoProfilActivity")) {
            Intent intee = new Intent(this, PhotoProfilActivity.class);
            intee.putExtra("aksi", "Lainnya");
           // startActivityForResult(intee, 1003);
            startActivity(intee);
        }
    }
    public void changepassword(View view){
        if(!namaactivitynye.equals("GantiPasswordActivity")) {
            Intent intee = new Intent(this, GantiPasswordActivity.class);
            intee.putExtra("aksi", "Lainnya");
            // startActivityForResult(intee, 1003);
            startActivity(intee);
        }
    }
    public void myqr(View view){
        Intent intee=new Intent(this, QrCodeActivity.class);
        intee.putExtra("aksi","Lainnya");
        startActivityForResult(intee,1003);
        //startActivity(intee);
    }
    public void myriwayat(View view){
        if(!namaactivitynye.equals("RiwayatActivity")) {
            Intent intee = new Intent(this, RiwayatActivity.class);
            intee.putExtra("aksi", "Riwayat");
            //startActivityForResult(intee,1003);
            startActivity(intee);
        }
    }
    public void myfavorit(View view){
        if(!namaactivitynye.equals("FavoritActivity")) {
            Intent intee = new Intent(this, FavoritActivity.class);
            intee.putExtra("aksi", "Favorit");
            //startActivityForResult(intee,1003);
            startActivity(intee);
        }
    }
    public void myselfqr(View view){
        Intent intee=new Intent(this, MyQrActivity.class);
        intee.putExtra("aksi","Favorit");
        //startActivityForResult(intee,1003);
        startActivity(intee);
    }
    public void gomain(View view){
        if(!namaactivitynye.equals("MainActivity")) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |  Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        }
    }
    public void lompatkeberanda(){
        if(!namaactivitynye.equals("MainActivity")) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |  Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        }
    }

    public void myabsentlog(View view){
        Intent intent = new Intent(this, AbsenLogActivity.class);
        startActivity(intent);
    }

    public void paksaforceclose(View view){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Ingin memaksa force close ?").setMessage("Fitur ini digunakan developer untuk memastika report crash dikirim dengan benar")
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Crashlytics.getInstance().crash(); // Force a crash

                    }
                }).setNegativeButton("tidak jadi", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                
            }
        })
        ;
        // Create the AlertDialog object and return it
        builder.create();
        builder.show();

    }
    public void showProgressDialog(boolean showprogress,String teks) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
        }
        if(showprogress){
           mProgressDialog.setMessage(teks);
                mProgressDialog.setIndeterminate(true);
                mProgressDialog.setCancelable(false);


            mProgressDialog.show();
        }
        else{
            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
        }

    }
    public void newdialog(String msg){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(msg)
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {


                    }
                })
        ;
        // Create the AlertDialog object and return it
        builder.create();
        builder.show();
    }*/


}
