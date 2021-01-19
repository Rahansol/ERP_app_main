package app.erp.com.erp_app.document_care;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.zxing.integration.android.IntentIntegrator;

import java.net.URI;

import app.erp.com.erp_app.R;

public class PreviewDialogActivity extends AppCompatActivity {

    ImageView preview_dialog;
    TextView tv_ok_dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_dialog);

        SharedPreferences pref= getSharedPreferences("imgUri_data", MODE_PRIVATE);
        String st_imgUri= pref.getString("imgUri", null);
        Uri imgUri= Uri.parse(st_imgUri);
        if (imgUri != null){
            preview_dialog= findViewById(R.id.preview_dialog);
            //preview_dialog.setImageURI(imgUri);
            Glide.with(this).load(imgUri).into(preview_dialog);
        }else {
            Toast.makeText(this, "사진을 선택하세요.", Toast.LENGTH_SHORT).show();
        }

        tv_ok_dialog= findViewById(R.id.tv_ok_dialog);
        tv_ok_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

}