package ch.heigvd.sym.labo3.views.barcode;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import ch.heigvd.sym.labo3.R;

/**
 * Activity that launches the scanner and prints the results in the textbox
 *
 * @author Jael Dubey, Loris Gilliand, Mateo Tutic, Luc Wachter
 * @version 1.0
 * @since 2019-11-22
 */
public class BarcodeActivity extends AppCompatActivity {
    private static int REQUEST_CODE = 1;

    private TextView barcodeContent;
    private TextView barcodeFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode);

        barcodeContent = findViewById(R.id.barcodeContent);
        barcodeFormat = findViewById(R.id.barcodeFormat);

        Button scanButton = findViewById(R.id.scanButton);

        // Start activity for result on button press
        scanButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, ScanActivity.class);
            startActivityForResult(intent, REQUEST_CODE);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Check if activity went well
        if (requestCode == REQUEST_CODE) {
            // For our result code
            if (resultCode == Activity.RESULT_OK) {
                // Set our text fields
                barcodeContent.setText(data.getStringExtra("contents"));
                barcodeFormat.setText(data.getStringExtra("format"));
            }
        }
    }
}
