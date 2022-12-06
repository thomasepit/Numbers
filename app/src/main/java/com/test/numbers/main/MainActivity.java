package com.test.numbers.main;


import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.test.numbers.data.DataClass;
import com.test.numbers.R;
import com.test.numbers.detail.DetailActivity;
import com.test.numbers.detail.FragmentDetail;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NumbersListFragment.Listener, Controller.Mylistner {
    private RequestQueue volley_request_queue;
    private StringRequest volley_stringrequest;
    ArrayList<DataClass> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getDeviceInfo(getApplicationContext(), Device.DEVICE_TYPE).equals("Tablet")) {
            setContentView(R.layout.activity_main_tablet);
        } else {
            setContentView(R.layout.activity_main);
        }

        if (savedInstanceState == null) {

            if (Controller.isNetworkConnected(MainActivity.this)) {
                get_request();

            } else {
                Controller.show_network_info_dialog(MainActivity.this);
            }


        }
    }

    @Override
    public void itemClicked(int pos) {

        if (findViewById(R.id.numbers_detail) == null) {
            Intent detailIntent = new Intent(this, DetailActivity.class);
            detailIntent.putExtra(FragmentDetail.item_position, pos);
            startActivity(detailIntent);

        } else {
            Bundle arguments = new Bundle();
            arguments.putInt(FragmentDetail.item_position, pos);
            FragmentDetail fragment = new FragmentDetail();
            fragment.setArguments(arguments);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.numbers_detail, fragment);
            ft.addToBackStack(null);
            ft.commit();
        }
    }

    public void get_request() {
        Controller.showLoader(MainActivity.this, "Please wait . . .");
        volley_request_queue = Volley.newRequestQueue(MainActivity.this);
        volley_stringrequest = new StringRequest(Request.Method.GET, Controller.LIST_DATA_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {
                    DataClass.list.clear();
                    JSONArray data = new JSONArray(response);

                    for (int index = 0; index < data.length(); index++) {
                        JSONObject obj = data.getJSONObject(index);
                        String name = obj.getString("name");
                        String image = obj.getString("image");
                        DataClass.list.add(new DataClass(name, image));
                    }

                    if (DataClass.list.size() > 0) {
                        Controller.stopLoader();
                        NumbersListFragment numbersListFragment = new NumbersListFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.list_numbers, numbersListFragment).commit();

                        if (findViewById(R.id.numbers_detail) != null) {
                            Bundle arguments = new Bundle();
                            arguments.putInt(FragmentDetail.item_position, 0);
                            FragmentDetail fragment = new FragmentDetail();
                            fragment.setArguments(arguments);
                            getSupportFragmentManager().beginTransaction().replace(R.id.numbers_detail, fragment).commit();
                        }

                    } else {
                        Toast.makeText(MainActivity.this, "No data found!", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_LONG).show();
                    Controller.stopLoader();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        volley_stringrequest.setRetryPolicy(new DefaultRetryPolicy(60000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        volley_request_queue.add(volley_stringrequest);

    }

    @Override
    public void func() {

        if (Controller.isNetworkConnected(MainActivity.this)) {
            get_request();
        } else {
            Controller.show_network_info_dialog(MainActivity.this);
        }
    }

    public enum Device {
        DEVICE_TYPE,
    }

    public static String getDeviceInfo (Context context, Device device) {
        try {
            switch (device) {
                case DEVICE_TYPE:
                    if (isTablet(context)) {
                        if (getDevice5inch(context)) {
                            return "Tablet";
                        } else {
                            return "Mobile";
                        }
                    } else {
                        return "Mobile";
                    }
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private static boolean getDevice5inch(Context context) {
        try {
            DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
            float yinch = displayMetrics.heightPixels / displayMetrics.ydpi;
            float xinch = displayMetrics.widthPixels / displayMetrics.xdpi;

            double diagonalInches = Math.sqrt(xinch * xinch + yinch * yinch);
            if (diagonalInches >= 7) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }


    private static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }
}
