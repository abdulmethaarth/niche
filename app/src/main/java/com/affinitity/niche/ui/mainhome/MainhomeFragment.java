package com.affinitity.niche.ui.mainhome;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.affinitity.niche.ConstantsStored;
import com.affinitity.niche.MyInterface;
import com.affinitity.niche.R;
import com.affinitity.niche.URLs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.MODE_PRIVATE;


public class MainhomeFragment extends Fragment {

    RecyclerView recyclerView;
    Activity context;
    private ArrayList<MainhomeModel> mainhomeModelArrayList;
    private MainhomeAdapter mainhomeAdapter;
    private ArrayList<ResultsResponse>resultsResponses ;
    TextView textView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_mainhome, container, false);
        recyclerView = (RecyclerView) root.findViewById(R.id.mainhomeRecyclerView);
        textView = (TextView) root.findViewById(R.id.sorry);
        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager( getContext()));

loadJSON();
        return  root;
    }

    private void loadJSON(){
        SharedPreferences prefs = getActivity().getSharedPreferences(ConstantsStored.MY_PREFS_NAME, MODE_PRIVATE);
       String userId = prefs.getString(ConstantsStored.user_id, "");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URLs.WEB_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        MyInterface request = retrofit.create(MyInterface.class);
        Call<MainhomeModel> call = request.getHome(userId);
        call.enqueue(new Callback<MainhomeModel>() {
            @Override
            public void onResponse(Call<MainhomeModel> call, Response<MainhomeModel> response) {

                MainhomeModel data = response.body();
                resultsResponses = response.body().getResults();
                if(data.getStatus().equals(true)) {
                    textView.setVisibility(View.GONE);
                 //   Log.d("Succcess",data.getStatus());
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                    recyclerView.setLayoutManager(linearLayoutManager);
                    mainhomeAdapter  = new MainhomeAdapter(resultsResponses,getContext());
                    recyclerView.setAdapter(mainhomeAdapter);
                }
                else {
                    textView.setVisibility(View.VISIBLE);
                    textView.setText("Sorry No Data Found");

                }



//                if(data.getStatus().equalsIgnoreCase("true"))
//                {
//                    Log.d("Succcess", data.getResults());
//                  recyclerView.setAdapter(mainhomeAdapter);
//                }else{
//                    Toast.makeText(context, "Not Loading", Toast.LENGTH_SHORT).show();
//                    Log.d("Succcess", data.getStatus());
//                }
//                Toast.makeText(context, "EWork Loading", Toast.LENGTH_SHORT).show();

//                MainhomeModel jsonResponse = response.body();
//                mArrayList = new ArrayList<>(Arrays.asList(jsonResponse.getData()));
//                mainhomeAdapter = new MainhomeAdapter(mArrayList, getActivity());
//                recyclerView.setAdapter(mainhomeAdapter);
            }

            @Override
            public void onFailure(Call<MainhomeModel> call, Throwable t) {
                Log.d("Error",t.getMessage());
                Toast.makeText(getContext(), "Error loading", Toast.LENGTH_SHORT).show();

            }
        });
    }
}