package ch.heigvd.sym.labo3.views.barcode;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.ViewGroup;

import com.google.zxing.Result;

import ch.heigvd.sym.labo3.R;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

/**
 * Handles the results from the scanner view
 *
 * Source : http://www.codeplayon.com/2018/10/android-create-a-bar-code-scanner-zxingscannerview/
 *
 * @author Jael Dubey, Loris Gilliand, Mateo Tutic, Luc Wachter
 * @version 1.0
 * @since 2019-11-28
 */
public class ScanActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView mScannerView;
    String contents, format;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

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
        contents = rawResult.getText();
        format = rawResult.getBarcodeFormat().toString();

        Intent returnIntent = new Intent();
        returnIntent.putExtra("contents", contents);
        returnIntent.putExtra("format", format);
        setResult(RESULT_OK, returnIntent);

        finish();
    }
}
