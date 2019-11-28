package ch.heigvd.sym.labo3.barcode;

import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

public class BaseScannerActivity extends AppCompatActivity {
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Respond to the action bar's Up/Home button
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
