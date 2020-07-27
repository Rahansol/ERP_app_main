package app.erp.com.erp_app;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import java.util.Map;

/**
 * Created by samsung on 2017-08-28.
 */

public class CustomScannerReserveItemActivity extends Activity implements DecoratedBarcodeView.TorchListener{

    private CaptureManager capture;
    private DecoratedBarcodeView barcodeScannerView;
    private ImageButton setting_btn,switchFlashlightButton;
    private Boolean switchFlashlightButtonCheck;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Button scan_status;
    TextView zxing_status_view , scan_barcode_info, scan_area, scan_unit_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserve_custom_scanner);
        zxing_status_view = (TextView)findViewById(R.id.zxing_status_view);
        pref= this.getSharedPreferences("scan_status", this.MODE_PRIVATE);
        editor = pref.edit();

        scan_status = (Button)findViewById(R.id.scan_status);
        scan_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putString("scan_type","stop");
                editor.commit();
                finish();
            }
        });

        // 바코드 스캔 정보
        scan_barcode_info = (TextView)findViewById(R.id.scan_barcode_info);
        scan_barcode_info.setText(pref.getString("scan_barcode",""));

        scan_area = (TextView)findViewById(R.id.scan_area);
        scan_area.setText(pref.getString("scan_area",""));

        scan_unit_name = (TextView)findViewById(R.id.scan_unit_name);
        scan_unit_name.setText(pref.getString("scan_unit",""));

        switchFlashlightButtonCheck = true;
//        setting_btn = (ImageButton)findViewById(R.id.setting_btn);
        switchFlashlightButton = (ImageButton)findViewById(R.id.switch_flashlight);

        if (!hasFlash()) {
            switchFlashlightButton.setVisibility(View.GONE);
        }

        barcodeScannerView = (DecoratedBarcodeView)findViewById(R.id.zxing_barcode_scanner);
        barcodeScannerView.setTorchListener(this);
        capture = new CaptureManager(this, barcodeScannerView);
        capture.initializeFromIntent(getIntent(), savedInstanceState);
        capture.decode();
    }

    @Override
    protected void onResume() {
        super.onResume();
        capture.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        capture.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        capture.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        capture.onSaveInstanceState(outState);
    }

    public void onBackPressed() {
        editor.putString("scan_type","stop");
        editor.commit();
        finish();
    }

    public void switchFlashlight(View view) {
        if (switchFlashlightButtonCheck) {
            barcodeScannerView.setTorchOn();
        } else {
            barcodeScannerView.setTorchOff();
        }
    }

    private boolean hasFlash() {
        return getApplicationContext().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
    }

    @Override
    public void onTorchOn() {
        switchFlashlightButton.setImageResource(R.drawable.ic_flash_on_white_36dp);
        switchFlashlightButtonCheck = false;
    }

    @Override
    public void onTorchOff() {
        switchFlashlightButton.setImageResource(R.drawable.ic_flash_off_white_36dp);
        switchFlashlightButtonCheck = true;
    }
}