package ch.heigvd.sym.labo3.barcode;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import ch.heigvd.sym.labo3.R;

// TODO comments, headers
// Source : http://www.codeplayon.com/2018/10/android-create-a-bar-code-scanner-zxingscannerview/
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
