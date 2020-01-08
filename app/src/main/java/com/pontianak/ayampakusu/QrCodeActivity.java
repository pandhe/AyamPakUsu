package com.pontianak.ayampakusu;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import androidx.annotation.NonNull;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetector;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetectorOptions;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.zxing.WriterException;
import com.pontianak.ayampakusu.barcodescanning.BarcodeGraphic;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;

public class QrCodeActivity extends AppCompatActivity {
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
    ImageView img_qr;
    int tryit=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code);
        service_connector=new Service_Connector();
        intent=getIntent();
        img_qr=findViewById(R.id.img_qr);

        preview = (CameraSourcePreview) findViewById(R.id.firePreview);
        if (preview == null) {
            Log.d("ez", "Preview is null");
        }
        graphicOverlay = (GraphicOverlay) findViewById(R.id.fireFaceOverlay);
        if (graphicOverlay == null) {
            Log.d("ez", "graphicOverlay is null");
        }

            createCameraSource(selectedModel);
        mAuth = FirebaseAuth.getInstance();

        currentUser = mAuth.getCurrentUser();

        if(currentUser!=null){
            startCameraSource();

        }
        getkodetransaksi();







    }
    private void getkodetransaksi(){
        currentUser.getIdToken(true).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
            @Override
            public void onComplete(@NonNull Task<GetTokenResult> task) {
                if (task.isSuccessful()) {
                    String idToken = task.getResult().getToken();
                    String firebase_token= FirebaseInstanceId.getInstance().getToken();

                    Map<String, String> params = new HashMap<String, String>();
                    params.put("idtoken", idToken);
                    params.put("ftoken", firebase_token);
                    service_connector.sendpostrequest(QrCodeActivity.this, "generate_kode_transaksi",params, new Service_Connector.VolleyResponseListener_v3() {
                        @Override
                        public void onError(String message) {

                        }

                        @Override
                        public void onResponese(String response) {
                            try{
                                JSONObject respon=new JSONObject(response);
                                WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
                                Display display = manager.getDefaultDisplay();
                                Point point = new Point();
                                display.getSize(point);

                                int width = point.x;
                                int height = point.y;
                                int smallerDimension = width < height ? width : height;
                                smallerDimension = smallerDimension * 3 / 4;
                                QRGEncoder qrgEncoder = new QRGEncoder(respon.getString("kode"), null, QRGContents.Type.TEXT, smallerDimension);
                                try {
                                    // Getting QR-Code as Bitmap
                                    Bitmap bitmap = qrgEncoder.encodeAsBitmap();
                                    // Setting Bitmap to ImageView
                                    img_qr.setImageBitmap(bitmap);
                                } catch (
                                        WriterException e) {
                                    Log.v("ez", e.toString());
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
                } else {
                    // Handle error -> task.getException();
                    Log.i("ez", task.getException().getMessage());
                }

            }
        });


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
                Log.i("ez",barcode.getRawValue());
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

                            Map<String, String> params = new HashMap<>();
                            params.put("hash",barcode.getRawValue());
                            params.put("idtoken", idToken);
                            params.put("cuttoken","0");
                            if(intent.getStringExtra("id_voucher")!=null){
                                params.put("cut_voucher",intent.getStringExtra("id_voucher"));

                            }

                            loadvoucher(params);

                        } else {
                            // Handle error -> task.getException();
                            Log.i("ez", task.getException().getMessage());
                        }

                    }
                });

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
        AlertDialog.Builder builder1 = new AlertDialog.Builder(QrCodeActivity.this);

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

    private void loadvoucher(final Map params){
        service_connector.sendpostrequest(QrCodeActivity.this, "getvoucher", params, new Service_Connector.VolleyResponseListener_v3() {
            @Override
            public void onError(String message) {

            }

            @Override
            public void onResponese(String response) {
                try{
                    JSONObject respon=new JSONObject(response);
                    if(respon.getString("status").equals("1")){
                        Intent intent=new Intent();
                        intent.putExtra("voucher",respon.getString("voucher"));
                        intent.putExtra("add_stiker",respon.getString("add_stiker"));
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                    else{
                        mulaiagik(respon.getString("message"));
                    }
                }
                catch (JSONException joe){

                }
            }

            @Override
            public void onNoConnection(String message) {
                tryit++;
                if(tryit<4) {
                    loadvoucher(params);
                }

            }

            @Override
            public void OnServerError(String message) {

            }

            @Override
            public void OnTimeOut() {

            }
        });
    }

}
