package danieljuric.borgwarner;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Sende Mail...", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                sendEmail("\n\n\nErsetze alle xxxxxxxxxx mit deiner Nachricht!", "[BW DMX Scanner] xxxxxxxxxx");
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
