package com.affinitity.niche.ui.maincategory;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.affinitity.niche.R;
import com.affinitity.niche.URLs;
import com.affinitity.niche.ui.categprylist.CategoryListFragment;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class MainFragment extends Fragment  implements MainCategoryAdapter.ItemListener {
    RecyclerView recyclerView;
    ArrayList<MainCategoryModels> mainCategoryModelsArrayList;
    private    MainCategoryAdapter  adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_main_category, container, false);
        recyclerView = (RecyclerView) root.findViewById(R.id.card_recycler_view);
        recyclerView.setHasFixedSize(true);
        mainCategoryModelsArrayList = new ArrayList<>();


        adapter = new MainCategoryAdapter(getActivity() , mainCategoryModelsArrayList, this);
        recyclerView.setAdapter(adapter);

        getMovieData();
        /**
         AutoFitGridLayoutManager that auto fits the cells by the column width defined.
         **/

         MainAutoFitGridLayoutManager layoutManager = new MainAutoFitGridLayoutManager(getActivity(), 400);
        recyclerView.setLayoutManager(layoutManager);

        return root;
    }


    public void onItemClick(MainCategoryModels item) {
        Bundle bundle = new Bundle();
        bundle.putString("id", item.getCategoryID() );


        CategoryListFragment categoryListFragment  = new CategoryListFragment();
        categoryListFragment.setArguments(bundle);
        getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                categoryListFragment).commit();
        Toast.makeText(getContext(), item.getCategoryID() + " is clicked", Toast.LENGTH_SHORT).show();
    }


    private void getMovieData() {
        String num =  getArguments().getString("paramid");
        final String urlnew = URLs.URL_CATEGORY+num;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlnew, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray array=jsonObject.getJSONArray("results");
                    for (int i=0; i<array.length(); i++ ){
                        JSONObject ob=array.getJSONObject(i);
                        MainCategoryModels mainCategoryItems=new MainCategoryModels(ob.getString("categoryID"),
                                ob.getString("categoryName")
                                ,ob.getString("categoryPic") );
                        mainCategoryModelsArrayList.add(mainCategoryItems);

                    }
                    recyclerView.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                    //  Toast.makeText(DetailsActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                // Toast.makeText(DetailsActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
        RequestQueue requestQueue= Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

}