package com.example.daily;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.sax.*;
import android.util.Xml;
import org.apache.commons.lang3.StringEscapeUtils;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

class NasaParser {
    private String mTitle;
    private String mDescription;
    private String mDate;
    private String mImageUrl;
    private NasaItem nasaItem;

    public NasaItem getNasaItem() {
        return nasaItem;
    }

    public void setNasaItem(NasaItem nasaItem) {
        this.nasaItem = nasaItem;
    }

    public void parse(InputStream is) throws IOException, SAXException {
        RootElement rss = new RootElement("rss");
        Element channel = rss.requireChild("channel");
        Element item = channel.requireChild("item");
        item.setElementListener(new ElementListener() {
            public void end() {
                onItem(mTitle, mDescription, mDate, mImageUrl);
            }

            public void start(Attributes attributes) {
                mTitle = mDescription = mDate = mImageUrl = null;
            }
        });
        item.getChild("title").setEndTextElementListener(new EndTextElementListener() {
            public void end(String body) {
                mTitle = body;
            }
        });
        item.getChild("description").setEndTextElementListener(new EndTextElementListener() {
            public void end(String body) {
                mDescription = body;
            }
        });
        item.getChild("pubDate").setEndTextElementListener(new EndTextElementListener() {
            public void end(String body) {
                mDate = body;
            }
        });
        item.getChild("enclosure").setStartElementListener(new StartElementListener() {
            public void start(Attributes attributes) {
                mImageUrl = attributes.getValue("", "url");
            }
        });
        Xml.parse(is, Xml.Encoding.UTF_8, rss.getContentHandler());
    }

    public void onItem(String title, String description, String date, String imageUrl) {
        // handle the parsed data
        NasaItem nasaItem = new NasaItem(title, StringEscapeUtils.unescapeHtml4(description), mDate, getBitmap(mImageUrl));
        setNasaItem(nasaItem);
    }

    public String getmTitle() {
        return mTitle;
    }

    public String getmDescription() {
        return mDescription;
    }

    public String getmDate() {
        return mDate;
    }

    public String getmImageUrl() {
        return mImageUrl;
    }

    // convert url to bitmap
    private Bitmap getBitmap(String url) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            input.close();
            return bitmap;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}