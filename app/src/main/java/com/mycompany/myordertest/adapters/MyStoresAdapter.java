package com.mycompany.myordertest.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import  com.mycompany.myordertest.R;
import com.mycompany.myordertest.rest.DTO.MyStoresPOJO;


import java.util.List;

import static com.mycompany.myordertest.constants.RAppConsts.BASE_URL;
import static com.mycompany.myordertest.constants.RAppConsts.kPrefTokenId;


public class MyStoresAdapter extends RecyclerView.Adapter<MyStoresAdapter.ViewHolder> {


    private List<MyStoresPOJO> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    Context context = null;


    // inflates the cell layout from xml when needed
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.main_store_row, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each cell
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtCloseOpen.setText("Open");
        holder.txtStoreName.setText(mData.get(position).name);
        holder.txtStoreType.setText("" + mData.get(position).typeLabel);
        holder.txtOpenHours.setText("   07:15-22:45");
        holder.txtColectareTitle.setText("Colectare livrare:");
        holder.txtColectareValue.setText(mData.get(position).orderfee + " lei");
        holder.txtOrderTitle.setText("Comanda minima:");
        holder.txtOrderValue.setText(mData.get(position).minimumorder + " lei");
        holder.txtTimeTitle.setText("Timp livrare:");
        holder.txtTimeValue.setText("~ " + mData.get(position).deliverytime + "min");

        RequestOptions options = new RequestOptions()
                .fitCenter()
                .placeholder(R.mipmap.ic_launcher_round)
                .error(R.mipmap.ic_launcher_round);


        //holder.imgLogo.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        SharedPreferences preferences = context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        String tokenId = preferences.getString(kPrefTokenId, "");
        String imgURL = BASE_URL + "clientapi/" + "getstoreimage?" + "id=" + mData.get(position).id + "&firebase_uid=" + tokenId;
        Glide.with(context).load(imgURL)
                .apply(options)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(holder.imgLogo);
    }

    // total number of cells
    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtStoreName;
        TextView txtCloseOpen;
        TextView txtStoreType;
        TextView txtOpenHours;
        TextView txtColectareTitle;
        TextView txtColectareValue;
        TextView txtOrderTitle;
        TextView txtOrderValue;
        TextView txtTimeTitle;
        TextView txtTimeValue;

        ConstraintLayout clTop;
        ConstraintLayout clBottom;
        ConstraintLayout clTopLeft;
        ConstraintLayout clTopRight;
        ConstraintLayout clTopRightClose;
        ConstraintLayout clTopRightName;
        ConstraintLayout clTopRightHours;
        ConstraintLayout clBottomFee;
        ConstraintLayout clBottomComanda;
        ConstraintLayout clBottomTime;
        ConstraintLayout clBackground;

        ImageView imgCLock;
        ImageView imgLogo;



        ViewHolder(View itemView) {
            super(itemView);

            clTop =  itemView.findViewById(R.id.main_store_view_top);
            clBottom =  itemView.findViewById(R.id.main_store_view_bottom);
            clTopLeft =  itemView.findViewById(R.id.main_store_view_top_left);
            clTopRight =  itemView.findViewById(R.id.main_store_view_top_right);
            clTopRightClose =  itemView.findViewById(R.id.main_store_view_top_right_open_close);
            clTopRightName =  itemView.findViewById(R.id.main_store_view_top_right_name);
            clTopRightHours =  itemView.findViewById(R.id.main_store_view_top_right_type);
            clBottomFee =  itemView.findViewById(R.id.main_store_view_bottom_fee);
            clBottomComanda =  itemView.findViewById(R.id.main_store_view_bottom_comanda);
            clBottomTime =  itemView.findViewById(R.id.main_store_view_bottom_livrare);
            clBackground = itemView.findViewById(R.id.main_store_background);

            txtStoreName = itemView.findViewById(R.id.main_store_view_top_txtView_name);
            txtCloseOpen = itemView.findViewById(R.id.main_store_view_top_txt_open_close);
            txtStoreType = itemView.findViewById(R.id.main_store_view_top_txtView_type);
            txtOpenHours = itemView.findViewById(R.id.main_store_view_top_txt_opening_hours);
            txtColectareTitle = itemView.findViewById(R.id.main_store_view_bottom_fee_txt_title);
            txtColectareValue = itemView.findViewById(R.id.main_store_view_bottom_fee_txt_value);
            txtOrderTitle = itemView.findViewById(R.id.main_store_view_bottom_comanda_txt_title);
            txtOrderValue = itemView.findViewById(R.id.main_store_view_bottom_comanda_txt_value);
            txtTimeTitle = itemView.findViewById(R.id.main_store_view_bottom_livrare_txt_title);
            txtTimeValue = itemView.findViewById(R.id.main_store_view_bottom_livrare_txt_value);

            imgCLock = itemView.findViewById(R.id.main_store_view_top_image_opening_hours);
            imgLogo = itemView.findViewById(R.id.main_store_view_top_left_image_logo);

            Typeface faceRegular = Typeface.createFromAsset(context.getAssets(), "font/Roboto-Bold.ttf");

            txtColectareValue.setTextColor(Color.BLACK);
            txtColectareValue.setTypeface(faceRegular);
            txtOrderValue.setTextColor(Color.BLACK);
            txtOrderValue.setTypeface(faceRegular);
            txtTimeValue.setTextColor(Color.RED);
            txtTimeValue.setTypeface(faceRegular);
            txtStoreName.setTextColor(Color.BLACK);
            txtStoreName.setTypeface(faceRegular);
            txtStoreName.setTextSize(25);


            clTop.setBackgroundColor(Color.TRANSPARENT);
            clBottom.setBackgroundColor(Color.TRANSPARENT);
            clTopLeft.setBackgroundColor(Color.TRANSPARENT);;
            clTopRight.setBackgroundColor(Color.TRANSPARENT);
            clTopRightClose.setBackgroundColor(Color.TRANSPARENT);
            clTopRightName.setBackgroundColor(Color.TRANSPARENT);
            clTopRightHours.setBackgroundColor(Color.TRANSPARENT);
            clBottomFee.setBackgroundColor(Color.TRANSPARENT);
            clBottomComanda.setBackgroundColor(Color.TRANSPARENT);
            clBottomTime.setBackgroundColor(Color.TRANSPARENT);

            GradientDrawable shape =  new GradientDrawable();
            shape.setCornerRadius( 18 );
            shape.setColor(Color.parseColor("#FFFFFF"));
            clBackground.setBackground(shape);
            clBackground.setElevation(20);

           // https://icons8.com/icons/set/clock
            Bitmap mBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.clock5).copy(Bitmap.Config.ARGB_8888, true);
            mBitmap.setHasAlpha(true);
            imgCLock.setImageBitmap(mBitmap);
           // imgCLock.setBackgroundColor(Color.TRANSPARENT);
            //            Glide.with(context)
//                    .asDrawable()
//                    .load(R.drawable.clock)
//                    .into(imgCLock);


           // Glide.with(context).load(R.drawable.selgros).into(imgLogo);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }
    public MyStoresAdapter(Context context, List<MyStoresPOJO> stores){

        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.mData = stores;
    }

    // convenience method for getting data at click position
    MyStoresPOJO getItem(int id) {
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
