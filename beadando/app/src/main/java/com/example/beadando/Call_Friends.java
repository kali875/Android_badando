package com.example.beadando;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.TextView;

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
import java.net.URI;

public class Call_Friends extends AsyncTask<Object, Integer, String>
{
    private int byGetOrPost = 0;
    public int querry_check;
    public boolean login_suc=false;
    TextView login_check;
    private Context context;
    public String Friend_Name=null;
    double lat2,long2;

    GoogleMap GMap;
    public Call_Friends(Context context,String Friend_name)
    {
        this.Friend_Name = Friend_name;
        this.context = context;
        String[] asd = new String[]{Friend_name};
        doInBackground(asd);
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

            String link = "http://test.keramia.testhosting.hu/Call_Friend.php?search="+Friend_Name;

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
            Integer number = Integer.valueOf(result);
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:"+number));
            context.startActivity(callIntent);
        }
    }
}

