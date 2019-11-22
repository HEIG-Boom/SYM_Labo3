package ch.heigvd.sym.labo3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class NFCActivity extends AppCompatActivity {
    // Graphics components
    private TextView txtUsername;
    private TextView txtPassword;
    private Button btnConnect;

    /**
     * Set text views and button used to connect with NFC
     * @param savedInstanceState Bundle object containing the activity's previously saved state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc);

        // Get graphics elements
        txtUsername = findViewById(R.id.txtUsername);
        txtPassword = findViewById(R.id.txtPassword);
        btnConnect = findViewById(R.id.btnConnect);

        // Connect with NFC
        btnConnect.setOnClickListener((v) -> {
            // TODO Check username, password and that NFC reads the correct data
        });
    }
}
