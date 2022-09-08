package com.affinitity.niche.ui.mainhome;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;


import com.affinitity.niche.R;
import com.affinitity.niche.ui.videoportion.VideoportionFragment;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class MainhomeAdapter extends RecyclerView.Adapter<MainhomeAdapter.ViewHolder> {

    private ArrayList<ResultsResponse> mainhomeModelList;
      Context context;

    public MainhomeAdapter(ArrayList<ResultsResponse> mainhomeModelList ,Context context) {
        this.mainhomeModelList = mainhomeModelList;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.manihome_list, parent, false);
        return new ViewHolder(view);
     }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
       /* ArrayList<ResultsResponse> main  = mainhomeModelList.get(position).getResults();*/
        holder.textViewtitle.setText(mainhomeModelList.get(position).getHomeData());
        holder.textViewtitle.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {

                     String value = String.valueOf(mainhomeModelList.get(position).isVisible());
                     if(value.equalsIgnoreCase("true")){
                         Bundle bundle = new Bundle();
                         bundle.putString("id", mainhomeModelList.get(position).getHomeID());

                         AppCompatActivity activity = (AppCompatActivity)v.getContext();
                         VideoportionFragment videoportionFragment = new VideoportionFragment();
                         videoportionFragment.setArguments(bundle);
                         activity.getSupportFragmentManager().beginTransaction()
                                 .replace(R.id.fragment_container, videoportionFragment)
                                 .commit();
                    }
                     else{
                         Toast.makeText(context, "This is not visible for you.", Toast.LENGTH_SHORT).show();
                     }



//                     MainActivity myActivity = (MainActivity) context;
//
//                     VideoportionFragment videoportionFragment = new VideoportionFragment();
//                     videoportionFragment.setArguments(bundle);
//                     videoportionFragment.getSupportFragmentManager().beginTransaction()
//                             .replace(R.id.fragment_container, videoportionFragment)
//                             .commit();


                 }
                 }
        );

    }

    @Override
    public int getItemCount() {
        return mainhomeModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewtitle;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewtitle =  (TextView) itemView.findViewById(R.id.textViewTitle);
            cardView = (CardView) itemView.findViewById(R.id.mainButton);
        }
    }
}
