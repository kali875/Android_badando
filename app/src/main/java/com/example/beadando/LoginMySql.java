package com.example.beadando;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.TextView;
import android.widget.Toast;

import com.example.beadando.ui.login.LoginActivity;
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

public class LoginMySql extends AsyncTask<Object, Integer, String>
{
    private int byGetOrPost = 0;
    public int querry_check;
    public boolean login_suc=false;
    TextView login_check;

    public LoginMySql(TextView login_check)
    {
        this.login_check = login_check;
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
                String password = (String)objects[1];
                String link = "http://test.keramia.testhosting.hu/login.php?username="+username+"&password="+password;

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
            login_check.setText("Username or Password Wrong");
        }
        else
        {
            LoginActivity.User_ID = Integer.parseInt(result);
            login_check.setText("Login Succes Fully");
        }
    }

}
