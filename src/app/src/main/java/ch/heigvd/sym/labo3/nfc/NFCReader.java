package ch.heigvd.sym.labo3.nfc;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.AsyncTask;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import ch.heigvd.sym.labo3.NFCActivity;

/**
 * This class handles all elements needed to read an NFC tag
 *
 * @author Jael Dubey, Loris Gilliand, Mateo Tutic, Luc Wachter
 * @version 1.0
 * @since 2019-11-15
 */
public class NFCReader {
    private static final String MIME_TEXT_PLAIN = "text/plain";
    private static final String TAG = "NFCLogin";

    private final Activity activity;    // The corresponding activity requesting the foreground dispatch
    private NfcAdapter mNfcAdapter;     // NFC core adapter

    /**
     * Constructor of NFCReader
     *
     * @param activity Activity that instantiates NFCReader
     */
    public NFCReader(Activity activity) throws Exception {
        this.activity = activity;

        mNfcAdapter = NfcAdapter.getDefaultAdapter(activity);

        // Check if the device supports NFC
        if (mNfcAdapter == null) {
            throw new Exception("This device doesn't support NFC");
        }

        // Check if the device has NFC activated
        if (!mNfcAdapter.isEnabled()) {
            throw new Exception("Please enable NFC");
        }
    }

    /**
     * Setup the foreground dispatch system that allows an activity to intercept an intent and claim priority over
     * other activities that handle the same intent
     */
    public void setupForegroundDispatch() {
        final Intent intent = new Intent(activity.getApplicationContext(), activity.getClass());
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        final PendingIntent pendingIntent = PendingIntent.getActivity(activity.getApplicationContext(), 0, intent, 0);

        IntentFilter[] filters = new IntentFilter[1];
        String[][] techList = new String[][]{};

        // Notice that this is the same filter as in our manifest.
        filters[0] = new IntentFilter();
        filters[0].addAction(NfcAdapter.ACTION_NDEF_DISCOVERED);
        filters[0].addCategory(Intent.CATEGORY_DEFAULT);
        try {
            filters[0].addDataType(MIME_TEXT_PLAIN);
        } catch (IntentFilter.MalformedMimeTypeException e) {
            Log.e(TAG, "MalformedMimeTypeException", e);
        }

        mNfcAdapter.enableForegroundDispatch(activity, pendingIntent, filters, techList);
    }

    /**
     * Stops the foreground dispatch system
     */
    public void stopForegroundDispatch() {
        mNfcAdapter.disableForegroundDispatch(activity);
    }

    /**
     * Handles new NDEF intent that encapsulates the NFC tag
     *
     * @param intent Intent that encapsulates the NFC tag and its identifying information
     */
    public void handleIntent(Intent intent) {
        String action = intent.getAction();
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            String type = intent.getType();
            if (MIME_TEXT_PLAIN.equals(type)) {
                Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                new NdefReaderTask().execute(tag);
            } else {
                Log.d(TAG, "Wrong mime type: " + type);
            }
        }
    }

    /**
     * Background task for reading the data in NFC Tag
     */
    private class NdefReaderTask extends AsyncTask<Tag, Void, String> {

        @Override
        protected String doInBackground(Tag... params) {
            Tag tag = params[0];

            Ndef ndef = Ndef.get(tag);

            if (ndef == null) {
                // NDEF is not supported by this Tag.
                return null;
            }

            NdefMessage ndefMessage = ndef.getCachedNdefMessage();

            NdefRecord[] records = ndefMessage.getRecords();
            for (NdefRecord ndefRecord : records) {
                if (ndefRecord.getTnf() == NdefRecord.TNF_WELL_KNOWN && Arrays.equals(ndefRecord.getType(),
                        NdefRecord.RTD_TEXT)) {
                    try {
                        return readText(ndefRecord);
                    } catch (UnsupportedEncodingException e) {
                        Log.e(TAG, "Unsupported Encoding", e);
                    }
                }
            }

            return null;
        }

        /**
         * Read the message contained in the NDEF record
         *
         * @param record NDEF record that encapsulates typed data
         * @return The text contained in the NDEF record
         * @throws UnsupportedEncodingException If the encoding is not supported
         */
        private String readText(NdefRecord record) throws UnsupportedEncodingException {
            byte[] payload = record.getPayload();

            // Get the Text Encoding
            String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16";

            // Get the Language Code
            int languageCodeLength = payload[0] & 0063;

            // Get the Text
            return new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1, textEncoding);
        }

        @Override
        protected void onPostExecute(String result) {
            // This is the activity that will handle the content in the NDEF record
            ((NFCActivity) activity).handleNFCMessage(result);
        }
    }
}
