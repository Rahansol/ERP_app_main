package app.erp.com.erp_app.document_care.myfragments;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import app.erp.com.erp_app.R;

public class CropImageActivity extends AppCompatActivity {

    Button btnCropImage;
    CropImageView ivCropImage;
    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop_image);

        context= getBaseContext();
        btnCropImage= findViewById(R.id.btn_cropImage);
        ivCropImage= findViewById(R.id.iv_cropImage);

        btnCropImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(this);

        CropImage.activity(G.CAPTURED_IMAGE_URI)
                .start(this);

      //  CropImage.activity()
                //.start(context, );

    }




}