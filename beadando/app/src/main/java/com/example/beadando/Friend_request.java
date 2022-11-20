package com.example.beadando;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.HttpResponse;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.client.HttpClient;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.client.methods.HttpGet;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;

public class Friend_request extends AsyncTask<Object, Integer, String> {
    public int querry_check;
    private Context context;
    private String  Friend_name = "";

    public Friend_request(Context context)
    {
        this.context = context;
    }
    @Override
    protected void onProgressUpdate(Integer... progress)
    {

    }
    @Override
    protected String doInBackground(Object[] objects)
    {

        try{
            String Friend_Name = (String)objects[0];
            Friend_name= Friend_Name;
            String link = "http://test.keramia.testhosting.hu/friends_request.php?search="+Friend_Name+"&user="+User.ID;
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
            Toast.makeText(context, "The Name is not found", Toast.LENGTH_SHORT).show();
        }
        else
        {
            User.friends.add(Friend_name);
            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, User.friends);
            MapsActivity.listView.setAdapter(arrayAdapter);
        }
    }
}

