package com.affinitity.niche.ui.maincategory;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.affinitity.niche.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class MainCategoryAdapter extends RecyclerView.Adapter<MainCategoryAdapter.ViewHolder> {

    ArrayList<MainCategoryModels> mValues;
    Context mContext;
    protected ItemListener mListener;

    public MainCategoryAdapter(Context context, ArrayList<MainCategoryModels> values, ItemListener itemListener) {

        mValues = values;
        mContext = context;
        mListener=itemListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView textView;
        public CircleImageView imageView;
      public LinearLayout  linearLayout;
        MainCategoryModels item;

        public ViewHolder(View v) {

            super(v);

            v.setOnClickListener(this);
     //     linearLayout =(LinearLayout)v.findViewById(R.id.linearLayout);
            textView = (TextView) v.findViewById(R.id.textView);
            imageView = (CircleImageView) v.findViewById(R.id.imageView);
          //  Glide.with(mContext).load(listData.getImageurl()).into(holder.imageView);

            //    relativeLayout = (LinearLayout) v.findViewById(R.id.relativeLayout);

        }

        public void setData(MainCategoryModels item) {
       this.item  = item;

            textView.setText(item.getCategoryName());
            Glide.with(mContext)
                    .load(item.getCategoryPic())
                    .error(R.drawable.ic_no_photo) //in case of error this is displayed
                    .into(imageView);
     //    linearLayout.setBackgroundColor(mContext.getResources().getColor(R.color.pureWhite));

//            imageView.setImageResource(item.getCategoryPic());

        }


        @Override
        public void onClick(View view) {
            if (mListener != null) {
                mListener.onItemClick(item);
            }
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.recycler_view_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder Vholder, int position) {
        Vholder.setData(mValues.get(position));

    }

    @Override
    public int getItemCount() {

        return mValues.size();
    }

    public interface ItemListener {
        void onItemClick(MainCategoryModels item);
    }
}