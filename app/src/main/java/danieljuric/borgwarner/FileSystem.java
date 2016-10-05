package danieljuric.borgwarner;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by dajuric on 05.10.2016.
 */

public class FileSystem {
    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    public File getStorageDir(String name) {
        // Gets the dir
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.getExternalStorageState()), name);
        if (!file.mkdirs()) {
            Log.e("ERROR", "Pfad nicht erstellt!");
        }

        return file;
    }
}
