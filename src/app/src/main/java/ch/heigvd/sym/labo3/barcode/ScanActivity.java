package ch.heigvd.sym.labo3.barcode;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.zxing.Result;

import ch.heigvd.sym.labo3.R;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScanActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView mScannerView;
    private static final int WRITE_EXST = 1;
    private static final int REQUEST_PERMISSION = 123;
    int CAMERA;
    String position, formt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
//        Thread.setDefaultUncaughtExceptionHandler(new UnCaughtException(this));

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, 5);
            }
        }

        ViewGroup contentFrame = findViewById(R.id.content_frame);
        mScannerView = new ZXingScannerView(this);
        contentFrame.addView(mScannerView);
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result rawResult) {
        Toast.makeText(this, "Contents = " + rawResult.getText() + ", Format = " + rawResult.getBarcodeFormat().toString(), Toast.LENGTH_LONG).show();
        position = rawResult.getText();
        formt = rawResult.getBarcodeFormat().toString();
        Intent intent = new Intent();
        intent.putExtra("Contents", position);
        intent.putExtra("Format", formt);
        setResult(RESULT_OK, intent);
        finish();
    }

//    private ZXingScannerView scannerView;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        scannerView = new ZXingScannerView(this);
//        setContentView(scannerView);
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        scannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
//        scannerView.startCamera();          // Start camera on resume
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        scannerView.stopCamera();           // Stop camera on pause
//    }
//
//    @Override
//    public void handleResult(Result rawResult) {
//        // Do something with the result here
//        // Log.v("tag", rawResult.getText()); // Prints scan results
//        // Log.v("tag", rawResult.getBarcodeFormat().toString()); // Prints the scan format (qrcode, pdf417 etc.)
//
//        BarcodeActivity.barCodeValue.setText(rawResult.getText());
//        onBackPressed();
//
//        // If you would like to resume scanning, call this method below:
//        //mScannerView.resumeCameraPreview(this);
//    }
}
