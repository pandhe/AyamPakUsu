package com.pontianak.ayampakusu;

import android.content.Intent;
import androidx.annotation.NonNull;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;

public class DaftarActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private Helper helper;
    private Service_Connector service_connector;
    private Button bt_daftar,bt_daftar_google;
    private TextInputEditText edt_email,edt_password,edt_nama,edt_no_telp,edt_ulang_password,edt_username;
    private TheConfig theConfig=new TheConfig();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar);
        bt_daftar=findViewById(R.id.bt_daftar);
        edt_no_telp=findViewById(R.id.edt_no_telp);
        edt_ulang_password=findViewById(R.id.edt_ulang_password);
        edt_email=findViewById(R.id.edt_email);
        edt_password=findViewById(R.id.edt_password);
        edt_username=findViewById(R.id.edt_username);

        mAuth = FirebaseAuth.getInstance();
        helper=new Helper(this);
        service_connector=new Service_Connector();

        TextView txt_pernah_masuk=findViewById(R.id.txt_pernah_masuk);
        txt_pernah_masuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(DaftarActivity.this,LoginActivity.class);
                startActivityForResult(intent,theConfig.REQUEST_LOGIN);

            }
        });

        bt_daftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (loginvalidation()) {
                    if (edt_password.getText().toString().equals(edt_ulang_password.getText().toString())) {

                        mAuth.createUserWithEmailAndPassword(edt_email.getText().toString(), edt_password.getText().toString())
                                .addOnCompleteListener(DaftarActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            // Sign in success, update UI with the signed-in user's information
                                            Log.d("ez", "createUserWithEmail:success");
                                            FirebaseUser user = mAuth.getCurrentUser();
                                            getuser(user);
                                        } else {
                                            Log.w("ez", "createUserWithEmail:failure", task.getException());

                                        }

                                        // ...
                                    }
                                });
                    } else {
                        edt_ulang_password.setError("Konfirmasi Password tidak sama ");

                    }

                }
            }
        });




    }
    private void getuser(final FirebaseUser user){
        user.getIdToken(true).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
            @Override
            public void onComplete(@NonNull Task<GetTokenResult> task) {
                if (task.isSuccessful()) {
                    String idToken = task.getResult().getToken();
                    Map<String,String>mymap=new HashMap<>();
                    mymap.put("id",user.getUid());
                    mymap.put("email",user.getEmail());
                    mymap.put("no_telp",edt_no_telp.getText().toString());
                    mymap.put("username",edt_username.getText().toString());
                    mymap.put("idtoken",idToken);


                    service_connector.sendpostrequest(DaftarActivity.this, "register", mymap, new Service_Connector.VolleyResponseListener_v3() {
                        @Override
                        public void onError(String message) {

                        }

                        @Override
                        public void onResponese(String response) {
                            try{
                                JSONObject jsonObject=new JSONObject(response);
                                if(jsonObject.getString("status").equals("1")){
                                    Intent intent=getIntent();
                                    helper.meditor.putBoolean("islogin",true);
                                    setResult(RESULT_OK,intent);
                                    DaftarActivity.this.finish();
                                }
                                else {

                                }
                            }
                            catch (Throwable t){

                            }
                        }

                        @Override
                        public void onNoConnection(String message) {

                        }

                        @Override
                        public void OnServerError(String message) {

                        }

                        @Override
                        public void OnTimeOut() {

                        }
                    });

                }
                else{
                    Log.i("ez",task.getException().getMessage());
                }
            }
        });



    }
    boolean loginvalidation(){
        boolean tr=true;
        if(edt_email.getText().toString().length()<4){
            edt_email.setError("Harus Terisi");
            tr=false;
        }
        if(edt_password.getText().toString().length()<4){
            edt_email.setError("Harus Terisi");
            tr=false;
        }
        return  tr;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==theConfig.REQUEST_LOGIN){
            if(resultCode==RESULT_OK){
                Intent intent=getIntent();
                setResult(RESULT_OK,intent);
                finish();
            }
        }
    }
}
