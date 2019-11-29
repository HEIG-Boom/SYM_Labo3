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
import me.dm7.barcodescanner.zxing.ZXingScannerView.ResultHandler;

/**
 * Handles the results from the scanner view
 * <p>
 * Source: http://www.codeplayon.com/2018/10/android-create-a-bar-code-scanner-zxingscannerview/
 *
 * @author Jael Dubey, Loris Gilliand, Mateo Tutic, Luc Wachter
 * @version 1.0
 * @since 2019-11-28
 */
public class ScanActivity extends AppCompatActivity implements ResultHandler {
    private ZXingScannerView scanner;
    String contents, format;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        // Request permission to use the camera
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, 5);
            }
        }

        // Set the contentFrame to the ZXingScannerView
        ViewGroup contentFrame = findViewById(R.id.content_frame);
        scanner = new ZXingScannerView(this);
        contentFrame.addView(scanner);
    }

    @Override
    public void onResume() {
        super.onResume();
        scanner.setResultHandler(this);
        scanner.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        scanner.stopCamera();
    }

    @Override
    public void handleResult(Result rawResult) {
        contents = rawResult.getText();
        format = rawResult.getBarcodeFormat().toString();

        // Return result to previous Activity
        Intent returnIntent = new Intent();
        returnIntent.putExtra("contents", contents);
        returnIntent.putExtra("format", format);
        setResult(RESULT_OK, returnIntent);

        // End this activity
        finish();
    }
}
