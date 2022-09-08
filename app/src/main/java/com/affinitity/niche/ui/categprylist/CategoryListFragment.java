package com.affinitity.niche.ui.categprylist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.affinitity.niche.MyInterface;
import com.affinitity.niche.R;
import com.affinitity.niche.URLs;
import com.affinitity.niche.ui.dashboard.AutoFitGridLayoutManager;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class CategoryListFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public static final String BASE_URL = "https://beaconsintl.com/";
    TextView textView;
RecyclerView recyclerView;
    private ArrayList<CategoryListModel> data;

private CategoryDataAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_category_list, container, false);

textView = (TextView) view.findViewById(R.id.catID);
        recyclerView = (RecyclerView)view.findViewById(R.id.card_recycler_view);
        recyclerView.setHasFixedSize(true);
        AutoFitGridLayoutManager layoutManager = new AutoFitGridLayoutManager(getActivity(), 500);
        recyclerView.setLayoutManager(layoutManager);

        String ID  = getArguments().getString("id");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URLs.WEB_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        MyInterface request = retrofit.create(MyInterface.class);
        Call<CategoryResponse> call = request.getCategory(ID);
        call.enqueue(new Callback<CategoryResponse>() {


            @Override
            public void onResponse(Call<CategoryResponse> call, Response<CategoryResponse> response) {
//                List<CategoryResponse> userList = response.body();
                //       List<CategoryModel> categoryModels  = (List<CategoryModel>) response.body().getResults();
                //  List<CategoryModel> contactModelList = new ArrayList<>();
                //   data = new ArrayList<>(Arrays.asList(jsonResponse.getAndroid()));
//                adapter = new CategoryDataAdapter((ArrayList<CategoryModel>) userList,getContext());
//                recyclerView.setAdapter(adapter);

//                textView.setText(response.body().getCategoryModelList().get(0).getVideoName());
                //   data  = new ArrayList<>(Arrays.asList(categoryModels.ge()));



    CategoryResponse categoryResponse = response.body();
    if(categoryResponse.isError() == true) {

        textView.setVisibility(View.GONE);
        data = new ArrayList<>(Arrays.asList(categoryResponse.getResponse()));
        adapter = new CategoryDataAdapter(data,getContext());
        recyclerView.setAdapter(adapter);
    }
    else {
        textView.setVisibility(View.VISIBLE);
        textView.setText("Sorry No Data Found");
    }


            }

            @Override
            public void onFailure(Call<CategoryResponse> call, Throwable t) {

            }
        });



        return view;
    }

    private void loadJSON(){

    }



}
