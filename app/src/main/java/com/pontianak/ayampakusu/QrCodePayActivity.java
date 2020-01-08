package com.pontianak.ayampakusu;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import androidx.annotation.NonNull;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetector;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetectorOptions;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.pontianak.ayampakusu.barcodescanning.BarcodeGraphic;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;

public class QrCodePayActivity extends AppCompatActivity {
    TextView barcodeInfo;
    SurfaceView cameraView;
    CameraSource cameraSource;
    private CameraSourcePreview preview;
    private GraphicOverlay graphicOverlay;
    private String selectedModel = "Barcode Detection";
    private Service_Connector service_connector;
    private FirebaseAuth mAuth;
    FirebaseUser currentUser;
    Intent intent;
    EditText txt_point;
    ImageButton bt_plus,bt_min;
    Helper helper;
    int maxpoint=0;
    int currpoint=0;
    String cpoint="";
    String current="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code_pay);
        service_connector=new Service_Connector();
        intent=getIntent();
        helper=new Helper(this);

        preview = (CameraSourcePreview) findViewById(R.id.firePreview);
        if (preview == null) {
            Log.d("ez", "Preview is null");
        }
        graphicOverlay = (GraphicOverlay) findViewById(R.id.fireFaceOverlay);
        if (graphicOverlay == null) {
            Log.d("ez", "graphicOverlay is null");
        }

        createCameraSource(selectedModel);
        startCameraSource();
        mAuth = FirebaseAuth.getInstance();

        currentUser = mAuth.getCurrentUser();
        Intent intent=getIntent();
        txt_point=findViewById(R.id.txt_point);
        maxpoint=helper.meisinteger(intent.getStringExtra("point"));


