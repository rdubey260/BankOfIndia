package Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.administrator.bankofindia.R;

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

import bean.UserDataInfoBean;

/**
 * Created by Administrator on 21-07-2016.
 */
public class Aadhaarseedingactivity extends AppCompatActivity {

    Toolbar toolseed;
    Button btnnsearch;
    EditText etaccountno, etcustomerid, etname, etmobileno;
    String AccNo;
    ArrayList<UserDataInfoBean> userinfoList = new ArrayList<>();
    ProgressDialog pDialog;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_seeding);

        toolseed = (Toolbar) findViewById(R.id.toolseed);
        btnnsearch = (Button) findViewById(R.id.btn_search);
        etaccountno = (EditText) findViewById(R.id.et_accountno);
        etcustomerid = (EditText) findViewById(R.id.et_customerid);
        etname = (EditText) findViewById(R.id.et_name);
        etmobileno = (EditText) findViewById(R.id.et_mobileno);

        btnnsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AccNo = etaccountno.getText().toString();
                String Customerid = etcustomerid.getText().toString();
                String UserName = etname.getText().toString();
                String UserMobileno = etmobileno.getText().toString();

                String url = "http://103.21.54.52/BOIWebAPI/api/BoiMember/GetRecord?ind=1&CustomerId=" + Customerid + "&SourceType=2&MobileNo=" + UserMobileno + "&CustomerName=" + UserName + "&AadhaarNo=&UserCode=8&Campcd=1&branchcd=8841&ZoneCode=1&AccountNo=" + AccNo + "&RecordNo=";
                // String url = "http://103.21.54.52/BOIWebAPI/api/BoiMember/GetRecord?ind=1&CustomerId=&SourceType=2&MobileNo=&CustomerName=amit%20yadav&AadhaarNo=&UserCode=8&Campcd=1&branchcd=8841&ZoneCode=1&AccountNo=&RecordNo=";

                ConnectivityManager ConnectionManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = ConnectionManager.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected() == true) {
                    GetUserData getUserData = new GetUserData();
                    getUserData.execute(url);

                } else {
                    Toast.makeText(Aadhaarseedingactivity.this, "Network Not Available", Toast.LENGTH_LONG).show();

                }
            }
        });
    }
    class GetUserData extends AsyncTask<String, Void, String> {
        //   JSONParser jsonParser = new JSONParser();
        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(Aadhaarseedingactivity.this);
            pDialog.setMessage("Fatching Data....");
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {

            String NewUrl = args[0];
           /* try {
                HashMap<String, String> params = new HashMap<>();
                params.put("Method", "GetRecord");
                params.put("AccountNo", args[0]);
                params.put("CustomerId", args[1]);
                params.put("CustomerName", args[2]);
                // params.put("CustomerName", "amit%20yadav");
                params.put("MobileNo", args[3]);
                params.put("SourceType", "2");
                params.put("UserCode", "8");
                params.put("Campcd", "1");
                params.put("branchcd", "8841");
                params.put("ZoneCode", "1");
                params.put("RecordNo", null);
                params.put("ind", "1");
                JSONObject json = jsonParser.makeHttpRequest(Constant.LOGIN_URL, "GET", params);
                return json;
            } catch (Exception e) {
                e.printStackTrace();
            }*/

          /*  try {
                InetAddress i = InetAddress.getByName(NewUrl);
            } catch (UnknownHostException e1) {
                e1.printStackTrace();
            }*/

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
                        Intent in = new Intent(Aadhaarseedingactivity.this, Showseedingactivity.class);
                        in.putExtra("list", userinfoList);
                        startActivity(in);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }


        }

    }


}
