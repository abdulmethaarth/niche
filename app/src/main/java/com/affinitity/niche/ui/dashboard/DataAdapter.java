package com.affinitity.niche.ui.dashboard;

 import android.content.Context;
 import android.content.Intent;
 import android.os.Bundle;
 import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
 import android.widget.Toast;

 import androidx.appcompat.app.AppCompatActivity;
 import androidx.cardview.widget.CardView;
 import androidx.fragment.app.FragmentActivity;
 import androidx.fragment.app.FragmentManager;
 import androidx.recyclerview.widget.RecyclerView;

 import com.affinitity.niche.R;
 import com.affinitity.niche.ui.videoPlayer.VideoPlayerFragment;
 import com.bumptech.glide.Glide;

 import java.util.ArrayList;

 import de.hdodenhof.circleimageview.CircleImageView;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> implements Filterable {
    private ArrayList<DashboardItems> mArrayList;
    private ArrayList<DashboardItems> mFilteredList;
Context context  ;
    public DataAdapter(ArrayList<DashboardItems> arrayList,Context context) {
      mArrayList = arrayList;
      mFilteredList = arrayList;
      this.context = context;


    }




    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.search_row_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {

        viewHolder.tv_name.setText(mFilteredList.get(i).getVideoName());
 //        viewHolder.tv_api_level.setText(mFilteredList.get(i).getVideo_id());
        Glide.with(context)
                .load(mFilteredList.get(i).getImageUrl())
                .error(R.drawable.ic_no_photo) //in case of error this is displayed
                .into(viewHolder.imageView);
        viewHolder.cardView.setOnClickListener(v -> {
            //    Toast.makeText(context, "Hello im here", Toast.LENGTH_SHORT).show();
//            Intent intent = new Intent(context, PlayerActivity.class);
//            Bundle bundle = new Bundle();
//
//            bundle.putString("urlString",mFilteredList.get(i).getVideoPath());
//
//            intent.putExtras(bundle);
//            context.startActivity(intent);
             Bundle bundle = new Bundle();
         //   bundle.putString("id", "1" );

            bundle.putString("urlString",mFilteredList.get(i).getVideoPath());
            bundle.putString("description",mFilteredList.get(i).getDescription());
            bundle.putString("videoId",mFilteredList.get(i).getVideo_id());
            bundle.putString("videoName",mFilteredList.get(i).getVideoName());
            bundle.putString("catogoryName",mFilteredList.get(i).getCategoryName());
            bundle.putString("imagurl",mFilteredList.get(i).getImageUrl());

//            CategoryListFragment categoryListFragment  = new CategoryListFragment();
//            categoryListFragment.setArguments(bundle);
//            manager().beginTransaction().replace(R.id.fragment_container,
//                    categoryListFragment).commit();

            VideoPlayerFragment videoPlayerFragment = new VideoPlayerFragment();
  videoPlayerFragment.setArguments(bundle);
            ((FragmentActivity)context).getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, videoPlayerFragment)
                    .commit();

        });
    }

    @Override
    public int getItemCount() {
        return mFilteredList.size();
    }

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();

                if (charString.isEmpty()) {
                     mFilteredList = mArrayList;
                } else {

                    ArrayList<DashboardItems> filteredList = new ArrayList<>();

                    for (DashboardItems androidVersion : mArrayList) {

                        if (androidVersion.getVideoName().toLowerCase().contains(charString) || androidVersion.getCategoryName().toLowerCase().contains(charString)) {

                            filteredList.add(androidVersion);
                        }
                    }

                    mFilteredList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mFilteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

      mFilteredList = (ArrayList<DashboardItems>) filterResults.values;
      notifyDataSetChanged();

            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_name,tv_version,tv_api_level;
        private  TextView nodata;
        private CardView cardView;

        CircleImageView imageView;
        public ViewHolder(View view) {
            super(view);

            cardView = (CardView) view.findViewById(R.id.cardView);

            nodata = (TextView) view.findViewById(R.id.nodata);

            imageView = (CircleImageView) view.findViewById(R.id.imageView);

            tv_name = (TextView)view.findViewById(R.id.tv_name);
            tv_version = (TextView)view.findViewById(R.id.tv_version);
            tv_api_level = (TextView)view.findViewById(R.id.tv_api_level);

        }
    }

}