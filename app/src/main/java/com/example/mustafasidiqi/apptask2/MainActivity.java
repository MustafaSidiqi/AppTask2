package com.example.mustafasidiqi.apptask2;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button collect = (Button) findViewById(R.id.collectBtn);
        collect.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {

        System.out.println("Henter ord fra DRs server....");
        new AsyncTask() {

            protected Object doInBackground(Object... arg0) {
                try {
                    String rssdata = hentUrl("https://www.version2.dk/it-nyheder/rss");
                    String titler = findTitler(rssdata);
                    return titler;
                } catch (Exception e) {
                    e.printStackTrace();
                    return e;
                }
            }

            @Override
            protected void onPostExecute(Object titler) {
                System.out.println("resultat: \n" + titler);
                setProgressBarIndeterminateVisibility(false);
            }
        }.execute();

    }

    private static String findTitler(String rssdata) {
        String titler = "";
        while (true) {
            int tit1 = rssdata.indexOf("<title>") + 7;
            int tit2 = rssdata.indexOf("</title>");
            if (tit2 == -1) break; // hop ud hvis der ikke er flere titler
            if (titler.length() > 400) break; // .. eller hvis vi har nok
            String titel = rssdata.substring(tit1, tit2);
            System.out.println(titel);
            titler = titler + titel + "\n";
            rssdata = rssdata.substring(tit2 + 8); // Søg videre i teksten efter næste titel
        }
        return titler;
    }

    public static String hentUrl(String url) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(new URL(url).openStream()));
        StringBuilder sb = new StringBuilder();
        String linje = br.readLine();
        while (linje != null) {
            sb.append(linje + "\n");
            linje = br.readLine();
        }
        return sb.toString();
    }
}
