package danieljuric.borgwarner;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*   Infos schreiben */
         final String text = "Programmierer: Daniel Juric\nStart Datum: 26. September 2016\nAlle Rechte Vorbehalten\n\n\nBedienungsanleitung:\n\nDie Lautstärke Tasten aktivieren die Taschenlampe, sodass du auch im dunklen scannen kannst.\nAchte darauf dass der DMX Code in der Mitte der Kamera ist und gut lesbar ist.";
        TextView about_text_all = (TextView) findViewById(R.id.about_text_all);
        about_text_all.setText(text);
        /*  Infos schreiben ende */

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Sende Mail...", Snackbar.LENGTH_LONG).setAction("Action", null).show();

                String debugInfo ="Debug-infos:";
                debugInfo += "\n OS Version: " + System.getProperty("os.version") + "(" + android.os.Build.VERSION.INCREMENTAL + ")";
                debugInfo += "\n OS API Level: " + android.os.Build.VERSION.SDK_INT;
                debugInfo += "\n Device: " + android.os.Build.DEVICE;
                debugInfo += "\n Model (and Product): " + android.os.Build.MODEL + " ("+ android.os.Build.PRODUCT + ")";
                debugInfo += "\n Timestamp: " + Build.TIME;
                debugInfo += "\n Serialcode: " + Build.SERIAL;
                debugInfo += "\n Display: " + Build.DISPLAY;
                debugInfo += "\n Bootloader: " + Build.BOOTLOADER;
                debugInfo += "\n Hardware: " + Build.HARDWARE;
                debugInfo += "\n Host: " + Build.HOST;


                sendEmail("\n\n\n========== Alles hier drunter nicht verändern! ==========\n\n" + debugInfo, "[BW DMX Scanner] Supportanfrage");
            }
        });
    }

    public void sendEmail(String text, String subject) {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822"); // message/rfc822 text/plain
        i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"mine6626@gmail.com", "jbienroth@borgwarner.com"});
        i.putExtra(Intent.EXTRA_SUBJECT, subject);
        i.putExtra(Intent.EXTRA_TEXT   , text);
        try {
            startActivity(Intent.createChooser(i, "Mail senden..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(AboutActivity.this, "Kein Email Programm installiert!", Toast.LENGTH_SHORT).show();
        }
    }

}
