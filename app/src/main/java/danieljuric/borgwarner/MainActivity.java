package danieljuric.borgwarner;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity {
    private AdView mAdView;
    String allCodes;
    String scanContent2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);

        // Ad Banner Main Unten
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        // Finde die Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // Setze Toolbar zu Actionbar für diese Activity
        // Die Toolbar darf nicht null sein
        setSupportActionBar(toolbar);

        FileSystem fileSystem = new FileSystem();
        Toast.makeText(this, fileSystem.loadFromTxt(), Toast.LENGTH_LONG).show();


        final Button main_button_encode = (Button) findViewById(R.id.main_button_encode);
        main_button_encode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator scanIntegrator = new IntentIntegrator(MainActivity.this);
                scanIntegrator.setPrompt("Scanne den NormCode");
                scanIntegrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES );
                scanIntegrator.setBeepEnabled(true);
                scanIntegrator.setOrientationLocked(false);
                scanIntegrator.setBarcodeImageEnabled(true);
                scanIntegrator.initiateScan();
            }
        });

    }

    /**
     *
     *  TOOLBAR
     *
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Fügt die Menu Items hinzu
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_about:
                openAboutActivity();
                return true;
            case R.id.menu_item_share_email:
                sendEmail(getAllCodes(), "[" + getScanContent() + "]");
                return true;
            case R.id.menu_item_share_whatsapp:
                sendWhatsapp(getAllCodes());
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     *
     * AB HIER ALLES NACH DEM SCAN
     *
     */



    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        Vibrator vibration = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        startVibration(vibration);




        final IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);

        if (scanningResult != null) {
            final String scanContent = scanningResult.getContents();
            //Toast.makeText(this, "Code Erkannt:\n" + scanContent, Toast.LENGTH_LONG).show();

            TextView main_code_string                          = (TextView) findViewById(R.id.main_code_string);
            TextView main_code_encoded                         = (TextView) findViewById(R.id.main_code_encoded);
            final TextView main_code_type_string               = (TextView) findViewById(R.id.main_code_type_string);
            final TextView main_code_plant_string              = (TextView) findViewById(R.id.main_code_plant_string);
            final TextView main_code_inventoryOfMachine_string = (TextView) findViewById(R.id.main_code_inventoryOfMachine_string);
            final TextView main_code_bwPartNo_string           = (TextView) findViewById(R.id.main_code_bwPartNo_string);
            final TextView main_code_EngRevLvl_string          = (TextView) findViewById(R.id.main_code_EngRevLvl_string);
            final TextView main_code_julainDate_string         = (TextView) findViewById(R.id.main_code_julianDate_string);
            final TextView main_code_counter_string            = (TextView) findViewById(R.id.main_code_counter_string);
            final Switch   main_switch_translateToLang         = (Switch)   findViewById(R.id.main_switch_translateToLang);


            main_code_encoded.setText(scanContent);
            main_code_string.setText(scanContent);

            // Rufe Prüfung auf
            if (checkNormCodeLengh(scanContent)) { // Norm Code
                main_code_type_string.setText("BW Normcode");

                // Rüfe Norm Typ Prüfung ab
                if(checkNormCodeType(scanContent)) { // Neuer Norm Code
                    main_code_type_string.setText("Neuer BW Norm Code");

                    // Setzt die Splitter
                    final String normCodeA_New = scanContent.substring(0, 2);
                    final String normCodeB_New = scanContent.substring(2, 9);
                    final String normCodeC_New = scanContent.substring(9, 20);
                    final String normCodeD_New = scanContent.substring(20, 22);
                    final String normCodeE_New = scanContent.substring(22, 27);
                    final String normCodeF_New = scanContent.substring(27, 32);

                    main_code_plant_string.setText(normCodeA_New);
                    main_code_inventoryOfMachine_string.setText(normCodeB_New);
                    main_code_bwPartNo_string.setText(normCodeC_New);
                    main_code_EngRevLvl_string.setText(normCodeD_New);
                    main_code_julainDate_string.setText(normCodeE_New);
                    main_code_counter_string.setText(normCodeF_New);


                    main_switch_translateToLang.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if(isChecked) {
                                main_code_plant_string.setText(getPlant(normCodeA_New));
                                main_code_julainDate_string.setText(getJulianDate(normCodeE_New));
                                /** TODO Setzte die restlichen Übersetzungen ein **/
                            } else {

                                main_code_plant_string.setText(normCodeA_New);
                                main_code_inventoryOfMachine_string.setText(normCodeB_New);
                                main_code_bwPartNo_string.setText(normCodeC_New);
                                main_code_EngRevLvl_string.setText(normCodeD_New);
                                main_code_julainDate_string.setText(normCodeE_New);
                                main_code_counter_string.setText(normCodeF_New);

                            }
                        }
                    });


                } else { // Alter Norm Code
                    main_code_type_string.setText("Alter BW Norm Code");

                    // Setze die Splitter
                    final String normCodeA_Old = scanContent.substring(0, 2);
                    final String normCodeB_Old = scanContent.substring(2, 10);
                    final String normCodeC_Old = scanContent.substring(10, 24);
                    final String normCodeD_Old = scanContent.substring(24, 32);

                    main_code_plant_string.setText(normCodeA_Old);
                    main_code_inventoryOfMachine_string.setText(normCodeB_Old);
                    main_code_bwPartNo_string.setText(normCodeC_Old);
                    main_code_EngRevLvl_string.setText(normCodeD_Old);
                    main_code_julainDate_string.setText("");
                    main_code_counter_string.setText("");

                    main_switch_translateToLang.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if(isChecked) {
                                main_code_plant_string.setText(getPlant(normCodeA_Old));
                                /** TODO Setzte die restlichen Übersetzungen ein **/
                            } else {

                                main_code_plant_string.setText(normCodeA_Old);
                                main_code_inventoryOfMachine_string.setText(normCodeB_Old);
                                main_code_bwPartNo_string.setText(normCodeC_Old);
                                main_code_EngRevLvl_string.setText(normCodeD_Old);
                            }

                        }
                    });

                }




                /** ========================== Kein BW Code ========================== **/
            } else { // Falscher Code
                main_code_type_string.setText("Falsche länge (" + scanContent.length() + ")");
            }

            /** ================== Nach der Einteilung ================== **/

            
            // Ordner Anlegen
            final String text = "Der gescanne Code:\n" + scanContent + "\n\nOrt: " + main_code_plant_string.getText() + "\nInventarnummer: " + main_code_inventoryOfMachine_string.getText() + "\nBW Teil Nummer: " + main_code_bwPartNo_string.getText() + "\nÄnderungszustand: " + main_code_EngRevLvl_string.getText() + "\nJulianisches Datum: " + main_code_julainDate_string.getText() + "\nZähler:" + main_code_counter_string.getText() + "\n\nArt des Codes: " + main_code_type_string.getText() + "\n";
            setScanContent(scanContent);
            setAllCodes(text);
            FileSystem fileSystem = new FileSystem();
            if(fileSystem.isExternalStorageWritable()) {
                fileSystem.getStorageDir("BW DMX Scanner"); // Ordner Name
            } else {
                Toast.makeText(this, "File not Created!", Toast.LENGTH_SHORT).show();
            }
            // Ordner Anlegen Fertig
            // Text Datei Anlegen
            fileSystem.saveToTxt(text);

        } else {
            Toast.makeText(this, "Keine Daten erhalten", Toast.LENGTH_SHORT).show();
        }


        
    }


    /** Methoden für den Code **/

    // Prüfe Normcode
    public boolean checkNormCodeLengh(String normCode) {
        boolean correctLengh = false;
        if (normCode.length() == 32) {
            correctLengh = true;
        }
        return correctLengh;
    }

    // Prüfe Normcode Art
    public boolean checkNormCodeType(String normCode) {
        boolean isNewNormCode = false;
        String isNewCodeCheker = normCode.substring(21, 22);
        if (!isNewCodeCheker.equalsIgnoreCase("0") && !isNewCodeCheker.equalsIgnoreCase("1") && !isNewCodeCheker.equalsIgnoreCase("2") && !isNewCodeCheker.equalsIgnoreCase("3") && !isNewCodeCheker.equalsIgnoreCase("4") && !isNewCodeCheker.equalsIgnoreCase("5") && !isNewCodeCheker.equalsIgnoreCase("6") && !isNewCodeCheker.equalsIgnoreCase("7") && !isNewCodeCheker.equalsIgnoreCase("8") && !isNewCodeCheker.equalsIgnoreCase("9")) { /** DONE Mache das Ganze Alphabet dazu, und das auch für die 2. Stelle! **/
            isNewNormCode = true;
        }

        return isNewNormCode;
    }

    public void startVibration(Vibrator v) {
        v.vibrate(100);
    }

    public String getPlant(String id) {
        switch (id) {
            case "50":
                return "Kirchheimbolanden (Deutschland)";

            case "30":
                return "Italiba (Brasilien)";

            case "70":
                return "Oroszlány (Ungarn)";

            case "75":
                return "Rzeszów (Polen)";

            case "60":
                return "Brandford (England)";

            // Ramos (Mexico): Keine Standort Kennung

            // Asheville (USA): Keine Standort Kennung

            case "90":
                return "Ningbo/Taicang (China)";

            case "91":
                return "PyongTaek (Korea)";

            case "00":
                return "Externer Lieferant";

            default:
                return id;
            }
        }

    public String getJulianDate(String julianDate) {
        Date date = null;
        try {
            date = new SimpleDateFormat("D").parse(julianDate);
            String g = new SimpleDateFormat("dd.MM.yyyy").format(date);
            return g;
        } catch (ParseException e) {
            e.printStackTrace();
            return "Error [Parse-143]";
        }
    }

    public void sendWhatsapp(String text) {
        String textEncoded = null;
        try {
            textEncoded = URLEncoder.encode(text, "UTF-8");
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("whatsapp://send?text=" + textEncoded));
            startActivity(i);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void sendEmail(String text, String subject) {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822"); // message/rfc822 text/plain
        //i.putExtra(Intent.EXTRA_EMAIL  , new String[]{""});
        i.putExtra(Intent.EXTRA_SUBJECT, subject);
        i.putExtra(Intent.EXTRA_TEXT   , text);
        try {
            startActivity(Intent.createChooser(i, "Mail senden..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(MainActivity.this, "Kein Email Programm installiert!", Toast.LENGTH_SHORT).show();
        }
    }

    public void setAllCodes(String allCodes) {
        if (allCodes == null) {
            this.allCodes = "Nichts Gescannt!";
        } else {
            this.allCodes = allCodes;
        }
    }

    public String getAllCodes() {
        return allCodes;
    }

    public void setScanContent(String scanContent) {
        if (scanContent == null) {
            this.scanContent2 = "Nichts Gescannt!";
        } else {
            this.scanContent2 = scanContent;
        }

    }

    public String getScanContent() {
        return scanContent2;
    }

    public void openAboutActivity(){
        Intent i = new Intent(MainActivity.this, AboutActivity.class);
        MainActivity.this.startActivity(i);
    }
}

