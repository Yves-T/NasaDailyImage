package com.example.daily;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class MyActivity extends Activity {
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

    public void onRefresh(View view) {
        pd = ProgressDialog.show(this, "Search", "Searching...", true, false);
        refreshFromFeed();
    }

    private void refreshFromFeed() {
        // fetch the xml file and parse it
        try {
            new DownloadImageTask().execute("").get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    private class DownloadImageTask extends AsyncTask<Object, Void, NasaItem> {

        @Override
        protected NasaItem doInBackground(Object... objects) {

            NasaParser nasaParser = new NasaParser();
            try {
                String NASA_URL = "http://www.nasa.gov/rss/image_of_the_day.rss";
                nasaParser.parse(new URL(NASA_URL).openStream());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            }
            return nasaParser.getNasaItem();

        }

        @Override
        protected void onPostExecute(NasaItem nasaItem) {
            super.onPreExecute();

            Log.i("nasa item title", nasaItem.getmTitle());
            TextView titleView = (TextView) findViewById(R.id.imageTitle);
            titleView.setText(nasaItem.getmTitle());
            Log.i("nasa item title", "title is set");

            TextView dateView = (TextView) findViewById(R.id.imageDate);
            dateView.setText(nasaItem.getmDate());
            Log.i("nasa date view title", "dateview is set");

            ImageView imageView = (ImageView) findViewById(R.id.imageDisplay);
            imageView.setImageBitmap(nasaItem.getmImageUrl());
            Log.i("nasa image", "image is set");

            TextView descriptionView = (TextView) findViewById(R.id.imagedescription);
            descriptionView.setText(nasaItem.getmDescription());
            Log.i("nasa description", "description is set");

            pd.dismiss();

        }
    }
}

