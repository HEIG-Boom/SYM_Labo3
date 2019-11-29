package ch.heigvd.sym.labo3;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;

import ch.heigvd.sym.labo3.nfc.INFCOnActivity;
import ch.heigvd.sym.labo3.nfc.NFCReader;
import ch.heigvd.sym.labo3.security.SecurityLevel;

/**
 * NFC connected activity used to display the current security level.
 * The user can check which security level he's in by means of buttons
 *
 * @author Jael Dubey, Loris Gilliand, Mateo Tutic, Luc Wachter
 * @version 1.0
 * @since 2019-11-15
 */
public class NFCConnectedActivity extends AppCompatActivity implements INFCOnActivity {
    // Graphics components
    private Button btnMaxSecurity;
    private Button btnMedSecurity;
    private Button btnMinSecurity;

    private SecurityLevel sLevel = SecurityLevel.HIGH;  // By default the security level is at the highest level
    private Timer timer = new Timer();                  // Timer used to decrease security level

    // This activity uses the NFCReader
    private NFCReader nfcReader;

    /**
     * Setup buttons to check the security level
     *
     * @param savedInstanceState Bundle object containing the activity's previously saved state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfcconnected);

        // Get graphics elements
        btnMaxSecurity = findViewById(R.id.btnMaxSecurity);
        btnMedSecurity = findViewById(R.id.btnMedSecurity);
        btnMinSecurity = findViewById(R.id.btnMinSecurity);

        // Add listener on each buttons
        btnMaxSecurity.setOnClickListener(v -> {
            if (sLevel == SecurityLevel.HIGH) {
                showSuccess();
            } else {
                showDefeat();
            }
        });

        btnMedSecurity.setOnClickListener(v -> {
            if (sLevel == SecurityLevel.HIGH || sLevel == SecurityLevel.MEDIUM) {
                showSuccess();
            } else {
                showDefeat();
            }
        });

        btnMinSecurity.setOnClickListener(v -> {
            if (sLevel == SecurityLevel.HIGH || sLevel == SecurityLevel.MEDIUM || sLevel == SecurityLevel.LOW) {
                showSuccess();
            } else {
                showDefeat();
            }
        });

        // Configure the timer to decrease security level every 10 seconds
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                decreaseSecurityLevel();
            }
        };
        timer.scheduleAtFixedRate(task, 10000, 10000);

        // By creating the NFCReader object, we check if device supports NFC and if it's activated
        try {
            nfcReader = new NFCReader(this);
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        // Handle NFC intent with the NFCReader
        nfcReader.handleIntent(getIntent());
    }

    /**
     * Decreases the security level
     */
    private void decreaseSecurityLevel() {
        switch (sLevel) {
            case HIGH:
                sLevel = SecurityLevel.MEDIUM;
                break;
            case MEDIUM:
                sLevel = SecurityLevel.LOW;
                break;
            case LOW:
                sLevel = SecurityLevel.NULL;
                break;
        }
    }

    /**
     * Let the user know that he has access to the specified security level
     */
    private void showSuccess() {
        Toast.makeText(this, "Sufficient security level", Toast.LENGTH_LONG).show();
    }

    /**
     * Let the user know that he doesn't have access to the specified security level
     */
    private void showDefeat() {
        Toast.makeText(this, "Insufficient security level", Toast.LENGTH_LONG).show();
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
        // When we read the nfc tag with test message, we reset the security level to maximum
        if (message.equals("test")) {
            sLevel = SecurityLevel.HIGH;
            Toast.makeText(this, "Security level reset to the maximum", Toast.LENGTH_LONG).show();
        }
    }
}
