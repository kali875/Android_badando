package com.example.beadando;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.beadando.ui.login.LoginActivity;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.HttpResponse;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.client.HttpClient;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.client.methods.HttpGet;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import android.view.View;

public class Location_shared extends AsyncTask<Object, Integer, String>
{
    private int byGetOrPost = 0;
    public int querry_check;
    public boolean login_suc=false;
    TextView login_check;
    private Context context;
    public String Friend_Name=null;
    double lat2,long2;

    GoogleMap GMap;
    public Location_shared(Context context,GoogleMap GMap,double lat2, double long2 )
    {
        this.context = context;
        this.GMap = GMap;
        this.lat2 = lat2;
        this.long2 = long2;
    }
    @Override
    protected void onProgressUpdate(Integer... progress)
    {

    }
    @Override
    protected String doInBackground(Object[] objects)
    {
        Friend_Name = (String)objects[0];
        try{

            String link = "http://test.keramia.testhosting.hu/Location_shared.php?search="+Friend_Name;

            //URL url = new URL(link);
            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet();
            request.setURI(new URI(link));
            HttpResponse response = client.execute(request);
            BufferedReader in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

            StringBuffer sb = new StringBuffer("");
            String line="";

            while ((line = in.readLine()) != null) {
                sb.append(line);
                querry_check=1;
                break;
            }

            in.close();
            return sb.toString();
        } catch(Exception e)
        {
            return new String("Exception: " + e.getMessage());
        }
    }
    @Override
    protected void onPostExecute(String result)
    {
        if(result.equals("0"))
        {
            login_suc = false;
        }
        else
        {
            GMap.clear();
            String[] arrOfStr = result.split("/");
            User.PhoneNumber = arrOfStr[2];
            GMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(arrOfStr[0]), Double.parseDouble(arrOfStr[1]))).title(Friend_Name));
            User.select_friend_pos = new LatLng(Double.parseDouble(arrOfStr[0]), Double.parseDouble(arrOfStr[1]));
            GMap.animateCamera(CameraUpdateFactory.newLatLngZoom(User.select_friend_pos, 10));
        }
    }
}

