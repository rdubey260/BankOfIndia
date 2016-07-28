package activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;


public class CustomerVerificationActivity extends AppCompatActivity {
    Toolbar toolbar;
    ImageView ivClick;
    Button btnsave, btnclear, btnexit, btngo;
    CheckBox chtac;
    EditText etaccountno, etcustomername, etmobileno, etadharno, etfullname;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_customerverification);
        toolbar = (Toolbar) findViewById(R.id.tool);
        ivClick = (ImageView) findViewById(R.id.img_take);
        btnsave = (Button) findViewById(R.id.btn_save);
        btnclear = (Button) findViewById(R.id.btn_clear);
        btnexit = (Button) findViewById(R.id.btn_exit);
        btngo = (Button) findViewById(R.id.btn_go);
        chtac = (CheckBox) findViewById(R.id.check_tandc);
        etaccountno = (EditText) findViewById(R.id.et_AccountNumber);
        etcustomername = (EditText) findViewById(R.id.et_customername);
        etmobileno = (EditText) findViewById(R.id.et_mobilenumber);
        etadharno = (EditText) findViewById(R.id.et_adharnumber);
        etfullname = (EditText) findViewById(R.id.et_fullname);


        btngo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etaccountno.getText().toString().length() == 0)

                {
                    etaccountno.setError("Please Enter  Account Number");
                }
            }
        });
        btnexit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), HomePageActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });
        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etaccountno.getText().toString().length() == 0)

                {
                    etaccountno.setError("Please Enter  Account Number");
                } else {
                    if (etmobileno.getText().toString().length() == 0) {
                        etmobileno.setError("Please Enter Mobile number");
                    } else {
                        if (etadharno.getText().toString().length() == 0) {
                            etadharno.setError("Please Enter Aadhar number");
                        } else {
                            if (etfullname.getText().toString().length() == 0)

                            {
                                etfullname.setError("Please Enter Your Fullname");

                            }
                        }

                    }
                }
            }
        });

        btnclear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etaccountno.setText("");
                etcustomername.setText("");
                etadharno.setText("");
                etmobileno.setText("");
                etfullname.setText("");

            }
        });


        ivClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent in = getPackageManager().getLaunchIntentForPackage("com.intsig.camscanner");

                if (in == null) {

                    in = new Intent(Intent.ACTION_VIEW);
                    in.setData(Uri.parse("market://details?id=" + "com.intsig.camscanner"));

                }
                startActivity(in);

            }
        });


    }
}