        bt_plus=findViewById(R.id.bt_plus);
        bt_min=findViewById(R.id.bt_min);
        bt_min.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mins();
            }
        });
        bt_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                plus();
            }
        });

        if(maxpoint>10000) {
            currpoint = 10000;
            txt_point.setText(helper.torupiah(currpoint));
        }
        else{
            currpoint=0;
            txt_point.setText(helper.torupiah(maxpoint));
            bt_plus.setEnabled(false);
            bt_min.setEnabled(false);
            txt_point.setEnabled(false);
        }

        txt_point.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {



            }

            @Override
            public void afterTextChanged(Editable s) {
               // txt_point.setText(helper.torupiah(helper.meisinteger(txt_point.getText().toString())));
                if(!s.toString().equals(current)){
                    txt_point.removeTextChangedListener(this);

                    String cleanString = s.toString().replaceAll("[Rp,R,.]", "");



                    String formatted="";
                    if(cleanString.length()>1) {
                        double parsed = Double.parseDouble(cleanString);
                        Log.i("ez",cleanString);
                        formatted = helper.formatRupiah.format((parsed));
                    }
                    else{
                        formatted =cleanString;

                    }


                    current = formatted;
                    txt_point.setText(formatted);
                    txt_point.setSelection(formatted.length());
                    currpoint=helper.meisinteger(txt_point.getText().toString().replaceAll("[Rp,R,.]", ""));

                    txt_point.addTextChangedListener(this);
                }

            }
        });







    }
    private void plus(){
        int tmb=currpoint+2000;
        if(tmb<=maxpoint){
            currpoint=tmb;
            txt_point.setText(helper.torupiah(currpoint));
        }
    }
    private void mins(){
        int tmb=currpoint-2000;
        if(tmb>=10000){
            currpoint=tmb;
            txt_point.setText(helper.torupiah(currpoint));
        }
    }
    private void createCameraSource(String model) {
        // If there's no existing cameraSource, create one.
        if (cameraSource == null) {
            cameraSource = new CameraSource(this, graphicOverlay);
        }



        cameraSource.setMachineLearningFrameProcessor(new BarcodeScanningProcessor());


    }
    private void startCameraSource() {
        if (cameraSource != null) {
            try {
                if (preview == null) {
                    Log.d("ez", "resume: Preview is null");
                }
                if (graphicOverlay == null) {
                    Log.d("ez", "resume: graphOverlay is null");
                }
                preview.start(cameraSource, graphicOverlay);
            } catch (IOException e) {
                Log.e("ez", "Unable to start camera source.", e);
                cameraSource.release();
                cameraSource = null;
            }
        }
    }
    public class BarcodeScanningProcessor extends VisionProcessorBase<List<FirebaseVisionBarcode>> {

        private static final String TAG = "BarcodeScanProc";

        private final FirebaseVisionBarcodeDetector detector;
        FirebaseVisionBarcodeDetectorOptions options =
                new FirebaseVisionBarcodeDetectorOptions.Builder()
                        .setBarcodeFormats(
                                FirebaseVisionBarcode.FORMAT_QR_CODE)
                        .build();

        public BarcodeScanningProcessor() {
            // Note that if you know which format of barcode your app is dealing with, detection will be
            // faster to specify the supported barcode formats one by one, e.g.
            // new FirebaseVisionBarcodeDetectorOptions.Builder()
            //     .setBarcodeFormats(FirebaseVisionBarcode.FORMAT_QR_CODE)
            //     .build();
            detector = FirebaseVision.getInstance().getVisionBarcodeDetector();

        }

        @Override
        public void stop() {
            try {
                detector.close();
            } catch (IOException e) {
                Log.e(TAG, "Exception thrown while trying to close Barcode Detector: " + e);
            }
        }

        @Override
        protected Task<List<FirebaseVisionBarcode>> detectInImage(FirebaseVisionImage image) {
            return detector.detectInImage(image);
        }

        @Override
        protected void onSuccess(
                @NonNull List<FirebaseVisionBarcode> barcodes,
                @NonNull FrameMetadata frameMetadata,
                @NonNull GraphicOverlay graphicOverlay) {
            graphicOverlay.clear();
            for (int i = 0; i < barcodes.size(); ++i) {
                final FirebaseVisionBarcode barcode = barcodes.get(i);
                cameraSource.release();
                if((currpoint>=10000)&&(currpoint<=maxpoint)) {


                    try {
                        detector.close();
                    } catch (IOException e) {
                        Log.e(TAG, "Exception thrown while trying to close Barcode Detector: " + e);
                    }
                    currentUser.getIdToken(true).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                        @Override
                        public void onComplete(@NonNull Task<GetTokenResult> task) {
                            if (task.isSuccessful()) {
                                String idToken = task.getResult().getToken();

                                Map<String, String> params = new HashMap<String, String>();
                                params.put("hash", barcode.getRawValue());
                                params.put("idtoken", idToken);
                                if (currpoint >= 10000)
                                    params.put("cuttoken", String.valueOf(currpoint));

                                if (intent.getStringExtra("id_voucher") != null) {
                                    params.put("cut_voucher", intent.getStringExtra("id_voucher"));

                                }
                                service_connector.sendpostrequest(QrCodePayActivity.this, "getvoucher", params, new Service_Connector.VolleyResponseListener_v3() {
                                    @Override
                                    public void onError(String message) {

                                    }

                                    @Override
                                    public void onResponese(String response) {
                                        try {
                                            JSONObject respon = new JSONObject(response);
                                            if (respon.getString("status").equals("1")) {
                                                Intent intent = new Intent();
                                                intent.putExtra("voucher", respon.getString("voucher"));
                                                intent.putExtra("add_stiker", respon.getString("add_stiker"));
                                                setResult(RESULT_OK, intent);
                                                finish();
                                            }
                                            else{
                                               mulaiagik(respon.getString("message"));
                                            }
                                        } catch (JSONException joe) {

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
                            } else {
                                // Handle error -> task.getException();
                                Log.i("ez", task.getException().getMessage());
                            }

                        }
                    });
                }
                else{
                    mulaiagik("Potongan Point Anda Harus Berada Di Kisaran "+helper.formatRupiah.format(10000)+" sampai dengan "+helper.formatRupiah.format(maxpoint));



                }

                BarcodeGraphic barcodeGraphic = new BarcodeGraphic(graphicOverlay, barcode);
                graphicOverlay.add(barcodeGraphic);
            }
        }

        @Override
        protected void onFailure(@NonNull Exception e) {
            Log.e(TAG, "Barcode detection failed " + e);
        }
    }
    private void mulaiagik(String mes){
        cameraSource.release();
        AlertDialog.Builder builder1 = new AlertDialog.Builder(QrCodePayActivity.this);

        builder1.setMessage(mes);
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Oke",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        startCameraSource();
                    }
                });



        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

}
