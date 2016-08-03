package activity;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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
import java.io.File;
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
    TextView tvvAdhr, tvMobile, tvNameCustomer;
    Button btnSearch, btnSave, btnreset, btnnclear;

    ArrayList<UserDataInfoBean> userinfoList = new ArrayList<>();
    ProgressDialog pDialog;
    LinearLayout llShowDetails, llSeeding;
    Reader in;
    String RecordNo, AccountNo;
    ImageView imgAadhar;
    private static final int CAMERA_REQUEST = 1888;
    String temp;
    Bitmap bitmap;
    String imgString;
    TextView tvName, tvTime;
    final int CROP_PIC = 2;
    private Uri picUri;

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

        tvName = (TextView) findViewById(R.id.tvuname1);
        tvTime = (TextView) findViewById(R.id.tvDATe);
        btnSave = (Button) findViewById(R.id.btn_save_new);
        btnnclear = (Button) findViewById(R.id.btn_clear);

        btnSearch = (Button) findViewById(R.id.btn_search);
        btnreset = (Button) findViewById(R.id.btn_reset);
        llSeeding = (LinearLayout) findViewById(R.id.LinearlayoutSeeding);
        llShowDetails = (LinearLayout) findViewById(R.id.linear_layout_panal);
        imgAadhar = (ImageView) findViewById(R.id.img_Aadhar);

        Intent in = getIntent();
        String uuserName = in.getStringExtra("name");
        String dte = in.getStringExtra("Date");

        tvName.setText(uuserName);
        tvTime.setText(dte);

        etAccountNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

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

            }

            @Override
            public void afterTextChanged(Editable s) {

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

                if (AccNo.toString().length() != 0 || Customerid.toString().length() != 0) {

                    //tv_error.setVisibility(View.GONE);
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
                    //tv_error.setVisibility(View.VISIBLE);
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

                /*if (etNameInAadhaar.getText().toString().length() == 0) {
                    // Toast.makeText(getApplicationContext(), "Name cannot be Blank", Toast.LENGTH_LONG).show();
                    etNameInAadhaar.setError("Please Enter Name as in Aadhaar");

                } else {
                    if (etNewAadhaarNo.getText().toString().length() == 0) {

                        // Toast.makeText(getApplicationContext(),"Please Enter currect password",Toast.LENGTH_SHORT);
                        etNewAadhaarNo.setError("Please Enter currect Aadhaar number");

                    } else {
                        if (etNewAadhaarNo.getText().length() == 12) {
*/
                if (validno() && validimage()) {
                    String upDateAdarNo = etNewAadhaarNo.getText().toString();
                    String upDateAdrName = etNameInAadhaar.getText().toString();

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

        imgAadhar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                File file = new File(Environment.getExternalStorageDirectory() + File.separator + "image.jpg");
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        });

    }


    class GetUserData extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(AadhaarSeedingSearchActivity.this);
            pDialog.setMessage("Fatching Data....");
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


            //   String NewUrl = args[0];
            Map<String, String> params = new LinkedHashMap<>();
            params.put("Method", "UpdateMemberForAll");
            params.put("AadhaarNo", args[0]);
            params.put("NewNameAsAadhaar", args[1]);
            params.put("AccountNo", args[2]);
            params.put("RecordNo", args[3]);
            params.put("SourceType", "1");
            params.put("UserCode", "13");
            params.put("branchcd", "9999");
            params.put("ZoneCode", "1");
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


    public static Bitmap decodeSampledBitmapFromFile(String path, int reqWidth, int reqHeight) { // BEST QUALITY MATCH

        //First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        // Calculate inSampleSize, Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        int inSampleSize = 1;

        if (height > reqHeight) {
            inSampleSize = Math.round((float) height / (float) reqHeight);
        }
        int expectedWidth = width / inSampleSize;

        if (expectedWidth > reqWidth) {
            //if(Math.round((float)width / (float)reqWidth) > inSampleSize) // If bigger SampSize..
            inSampleSize = Math.round((float) width / (float) reqWidth);
        }

        options.inSampleSize = inSampleSize;

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(path, options);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST) {
            //Get our saved file into a bitmap object:
            File file = new File(Environment.getExternalStorageDirectory() + File.separator + "image.jpg");
            bitmap = decodeSampledBitmapFromFile(file.getAbsolutePath(), 500, 500);
//	       ImageView imageView = (ImageView) findViewById(R.id.Imageprev);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            imgString = Base64.encodeToString(getBytesFromBitmap(bitmap), Base64.NO_WRAP);
            imgAadhar.setImageBitmap(bitmap);

        }


    }

    public boolean validno()
    {
        if (etNewAadhaarNo.getText().length()==0 && etNewAadhaarNo.getText().length()<12){
            Toast.makeText(AadhaarSeedingSearchActivity.this,"enter",Toast.LENGTH_LONG).show();
        }
         return true;
    }

    public boolean validimage()
    {
        if (imgAadhar.getTag()==null){
            Toast.makeText(AadhaarSeedingSearchActivity.this,"enter image",Toast.LENGTH_LONG).show();
        }
        return true;
    }
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
            cropIntent.putExtra("aspectX", 2);
            cropIntent.putExtra("aspectY", 1);
            // indicate output X and Y
            cropIntent.putExtra("outputX", 256);
            cropIntent.putExtra("outputY", 256);
            // retrieve data on return
            cropIntent.putExtra("return-data", true);
            // start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, CROP_PIC);
        }
        // respond to users whose devices do not support the crop action
        catch (ActivityNotFoundException anfe) {
            Toast toast = Toast
                    .makeText(this, "This device doesn't support the crop action!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }



}
