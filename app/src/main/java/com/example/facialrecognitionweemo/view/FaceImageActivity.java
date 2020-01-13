package com.example.facialrecognitionweemo.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.facialrecognitionweemo.R;
import com.example.facialrecognitionweemo.model.ImageFile;
import com.example.facialrecognitionweemo.presenter.Presenter;
import com.example.facialrecognitionweemo.presenter.UserActionListener;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;

public class FaceImageActivity extends AppCompatActivity implements com.example.facialrecognitionweemo.view.View {


    private ImageView imageView;
    private ImageView imageView2;
    private ImageView imageView3;

    final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    public static final int REQUEST_CODE_IMAGE_CAPTURE = 0x1001;
    private static final int SELECT_PICTURE = 1;

    private String selectedImagePath;
    private UserActionListener userActionListener;
    private Integer option = 0;
    private Bitmap myBitmap;
    private Uri imgUri;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_image);

        userActionListener = new Presenter(this);

        imageView = (ImageView) findViewById(R.id.ivPhoto1);
        imageView2 = (ImageView) findViewById(R.id.ivPhoto2);
        imageView3 = (ImageView) findViewById(R.id.ivPhoto3);



        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photoOps();
                option = 0;
            }
        });

        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photoOps();
                option = 1;
            }
        });

        imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photoOps();
                option = 2;
            }
        });




    }



    private void photoOps() {
        try {
            userActionListener.takePicture(this);
        } catch (IOException ioe) {
            if (getBaseContext()!= null) {
                Toast.makeText(this, "ERROR", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void openCamera(Context context, ImageFile imageFile) {
        // Open the camera to take a picture.
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Check if there is a camera app installed to handle our Intent
        if (takePictureIntent.resolveActivity(this.getPackageManager()) != null) {
            //Use FileProvider to get the image file uri
            imgUri = FileProvider.getUriForFile(this,"com.example.facialrecognitionweemo", imageFile.getFile());
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);
            startActivityForResult(takePictureIntent, REQUEST_CODE_IMAGE_CAPTURE);
        } else {
            Toast.makeText(this, "NO PUEDE CONECTAR A LA CAMARA", Toast.LENGTH_LONG) .show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SELECT_PICTURE && resultCode == Activity.RESULT_OK) {
            Log.e("HERE","HERE");
            Uri selectedImageUri = data.getData();
            selectedImagePath = getPath(selectedImageUri);
            userActionListener.imageAvailable();
        } else if (REQUEST_CODE_IMAGE_CAPTURE == requestCode && Activity.RESULT_OK == resultCode) {
            // If an image is received, display it on the ImageView.
//            Log.e("HERE","HERE NOT "+data.getData());
            userActionListener.imageAvailable();
        } else {
            userActionListener.imageCaptureFailed();
            Log.e("HERE","HERE NOT NOT");
        }
    }

    /**
     * helper to retrieve the path of an image URI
     */
    private String getPath(Uri uri) {
        // just some safety built in
        if( uri == null ) {
            // TODO perform some logging or show user feedback
            return null;
        }
        // try to retrieve the image from the media store first
        // this will only work for images selected from gallery
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = this.managedQuery(uri, projection, null, null, null);
        if( cursor != null ){
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String path = cursor.getString(column_index);
            return path;
        }
        // this is our fallback here
        return uri.getPath();
    }

    @Override
    public void showImagePreview(@NonNull String imageUrl) {
        if (imageUrl == null) return;

        if (option == 0){
            Bitmap bp1 = BitmapFactory.decodeFile(imageUrl.replace("file://",""));
            Glide.with(this).load(imageUrl).into(imageView);
//            imageView.setImageBitmap(bp);
        } else if (option == 1){
            Bitmap bp2 = BitmapFactory.decodeFile(imageUrl.replace("file://",""));
            Glide.with(this).load(imageUrl).into(imageView2);
        } else {
            Bitmap bp3 = BitmapFactory.decodeFile(imageUrl.replace("file://",""));
            Glide.with(this).load(imageUrl).into(imageView3);
        }


    }

    @Override
    public void showImageError() {
        Toast.makeText(this, "ERROR 2", Toast.LENGTH_SHORT).show();
    }



}
