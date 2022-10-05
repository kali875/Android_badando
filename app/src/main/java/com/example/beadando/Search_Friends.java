package com.example.beadando;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;

import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.HttpResponse;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.client.HttpClient;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.client.methods.HttpGet;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.List;

public class Search_Friends extends AsyncTask<Object, Integer, String>
{
    private int byGetOrPost = 0;
    public int querry_check;
    public boolean login_suc=false;
    private Context context;
    public Search_Friends(Context context)
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
            String username = (String)objects[0];
            String link = "http://test.keramia.testhosting.hu/friends.php?search="+username;

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
            String[] arrOfStr = result.split("/");
            for (String value:arrOfStr)
            {
                User.friends.add(value);
            }
            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, User.friends);
            MapsActivity.listView.setAdapter(arrayAdapter);
        }
    }
}
