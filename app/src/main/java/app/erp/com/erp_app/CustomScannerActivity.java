package app.erp.com.erp_app;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

/**
 * Created by samsung on 2017-08-28.
 */

public class CustomScannerActivity extends Activity implements DecoratedBarcodeView.TorchListener{

    private CaptureManager capture;
    private DecoratedBarcodeView barcodeScannerView;
    private ImageButton setting_btn,switchFlashlightButton;
    private Boolean switchFlashlightButtonCheck;
    SharedPreferences pref;
    TextView zxing_status_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_scanner);
        zxing_status_view = (TextView)findViewById(R.id.zxing_status_view);
        pref= this.getSharedPreferences("barcode_type", this.MODE_PRIVATE);
        String camera_type =  pref.getString("camera_type",null) ;
        if(camera_type.equals("bus")){
            zxing_status_view.setText("차량 바코드를 \n 스캔해주세요.");
        }else if (camera_type.equals("unit")){
            zxing_status_view.setText("단말기 바코드를 \n 스캔해주세요.");
        }else if(camera_type.equals("office")){
            zxing_status_view.setText("영업소 바코드를 \n 스캔해주세요.");
        }else{
            zxing_status_view.setText("바코드를 \n 스캔해주세요.");
        }

        switchFlashlightButtonCheck = true;

        setting_btn = (ImageButton)findViewById(R.id.setting_btn);
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