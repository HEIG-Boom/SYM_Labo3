package ch.heigvd.sym.labo3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class BarcodeActivity extends AppCompatActivity {

    public static TextView barCodeValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode);

        barCodeValue = findViewById(R.id.barcodeValue);

        Button scanButton = findViewById(R.id.scanButton);

        scanButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, ScanActivity.class);
            startActivity(intent);
        });

    }
}
