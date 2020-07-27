package app.erp.com.erp_app.document_care;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import app.erp.com.erp_app.R;
import app.erp.com.erp_app.jsonparser.JSONParser;

public class CameraTest2Activity extends AppCompatActivity implements View.OnClickListener {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_IMAGE_GALLERY = 2;

    static final int REQUEST_VIDEO_CAPTURE = 3;
    static final int REQUEST_VIDEO_GALLERY = 4;

    String mCurrentPhotoPath;
    Uri imageUri;
    Uri photoURI, albumURI;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_test2);
        PermissionCheck();

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        Button pic_camera = (Button)findViewById(R.id.pic_camera);
        Button mv_camera = (Button)findViewById(R.id.mv_camera);
        Button pic_gallery = (Button)findViewById(R.id.pic_gallery);
        Button mv_gallery = (Button)findViewById(R.id.mv_gallery);

        pic_camera.setOnClickListener(this);
        mv_camera.setOnClickListener(this);
        pic_gallery.setOnClickListener(this);
        mv_gallery.setOnClickListener(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ImageUploadJson().execute();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.pic_camera :
                captureCamera(MediaStore.ACTION_IMAGE_CAPTURE,REQUEST_IMAGE_CAPTURE);
                break;
            case R.id.mv_camera :
                captureCamera(MediaStore.ACTION_VIDEO_CAPTURE,REQUEST_VIDEO_CAPTURE);
                break;
            case R.id.pic_gallery :
                getAlbum(REQUEST_IMAGE_GALLERY);
                break;
            case R.id.mv_gallery :
                getAlbum(REQUEST_VIDEO_GALLERY);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode){
                case REQUEST_IMAGE_CAPTURE :
                    galleryAddPic();
                    setImageView();
                    break;
                case REQUEST_VIDEO_CAPTURE :
                    galleryAddPic();
                    break;
                case REQUEST_IMAGE_GALLERY :
                    try {
                        // 선택한 이미지에서 비트맵 생성
                        InputStream in = getContentResolver().openInputStream(data.getData());
                        Uri selectedImage = data.getData();
                        Bitmap img = BitmapFactory.decodeStream(in);

                        int column_index=0;
                        String[] proj = {MediaStore.Images.Media.DATA};
                        Cursor cursor = getContentResolver().query(selectedImage, proj, null, null, null);
                        if(cursor.moveToFirst()){
                            column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                        }
                        String gallery_path =  cursor.getString(column_index);
                        in.close();
                        // 이미지뷰에 세팅
                        mCurrentPhotoPath = gallery_path;
                        setImageView();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case REQUEST_VIDEO_GALLERY :
                    try {
                        // 선택한 이미지에서 비트맵 생성
                        InputStream in = getContentResolver().openInputStream(data.getData());
                        Uri selectedImage = data.getData();
                        Bitmap img = BitmapFactory.decodeStream(in);

                        int column_index=0;
                        String[] proj = {MediaStore.Images.Media.DATA};
                        Cursor cursor = getContentResolver().query(selectedImage, proj, null, null, null);
                        if(cursor.moveToFirst()){
                            column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                        }
                        String gallery_path =  cursor.getString(column_index);
                        in.close();
                        // 이미지뷰에 세팅
                        mCurrentPhotoPath = gallery_path;
                        Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(mCurrentPhotoPath, MediaStore.Video.Thumbnails.FULL_SCREEN_KIND);
                        ((ImageView)findViewById(R.id.img_view)).setImageBitmap(bitmap);
//                        setImageView();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }

    private void setImageView(){
        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath);
        ExifInterface exif = null;

        try {
            exif = new ExifInterface(mCurrentPhotoPath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        int exifOrientation;
        int exifDegree;

        if (exif != null) {
            exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            exifDegree = exifOrientationToDegrees(exifOrientation);
        } else {
            exifDegree = 0;
        }
        ((ImageView)findViewById(R.id.img_view)).setImageBitmap(rotate(bitmap, exifDegree));
    }

    private void galleryAddPic(){
        Log.i("galleryAddPic", "Call");
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        // 해당 경로에 있는 파일을 객체화(새로 파일을 만든다는 것으로 이해하면 안 됨)
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        sendBroadcast(mediaScanIntent);
        Toast.makeText(this, "사진이 앨범에 저장되었습니다.", Toast.LENGTH_SHORT).show();
    }

    private void getAlbum(int intentType){
        Log.i("getAlbum", "Call");
        Intent intent = new Intent(Intent.ACTION_PICK);
        if(intentType == 2){
            intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
        }else if(intentType == 4){
            intent.setType(MediaStore.Video.Media.CONTENT_TYPE);
        }
//        intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, intentType);
    }

    private void captureCamera(String cameraType , int intenType){
        String state = Environment.getExternalStorageState();
        // 외장 메모리 검사
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            Intent takePictureIntent = new Intent(cameraType);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                File photoFile = null;
                try {
                    photoFile = createImageFile(intenType);
                } catch (IOException ex) {
                    Log.e("captureCamera Error", ex.toString());
                }
                if (photoFile != null) {
                    // getUriForFile의 두 번째 인자는 Manifest provier의 authorites와 일치해야 함
                    Uri providerURI = FileProvider.getUriForFile(this, getPackageName(), photoFile);
                    imageUri = providerURI;
                    // 인텐트에 전달할 때는 FileProvier의 Return값인 content://로만!!, providerURI의 값에 카메라 데이터를 넣어 보냄
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, providerURI);
                    startActivityForResult(takePictureIntent, intenType);
                }
            }
        } else {
            Toast.makeText(this, "저장공간이 접근 불가능한 기기입니다", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    public File createImageFile(int intenType) throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "";
        if(intenType == 1){
            imageFileName = "JPEG_" + timeStamp + ".jpg";
        }else{
            imageFileName = "MP4_" + timeStamp + ".mp4";
        }
        File imageFile = null;
        File storageDir = new File(Environment.getExternalStorageDirectory() + "/IERP");

        if (!storageDir.exists()) {
            Log.i("mCurrentPhotoPath1", storageDir.toString());
            storageDir.mkdirs();
        }

        imageFile = new File(storageDir, imageFileName);
        mCurrentPhotoPath = imageFile.getAbsolutePath();

        return imageFile;
    }

    void PermissionCheck(){
        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                String savePath = Environment.getExternalStoragePublicDirectory("IERP").toString();
                File dir = new File(savePath);
                if(!dir.exists())
                {
                    dir.mkdirs();    // 폴더를 만든다
                }
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
            }
        };

        TedPermission.with(this)
                .setPermissionListener(permissionListener)
                .setRationaleMessage("구글 로그인을 하기 위해서는 주소록 접근 권한이 필요해요")
                .setDeniedMessage("왜 거부하셨어요...\n하지만 [설정] > [권한] 에서 권한을 허용할 수 있어요.")
                .setPermissions(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE , Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check();
    }
    private int exifOrientationToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }
    private Bitmap rotate(Bitmap bitmap, float degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    private class ImageUploadJson extends AsyncTask<Void , Void , Boolean>{
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(CameraTest2Activity.this);
            progressDialog.setMessage("업로드 중...");
            progressDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
//            try {
////                JSONObject jsonObject = JSONParser.uploadImage(mCurrentPhotoPath);
////                if (jsonObject != null)
////                    return jsonObject.getString("result").equals("success");
//
//            } catch (JSONException e) {
//                Log.i("TAG", "Error : " + e.getLocalizedMessage());
//            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (progressDialog != null)
                progressDialog.dismiss();
        }
    }
}
