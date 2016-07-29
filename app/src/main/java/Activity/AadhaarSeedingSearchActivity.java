package activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import bean.UserDataInfoBean;

public class AadhaarSeedingSearchActivity extends AppCompatActivity {
    EditText etAccountNo, etCoustomerId, etNewAadhaarNo, etNameInAadhaar;
    TextView tvvAdhr, tvMobile, tvNameCustomer, tv_error;
    Button btnSearch, btnSave, btnreset;
    ArrayList<UserDataInfoBean> userinfoList = new ArrayList<>();
    ProgressDialog pDialog;
    LinearLayout llShowDetails, llSeeding;
    Reader in;
    String RecordNo,AccountNo;
    ImageView imgAadhar;
    private static final int CAMERA_REQUEST = 1888;
    String temp;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aadhaar_seeding_search);
        etAccountNo = (EditText) findViewById(R.id.et_accountno_new);
        etCoustomerId = (EditText) findViewById(R.id.et_customerid_new);
        etNewAadhaarNo = (EditText) findViewById(R.id.tv_show_adhr_no_new);
        etNameInAadhaar = (EditText) findViewById(R.id.edt_show_adhr_name_new);
        tvvAdhr = (TextView) findViewById(R.id.tv_show_adhr_no);
        tvMobile = (TextView) findViewById(R.id.tv_mobileno_new);
        tvNameCustomer = (TextView) findViewById(R.id.tv_name_new);
        tv_error = (TextView) findViewById(R.id.tv_error);
        btnSave = (Button) findViewById(R.id.btn_save_new);
        btnSearch = (Button) findViewById(R.id.btn_search);
        btnreset = (Button) findViewById(R.id.btn_reset);
        llSeeding = (LinearLayout) findViewById(R.id.LinearlayoutSeeding);
        llShowDetails = (LinearLayout) findViewById(R.id.linear_layout_panal);
        imgAadhar = (ImageView) findViewById(R.id.img_Aadhar);

        etAccountNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tv_error.setVisibility(View.INVISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        etCoustomerId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tv_error.setVisibility(View.INVISIBLE);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String AccNo = etAccountNo.getText().toString();
                String Customerid = etCoustomerId.getText().toString();

                if (AccNo.toString().length() != 0 || Customerid.toString().length() != 0) {


                    tv_error.setVisibility(View.GONE);
                    String url = "http://103.21.54.52/BOIWebAPI/api/BoiMember/GetRecord?ind=1&CustomerId=" + Customerid + "&SourceType=1&MobileNo=&CustomerName=&AadhaarNo=&UserCode=8&Campcd=1&branchcd=8841&ZoneCode=1&AccountNo=" + AccNo + "&RecordNo=";
                    // String url = "http://103.21.54.52/BOIWebAPI/api/BoiMember/GetRecord?ind=1&CustomerId=&SourceType=2&MobileNo=&CustomerName=amit%20yadav&AadhaarNo=&UserCode=8&Campcd=1&branchcd=8841&ZoneCode=1&AccountNo=&RecordNo=";

                    ConnectivityManager ConnectionManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo networkInfo = ConnectionManager.getActiveNetworkInfo();
                    if (networkInfo != null && networkInfo.isConnected() == true) {
                        GetUserData getUserData = new GetUserData();
                        getUserData.execute(url);

                    } else {
                        Toast.makeText(AadhaarSeedingSearchActivity.this, "Network Not Available", Toast.LENGTH_LONG).show();

                    }

                } else {
                    tv_error.setVisibility(View.VISIBLE);
                }
            }
        });

        btnreset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetData();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String upDateAdarNo = etNewAadhaarNo.getText().toString();
                String upDateAdrName = etNameInAadhaar.getText().toString();

                ConnectivityManager ConnectionManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = ConnectionManager.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected() == true) {
                    UpdateUserData getUserData = new UpdateUserData();
                    getUserData.execute( upDateAdarNo,upDateAdrName,AccountNo,RecordNo);

                } else {
                    Toast.makeText(AadhaarSeedingSearchActivity.this, "Network Not Available", Toast.LENGTH_LONG).show();

                }
            }
        });

        imgAadhar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        });

    }


        class GetUserData extends AsyncTask<String, Void, String> {
        //   JSONParser jsonParser = new JSONParser();
        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(AadhaarSeedingSearchActivity.this);
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
                        RecordNo = jsonObject.getString("RecordNo");
                        String CustomerId = jsonObject.getString("CustomerId");
                        AccountNo = jsonObject.getString("AccountNo");
                        String CustomerName = jsonObject.getString("CustomerName");
                        String AadhaarNo = jsonObject.getString("AadhaarNo");
                        String MobileNo = jsonObject.getString("MobileNo");
                        String BranchCode = jsonObject.getString("BranchCode");
                        String BranchName = jsonObject.getString("BranchName");
                        String NewAadhaarNo = jsonObject.getString("NewAadhaarNo");
                        String NewNameAsAadhaar = jsonObject.getString("NewNameAsAadhaar");
                        String NewMobileNo = jsonObject.getString("NewMobileNo");

                        llSeeding.setVisibility(View.VISIBLE);
                        llShowDetails.setVisibility(View.VISIBLE);
                        btnSearch.setVisibility(View.GONE);

                        tvvAdhr.setText(AadhaarNo);
                        tvMobile.setText(MobileNo);
                        tvNameCustomer.setText(CustomerName);


                        //    userinfoList.add(new UserDataInfoBean(RecordNo, CustomerId, AccountNo, CustomerName, AadhaarNo, MobileNo, BranchCode, BranchName, NewAadhaarNo, NewNameAsAadhaar, NewMobileNo));

                    }
                    pDialog.dismiss();


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }


        }

    }

    class UpdateUserData extends AsyncTask<String, Void, String> {
      //  JSONParser jsonParser = new JSONParser();

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(AadhaarSeedingSearchActivity.this);
            pDialog.setMessage("Updating...");
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {

            //   String NewUrl = args[0];
            Map<String, String> params = new LinkedHashMap<>();
            params.put("Method", "UpdateMemberForAll");
            params.put("AadhaarNo", args[0]);
            params.put("NewNameAsAadhaar", args[1]);
            params.put("AccountNo",args[2]);
            params.put("RecordNo",args[3]);
            params.put("SourceType", "1");
            params.put("UserCode", "8");
            params.put("branchcd", "8841");
            params.put("ZoneCode", "1");
            params.put("ind", "2");
            params.put("Img",temp);

            StringBuilder postData = new StringBuilder();
            for (Map.Entry<String, String> param : params.entrySet()) {
                if (postData.length() != 0) postData.append('&');
                try {
                    postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                postData.append('=');
                try {
                    postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            byte[] postDataBytes = new byte[0];
            try {
                postDataBytes = postData.toString().getBytes("UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            URL url = null;
            try {
                url = new URL("http://103.21.54.52/BOIWebAPI/api/BoiMember/UpdateMemberForAll?");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            HttpURLConnection conn = null;
            try {
                conn = (HttpURLConnection) url.openConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                conn.setRequestMethod("PUT");
            } catch (ProtocolException e) {
                e.printStackTrace();
            }
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
            conn.setDoOutput(true);
            try {
                conn.getOutputStream().write(postDataBytes);
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            } catch (IOException e) {
                e.printStackTrace();
            }

            StringBuilder sb = new StringBuilder();
            try {
                for (int c; (c = in.read()) >= 0; )
                    sb.append((char) c);
            } catch (IOException e) {
                e.printStackTrace();
            }
            String response = sb.toString();

            return response;

               /* JSONObject json = jsonParser.makeHttpRequest(Constant.LOGIN_URL, "PUT", params);
               return json;
            } catch (Exception e) {
                e.printStackTrace();
            }*/

          /*  try {
                InetAddress i = InetAddress.getByName(NewUrl);
            } catch (UnknownHostException e1) {
                e1.printStackTrace();
            }*/

         /*   HttpURLConnection ht = null;
            try {
                URL ur = new URL(NewUrl);
                ht = (HttpURLConnection) ur.openConnection();
                ht.setRequestMethod("PUT");
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

         */


        }

        protected void onPostExecute(String json) {


            if (json != null) {

                Toast.makeText(AadhaarSeedingSearchActivity.this, json.toString(), Toast.LENGTH_LONG).show();
                resetData();

            }

            pDialog.dismiss();


        }

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            ByteArrayOutputStream baos=new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.JPEG,100, baos);
            byte [] b=baos.toByteArray();
            temp=Base64.encodeToString(b, Base64.NO_WRAP);
            imgAadhar.setImageBitmap(photo);
        }
    }

    public void resetData(){

        etAccountNo.setText("");
        etCoustomerId.setText("");
        imgAadhar.setImageResource(R.drawable.camera);
        btnSearch.setVisibility(View.VISIBLE);
        llSeeding.setVisibility(View.GONE);
        llShowDetails.setVisibility(View.GONE);

    }
}
