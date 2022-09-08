package com.affinitity.niche.ui.pdf;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.affinitity.niche.MainActivity;
import com.affinitity.niche.MyInterface;
import com.affinitity.niche.R;
import com.affinitity.niche.URLs;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class PdfFragment extends Fragment {
    private SearchView.OnQueryTextListener queryTextListener;
    public static final String BASE_URL = "https://beaconsintl.com/";
    private RecyclerView mRecyclerView;
    TextView nodata;
    private ArrayList<PdfModel> mArrayList;
    private SearchView searchView = null;
    private PdfAdapter mAdapter;
    Context context;

    private MainActivity mainActivity;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        View root =  inflater.inflate(R.layout.fragment_dashboard, container, false);
        mRecyclerView = (RecyclerView) root.findViewById(R.id.card_recycler_view);
        nodata  = (TextView) root. findViewById(R.id.nodata);
        mRecyclerView.setHasFixedSize(true);

//        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
//        mRecyclerView.setLayoutManager(layoutManager);

        mainActivity      = (MainActivity) getActivity();
        mainActivity.getSupportActionBar().setTitle("PDF");
 AutoFitGridLayoutManager layoutManager = new AutoFitGridLayoutManager(getActivity(), 500);
        mRecyclerView.setLayoutManager(layoutManager);
        loadJSON();
        return  root;
    }

    private void loadJSON(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URLs.WEB_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        MyInterface request = retrofit.create(MyInterface.class);
        Call<PdfResponse> call = request.getPDF();
        call.enqueue(new Callback<PdfResponse>() {
            @Override
            public void onResponse(Call<PdfResponse> call, Response<PdfResponse> response) {

                PdfResponse jsonResponse = response.body();
                mArrayList = new ArrayList<>(Arrays.asList(jsonResponse.getData()));
                mAdapter = new PdfAdapter(mArrayList, getActivity());
                mRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onFailure(Call<PdfResponse> call, Throwable t) {
                Log.d("Error",t.getMessage());
            }
        });
    }



    @Override
    public void  onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.search_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(context.SEARCH_SERVICE);

        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));

            queryTextListener = new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextChange(String newText) {
                    Log.i("onQueryTextChange", newText);
                    mAdapter.getFilter().filter(newText);
                    if (mAdapter.getItemCount() == 0) {
                        mRecyclerView.setVisibility(View.GONE);
                        nodata.setVisibility(View.VISIBLE);
                    } else {
                        mRecyclerView.setVisibility(View.VISIBLE);
                        nodata.setVisibility(View.GONE);
                    }

//                    if (mAdapter.getItemCount()<=0){
//                        Toast.makeText(getActivity(), "No Data", Toast.LENGTH_SHORT).show();
//                    } else
                    // mAdapter.getFilter().filter(newText);
                    // if (mAdapter != null)  mAdapter.getFilter().filter(newText);


                    return true;
                }
                @Override
                public boolean onQueryTextSubmit(String query) {
                    Log.i("onQueryTextSubmit", query);

                    return false;
                }


            };

            searchView.setOnQueryTextListener(queryTextListener);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                // Not implemented here

                return false;
            default:
                break;
        }
        searchView.setOnQueryTextListener(queryTextListener);
        return super.onOptionsItemSelected(item);
    }
//    private void search(SearchView searchView) {
//
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//
//                if (mAdapter != null) mAdapter.getFilter().filter(newText);
//                return true;
//            }
//        });
//    }
//

}