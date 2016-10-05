package danieljuric.borgwarner;

import android.os.Environment;
import android.util.Log;

import java.io.File;

/**
 * Erstellt von: dajuric
 * Datum: 05.10.2016
 * Packagename: ${PACKAGE_NAME}
 */

class FileSystem {
    /* Checks if external storage is available for read and write */
    boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
    }

    File getStorageDir(String name) {
        // Gets the dir
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.getExternalStorageState()), name);
        if (!file.mkdirs()) {
            Log.e("ERROR", "Pfad nicht erstellt!");
        }

        return file;
    }
}
