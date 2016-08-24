package activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import adapter.ShowSeedingAdapter;
import bean.UserDataInfoBean;

public class Showseedingactivity extends AppCompatActivity {
    ListView lvData;
    ShowSeedingAdapter showSeedingAdapter;
    ArrayList<UserDataInfoBean> userinfoList = new ArrayList<>();
    ProgressDialog pDialog;
    String AccNo, UserName, UserMobileno, Customerid;
    String UserCode, BranchCode, ZoneCode;
    TextView tvName, tvTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_showseedingactivity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool1);
        tvName = (TextView) findViewById(R.id.tvuname2);
        tvTime = (TextView) findViewById(R.id.tvDATe1);
        lvData = (ListView) findViewById(R.id.sedding_listview);






        Intent intent = getIntent();
        //  userinfoList = (ArrayList<UserDataInfoBean>) intent.getSerializableExtra("list");
        if (userinfoList != null) {
            showSeedingAdapter = new ShowSeedingAdapter(this, userinfoList);
            lvData.setAdapter(showSeedingAdapter);

        }
        Customerid = intent.getStringExtra("coustomerId");
        UserMobileno = intent.getStringExtra("UserMobileno");
        UserName = intent.getStringExtra("UserName");
        AccNo = intent.getStringExtra("AccNo");
        String uuserName = intent.getStringExtra("uuserName");
        String dte = intent.getStringExtra("dte");

        tvName.setText(uuserName);
        tvTime.setText(dte);


        SharedPreferences prefs = getSharedPreferences("loginData", MODE_PRIVATE);

        UserCode = prefs.getString("UserCode", "defUserCode");
        BranchCode = prefs.getString("BranchCode", "defBranchCode");
        ZoneCode = prefs.getString("ZoneCode", "defZoneCode");
    }


    @Override
    protected void onResume() {
        super.onResume();

        String url = "http://103.21.54.52/BOIWebAPI/api/BoiMember/GetRecord?ind=1&CustomerId=" + Customerid + "&SourceType=2&MobileNo=" + UserMobileno + "&CustomerName=" + UserName + "&AadhaarNo=&UserCode=" + UserCode + "&Campcd=1&branchcd=" + BranchCode + "&ZoneCode=" + ZoneCode + "&AccountNo=" + AccNo + "&RecordNo=";
        // String url = "http://103.21.54.52/BOIWebAPI/api/BoiMember/GetRecord?ind=1&CustomerId=&SourceType=2&MobileNo=&CustomerName=amit%20yadav&AadhaarNo=&UserCode=8&Campcd=1&branchcd=8841&ZoneCode=1&AccountNo=&RecordNo=";

        ConnectivityManager ConnectionManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = ConnectionManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected() == true) {
            GetUserData getUserData = new GetUserData();
            getUserData.execute(url);

        } else {
            Toast.makeText(Showseedingactivity.this, "Network Not Available", Toast.LENGTH_LONG).show();

        }

        if (userinfoList != null) {
            showSeedingAdapter = new ShowSeedingAdapter(this, userinfoList);
            lvData.setAdapter(showSeedingAdapter);

        }
    }

    class GetUserData extends AsyncTask<String, Void, String> {
        //   JSONParser jsonParser = new JSONParser();
        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(Showseedingactivity.this);
            pDialog.setMessage("Fetching Data....");
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {

            String NewUrl = args[0];


            HttpURLConnection ht = null;
            try {
                URL ur = new URL(NewUrl);
                ht = (HttpURLConnection) ur.openConnection();
                ht.setRequestMethod("GET");
                ht.connect();
                int status = ht.getResponseCode();
                switch (status) {
                    case 200:
                        BufferedReader br = new BufferedReader(new InputStreamReader(ht.getInputStream()));
                        StringBuilder sr = new StringBuilder();
                        String line;
                        while ((line = br.readLine()) != null) {
                            sr.append(line + "\n");
                        }
                        br.close();
                        return sr.toString();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (ht != null) {
                    try {
                        ht.disconnect();
                    } catch (Exception ex) {
                        Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            return null;
        }

        protected void onPostExecute(String json) {


            if (json != null) {
                // Toast.makeText(Aadhaarseedingactivity.this, json.toString(), Toast.LENGTH_LONG).show();
                try {
                    userinfoList = new ArrayList<>();
                    JSONArray jsonArry = new JSONArray(json);
                    for (int i = 0; i < jsonArry.length(); i++) {

                        JSONObject jsonObject = jsonArry.getJSONObject(i);
                        String RecordNo = jsonObject.getString("RecordNo");
                        String CustomerId = jsonObject.getString("CustomerId");
                        String AccountNo = jsonObject.getString("AccountNo");
                        String CustomerName = jsonObject.getString("CustomerName");
                        String AadhaarNo = jsonObject.getString("AadhaarNo");
                        String MobileNo = jsonObject.getString("MobileNo");
                        String BranchCode = jsonObject.getString("BranchCode");
                        String BranchName = jsonObject.getString("BranchName");
                        String NewAadhaarNo = jsonObject.getString("NewAadhaarNo");
                        String NewNameAsAadhaar = jsonObject.getString("NewNameAsAadhaar");
                        String NewMobileNo = jsonObject.getString("NewMobileNo");

                        userinfoList.add(new UserDataInfoBean(RecordNo, CustomerId, AccountNo, CustomerName, AadhaarNo, MobileNo, BranchCode, BranchName, NewAadhaarNo, NewNameAsAadhaar, NewMobileNo));

                    }
                    pDialog.dismiss();
                    if (userinfoList != null) {
                        showSeedingAdapter = new ShowSeedingAdapter(Showseedingactivity.this, userinfoList);
                        lvData.setAdapter(showSeedingAdapter);
                        showSeedingAdapter.notifyDataSetChanged();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }

    }
}
