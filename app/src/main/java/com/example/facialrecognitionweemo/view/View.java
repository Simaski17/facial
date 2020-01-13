package com.example.facialrecognitionweemo.view;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.facialrecognitionweemo.model.ImageFile;

/**
 * Created by ptyagi on 4/12/17.
 */

public interface View {
    void openCamera(Context context, ImageFile imageFile);
    void showImagePreview(@NonNull String uri);
    void showImageError();
}
