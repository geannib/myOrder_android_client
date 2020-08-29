package com.mycompany.myordertest.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.util.StringBuilderPrinter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.mycompany.myordertest.MyAddressesDialog;
import com.mycompany.myordertest.MyCartActivity;
import com.mycompany.myordertest.MyStoresActivity;
import  com.mycompany.myordertest.R;
import com.mycompany.myordertest.rest.DTO.MyAddressPOJO;
import com.mycompany.myordertest.rest.DTO.MyStoresPOJO;
import com.mycompany.myordertest.rest.response.MyAddressesAPIResponse;


import java.util.List;

import static com.mycompany.myordertest.constants.RAppConsts.BASE_URL;
import static com.mycompany.myordertest.constants.RAppConsts.kPrefTokenId;


public class MyAddressesAdapter extends RecyclerView.Adapter<MyAddressesAdapter.ViewHolder> {


    private List<MyAddressPOJO> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    Context context = null;


    // inflates the cell layout from xml when needed
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = new ConstraintLayout((context)); //mInflater.inflate(R.layout.main_store_row, parent, false);
        view.setId(View.generateViewId());
        return new MyAddressesAdapter.ViewHolder(view);
    }

    // binds the data to the TextView in each cell
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String fullAddr = new StringBuilder()
                .append(mData.get(position).country)
                .append(", ")
                .append(mData.get(position).street)
                .append(", ")
                .append(mData.get(position).streetNumber)
                .toString();
       holder.txtddress.setText(fullAddr);

    }

    // total number of cells
    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ConstraintLayout clBackground;
        ConstraintLayout clLeft;
        ConstraintLayout clMiddle;
        ConstraintLayout clRight;
        TextView txtddress;


        ViewHolder(View itemView) {
            super(itemView);

            ConstraintLayout clItemView = (ConstraintLayout)itemView;
            clBackground =  new ConstraintLayout(context);
            clLeft = new ConstraintLayout(context);
            clMiddle = new ConstraintLayout(context);
            clRight = new ConstraintLayout(context);
            txtddress = new TextView(context);

            clItemView.addView(clBackground);
            clBackground.addView(clRight);
            clBackground.addView(clMiddle);
            clBackground.addView(clLeft);
            clMiddle.addView(txtddress);

            clBackground.setTag(100);
            clBackground.setId(View.generateViewId());
            clLeft.setTag(101);
            clLeft.setId(View.generateViewId());
            clMiddle.setTag(102);
            clMiddle.setId(View.generateViewId());
            clRight.setTag(103);
            clRight.setId(View.generateViewId());
            txtddress.setTag(200);
            txtddress.setId(View.generateViewId());

            Typeface faceRegular = Typeface.createFromAsset(context.getAssets(), "font/Roboto-Bold.ttf");
            txtddress.setTypeface(faceRegular);

            final View activityRootView = ((MyStoresActivity)context).findViewById(R.id.drawer_layout);
            int mainW = activityRootView.getWidth();
            int mainH = activityRootView.getHeight();
            int emptySpace = mainW / 20;
            int newW = (mainW - emptySpace) / 1;
            int newH = (int)(mainH / 15.5);

            // Layout main view
            ConstraintSet constrainCard = new ConstraintSet();
            constrainCard.connect(clBackground.getId(), ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT,0);
            constrainCard.connect(clBackground.getId(), ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT, 0);
            constrainCard.connect(clBackground.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 20);
            constrainCard.connect(clBackground.getId(), ConstraintSet.BOTTOM,  ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 20);
            constrainCard.constrainHeight(clBackground.getId(), newH);
            constrainCard.constrainWidth(clBackground.getId(), newW);

            // Layout left view
            ConstraintSet constrainLeft = new ConstraintSet();
            constrainLeft.connect(clLeft.getId(), ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT, 0);
            constrainLeft.connect(clLeft.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 20);
            constrainLeft.connect(clLeft.getId(), ConstraintSet.BOTTOM,  ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 20);
            constrainLeft.constrainPercentWidth(clLeft.getId(), (float)0.20);

            // Layout left view
            ConstraintSet constrainMiddle = new ConstraintSet();
            constrainMiddle.connect(clMiddle.getId(), ConstraintSet.LEFT, clLeft.getId(), ConstraintSet.RIGHT, 0);
            constrainMiddle.connect(clMiddle.getId(), ConstraintSet.RIGHT, clRight.getId(), ConstraintSet.LEFT, 0);
            constrainMiddle.connect(clMiddle.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 20);
            constrainMiddle.connect(clMiddle.getId(), ConstraintSet.BOTTOM,  ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 20);
            constrainMiddle.constrainPercentWidth(clMiddle.getId(), (float)0.45);

            // Layout left view
            ConstraintSet constrainRight = new ConstraintSet();
            constrainRight.connect(clRight.getId(), ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT, 0);
            constrainRight.connect(clRight.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 20);
            constrainRight.connect(clRight.getId(), ConstraintSet.BOTTOM,  ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 20);
            constrainRight.constrainPercentWidth(clRight.getId(), (float)0.35);

            // Address
            ConstraintSet constrainAddress = new ConstraintSet();
            constrainAddress.connect(txtddress.getId(), ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT, 0);
            constrainAddress.connect(txtddress.getId(), ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT, 0);
            constrainAddress.connect(txtddress.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 7);
            constrainAddress.connect(txtddress.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 7);



            constrainCard.applyTo(clItemView);
            constrainLeft.applyTo(clBackground);
            constrainRight.applyTo(clBackground);
            constrainMiddle.applyTo(clBackground);
            constrainAddress.applyTo(clMiddle);

            clLeft.setBackgroundColor(Color.TRANSPARENT);
            clMiddle.setBackgroundColor(Color.TRANSPARENT);;
            clRight.setBackgroundColor(Color.TRANSPARENT);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }
    public MyAddressesAdapter(Context context, List<MyAddressPOJO> addresses){

        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.mData = addresses;
    }

    // convenience method for getting data at click position
    MyAddressPOJO getItem(int id) {
        return mData.get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
