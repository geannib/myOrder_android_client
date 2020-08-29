package com.mycompany.myordertest.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.mycompany.myordertest.CategoriesActivity;
import com.mycompany.myordertest.R;
import com.mycompany.myordertest.rest.DTO.MyCategoryPOJO;
import com.mycompany.myordertest.utils.MyAppSession;

import java.util.List;

import static com.mycompany.myordertest.constants.RAppConsts.BASE_URL;

public class MyCategoriesAdapter extends BaseAdapter {

    private final Context mContext;
    private final List<MyCategoryPOJO> categories;

    // 1
    public MyCategoriesAdapter(Context context, List<MyCategoryPOJO> categories) {
        this.mContext = context;
        this.categories = categories;
    }

    // 2
    @Override
    public int getCount() {
        return categories.size();
    }

    // 3
    @Override
    public long getItemId(int position) {
        return 0;
    }

    // 4
    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // 1
        final MyCategoryPOJO category = categories.get(position);

        // 2
        final LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        ConstraintLayout clsCell = (ConstraintLayout) layoutInflater.inflate(R.layout.category_cell, null);
        if (convertView == null) {

            convertView = clsCell;

        }

        final ImageView imgPoza = convertView.findViewById(R.id.categories_img_poza);
        final TextView txtName = convertView.findViewById(R.id.categories_txt_name);
        final ConstraintLayout clBackground = convertView.findViewById(R.id.cetegories_cl_background);

        imgPoza.setScaleType(ImageView.ScaleType.FIT_CENTER);
        Typeface faceRegular = Typeface.createFromAsset(mContext.getAssets(), "font/Roboto-Bold.ttf");

        txtName.setTextColor(Color.BLACK);
        txtName.setTypeface(faceRegular);
        txtName.setTextSize(15);

        GradientDrawable shape =  new GradientDrawable();
        shape.setCornerRadius( 18 );
        shape.setColor(Color.parseColor("#FFFFFF"));
        clBackground.setBackground(shape);
        clBackground.setElevation(10);

        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher_round)
                .error(R.mipmap.ic_launcher_round);


        String imgURL = BASE_URL + "clientapi/" + "getcategoryimage?" + "id=" + category.id + "&tmp=22&firebase_uid=" + MyAppSession.getInstance().getCurrentUser().getUid();
        Glide.with(mContext.getApplicationContext())
                .asBitmap()
                .load(imgURL)
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap bitmap,
                                                Transition<? super Bitmap> transition) {
                        imgPoza.setImageBitmap(bitmap);
                    }});
//        Glide.with(mContext).load(imgURL)
//                //.apply(options)
//                .into(imgPoza);
        //Glide.with(mContext).load(R.drawable.pisica).into(imgPoza);
        // 4
        final View activityRootView = ((CategoriesActivity)mContext).findViewById(R.id.categories_cl_main);
        int mainW =  activityRootView.getWidth();
        int mainH = activityRootView.getHeight();
        ViewGroup.LayoutParams lp =  clBackground.getLayoutParams();
        int emptySpace = mainW / 10;
        lp.width = (mainW - emptySpace) / 3;
        lp.height = (int)(lp.width * 2);
        clBackground.setLayoutParams(lp);


        txtName.setText(category.label);

        return convertView;
    }


}


