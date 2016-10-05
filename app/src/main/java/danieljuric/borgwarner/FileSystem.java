package danieljuric.borgwarner;

import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

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
        File file = new File("/storage/emulated/0/BW-DMX-Scanner");
        if (!file.mkdirs()) {
            Log.e("ERROR", "Pfad nicht erstellt!");
        }

        return file;
    }

    public void saveToTxt(String text) {
        try {
            File file = new File("/storage/emulated/0/BW-DMX-Scanner");
            file.createNewFile();
            FileOutputStream fOut = new FileOutputStream(file);
            OutputStreamWriter OutWriter = new OutputStreamWriter(fOut);
            OutWriter.append(text);
            OutWriter.close();
            fOut.close();


        } catch (Exception e) {

        }
    }

    public String loadFromTxt() {
        try {
            FileInputStream inputStream = new FileInputStream("logfile.txt");
            String logfile = inputStream.toString();
            inputStream.close();
            return logfile;
        } catch (IOException e) {
            Log.e("Exception", "IO");
            e.printStackTrace();
            return "IO Exeption Error";
        }
    }
}
