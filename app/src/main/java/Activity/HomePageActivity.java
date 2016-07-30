package activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import java.util.Calendar;
import java.util.Date;


public class HomePageActivity extends AppCompatActivity {

    Button btfirst,btsecond,btthird,btfourth;
    TextView tvuname,tvDAT;
    Toolbar toolbar;
    String dt;
    String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_homepage);


        toolbar = (Toolbar) findViewById(R.id.tool1);


        btfirst = (Button) findViewById(R.id.bt_first);
        btsecond = (Button) findViewById(R.id.bt_second);
        btthird = (Button) findViewById(R.id.bt_third);
        btfourth = (Button) findViewById(R.id.bt_forth);

        tvuname = (TextView) findViewById(R.id.tvuname);
        tvDAT = (TextView) findViewById(R.id.tvDAT);


        Date cal = (Date) Calendar.getInstance().getTime();
        dt = cal.toLocaleString();
        tvDAT.setText(dt.toString());

        Intent in = getIntent();
        userName= in.getStringExtra("mytext");

        tvuname.setText(userName);

        btfirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomePageActivity.this, CustomerVerificationActivity.class);
                startActivity(i);
            }
        });


        btsecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomePageActivity.this, Aadhaarseedingactivity.class);
                i.putExtra("name",userName);
                i.putExtra("Date",dt);
                startActivity(i);
            }
        });


        btthird.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomePageActivity.this, AadhaarSeedingSearchActivity.class);
                i.putExtra("name",userName);
                i.putExtra("Date",dt);
                startActivity(i);
            }
        });


        btfourth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomePageActivity.this, CustomerVerificationActivity.class);
                startActivity(i);
            }
        });

    }
}
