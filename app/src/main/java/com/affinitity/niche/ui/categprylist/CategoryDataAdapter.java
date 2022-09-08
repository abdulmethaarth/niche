package com.affinitity.niche.ui.categprylist;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.affinitity.niche.R;
import com.affinitity.niche.ui.pdfView.PdfViewFragment;
import com.affinitity.niche.ui.videoPlayer.VideoPlayerFragment;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

class CategoryDataAdapter extends RecyclerView.Adapter<CategoryDataAdapter.ViewHolder> {
   private ArrayList<CategoryListModel> categoryListModels;
   Context context;

    public CategoryDataAdapter(ArrayList<CategoryListModel> categoryListModels,Context context) {
        this.categoryListModels = categoryListModels;
        this.context = context;


    }

    @Override
   public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
       View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_category_row, viewGroup, false);
       return new ViewHolder(view);
   }

    @Override
    public void onBindViewHolder(ViewHolder holder, final  int i) {
        holder.tv_name.setText(categoryListModels.get(i).getVideoName());
//        holder.tv_version.setText(categoryListModels.get(i).getVideoName());
//        holder.tv_api_level.setText(categoryListModels.get(i).getVideoName());
        Glide.with(context)
                .load(categoryListModels.get(i).getImageUrl())
                .error(R.drawable.ic_no_photo) //in case of error this is displayed
                .into(holder.imageView);
holder.cardView.setOnClickListener(v -> {
    Bundle bundle = new Bundle();
    //   bundle.putString("id", "1" );



    if(categoryListModels.get(i).getType().equals("video")  ) {
        bundle.putString("urlString",categoryListModels.get(i).getVideoPath());

        bundle.putString("description",categoryListModels.get(i).getDescription());
        VideoPlayerFragment videoPlayerFragment = new VideoPlayerFragment();
        videoPlayerFragment.setArguments(bundle);
        ((FragmentActivity) context).getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, videoPlayerFragment)
                .commit();
    }

    if(categoryListModels.get(i).getType().equals("pdf")) {
        bundle.putString("filename", categoryListModels.get(i).getVideoName());
        bundle.putString("urlString", categoryListModels.get(i).getVideoPath());
//            CategoryListFragment categoryListFragment  = new CategoryListFragment();
//            categoryListFragment.setArguments(bundle);
//            manager().beginTransaction().replace(R.id.fragment_container,
//                    categoryListFragment).commit();

        PdfViewFragment pdfViewFragment = new PdfViewFragment();
        pdfViewFragment.setArguments(bundle);
        ((FragmentActivity)context).getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, pdfViewFragment)
                .commit();

    }



    });
    }




   @Override
   public int getItemCount() {
       return categoryListModels.size();
   }

   public class ViewHolder extends RecyclerView.ViewHolder{
       private TextView tv_name,tv_version,tv_api_level;
       CardView cardView;
       CircleImageView imageView;
       public ViewHolder(View view) {
           super(view);
           imageView = (CircleImageView) view.findViewById(R.id.imageView);

           cardView = (CardView) view.findViewById(R.id.cardView);

           tv_name = (TextView)view.findViewById(R.id.tv_name);
           tv_version = (TextView)view.findViewById(R.id.tv_version);
           tv_api_level = (TextView)view.findViewById(R.id.tv_api_level);

       }
   }

}
