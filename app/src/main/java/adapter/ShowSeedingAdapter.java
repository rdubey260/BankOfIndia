package adapter;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.bankofindia.Constant;
import com.example.administrator.bankofindia.JSONParser;
import com.example.administrator.bankofindia.R;

import java.io.BufferedReader;
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

import bean.UserDataInfoBean;

/**
 * Created by Administrator on 22-07-2016.
 */
public class ShowSeedingAdapter extends BaseAdapter {

    ArrayList<UserDataInfoBean> userinfoList = new ArrayList();
    Context context;
    ProgressDialog pDialog;
    Reader in;

    public ShowSeedingAdapter(Context context, ArrayList<UserDataInfoBean> userinfoList) {
        this.userinfoList = userinfoList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return userinfoList.size();
    }

    @Override
    public Object getItem(int position) {
        return userinfoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater layInf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = layInf.inflate(R.layout.user_data_row, parent, false);
        TextView tvSno = (TextView) convertView.findViewById(R.id.tv_s_no1);
        TextView tvCustomrId = (TextView) convertView.findViewById(R.id.tv_customer_id1);
        TextView tvAccount = (TextView) convertView.findViewById(R.id.tv_acc_no1);
        TextView tvCustomerName = (TextView) convertView.findViewById(R.id.tv_custmer_name1);
        TextView Addrno = (TextView) convertView.findViewById(R.id.tv_aadhar_no1);
        final CheckBox selectcheckbox = (CheckBox) convertView.findViewById(R.id.select_checkbox);

        int pos = position + 1;
        String p = String.valueOf(pos);
        tvSno.setText(p);
        tvCustomerName.setText(userinfoList.get(position).getCustomerName());
        tvCustomrId.setText(userinfoList.get(position).getCustomerId());
        tvAccount.setText(userinfoList.get(position).getAccountNo());
        Addrno.setText(userinfoList.get(position).getAadhaarNo());

        selectcheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.dialog);
                final EditText edtAdhrNo = (EditText) dialog.findViewById(R.id.et_dialog_adhr);
                final EditText edtAdhrNoAsInAdr = (EditText) dialog.findViewById(R.id.et_no_per_adhr);
                Button btnSave = (Button) dialog.findViewById(R.id.bt_dialog_save);
                Button btnExit = (Button) dialog.findViewById(R.id.bt_diaog_exit);
                Button btnClear = (Button) dialog.findViewById(R.id.bt_dialog_clear);
                dialog.show();

                btnExit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        selectcheckbox.setChecked(false);
                    }
                });

                btnClear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        edtAdhrNo.setText("");
                        edtAdhrNoAsInAdr.setText("");
                    }
                });

                btnSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectcheckbox.setChecked(false);

                        String upDateAdarNo = edtAdhrNo.getText().toString();
                        String upDateAdrName = edtAdhrNoAsInAdr.getText().toString();
                        String accountNo = userinfoList.get(position).getAccountNo();
                        String recordNo = userinfoList.get(position).getRecordNo();

                        //  JsonParams jsnp= new JsonParams(upDateAdarNo,upDateAdrName,"8841","1",accountNo,recordNo,"1","8");


                        //   String url = "http://103.21.54.52/BOIWebAPI/api/BoiMember/UpdateMember?"+String.valueOf(jsnp);
                        // String url = "http://103.21.54.52/BOIWebAPI/api/BoiMember/GetRecord?ind=1&CustomerId=&SourceType=2&MobileNo=&CustomerName=amit%20yadav&AadhaarNo=&UserCode=8&Campcd=1&branchcd=8841&ZoneCode=1&AccountNo=&RecordNo=";
                        //  String url = "http://103.21.54.52/BOIWebAPI/api/BoiMember/UpdateMember?ind=1&AadhaarNo="+upDateAdarNo+"&branchcd=8841&ZoneCode=1&AccountNo="+accountNo+"&NewNameAsAadhaar="+upDateAdrName+"&SourceType=1&UserCode=8&RecordNo="+recordNo;
                        ConnectivityManager ConnectionManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                        NetworkInfo networkInfo = ConnectionManager.getActiveNetworkInfo();
                        if (networkInfo != null && networkInfo.isConnected() == true) {
                            UpdateUserData getUserData = new UpdateUserData();
                            getUserData.execute(upDateAdarNo, upDateAdrName, accountNo, recordNo);

                        } else {
                            Toast.makeText(context, "Network Not Available", Toast.LENGTH_LONG).show();

                        }
                    }
                });
            }
        });

        return convertView;
    }

    class UpdateUserData extends AsyncTask<String, Void, String> {
        JSONParser jsonParser = new JSONParser();

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(context);
            pDialog.setMessage("Fatching Data....");
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {

         //   String NewUrl = args[0];
            Map<String, String> params = new LinkedHashMap<>();
            params.put("Method", "UpdateMember");
            params.put("AadhaarNo", args[0]);
            params.put("NewNameAsAadhaar", args[1]);
            params.put("AccountNo", args[2]);
            params.put("RecordNo", args[3]);
            params.put("SourceType", "1");
            params.put("UserCode", "8");
            params.put("branchcd", "8841");
            params.put("ZoneCode", "1");
            params.put("ind", "1");

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
                url = new URL("http://103.21.54.52/BOIWebAPI/api/BoiMember/UpdateMember?");
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
                Toast.makeText(context, json.toString(), Toast.LENGTH_LONG).show();

            }
            pDialog.dismiss();


        }

    }


}



