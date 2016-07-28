package Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;

import com.example.administrator.bankofindia.R;

import java.util.ArrayList;

import adapter.ShowSeedingAdapter;
import bean.UserDataInfoBean;

public class Showseedingactivity extends AppCompatActivity {
    ListView lvData;
    ShowSeedingAdapter showSeedingAdapter;
    ArrayList<UserDataInfoBean> userinfoList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_showseedingactivity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool1);
        lvData = (ListView) findViewById(R.id.sedding_listview);

        Intent intent = getIntent();
        userinfoList = (ArrayList<UserDataInfoBean>) intent.getSerializableExtra("list");
        if(userinfoList!= null) {
            showSeedingAdapter = new ShowSeedingAdapter(this, userinfoList);
            lvData.setAdapter(showSeedingAdapter);

        }
    }


}