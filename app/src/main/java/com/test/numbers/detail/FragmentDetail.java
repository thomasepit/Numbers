package com.test.numbers.detail;


import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;
import com.test.numbers.main.Controller;
import com.test.numbers.data.DataClass;
import com.test.numbers.R;

import org.json.JSONObject;


public class FragmentDetail extends Fragment implements Controller.Mylistner {
    public static final String item_position = "position";
    private DataClass numbers;
    private RequestQueue volley_request_queue;
    private StringRequest volley_stringrequest;
    private TextView txt_text, txt_number;
    private ImageView imageView;

    public FragmentDetail() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(item_position)) {
            numbers = DataClass.list.get(getArguments().getInt(item_position));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.detail_fragment, container, false);

        if (numbers != null) {
            txt_number = (TextView) v.findViewById(R.id.txt_number);
            txt_text = (TextView) v.findViewById(R.id.txt_text);

            txt_number.setText(numbers.getName());
            imageView = (ImageView) v.findViewById(R.id.image);
            if (Controller.isNetworkConnected(getActivity())) {

                fetch_data(numbers.getName());
            } else {
                Controller.show_network_info_dialog(getActivity());
            }
        }

        return v;
    }


    public void fetch_data(String name) {
        Controller.showLoader(getActivity(), "Please wait . . .");
        volley_request_queue = Volley.newRequestQueue(getActivity());
        volley_stringrequest = new StringRequest(Request.Method.GET, Controller.DETAIL_DATA_URL + name, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    if (response != null) {
                        JSONObject obj = new JSONObject(response);

                        String name = obj.getString("name");
                        String text = obj.getString("text");
                        String large_image = obj.getString("image");
                        txt_number.setText(name);
                        txt_text.setText(text);
                        Picasso.get().load((large_image)).into(imageView);

                        Controller.stopLoader();


                    }


                } catch (Exception e) {
                    Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();
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
        if (Controller.isNetworkConnected(getActivity())) {

            fetch_data(numbers.getName());
        } else {
            Controller.show_network_info_dialog(getActivity());
        }

    }
}
