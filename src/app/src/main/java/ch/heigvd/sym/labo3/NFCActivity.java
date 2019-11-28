package ch.heigvd.sym.labo3;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import ch.heigvd.sym.labo3.nfc.INFCOnActivity;
import ch.heigvd.sym.labo3.nfc.NFCReader;

/**
 * NFC activity class used to display the login form.
 * To be able to log in, we have to read the NFC tag that contains "test" message.
 *
 * @author Jael Dubey, Loris Gilliand, Mateo Tutic, Luc Wachter
 * @version 1.0
 * @since 2019-11-15
 */
public class NFCActivity extends AppCompatActivity implements INFCOnActivity {
    // Hard coded user credentials
    private static final String USERNAME = "toto";
    private static final String PASSWORD = "1234";

    // Graphics components
    private TextView txtUsername;
    private TextView txtPassword;
    private Button btnConnect;

    // This activity uses the NFCReader
    private NFCReader nfcReader;

    /**
     * Set text views and button and handles with NFC reader
     *
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

        // By creating the NFCReader object, we check if device supports NFC and if it's activated
        try {
            nfcReader = new NFCReader(this);
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        // Start NFCConnectedActivity if user credentials are correct.
        // The button is enabled only it the user has read the correct NFC tag
        btnConnect.setOnClickListener(v -> {
            if (txtUsername.getText().toString().equals(USERNAME) && txtPassword.getText().toString().equals(PASSWORD)) {
                Intent intent = new Intent(this, NFCConnectedActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Wrong username or/and password.", Toast.LENGTH_LONG).show();
            }
        });

        // Handle NFC intent with the NFCReader
        nfcReader.handleIntent(getIntent());
    }

    @Override
    protected void onResume() {
        super.onResume();
        // It's important, that the activity is in the foreground (resumed). Otherwise an IllegalStateException is
        // thrown.
        nfcReader.setupForegroundDispatch();
    }

    @Override
    protected void onPause() {
        // Call this before onPause, otherwise an IllegalArgumentException is thrown as well.
        nfcReader.stopForegroundDispatch();
        super.onPause();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        nfcReader.handleIntent(intent);
    }

    @Override
    public void handleNFCMessage(String message) {
        if (message.equals("test")) {
            btnConnect.setEnabled(true);
        }
    }
}
