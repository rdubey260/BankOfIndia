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
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoginFormActivity extends AppCompatActivity {

    Button btnLogin, btnClear;
    EditText etUserName, etpassword;
    Toolbar toolbar;
    ProgressDialog pDialog;
    String username;
    ProgressBar pbar;
    String UserName;
    String UserFullName;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_loginform);

        toolbar = (Toolbar) findViewById(R.id.tool2);


        btnClear = (Button) findViewById(R.id.btnClear);
        btnLogin = (Button) findViewById(R.id.btnLogin);

        etUserName = (EditText) findViewById(R.id.etUserName);
        etpassword = (EditText) findViewById(R.id.etPassword);




        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etUserName.getText().toString().trim().length() == 0) {
                    // Toast.makeText(getApplicationContext(), "Name cannot be Blank", Toast.LENGTH_LONG).show();
                    etUserName.setError("Please enter user name");

                    return;
                } else {
                    if (etpassword.getText().toString().trim().length() == 0) {

                        // Toast.makeText(getApplicationContext(),"Please Enter currect password",Toast.LENGTH_SHORT);
                        etpassword.setError("Please enter correct password");

                    } else {

                        String password = etpassword.getText().toString().trim();
                        username = etUserName.getText().toString().trim();

                        String url = "http://103.21.54.52/BOIWebAPI/api/BoiMember/CheckLogin?ind=0&Password=" + password + "&UserName=" + username;

                        ConnectivityManager ConnectionManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                        NetworkInfo networkInfo = ConnectionManager.getActiveNetworkInfo();
                        if (networkInfo != null && networkInfo.isConnected() == true) {
                            GetUserData getUserData = new GetUserData();
                            getUserData.execute(url);

                        } else {

                            Toast.makeText(LoginFormActivity.this, "Network Not Available", Toast.LENGTH_LONG).show();

                        }


                    }
                }

            }
        });
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etUserName.setText("");
                etpassword.setText("");
            }
        });
    }


    class GetUserData extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(LoginFormActivity.this);
            pDialog.setMessage("Please Wait...");
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
                pDialog.dismiss();

                try {
                    JSONArray jsonArry = new JSONArray(json);
                    for (int i = 0; i < jsonArry.length(); i++) {

                        JSONObject jsonObject = jsonArry.getJSONObject(i);
                        String UserCode = jsonObject.getString("UserCode");
                        UserFullName = jsonObject.getString("UserFullName");
                        String BcCode = jsonObject.getString("BcCode");
                        String LevelCode = jsonObject.getString("LevelCode");
                        UserName = jsonObject.getString("UserName");
                        String ZoneCode = jsonObject.getString("ZoneCode");
                        String ZoneDesc = jsonObject.getString("ZoneDesc");
                        String CityCode = jsonObject.getString("CityCode");
                        String CityName = jsonObject.getString("CityName");
                        String BranchCode = jsonObject.getString("BranchCode");
                        String BranchName = jsonObject.getString("BranchName");
                        String CampCode = jsonObject.getString("CampCode");
                        String CampDesc = jsonObject.getString("CampDesc");

                        SharedPreferences.Editor editor = getSharedPreferences("loginData", MODE_PRIVATE).edit();
                        editor.putString("UserCode", UserCode);
                        editor.putString("BranchCode", BranchCode);
                        editor.putString("ZoneCode", ZoneCode);

                        editor.commit();

                    }

                    Intent in = new Intent(LoginFormActivity.this, HomePageActivity.class);
                    in.putExtra("mytext", UserFullName);
                    startActivity(in);
                    etUserName.setText("");
                    etpassword.setText("");


                } catch (JSONException e) {
                    Toast.makeText(LoginFormActivity.this, "Enter valid username/password", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            }


        }

    }
}
