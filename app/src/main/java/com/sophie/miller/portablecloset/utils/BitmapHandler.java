package com.sophie.miller.portablecloset.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import java.io.ByteArrayOutputStream;
import java.io.File;

import static java.security.AccessController.getContext;

public class BitmapHandler {
    private static final String FILE_PROVIDER_AUTHORITY = "com.sophie.miller.portablecloset.fileprovider";
    private static final String PATH = "PortableClosetImages/";


    /**
     * takes Bitmap as input and returns byte[]
     *
     * @param imageBitmap
     * @return byte[] of Bitmap input
     */
    public byte[] bitmapToByteArray(Bitmap imageBitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }

    /**
     * takes byte[] and returns Bitmap
     *
     * @param image
     * @return Bitmap from the byte[] input
     */
    public Bitmap byteArrayToBitmap(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }
    //
    public void loadImage(Context context) {
        File imagePath = new File(context.getFilesDir(), PATH);
        File newFile = new File(imagePath, "default_image.jpg");//todo fix add name
        // Uri contentUri = getUriForFile(getContext(), "com.mydomain.fileprovider", newFile);
    }

    public boolean deleteImage(String imagePath) {
        //get image
        File imageFile = new File(imagePath);

        // Delete the image
        return imageFile.delete();
    }
    public void saveImage(){

    }
}
