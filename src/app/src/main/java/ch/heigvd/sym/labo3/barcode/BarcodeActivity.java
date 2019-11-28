package ch.heigvd.sym.labo3.barcode;

import androidx.appcompat.app.AppCompatActivity;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode);

        TextView barCodeValue = findViewById(R.id.barcodeValue);

        Button scanButton = findViewById(R.id.scanButton);

        // TODO start activity for result
        scanButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, ScanActivity.class);
            startActivity(intent);
        });
    }
}
