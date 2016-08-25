package activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.scanlibrary.ScanActivity;
import com.scanlibrary.ScanConstants;
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
import java.util.regex.Pattern;

import bean.UserDataInfoBean;

public class AadhaarSeedingSearchActivity extends AppCompatActivity {

    private static final String RESPONSE_CODE = null;
    EditText etAccountNo, etCoustomerId, etNewAadhaarNo, etNameInAadhaar;
    TextView tvvAdhr, tvMobile, tvNameCustomer,tvNamestatic;
    Button btnSearch, btnSave, btnreset, btnnclear;

    ArrayList<UserDataInfoBean> userinfoList = new ArrayList<>();
    ProgressDialog pDialog;
    LinearLayout llShowDetails, llSeeding;
    Reader in;
    String RecordNo, AccountNo;
    ImageView imgAadhar;
    private static final int REQUEST_CODE = 99;
    Bitmap bitmap;
    String imgString;
    TextView tvName, tvTime;

    private int CAMERA_CAPTURE = 1;
    private Uri picUri = null;
    final int CROP_PIC = 2;
    byte[] picByte;
    static byte[] profileB;
    static Bitmap picB;
    String UserCode, BranchCode, ZoneCode;
    private String selectedImagePath;


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
        /*tv_error = (TextView) findViewById(R.id.tv_error);*/
        tvNamestatic = (TextView) findViewById(R.id.tv_name);
        tvName = (TextView) findViewById(R.id.tvuname1);
        tvTime = (TextView) findViewById(R.id.tvDATe);
        btnSave = (Button) findViewById(R.id.btn_save_new);
        btnnclear = (Button) findViewById(R.id.btn_clear);

        btnSearch = (Button) findViewById(R.id.btn_search);
        btnreset = (Button) findViewById(R.id.btn_reset);
        llSeeding = (LinearLayout) findViewById(R.id.LinearlayoutSeeding);
        llShowDetails = (LinearLayout) findViewById(R.id.linear_layout_panal);
        imgAadhar = (ImageView) findViewById(R.id.img_Aadhar);


        imgAadhar.setOnClickListener(new ScanButtonClickListener(ScanConstants.OPEN_CAMERA));

        SharedPreferences prefs = getSharedPreferences("loginData", MODE_PRIVATE);

        UserCode = prefs.getString("UserCode", "defUserCode");
        BranchCode = prefs.getString("BranchCode", "defBranchCode");
        ZoneCode = prefs.getString("ZoneCode", "defZoneCode");

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int height = dm.heightPixels;
        int width = dm.widthPixels;

        if (height <= 800 && width <= 480) {

            imgAadhar.getLayoutParams().height = 81;
            imgAadhar.getLayoutParams().width = 81;

        } else if (height <= 1280 && width <= 720) {

            imgAadhar.getLayoutParams().height = 161;
            imgAadhar.getLayoutParams().width = 142;

        } else if (height <= 1920 && width <= 1080) {

            imgAadhar.getLayoutParams().height = 155;
            imgAadhar.getLayoutParams().width = 155;
        } else if (height <= 940 && width <= 600) {

            imgAadhar.getLayoutParams().height = 102;
            imgAadhar.getLayoutParams().width = 102;

        } else if (height <= 2560&& width <= 1440) {
            imgAadhar.getLayoutParams().height = 240;
            imgAadhar.getLayoutParams().width = 220;
        }

        Intent in = getIntent();
        String uuserName = in.getStringExtra("name");
        String dte = in.getStringExtra("Date");

        tvName.setText(uuserName);
        tvTime.setText(dte);

        etNewAadhaarNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //  validAadharNo(etNewAadhaarNo.getText().toString());
                String ss= String.valueOf(s).trim();
                if (ss.toString().trim().length() <= 12 ) {
                        validateAadharNumber(ss);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (s.toString().trim().length() == 0) {
                    etNewAadhaarNo.setError(null);
                }
            }
        });

        etAccountNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (etAccountNo.getText().toString().trim().length() < 15) {
                    etAccountNo.setError("Enter Valid Account no");
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().trim().length() == 0) {

                    etAccountNo.setError(null);
                }
            }
        });


        etCoustomerId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (etCoustomerId.getText().toString().trim().length() < 9)
                {
                    etCoustomerId.setError("Enter Valid Id");

                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().trim().length() == 0) {

                    etCoustomerId.setError(null);
                }
            }
        });

        btnnclear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etAccountNo.setText("");
                etCoustomerId.setText("");
            }
        });


        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String AccNo = etAccountNo.getText().toString();
                String Customerid = etCoustomerId.getText().toString();

                if ((AccNo.toString().trim().length() != 0) || (Customerid.toString().trim().length() != 0)) {

                    if (etAccountNo.getText().toString().trim().length() == 15 || etCoustomerId.getText().toString().trim().length() == 9) {

                        //tv_error.setVisibility(View.GONE);
                        String url = "http://103.21.54.52/BOIWebAPI/api/BoiMember/GetRecord?ind=1&CustomerId=" + Customerid + "&SourceType=1&MobileNo=&CustomerName=&AadhaarNo=&UserCode=" + UserCode + "&Campcd=1&branchcd=" + BranchCode + "&ZoneCode=" + ZoneCode + "&AccountNo=" + AccNo + "&RecordNo=";
                        // String url = "http://103.21.54.52/BOIWebAPI/api/BoiMember/GetRecord?ind=1&CustomerId=&SourceType=2&MobileNo=&CustomerName=amit%20yadav&AadhaarNo=&UserCode=8&Campcd=1&branchcd=8841&ZoneCode=1&AccountNo=&RecordNo=";

                        ConnectivityManager ConnectionManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                        NetworkInfo networkInfo = ConnectionManager.getActiveNetworkInfo();
                        if (networkInfo != null && networkInfo.isConnected() == true) {
                            GetUserData getUserData = new GetUserData();
                            getUserData.execute(url);

                        }


                    } else {
                        Toast.makeText(AadhaarSeedingSearchActivity.this, "Network Not Available", Toast.LENGTH_LONG).show();
                    }


                } else {
                    Toast.makeText(AadhaarSeedingSearchActivity.this, "Atleast enter one", Toast.LENGTH_LONG).show();
                }
            }


        });

        btnreset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //     resetData();
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String upDateAdarNo = etNewAadhaarNo.getText().toString();
                String upDateAdrName = etNameInAadhaar.getText().toString();

                if (validAadharName() && validateAadharNumber(upDateAdarNo) && validImage()) {

                    ConnectivityManager ConnectionManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo networkInfo = ConnectionManager.getActiveNetworkInfo();
                    if (networkInfo != null && networkInfo.isConnected() == true) {
                        UpdateUserData getUserData = new UpdateUserData();
                        getUserData.execute(upDateAdarNo, upDateAdrName, AccountNo, RecordNo);

                    } else {
                        Toast.makeText(AadhaarSeedingSearchActivity.this, "Network Not Available", Toast.LENGTH_LONG).show();

                    }

                }
            }

        });

        /*imgAadhar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                *//*Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.intsig.camscanner");
                if (launchIntent == null) {
                    // Bring user to the market or let them choose an app?
                    launchIntent = new Intent(Intent.ACTION_VIEW);
                    launchIntent.setData(Uri.parse("market://details?id=" + "com.intsig.camscanner"));
                }

                startActivity(launchIntent);*//*

               *//* Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.intsig.camscanner");
                 if (launchIntent == null) {
                    // Bring user to the market or let them choose an app?
                    launchIntent = new Intent(Intent.ACTION_PICK);
                     launchIntent.setType("image*//**//*");
                   *//**//* Uri uri = Uri.fromFile(new File("/sdcard/source.jpg")); //Or  content uri picked from gallery
                    launchIntent.putExtra(Intent.EXTRA_STREAM, uri);
                    launchIntent.putExtra("scanned_image", "sdcard/scanned.jpg");
                    launchIntent.putExtra("pdf_path", "sdcard/processed.jpg");
                    launchIntent.putExtra("org_image", "sdcard/org.jpg");*//**//*
                    launchIntent.setData(Uri.parse("market://details?id=" + "com.intsig.camscanner"));
                }
                startActivityForResult(launchIntent, CAMERA_CAPTURE);*//*

               *//* try {
                    Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(captureIntent, CAMERA_CAPTURE);
                } catch (ActivityNotFoundException anfe) {
                    Toast toast = Toast.makeText(AadhaarSeedingSearchActivity.this, "This device doesn't support the crop action!",
                            Toast.LENGTH_SHORT);
                    toast.show();
                }*//*
            }
        });
*/

    }


    class GetUserData extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(AadhaarSeedingSearchActivity.this);
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

                try {
                    userinfoList = new ArrayList<>();
                    JSONArray jsonArry = new JSONArray(json);

                    if (jsonArry.length() > 0) {
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


                            etAccountNo.setEnabled(false);
                            etCoustomerId.setEnabled(false);
                            llSeeding.setVisibility(View.VISIBLE);
                            llShowDetails.setVisibility(View.VISIBLE);
                            btnSearch.setVisibility(View.GONE);
                            btnnclear.setVisibility(View.GONE);
                            tvvAdhr.setText(AadhaarNo);
                            tvMobile.setText(MobileNo);
                            tvNameCustomer.setText(CustomerName);


                            //    userinfoList.add(new UserDataInfoBean(RecordNo, CustomerId, AccountNo, CustomerName, AadhaarNo, MobileNo, BranchCode, BranchName, NewAadhaarNo, NewNameAsAadhaar, NewMobileNo));

                        }
                    } else {

                        Toast.makeText(AadhaarSeedingSearchActivity.this, "No result found", Toast.LENGTH_LONG).show();
                    }
                    pDialog.dismiss();


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }


        }

    }

    class UpdateUserData extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(AadhaarSeedingSearchActivity.this);
            pDialog.setMessage("Updating...");
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {


            Map<String, String> params = new LinkedHashMap<>();
            params.put("Method", "UpdateMemberForAll");
            params.put("AadhaarNo", args[0]);
            params.put("NewNameAsAadhaar", args[1]);
            params.put("AccountNo", args[2]);
            params.put("RecordNo", args[3]);
            params.put("SourceType", "1");
            params.put("UserCode", UserCode);
            params.put("branchcd", BranchCode);
            params.put("ZoneCode", ZoneCode);
            params.put("ind", "2");
            params.put("Img", imgString);

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

        }

        protected void onPostExecute(String json) {


            if (json != null) {

                Toast.makeText(AadhaarSeedingSearchActivity.this, json.toString(), Toast.LENGTH_LONG).show();
                etNewAadhaarNo.setText("");
                etNameInAadhaar.setText("");
                imgAadhar.setImageDrawable(null);

                resetData();

            }

            pDialog.dismiss();


        }
    }


    public void resetData() {

        etAccountNo.setText("");
        etCoustomerId.setText("");


        btnSearch.setVisibility(View.VISIBLE);
        llSeeding.setVisibility(View.GONE);
        llShowDetails.setVisibility(View.GONE);
        btnnclear.setVisibility(View.VISIBLE);
        etAccountNo.setEnabled(true);
        etCoustomerId.setEnabled(true);
    }

    public byte[] getBytesFromBitmap(Bitmap photo) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        return stream.toByteArray();
    }




    public String getPath(Uri uri) {

        if (uri == null) {
            return null;
        }

        // this will only work for images selected from gallery
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }

        return uri.getPath();
    }


    public void setProfileImageByte(Bitmap picBitmap, byte[] profileByte) {
        picB = picBitmap;
        profileB = profileByte;
    }

    public byte[] getImageBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    /**
     * Helper method to carry out crop operation
     */
    private void performCrop() {
        // take care of exceptions
        try {
            // call the standard crop action intent (the user device may not
            // support it)
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            // indicate image type and Uri
            cropIntent.setDataAndType(picUri, "image/*");
            // set crop properties
            cropIntent.putExtra("crop", "true");
            // indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            // indicate output X and Y
            /*cropIntent.putExtra("outputX", 256);
            cropIntent.putExtra("outputY", 256);*/

            cropIntent.putExtra("outputX", 550);
            cropIntent.putExtra("outputY", 650);
            // retrieve data on return
            cropIntent.putExtra("return-data", true);
            // start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, CROP_PIC);
        }
        // respond to users whose devices do not support the crop action
        catch (ActivityNotFoundException anfe) {
            // display an error message
            String errorMessage = "Whoops - your device doesn't support the crop action!";
            Toast toast = Toast.makeText(AadhaarSeedingSearchActivity.this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }


    private class ScanButtonClickListener implements View.OnClickListener {

        private int preference;

        public ScanButtonClickListener(int preference) {
            this.preference = preference;
        }

        public ScanButtonClickListener() {
        }

        @Override
        public void onClick(View v) {

            MarshMelloPermission marshMallowPermission = new MarshMelloPermission(AadhaarSeedingSearchActivity.this);
            if (!marshMallowPermission.checkPermissionForCamera()) {
                marshMallowPermission.requestPermissionForCamera();
            } else {
                if (!marshMallowPermission.checkPermissionForExternalStorage()) {
                    marshMallowPermission.requestPermissionForExternalStorage();
                } else {
                    startScan(preference);
                }
            }
        }
    }

    protected void startScan(int preference) {

        Intent intent = new Intent(this, ScanActivity.class);
        intent.putExtra(ScanConstants.OPEN_INTENT_PREFERENCE, preference);
        startActivityForResult(intent, REQUEST_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getExtras().getParcelable(ScanConstants.SCANNED_RESULT);
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                //getContentResolver().delete(uri, null, null);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                imgString = Base64.encodeToString(getBytesFromBitmap(bitmap), Base64.NO_WRAP);
                imgAadhar.setScaleType(ImageView.ScaleType.FIT_XY);
                imgAadhar.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /*public boolean validAadharNo(String aadharNumber) {

       *//* if (etNewAadhaarNo.getText().toString().trim().length() < 12) {
            //  Toast.makeText(AadhaarSeedingSearchActivity.this, "Enter Valid Aadhaar No", Toast.LENGTH_SHORT).show();
            etNewAadhaarNo.setError("Enter valid aadhaar no ");

        } else {

            etNewAadhaarNo.setError(null);
            return true;
        }
        return false;*//*
        Pattern aadharPattern = Pattern.compile("\\d{12}");
        boolean isValidAadhar = aadharPattern.matcher(aadharNumber).matches();
            if (isValidAadhar) {
                isValidAadhar = Verhoeff.validateVerhoeff(aadharNumber);
            } else {
                //Toast.makeText(AadhaarSeedingSearchActivity.this, "Enter Valid Aadhaar No", Toast.LENGTH_SHORT).show();
                etNewAadhaarNo.setError("Enter Valid Aadhaar No");
            }

        return isValidAadhar;
    }
*/
    public  boolean validateAadharNumber(String aadharNumber) {
        Pattern aadharPattern = Pattern.compile("\\d{12}");
        boolean isValidAadhar = aadharPattern.matcher(aadharNumber).matches();
        if (isValidAadhar) {
            isValidAadhar = VerhoeffAlgorithm.validateVerhoeff(aadharNumber);
            if (isValidAadhar == false) {
                etNewAadhaarNo.setError("Invalid aadhaar number");
            }
        } else {
            etNewAadhaarNo.setError("Incomplete Aadhaar number");
        }
        return isValidAadhar;
    }

    public boolean validAadharName() {

        if (etNameInAadhaar.getText().toString().trim().equalsIgnoreCase("")) {
            Toast.makeText(AadhaarSeedingSearchActivity.this, "Enter Valid Aadhaar Name", Toast.LENGTH_SHORT).show();
        } else {
            return true;
        }

        return false;
    }

    public boolean validImage() {

        if (imgAadhar.getDrawable() == null) {

            Toast.makeText(AadhaarSeedingSearchActivity.this, "Please Click Image", Toast.LENGTH_SHORT).show();
        } else {

            return true;
        }

        return false;
    }

    public boolean validaAccountno() {

        if (etAccountNo.getText().toString().trim().length() <= 15) {
            etAccountNo.setError("Enter valid account no ");

            //  Toast.makeText(AadhaarSeedingSearchActivity.this, "Please Enter valid Account Number", Toast.LENGTH_SHORT).show();
        } else {


            etAccountNo.setError(null);
            return true;

        }

        return false;
    }

    public boolean validaCustomerId() {

        if (etCoustomerId.getText().toString().trim().length() <= 9) {

            etCoustomerId.setError("Enter valid id ");

            //  Toast.makeText(AadhaarSeedingSearchActivity.this, "Please Enter valid Coustomer Id", Toast.LENGTH_SHORT).show();
        } else {


            etCoustomerId.setError(null);

            return true;
        }

        return false;
    }

}




