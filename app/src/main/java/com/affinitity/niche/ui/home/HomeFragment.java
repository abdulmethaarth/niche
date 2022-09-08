package com.affinitity.niche.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.affinitity.niche.R;
import com.affinitity.niche.URLs;
import com.affinitity.niche.ui.categprylist.CategoryListFragment;
import com.affinitity.niche.ui.maincategory.MainFragment;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.viewpagerindicator.CirclePageIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class HomeFragment extends Fragment implements RecyclerViewAdapter.ItemListener {
    public Context context = getActivity();
    private HomeViewModel homeViewModel;
    RecyclerView recyclerView;
    ArrayList<CategoryModel> categoryModelArrayList;
    private    RecyclerViewAdapter adapter;
private Button category;

    //slider
    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;

    private static final Integer[] IMAGES= {R.drawable.one,R.drawable.two,R.drawable.three,R.drawable.five};
    private ArrayList<Integer> ImagesArray = new ArrayList<Integer>();


    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View root =  inflater.inflate(R.layout.fragment_home, container, false);
        category = (Button) root.findViewById(R.id.category);



        recyclerView = (RecyclerView) root.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        categoryModelArrayList = new ArrayList<>();


      adapter = new RecyclerViewAdapter(getContext() , categoryModelArrayList, this);
        recyclerView.setAdapter(adapter);
        AutoFitGridLayoutManager layoutManager = new AutoFitGridLayoutManager(getContext(), 500);
        recyclerView.setLayoutManager(layoutManager);


getMovieData();
        /**
         AutoFitGridLayoutManager that auto fits the cells by the column width defined.
         **/



        for(int i=0;i<IMAGES.length;i++)
            ImagesArray.add(IMAGES[i]);

        mPager = (ViewPager) root.findViewById(R.id.pager);


        mPager.setAdapter(new HomeSlidingAdapter(getContext(),ImagesArray));


        CirclePageIndicator indicator = (CirclePageIndicator)
               root.findViewById(R.id.indicator);

        indicator.setViewPager(mPager);

        final float density = getResources().getDisplayMetrics().density;

//Set circle indicator radius
        indicator.setRadius(5 * density);

        NUM_PAGES =IMAGES.length;

        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 3000, 3000);

        // Pager listener over indicator
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                currentPage = position;

            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int pos) {

            }
        });



        category.setOnClickListener(v -> {



            MainFragment mainFragment  = new MainFragment();
        //    categoryListFragment.setArguments(bundle);
            getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    mainFragment).commit();
        //    Toast.makeText(getContext(), item.getCategoryID() + " is clicked", Toast.LENGTH_SHORT).show();
        });
        return root;
    }

    @Override
    public void onItemClick(CategoryModel item) {
        Bundle bundle = new Bundle();
        bundle.putString("id", item.getCategoryID() );


        CategoryListFragment categoryListFragment  = new CategoryListFragment();
        categoryListFragment.setArguments(bundle);
        getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                categoryListFragment).commit();
   //     Toast.makeText(getContext(), item.getCategoryID() + " is clicked", Toast.LENGTH_SHORT).show();
     }


    private void getMovieData() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URLs.URL_CATEGORY, response -> {
            try {
                JSONObject jsonObject=new JSONObject(response);
                JSONArray array=jsonObject.getJSONArray("results");
                for (int i=0; i<array.length(); i++ ){
                    JSONObject ob=array.getJSONObject(i);
                    CategoryModel categoryModel=new CategoryModel(ob.getString("categoryID"),
                            ob.getString("categoryName")
                            ,ob.getString("categoryPic") );
                    categoryModelArrayList.add(categoryModel);

                }
                recyclerView.setAdapter(adapter);
            } catch (JSONException e) {
                e.printStackTrace();
                //  Toast.makeText(DetailsActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
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