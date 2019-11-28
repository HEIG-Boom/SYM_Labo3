package ch.heigvd.sym.labo3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import ch.heigvd.sym.labo3.barcode.BarcodeActivity;

/**
 * Main activity class used to display the two buttons to start NFC and Barcode activities
 *
 * @author Jael Dubey, Loris Gilliand, Mateo Tutic, Luc Wachter
 * @version 1.0
 * @since 2019-11-15
 */
public class MainActivity extends AppCompatActivity {
    // Graphics components
    private Button btnNFC = null;
    private Button btnBarcode = null;

    /**
     * Set up buttons used to launch NFC and Barcode activities
     *
     * @param savedInstanceState Bundle object containing the activity's previously saved state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnNFC = findViewById(R.id.btnNFC);
        btnBarcode = findViewById(R.id.btnBarcode);

        // Launch the appropriate activity
        btnNFC.setOnClickListener((v) -> {
            Intent intent = new Intent(this, NFCActivity.class);
            startActivity(intent);
        });

        btnBarcode.setOnClickListener((v) -> {
            Intent intent = new Intent(this, BarcodeActivity.class);
            startActivity(intent);
        });
    }
}
