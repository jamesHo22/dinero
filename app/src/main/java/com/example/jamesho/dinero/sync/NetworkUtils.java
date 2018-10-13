package com.example.jamesho.dinero.sync;

import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;

/**
 * Created by jamesho on 2018-10-08.
 */

public class NetworkUtils {

    final static String BASE_AUTHORITY_SERVER = "baconipsum.com";
    //https://baconipsum.com/api/?type=meat-and-filler&paras=1
    final static String PATH_TEST = "api/";
    final static String PATH_USER_LOGIN = "";

    /***
     *
     * @param type: and integer that specifies which endpoint to call.
     * @return returns a URL object
     */
    public static URL buildUrl(int type) {
        // FIXME: Change this to a switch case to change between endpoints
        if (type == 0) {
            // The user login endpoint
            Uri.Builder builder = new Uri.Builder();
            builder.scheme("http")
                    .authority(BASE_AUTHORITY_SERVER)
                    .appendPath(PATH_USER_LOGIN);
            String myUrl = builder.build().toString();
            URL builtUrl = null;
            try {
                try {
                    builtUrl = new URL(URLDecoder.decode(myUrl, "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            Log.v("uri", builtUrl.toString());
            return builtUrl;

        } else {
            // the get data endpoint
            Uri.Builder builder = new Uri.Builder();
            builder.scheme("https")
                    .authority(BASE_AUTHORITY_SERVER)
                    .appendPath(PATH_TEST)
                    .appendQueryParameter("type", "meat-and-filler")
                    .appendQueryParameter("sentences", "3");
            String myUrl = builder.build().toString();
            URL builtUrl = null;
            try {
                try {
                    builtUrl = new URL(URLDecoder.decode(myUrl, "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            Log.v("uri", builtUrl.toString());
            return builtUrl;
        }
    }

    /**
     * makes the network call to the given URLConnection
     * @param urlConnection: A URLConnection object
     * @return the string from the network call
     * @throws IOException
     */
    public static String getResponseFromHTTPUrl(URLConnection urlConnection) throws IOException {
        // Gets the response from the server
        InputStream in = urlConnection.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuilder result = new StringBuilder();
        String line;
        while((line = reader.readLine()) != null) {
            result.append(line);
        }
        //System.out.println(result.toString());
        return result.toString();
    }
}
