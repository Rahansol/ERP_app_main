package app.erp.com.erp_app.document_care.myfragments;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import app.erp.com.erp_app.R;

public class ImageUploadDialog extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_upload_dialog);
    }

    public void clickOK(View view) {
        finish();
    }
}