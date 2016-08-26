package activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.scanlibrary.ScanActivity;
import com.scanlibrary.ScanConstants;

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
import java.util.regex.Pattern;

import bean.UserDataInfoBean;

/**
 * Created by Administrator on 17-08-2016.
 */
public class DialogfragmentActivity extends Activity {

    ArrayList<UserDataInfoBean> userinfoList = new ArrayList();

    ProgressDialog pDialog;
    Reader in;
    // Dialog dialog;
    String UserCode, BranchCode, ZoneCode, AccountNo, RecordNo;
    EditText edtAdhrNo;
    EditText edtAdhrNoAsInAdr;
    Button btnSave, btnExit, btnClear;
    ImageView img_aadharimg;
    private static final int REQUEST_CODE = 99;
    Bitmap bitmap;
    String imgString;
    String upDateAdarNo, upDateAdrName;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.dialog);
        edtAdhrNo = (EditText) findViewById(R.id.et_dialog_adhr);
        edtAdhrNoAsInAdr = (EditText) findViewById(R.id.et_no_per_adhr);
        btnSave = (Button) findViewById(R.id.bt_dialog_save);
        btnExit = (Button) findViewById(R.id.bt_diaog_exit);
        btnClear = (Button) findViewById(R.id.bt_dialog_clear);
        img_aadharimg = (ImageView) findViewById(R.id.img_Aadharimg);
        img_aadharimg.setOnClickListener(new ScanButtonClickListener(ScanConstants.OPEN_CAMERA));

        SharedPreferences prefs = getSharedPreferences("loginData", MODE_PRIVATE);

        UserCode = prefs.getString("UserCode", "defUserCode");
        BranchCode = prefs.getString("BranchCode", "defBranchCode");
        ZoneCode = prefs.getString("ZoneCode", "defZoneCode");

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int height = dm.heightPixels;
        int width = dm.widthPixels;

        if (height <= 800 && width <= 480) {

            img_aadharimg.getLayoutParams().height = 81;
            img_aadharimg.getLayoutParams().width = 81;

        } else if (height <= 1280 && width <= 720) {

            img_aadharimg.getLayoutParams().height = 161;
            img_aadharimg.getLayoutParams().width = 142;

        } else if (height <= 1920 && width <= 1080) {

            img_aadharimg.getLayoutParams().height = 155;
            img_aadharimg.getLayoutParams().width = 155;
        } else if (height <= 940 && width <= 600) {

            img_aadharimg.getLayoutParams().height = 102;
            img_aadharimg.getLayoutParams().width = 102;
        } else if (height <= 2560 && width <= 1440) {

            img_aadharimg.getLayoutParams().height = 240;
            img_aadharimg.getLayoutParams().width = 220;
        }

        Intent in = getIntent();
        AccountNo = in.getStringExtra("accountNo");
        RecordNo = in.getStringExtra("recordNo");


        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtAdhrNo.setText("");
                edtAdhrNoAsInAdr.setText("");

            }
        });

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();

            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upDateAdarNo = edtAdhrNo.getText().toString();
                upDateAdrName = edtAdhrNoAsInAdr.getText().toString();

                if (validAadharName() && validateAadharNumber(upDateAdarNo) && validImage()) {


                    ConnectivityManager ConnectionManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo networkInfo = ConnectionManager.getActiveNetworkInfo();
                    if (networkInfo != null && networkInfo.isConnected() == true) {

                        UpdateUserData getUserData = new UpdateUserData();
                        getUserData.execute(upDateAdarNo, upDateAdrName, AccountNo, RecordNo);

                    } else {
                        Toast.makeText(DialogfragmentActivity.this, "Network not available", Toast.LENGTH_LONG).show();

                    }
                }
            }

        });

        edtAdhrNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String ss = String.valueOf(s).trim();
                if (ss.toString().trim().length() <= 12) {
                    validateAadharNumber(ss);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().trim().length() == 0) {

                    edtAdhrNo.setError(null);
                }
            }
        });
    }


    class UpdateUserData extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(DialogfragmentActivity.this);
            pDialog.setMessage("Updating....");
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
            params.put("UserCode", UserCode);
            params.put("branchcd", BranchCode);
            params.put("ZoneCode", ZoneCode);
            params.put("ind", "1");
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
                Toast.makeText(DialogfragmentActivity.this, json.toString(), Toast.LENGTH_LONG).show();
                pDialog.dismiss();
            }
            finish();


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

            MarshMelloPermission marshMallowPermission = new MarshMelloPermission(DialogfragmentActivity.this);
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
                img_aadharimg.setScaleType(ImageView.ScaleType.FIT_XY);
                img_aadharimg.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public byte[] getBytesFromBitmap(Bitmap photo) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        return stream.toByteArray();
    }

    public boolean validateAadharNumber(String aadharNumber) {
        Pattern aadharPattern = Pattern.compile("\\d{12}");
        boolean isValidAadhar = aadharPattern.matcher(aadharNumber).matches();
        if (isValidAadhar) {
            isValidAadhar = VerhoeffAlgorithm.validateVerhoeff(aadharNumber);
            if (isValidAadhar == false) {
                edtAdhrNo.setError("Invalid Aadhar number");
            }
        } else {
            edtAdhrNo.setError("Incomplete Aadhaar number");
        }
        return isValidAadhar;
    }

    public boolean validAadharName() {

        if (edtAdhrNoAsInAdr.getText().toString().trim().equalsIgnoreCase("")) {
            Toast.makeText(DialogfragmentActivity.this, "Enter Valid Aadhaar Name", Toast.LENGTH_SHORT).show();
        } else {
            return true;
        }

        return false;
    }

    public boolean validImage() {

        if (img_aadharimg.getDrawable() == null) {

            Toast.makeText(DialogfragmentActivity.this, "Please Click Image", Toast.LENGTH_SHORT).show();
        } else {

            return true;
        }

        return false;
    }

}