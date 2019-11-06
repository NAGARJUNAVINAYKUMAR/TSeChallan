package com.tspolice.echallan.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.util.Log;

import com.tspolice.echallan.R;

import java.io.File;

public class CameraUtils {

    public static final int REQUEST_IMAGE_CAPTURE = 111;
    public static final int REQUEST_VIDEO_CAPTURE = 222;
    public static final int REQUEST_PICK_GALLERY = 333;
    public static final int MEDIA_TYPE_IMAGE = 1;
    private static final int MEDIA_TYPE_VIDEO = 2;
    public static final String KEY_IMAGE_PATH = "KEY_IMAGE_PATH";
    public static final int BITMAP_SAMPLE_SIZE = 8;

    /**
     * Refreshes gallery on adding new image/video. Gallery won't be refreshed
     * on older devices until device is rebooted
     */
    public static boolean isDeviceSupportCamera(Context context) {
        return !context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    /* Creates and returns the image or video file before opening the camera */
    public static File getOutputMediaFile(Context context, final int type) {
        // External sdcard location
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory
                (Environment.DIRECTORY_PICTURES), context.getResources().getString(R.string.app_name));
        // Creates storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.e(Constants.APP_LOG, "Oops! Failed create directory");
                return null;
            }
        }
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG" + DateUtils.getTimeStamp() + ".jpg");
        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "VID" + DateUtils.getTimeStamp() + ".mp4");
        } else {
            return null;
        }
        return mediaFile;
    }

    public static Uri getOutputMediaFileUri(Context context, File file) {
        return FileProvider.getUriForFile(context, context.getPackageName() + ".provider", file);
    }

    /**
     * Refreshes gallery on adding new image/video. Gallery won't be refreshed
     * on older devices until device is rebooted
     */
    public static void refreshGallery(Context context, String filePath) {
        // ScanFile so it will be appeared on Gallery
        MediaScannerConnection.scanFile(context, new String[]{filePath}, null,
                new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, Uri uri) {
                        Log.i(Constants.APP_LOG, "Gallery refreshed...");
                    }
                });
    }

    /**
     * Downsizing the bitmap to avoid OutOfMemory exceptions
     */
    public static Bitmap optimizeBitmap(int sampleSize, String filePath) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        // downsizing image as it throws OutOfMemory Exception for larger images
        options.inSampleSize = sampleSize;
        return BitmapFactory.decodeFile(filePath, options);
    }
}
