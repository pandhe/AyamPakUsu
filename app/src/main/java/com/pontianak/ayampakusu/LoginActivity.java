package com.pontianak.ayampakusu;

import android.content.DialogInterface;
import android.content.Intent;
import androidx.annotation.NonNull;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.auth.GoogleAuthProvider;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class LoginActivity extends AppCompatActivity {
    FirebaseUser currentUser;
    private FirebaseAuth mAuth;
    Service_Connector service_connector;
    Helper helper;
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 9001;
    private ConstraintLayout group2;
    private TextInputEditText edt_email;
    private TextInputEditText edt_password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        service_connector=new Service_Connector();
        helper=new Helper(this);
        mAuth = FirebaseAuth.getInstance();
        group2=findViewById(R.id.const_loading_lala);

        Button bt_login=findViewById(R.id.bt_login);
        Button bt_login_google=findViewById(R.id.bt_login_google);
        edt_email=findViewById(R.id.edt_email);
        edt_password=findViewById(R.id.edt_password);
        TextView txt_lupa=findViewById(R.id.txt_lupa);
        txt_lupa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isEmailValid(edt_email.getText().toString())) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    builder.setTitle("Lupa Kata Sandi ?");
                    builder.setMessage("Jika anda mendaftar menggunakan alamat email, anda akan menerima email berisi tautan untuk menyetel ulang kata sandi anda")
                            .setPositiveButton("Setel Ulang", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // FIRE ZE MISSILES!
                                    mAuth.sendPasswordResetEmail(edt_email.getText().toString())
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){

                                                    }
                                                }
                                            });
                                }
                            })
                            .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // User cancelled the dialog
                                }
                            });
                    // Create the AlertDialog object and return it
                    builder.create();
                    builder.show();
                }
                else{
                    edt_email.setError("Isi Email Anda Terlebih Dahulu");
                }
            }
        });

        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(loginvalidation()){
                group2.setVisibility(View.VISIBLE);
                mAuth.signInWithEmailAndPassword(edt_email.getText().toString(), edt_password.getText().toString())
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    //Log.d(TAG, "signInWithEmail:success");

                                    updateUI();

                                } else {
                                    // If sign in fails, display a message to the user.
                                    //Log.w(TAG, "signInWithEmail:failure", task.getException());
                                    Toast.makeText(LoginActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                    group2.setVisibility(View.INVISIBLE);
                                   // updateUI(null);
                                }

                                // ...
                            }
                        });
            }}
        });
        bt_login_google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(LoginActivity.this, gso);
    }
    private void getuser(final FirebaseUser user){
        group2.setVisibility(View.VISIBLE);
        user.getIdToken(true).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
            @Override
            public void onComplete(@NonNull Task<GetTokenResult> task) {

                if (task.isSuccessful()) {
                    String idToken = task.getResult().getToken();
                    Map<String,String> mymap=new HashMap<>();
                    if(user.getPhoneNumber()!=null)
                        mymap.put("no_telp",user.getPhoneNumber());

                    mymap.put("id",user.getUid());
                    mymap.put("email",user.getEmail());

                    mymap.put("idtoken",idToken);

                    Log.i("ez","loginuseerrr"+user.getUid());


                    service_connector.sendpostrequest(LoginActivity.this, "register", mymap, new Service_Connector.VolleyResponseListener_v3() {
                        @Override
                        public void onError(String message) {
                            Log.i("ez",message);
                            group2.setVisibility(View.INVISIBLE);

                        }

                        @Override
                        public void onResponese(String response) {
                            try{
                                JSONObject jsonObject=new JSONObject(response);
                                if(jsonObject.getString("status").equals("1")){
                                    Intent intent=getIntent();
                                    helper.meditor.putBoolean("islogin",true);
                                    setResult(RESULT_OK,intent);
                                    finish();
                                }
                                else {
                                    group2.setVisibility(View.INVISIBLE);
                                }
                            }
                            catch (Throwable t){

                            }
                        }

                        @Override
                        public void onNoConnection(String message) {
                            group2.setVisibility(View.INVISIBLE);

                        }

                        @Override
                        public void OnServerError(String message) {
                            Log.i("ez",message);
                            group2.setVisibility(View.INVISIBLE);

                        }

                        @Override
                        public void OnTimeOut() {
                            group2.setVisibility(View.INVISIBLE);

                        }
                    });

                }
                else{
                    Log.i("ez",task.getException().getMessage());
                }
            }
        });



    }
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    private void updateUI(){
        Intent intent=new Intent();

        this.setResult(RESULT_OK, intent);
        finish();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("ezz", "Google sign in failed", e);
                // [START_EXCLUDE]
                //updateUI(null);
                // [END_EXCLUDE]
            }
        }
    }
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d("ez", "firebaseAuthWithGoogle:" + acct.getId());
        group2.setVisibility(View.VISIBLE);
        // [START_EXCLUDE silent]
        //showProgressDialog();
        // [END_EXCLUDE]

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("ez", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            getuser(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("ez", "signInWithCredential:failure", task.getException());
                            group2.setVisibility(View.INVISIBLE);
                            //Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                           // updateUI();
                        }

                        // [START_EXCLUDE]
                       // hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
    }
    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
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
}
