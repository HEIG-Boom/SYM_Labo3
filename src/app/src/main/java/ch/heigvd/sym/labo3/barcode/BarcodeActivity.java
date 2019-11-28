package ch.heigvd.sym.labo3.barcode;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import ch.heigvd.sym.labo3.R;

// TODO comments, headers

/**
 * Activity that launches the scanner and prints the results in the textbox
 *
 * @author Jael Dubey, Loris Gilliand, Mateo Tutic, Luc Wachter
 * @version 1.0
 * @since 2019-11-22
 */
public class BarcodeActivity extends AppCompatActivity {
    private static int REQUEST_CODE = 1;

    private TextView barCodeValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode);

        barCodeValue = findViewById(R.id.barcodeValue);

        Button scanButton = findViewById(R.id.scanButton);

        scanButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, ScanActivity.class);
            startActivityForResult(intent, REQUEST_CODE);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                barCodeValue.setText(data.getStringExtra("contents"));
            } else {
                // Log error
            }
        }
    }
}
