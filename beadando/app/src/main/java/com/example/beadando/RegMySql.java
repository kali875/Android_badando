package com.example.beadando;


import android.os.AsyncTask;
import android.widget.TextView;

import com.example.beadando.ui.login.LoginActivity;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.HttpResponse;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.client.HttpClient;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.client.methods.HttpGet;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;

public class RegMySql extends AsyncTask<Object, Integer, String>
{
    public int querry_check;
    public boolean login_suc=false;
    TextView login_check;

    public RegMySql(TextView login_check)
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
            String email = (String)objects[2];
            String phone = (String)objects[3];
            String link = "http://test.keramia.testhosting.hu/reg.php?username="+username+"&password="+password+"&email="+email+"&phonenumber="+phone;

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
        if(result.equals("Username is reserved"))
        {
            login_suc = false;
            login_check.setText(result);
        }
        else
        {
            login_check.setText("Registration Succes Fully, please you login");
        }
    }

}
