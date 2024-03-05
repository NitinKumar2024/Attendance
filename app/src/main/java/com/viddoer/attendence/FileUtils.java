package com.viddoer.attendence;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.webkit.MimeTypeMap;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileUtils {

    public static File getFileFromUri(Context context, Uri uri) {
        InputStream inputStream = null;
        FileOutputStream outputStream = null;
        try {
            ContentResolver contentResolver = context.getContentResolver();
            String extension = getFileExtension(contentResolver, uri);
            File file = createTempFile(context, extension);
            if (file == null) {
                return null;
            }

            inputStream = contentResolver.openInputStream(uri);
            if (inputStream == null) {
                return null;
            }

            outputStream = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
            return file;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private static String getFileExtension(ContentResolver contentResolver, Uri uri) {
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private static File createTempFile(Context context, String extension) throws IOException {
        String fileName = "temp_file";
        File directory = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        if (directory == null) {
            directory = context.getFilesDir();
        }
        return File.createTempFile(fileName, "." + extension, directory);
    }
}
