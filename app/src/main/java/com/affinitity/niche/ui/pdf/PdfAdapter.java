package com.affinitity.niche.ui.pdf;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.affinitity.niche.R;
import com.affinitity.niche.ui.pdfView.PdfViewFragment;
import com.bumptech.glide.Glide;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class PdfAdapter extends RecyclerView.Adapter<PdfAdapter.ViewHolder> implements Filterable {
    private ArrayList<PdfModel> mArrayList;
    private ArrayList<PdfModel> mFilteredList;
Context context  ;
    public PdfAdapter(ArrayList<PdfModel> arrayList, Context context) {
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

        viewHolder.tv_name.setText(mFilteredList.get(i).getFilename());
//        viewHolder.tv_version.setText(mFilteredList.get(i).getFilename());
//        viewHolder.tv_api_level.setText(mFilteredList.get(i).getFilename());
        Glide.with(context)
                .load(mFilteredList.get(i).getPdfImage())
                .error(R.drawable.ic_no_photo) //in case of error this is displayed
                .into(viewHolder.circleImageView);


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
            bundle.putString("filename", mFilteredList.get(i).getFilename());
            bundle.putString("urlString", mFilteredList.get(i).getFilepath());
//            CategoryListFragment categoryListFragment  = new CategoryListFragment();
//            categoryListFragment.setArguments(bundle);
//            manager().beginTransaction().replace(R.id.fragment_container,
//                    categoryListFragment).commit();

            PdfViewFragment pdfViewFragment = new PdfViewFragment();
            pdfViewFragment.setArguments(bundle);
            ((FragmentActivity)context).getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, pdfViewFragment)
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

                    ArrayList<PdfModel> filteredList = new ArrayList<>();

                    for (PdfModel  androidVersion : mArrayList) {

                        if (androidVersion.getFilename().toLowerCase().contains(charString)) {

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

      mFilteredList = (ArrayList<PdfModel>) filterResults.values;
      notifyDataSetChanged();

            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_name,tv_version,tv_api_level;
        CircleImageView circleImageView;
        private  TextView nodata;
        private CardView cardView;
        public ViewHolder(View view) {
            super(view);

            circleImageView  = (CircleImageView) view.findViewById(R.id.imageView);

            cardView = (CardView) view.findViewById(R.id.cardView);

            nodata = (TextView) view.findViewById(R.id.nodata);

            tv_name = (TextView)view.findViewById(R.id.tv_name);
            tv_version = (TextView)view.findViewById(R.id.tv_version);
            tv_version.setVisibility(View.GONE);
            tv_api_level = (TextView)view.findViewById(R.id.tv_api_level);
            tv_api_level.setVisibility(View.GONE);

        }
    }

}