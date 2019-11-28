package ch.heigvd.sym.labo3.nfc;

/**
 * Used for activities that will use NFC with NFCReader class
 *
 * @author Jael Dubey, Loris Gilliand, Mateo Tutic, Luc Wachter
 * @version 1.0
 * @since 2019-11-15
 */
public interface INFCOnActivity {
    /**
     * Handles the message read in the NDEF record
     * @param message the message read in the NFC tag
     */
    void handleNFCMessage(String message);
}
