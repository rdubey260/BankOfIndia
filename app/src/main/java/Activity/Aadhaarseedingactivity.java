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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
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

import bean.UserDataInfoBean;

/**
 * Created by Administrator on 21-07-2016.
 */
public class Aadhaarseedingactivity extends AppCompatActivity {

    Toolbar toolseed;
    Button btnnsearch;
    EditText etaccountno, etcustomerid, etname, etmobileno;
    String AccNo,UserName,UserMobileno,Customerid;
    ArrayList<UserDataInfoBean> userinfoList = new ArrayList<>();
    ProgressDialog pDialog;
    TextView tvName, tvTime;
    String UserCode, BranchCode, ZoneCode;
    String uuserName,dte;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_seeding);

        toolseed = (Toolbar) findViewById(R.id.tool2);
        btnnsearch = (Button) findViewById(R.id.btn_search);

        etaccountno = (EditText) findViewById(R.id.et_accountno);
        etcustomerid = (EditText) findViewById(R.id.et_customerid);
        etname = (EditText) findViewById(R.id.et_name);
        etmobileno = (EditText) findViewById(R.id.et_mobileno);
        tvName = (TextView) findViewById(R.id.tvuname2);
        tvTime = (TextView) findViewById(R.id.tvDATe1);

        Intent in = getIntent();
         uuserName = in.getStringExtra("name");
         dte = in.getStringExtra("Date");

        tvName.setText(uuserName);
        tvTime.setText(dte);

        SharedPreferences prefs = getSharedPreferences("loginData", MODE_PRIVATE);

        UserCode = prefs.getString("UserCode", "defUserCode");
        BranchCode = prefs.getString("BranchCode", "defBranchCode");
        ZoneCode = prefs.getString("ZoneCode", "defZoneCode");


        btnnsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!etaccountno.getText().toString().equalsIgnoreCase("") || !etcustomerid.getText().toString().equalsIgnoreCase("") || !etmobileno.getText().toString().equalsIgnoreCase("") || !etname.getText().toString().equalsIgnoreCase("")) {

                    if (etaccountno.getText().toString().length() == 15 || etcustomerid.getText().toString().length() == 9 || etname.getText().toString().trim().length() != 0 || isValidPhoneNumber(etmobileno.getText().toString())) {

                        AccNo = etaccountno.getText().toString();
                        Customerid = etcustomerid.getText().toString();
                        UserName = etname.getText().toString();
                        UserMobileno = etmobileno.getText().toString();

                        String url = "http://103.21.54.52/BOIWebAPI/api/BoiMember/GetRecord?ind=1&CustomerId=" + Customerid + "&SourceType=2&MobileNo=" + UserMobileno + "&CustomerName=" + UserName + "&AadhaarNo=&UserCode=" + UserCode + "&Campcd=1&branchcd=" + BranchCode + "&ZoneCode=" + ZoneCode + "&AccountNo=" + AccNo + "&RecordNo=";
                        // String url = "http://103.21.54.52/BOIWebAPI/api/BoiMember/GetRecord?ind=1&CustomerId=&SourceType=2&MobileNo=&CustomerName=amit%20yadav&AadhaarNo=&UserCode=8&Campcd=1&branchcd=8841&ZoneCode=1&AccountNo=&RecordNo=";

                      /*  ConnectivityManager ConnectionManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                        NetworkInfo networkInfo = ConnectionManager.getActiveNetworkInfo();
                        if (networkInfo != null && networkInfo.isConnected() == true) {
                            GetUserData getUserData = new GetUserData();
                            getUserData.execute(url);

                        } else {
                            Toast.makeText(Aadhaarseedingactivity.this, "Network Not Available", Toast.LENGTH_LONG).show();

                        }*/
                        Intent in = new Intent(Aadhaarseedingactivity.this, Showseedingactivity.class);
                     //   in.putExtra("list", userinfoList);
                        in.putExtra("coustomerId",Customerid);
                        in.putExtra("AccNo",AccNo);
                        in.putExtra("UserMobileno",UserMobileno);
                        in.putExtra("UserName",UserName);
                        in.putExtra("uuserName",uuserName);
                        in.putExtra("dte",dte);
                        startActivity(in);

                    }
                    } else {

                        Toast.makeText(Aadhaarseedingactivity.this, "Enter at least one field ", Toast.LENGTH_LONG).show();
                    }

            }
        });

        etaccountno.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (etaccountno.getText().toString().trim().length() < 15) {
                    etaccountno.setError("Enter Valid Account no");
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().trim().length() == 0) {

                    etaccountno.setError(null);
                }
            }
        });

        etcustomerid.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (etcustomerid.getText().toString().trim().length() < 9) {
                    etcustomerid.setError("Enter Valid Customer Id ");
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().trim().length() == 0) {

                    etcustomerid.setError(null);
                }
            }
        });

        etmobileno.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (etmobileno.getText().toString().trim().length() < 10) {
                    etmobileno.setError("Enter Valid Mobile no");
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().trim().length() == 0) {

                    etmobileno.setError(null);
                }
            }
        });


    }

    class GetUserData extends AsyncTask<String, Void, String> {
        //   JSONParser jsonParser = new JSONParser();
        @Override
        protected void onPreExecute() {
           /* pDialog = new ProgressDialog(Aadhaarseedingactivity.this);
            pDialog.setMessage("Fetching Data....");
            pDialog.show();*/
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
                  //  pDialog.dismiss();
                    if (userinfoList != null) {

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }

    }

 /* public boolean validaMobileno() {

        if (!android.util.Patterns.PHONE.matcher(etmobileno.getText().toString()).matches()) {
            etmobileno.setError("enter valid no");

            //  Toast.makeText(AadhaarSeedingSearchActivity.this, "Please Enter valid Account Number", Toast.LENGTH_SHORT).show();
        } else {

            etmobileno.setError(null);
            return true;

        }
          return  false;
    }*/
   private boolean isValidPhoneNumber(CharSequence phoneNumber) {
       if (phoneNumber.length()!=10) {
           return false;
       } else {
           return android.util.Patterns.PHONE.matcher(phoneNumber).matches();
       }
   }
}
