package com.affinitity.niche.ui.home;

import android.content.Context;
 import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.affinitity.niche.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    ArrayList<CategoryModel> mValues;
    Context mContext;
    protected ItemListener mListener;

    public RecyclerViewAdapter(Context context, ArrayList<CategoryModel> values, ItemListener itemListener) {

        mValues = values;
        mContext = context;
        mListener=itemListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView textView;
        public CircleImageView imageView;

        public LinearLayout  relativeLayout;
        CategoryModel item;

        public ViewHolder(View v) {

            super(v);

            v.setOnClickListener(this);
            textView = (TextView) v.findViewById(R.id.textView);
            imageView = (CircleImageView) v.findViewById(R.id.imageView);
          //  Glide.with(mContext).load(listData.getImageurl()).into(holder.imageView);
          //  imageView.setAdjustViewBounds(true);


            //    relativeLayout = (LinearLayout) v.findViewById(R.id.relativeLayout);

        }

        public void setData(CategoryModel item) {
       this.item  = item;

            textView.setText(item.getCategoryName());
           // Glide.with(mContext).load(item.getCategoryPic()).into(imageView);
            Glide.with(mContext)
                    .load(item.getCategoryPic())
                    .error(R.drawable.ic_no_photo) //in case of error this is displayed
                    .into(imageView);

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
        void onItemClick(CategoryModel item);
    }
}