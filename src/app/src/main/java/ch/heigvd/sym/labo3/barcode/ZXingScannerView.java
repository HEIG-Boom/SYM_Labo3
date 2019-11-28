package ch.heigvd.sym.labo3.barcode;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.hardware.Camera;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.Log;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.PlanarYUVLuminanceSource;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import me.dm7.barcodescanner.core.BarcodeScannerView;
import me.dm7.barcodescanner.core.DisplayUtils;

// TODO rename things and log exceptions!!!
/**
 * Actual scanner view, integrated in the app
 *
 * Source : http://www.codeplayon.com/2018/10/android-create-a-bar-code-scanner-zxingscannerview/
 *
 * @author Jael Dubey, Loris Gilliand, Mateo Tutic, Luc Wachter
 * @version 1.0
 * @since 2019-11-28
 */
public class ZXingScannerView extends BarcodeScannerView {
    private static final String TAG = "ZXingScannerView";

    public interface ResultHandler {
        void handleResult(Result rawResult);
    }

    private MultiFormatReader mMultiFormatReader;
    public static final List<BarcodeFormat> ALL_FORMATS = new ArrayList<>();
    private ResultHandler mResultHandler;

    static {
        ALL_FORMATS.add(BarcodeFormat.UPC_A);
        ALL_FORMATS.add(BarcodeFormat.UPC_E);
        ALL_FORMATS.add(BarcodeFormat.EAN_13);
        ALL_FORMATS.add(BarcodeFormat.EAN_8);
        ALL_FORMATS.add(BarcodeFormat.RSS_14);
        ALL_FORMATS.add(BarcodeFormat.CODE_39);
        ALL_FORMATS.add(BarcodeFormat.CODE_93);
        ALL_FORMATS.add(BarcodeFormat.CODE_128);
        ALL_FORMATS.add(BarcodeFormat.ITF);
        ALL_FORMATS.add(BarcodeFormat.CODABAR);
        ALL_FORMATS.add(BarcodeFormat.QR_CODE);
        ALL_FORMATS.add(BarcodeFormat.DATA_MATRIX);
        ALL_FORMATS.add(BarcodeFormat.PDF_417);
    }

    public ZXingScannerView(Context context) {
        super(context);
        initMultiFormatReader();
    }

    public ZXingScannerView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initMultiFormatReader();
    }

    private void initMultiFormatReader() {
        Map<DecodeHintType, Object> hints = new EnumMap<>(DecodeHintType.class);
        hints.put(DecodeHintType.POSSIBLE_FORMATS, ALL_FORMATS);
        mMultiFormatReader = new MultiFormatReader();
        mMultiFormatReader.setHints(hints);
    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        if (mResultHandler == null) {
            return;
        }

        try {
            Camera.Parameters parameters = camera.getParameters();
            Camera.Size size = parameters.getPreviewSize();
            int width = size.width;
            int height = size.height;

            if (DisplayUtils.getScreenOrientation(getContext()) == Configuration.ORIENTATION_PORTRAIT) {
                byte[] rotatedData = new byte[data.length];
                for (int y = 0; y < height; y++) {
                    for (int x = 0; x < width; x++)
                        rotatedData[x * height + height - y - 1] = data[x + y * width];
                }
                int tmp = width;
                width = height;
                height = tmp;
                data = rotatedData;
            }

            Result rawResult = null;
            PlanarYUVLuminanceSource source = buildLuminanceSource(data, width, height);

            if (source != null) {
                BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
                try {
                    rawResult = mMultiFormatReader.decodeWithState(bitmap);
                } catch (ReaderException re) {
                    // continue
                } catch (NullPointerException npe) {
                    // This is terrible
                } catch (ArrayIndexOutOfBoundsException e) {
                } finally {
                    mMultiFormatReader.reset();
                }
            }

            final Result finalRawResult = rawResult;
            if (finalRawResult != null) {
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(() -> {
                    // Stopping the preview can take a little long.
                    // So we want to set result handler to null to discard subsequent calls to
                    // onPreviewFrame.
                    ResultHandler tmpResultHandler = mResultHandler;
                    mResultHandler = null;
                    stopCameraPreview();
                    if (tmpResultHandler != null) {
                        tmpResultHandler.handleResult(finalRawResult);
                    }
                });
            } else {
                camera.setOneShotPreviewCallback(this);
            }
        } catch (RuntimeException e) {
            // TODO: Terrible hack. It is possible that this method is invoked after camera is released.
            Log.e(TAG, e.toString(), e);
        }
    }

    public PlanarYUVLuminanceSource buildLuminanceSource(byte[] data, int width, int height) {
        Rect rect = getFramingRectInPreview(width, height);
        if (rect == null) {
            return null;
        }
        // Go ahead and assume it's YUV rather than die.
        PlanarYUVLuminanceSource source = null;
        try {
            source = new PlanarYUVLuminanceSource(data, width, height, rect.left, rect.top, rect.width(), rect.height(), false);
        } catch (Exception e) {
        }
        return source;
    }
}
