package com.tspolice.echallan.utils;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.net.Uri;

import java.io.File;

public class SingleMediaScanner implements MediaScannerConnection.MediaScannerConnectionClient {

    private MediaScannerConnection mediaScannerConnection;
    private File file;

    public SingleMediaScanner(Context context, File file) {
        this.mediaScannerConnection = new MediaScannerConnection(context, SingleMediaScanner.this);
        this.file = file;
        mediaScannerConnection.connect();
    }

    @Override
    public void onMediaScannerConnected() {
        mediaScannerConnection.scanFile(file.getAbsolutePath(), null);
    }

    @Override
    public void onScanCompleted(String path, Uri uri) {
        mediaScannerConnection.disconnect();
    }
}
