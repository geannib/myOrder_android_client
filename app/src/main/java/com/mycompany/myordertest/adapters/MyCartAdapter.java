package com.mycompany.myordertest.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.mycompany.myordertest.CategoriesActivity;
import com.mycompany.myordertest.MyCartActivity;
import com.mycompany.myordertest.ProductsActivity;
import  com.mycompany.myordertest.R;
import com.mycompany.myordertest.constants.RAppConsts;
import com.mycompany.myordertest.rest.DTO.MyProductPOJO;
import com.mycompany.myordertest.rest.DTO.MyStoresPOJO;
import com.mycompany.myordertest.utils.MyAppSession;
import com.mycompany.myordertest.utils.MyBuyProduct;


import java.util.List;

import static com.mycompany.myordertest.constants.RAppConsts.BASE_URL;


public class MyCartAdapter extends RecyclerView.Adapter<MyCartAdapter.ViewHolder> {

    private static final String TAG = MyCartAdapter.class.getName();

    private List<MyBuyProduct> mData;
    private LayoutInflater mInflater;
    private ItemBuyClickListener mClickListener;
    Context context = null;


    // inflates the cell layout from xml when needed
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = new ConstraintLayout((context)); //mInflater.inflate(R.layout.main_store_row, parent, false);
        view.setId(View.generateViewId());
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each cell
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        MyBuyProduct bProd = mData.get(position);
        holder.txtProdQuantity.setText("" + bProd.quantity);
        holder.txtFullPrice.setText(bProd.price + " lei");
        holder.txtProdName.setText(bProd.name);

        String imgURL = BASE_URL + "clientapi/" + "getproductimage?" + "id=" + bProd.id + "&firebase_uid=" + MyAppSession.getInstance().getCurrentUser().getUid();

        Glide.with(context.getApplicationContext())
                .asBitmap()
                .load(imgURL)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap bitmap,
                                                Transition<? super Bitmap> transition) {
                        holder.imgProdImg.setImageBitmap(bitmap);
                    }
                });

        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyBuyProduct modProd = MyAppSession.getInstance().getLocalCart().getAllProducts().get(position);
                MyAppSession.getInstance().getLocalCart().deleProduct(modProd);
                MyAppSession.getInstance().saveLocalCart();
               mClickListener.onItemRemoved(view, position);
            }
        });

        holder.txtBottomMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyBuyProduct modProd = MyAppSession.getInstance().getLocalCart().getAllProducts().get(position);
                if (modProd.quantity > RAppConsts.minQuantity) {
                    MyAppSession.getInstance().getLocalCart().modifyQuantityProduct(bProd.id, -1);
                    MyAppSession.getInstance().saveLocalCart();
                    modProd = MyAppSession.getInstance().getLocalCart().getAllProducts().get(position);
                    holder.txtFullPrice.setText(String.format( "%.2f RON", (modProd.price * modProd.quantity) ));
                    holder.txtProdQuantity.setText("" + modProd.quantity);
                    mClickListener.onItemUpdated(view, position);
                }else {
                    doShake(holder.clRightBottom);
                }
            }
        });

        holder.txtBottomPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyBuyProduct modProd = MyAppSession.getInstance().getLocalCart().getAllProducts().get(position);
                if (modProd.quantity < RAppConsts.maxQuantity) {
                    MyAppSession.getInstance().getLocalCart().modifyQuantityProduct(bProd.id, 1);
                    MyAppSession.getInstance().saveLocalCart();
                    modProd = MyAppSession.getInstance().getLocalCart().getAllProducts().get(position);
                    holder.txtFullPrice.setText(String.format( "%.2f RON", (modProd.price * modProd.quantity) ));
                    holder.txtProdQuantity.setText("" + modProd.quantity);
                    mClickListener.onItemUpdated(view, position);
                }else {
                    doShake(holder.clRightBottom);
                }
            }
        });
    }

    private  void doShake(View view){
        final Animation animShake = AnimationUtils.loadAnimation(context, R.anim.shake);
        view.startAnimation(animShake);
    }
    // total number of cells
    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtProdName;
        TextView txtProdQuantity;
        TextView txtFullPrice;
        ImageView imgProdImg;
        ImageView imgDelete;
        TextView txtBottomMinus;
        TextView txtBottomPlus;

        ConstraintLayout clBackground;
        ConstraintLayout clLeft;
        ConstraintLayout clMiddle;
        ConstraintLayout clRight;
        ConstraintLayout clRightTop;
        ConstraintLayout clRightBottom;

        ImageView imgCLock;
        ImageView imgLogo;



        ViewHolder(View itemView) {
            super(itemView);

            txtProdName = new TextView(context);
            txtProdQuantity = new TextView(context);
            txtFullPrice = new TextView(context);
            txtBottomMinus = new TextView(context);
            txtBottomPlus = new TextView(context);
            imgProdImg = new ImageView(context);
            imgDelete = new ImageView(context);

            clBackground =  new ConstraintLayout(context);
            clLeft = new ConstraintLayout(context);
            clMiddle = new ConstraintLayout(context);
            clRight = new ConstraintLayout(context);
            clRightTop = new ConstraintLayout(context);
            clRightBottom = new ConstraintLayout(context);

            clBackground.setTag(100);
            clBackground.setId(View.generateViewId());
            clLeft.setTag(101);
            clLeft.setId(View.generateViewId());
            clMiddle.setTag(102);
            clMiddle.setId(View.generateViewId());
            clRight.setTag(103);
            clRight.setId(View.generateViewId());
            clRightTop.setTag(104);
            clRightTop.setId(View.generateViewId());
            clRightBottom.setTag(105);
            clRightBottom.setId(View.generateViewId());
            txtProdName.setTag(200);
            txtProdName.setId(View.generateViewId());
            txtProdQuantity.setTag(201);
            txtProdQuantity.setId(View.generateViewId());
            txtFullPrice.setTag(202);
            txtFullPrice.setId(View.generateViewId());
            imgProdImg.setTag(203);
            imgProdImg.setId(View.generateViewId());
//            imgDelete.setTag(206);
            imgDelete.setId(View.generateViewId());

            txtBottomMinus.setId(View.generateViewId());
            txtBottomMinus.setTag(204);

            txtBottomPlus.setTag(205);
            txtBottomPlus.setId(View.generateViewId());

            ConstraintLayout clItemView = (ConstraintLayout)itemView;
            clItemView.addView(clBackground);
            clBackground.addView(clLeft);
            clBackground.addView(clMiddle);
            clBackground.addView(clRight);
            clRight.addView(clRightTop);
            clRight.addView(clRightBottom);
            clMiddle.addView(txtProdName);
            clRightTop.addView(txtFullPrice);
            clRightTop.addView(imgDelete);
            clRightBottom.addView(txtBottomMinus);
            clRightBottom.addView(txtBottomPlus);
            clRightBottom.addView(txtProdQuantity);
            clLeft.addView(imgProdImg);

            Typeface faceBold = Typeface.createFromAsset(context.getAssets(), "font/Roboto-Bold.ttf");
            Typeface faceRegular = Typeface.createFromAsset(context.getAssets(), "font/Roboto-Regular.ttf");

            txtBottomPlus.setTextColor(Color.GRAY);
            txtBottomMinus.setTextColor(Color.GRAY);
            txtProdQuantity.setTextColor(Color.GRAY);
            txtFullPrice.setTextColor(Color.RED);
            txtProdName.setTextColor(Color.BLACK);
            txtBottomMinus.setGravity(Gravity.CENTER);
            txtBottomMinus.setText("-");
            txtBottomPlus.setGravity(Gravity.CENTER);
            txtBottomPlus.setText("+");
            txtProdQuantity.setGravity(Gravity.CENTER);
            txtFullPrice.setGravity(Gravity.CENTER);
            txtProdName.setGravity(Gravity.CENTER);
            txtProdQuantity.setTypeface(faceRegular);
            txtBottomPlus.setTypeface(faceRegular);
            txtBottomMinus.setTypeface(faceRegular);
            txtFullPrice.setTypeface(faceBold);
            txtBottomMinus.setTextSize(20);
            txtBottomPlus.setTextSize(20);
            txtProdQuantity.setTextSize(17);
            txtFullPrice.setTextSize(15);
            txtProdName.setTextSize(15);

            GradientDrawable shape =  new GradientDrawable();
            shape.setCornerRadius( 18 );
            shape.setColor(Color.parseColor("#FFFFFF"));
            shape.setStroke(2, Color.BLACK);
            clRightBottom.setBackground(shape);
//
            Glide.with(context).load(R.drawable.delete).into(imgDelete);
//            imgLogo = itemView.findViewById(R.id.main_store_view_top_left_image_logo);




           // clBackground.setBackgroundColor(Color.RED);
            clLeft.setBackgroundColor(Color.TRANSPARENT);
            clMiddle.setBackgroundColor(Color.TRANSPARENT);;
            clRight.setBackgroundColor(Color.TRANSPARENT);
           // clRightBottom.setBackgroundColor(Color.MAGENTA);
            clRightTop.setBackgroundColor(Color.TRANSPARENT);


            GradientDrawable shapeMain =  new GradientDrawable();
            shapeMain.setCornerRadius( 18 );
            shapeMain.setColor(Color.parseColor("#FFFFFF"));
            clBackground.setBackground(shapeMain);
            clBackground.setElevation(10);

            final View activityRootView = ((MyCartActivity) context).findViewById(R.id.cart_cl_main);
            int mainW = activityRootView.getWidth();
            int mainH = activityRootView.getHeight();
            int emptySpace = mainW / 20;
            int newW = (mainW - emptySpace) / 1;
            int newH = (int)(mainH / 5.5);

            // Layout main view
            ConstraintSet constrainCard = new ConstraintSet();
            constrainCard.connect(clBackground.getId(), ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT, emptySpace/2);
            constrainCard.connect(clBackground.getId(), ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT, emptySpace/2);
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

            // Layout middle view
            ConstraintSet constrainMiddle = new ConstraintSet();
            constrainMiddle.connect(clMiddle.getId(), ConstraintSet.LEFT, clLeft.getId(), ConstraintSet.RIGHT, 8);
            constrainMiddle.connect(clMiddle.getId(), ConstraintSet.RIGHT, clRight.getId(), ConstraintSet.LEFT, 8);
            constrainMiddle.connect(clMiddle.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 20);
            constrainMiddle.connect(clMiddle.getId(), ConstraintSet.BOTTOM,  ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 20);
            constrainMiddle.constrainPercentWidth(clMiddle.getId(), (float)0.45);

            // Layout left view
            ConstraintSet constrainRight = new ConstraintSet();
            constrainRight.connect(clRight.getId(), ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT, 0);
            constrainRight.connect(clRight.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 20);
            constrainRight.connect(clRight.getId(), ConstraintSet.BOTTOM,  ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 20);
            constrainRight.constrainPercentWidth(clRight.getId(), (float)0.35);

            // Layout right top view
            ConstraintSet constrainRightTop = new ConstraintSet();
            constrainRightTop.connect(clRightTop.getId(), ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT, 0);
            constrainRightTop.connect(clRightTop.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 20);
            constrainRightTop.connect(clRightTop.getId(), ConstraintSet.LEFT,  ConstraintSet.PARENT_ID, ConstraintSet.LEFT, 0);
            constrainRightTop.constrainPercentHeight(clRightTop.getId(), (float)0.5);

            // Layout right bottom view
            ConstraintSet constrainRightBottom = new ConstraintSet();
            constrainRightBottom.connect(clRightBottom.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0);
            constrainRightBottom.connect(clRightBottom.getId(), ConstraintSet.TOP, clRightTop.getId(), ConstraintSet.BOTTOM, 0);
            constrainRightBottom.connect(clRightBottom.getId(), ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT, 5);
            constrainRightBottom.connect(clRightBottom.getId(), ConstraintSet.LEFT,  ConstraintSet.PARENT_ID, ConstraintSet.LEFT, 5);
            constrainRightBottom.constrainPercentHeight(clRightBottom.getId(), (float)0.3);

            // Prod bottom - img minus
            ConstraintSet constrainProdBottomMinus= new ConstraintSet();
            constrainProdBottomMinus.connect(txtBottomMinus.getId(), ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT, 7);
            constrainProdBottomMinus.connect(txtBottomMinus.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 7);
            constrainProdBottomMinus.connect(txtBottomMinus.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 7);
            constrainProdBottomMinus.constrainPercentWidth(txtBottomMinus.getId(), (float) 0.33);

            // Prod bottom - img plus
            ConstraintSet constrainProdBottomPlus= new ConstraintSet();
            constrainProdBottomPlus.connect(txtBottomPlus.getId(), ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT, 7);
            constrainProdBottomPlus.connect(txtBottomPlus.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 7);
            constrainProdBottomPlus.connect(txtBottomPlus.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 7);
            constrainProdBottomPlus.constrainPercentWidth(txtBottomPlus.getId(), (float) 0.33);

            // Prod bottom - txt count
            ConstraintSet constrainProdBottomCount = new ConstraintSet();
            constrainProdBottomCount.connect(txtProdQuantity.getId(), ConstraintSet.RIGHT, txtBottomPlus.getId(), ConstraintSet.LEFT, 0);
            constrainProdBottomCount.connect(txtProdQuantity.getId(), ConstraintSet.LEFT, txtBottomMinus.getId(), ConstraintSet.RIGHT, 0);
            constrainProdBottomCount.connect(txtProdQuantity.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 7);
            constrainProdBottomCount.connect(txtProdQuantity.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 7);

            // Prod total price
            ConstraintSet constrainProdTotalPrice = new ConstraintSet();
            constrainProdTotalPrice.connect(txtFullPrice.getId(), ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT, 0);
            constrainProdTotalPrice.connect(txtFullPrice.getId(), ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT, 0);
            constrainProdTotalPrice.connect(txtFullPrice.getId(), ConstraintSet.TOP, imgDelete.getId(), ConstraintSet.BOTTOM, 7);
            constrainProdTotalPrice.connect(txtFullPrice.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 7);

            // Img delete
            ConstraintSet constrainImgDelete = new ConstraintSet();
            constrainImgDelete.connect(imgDelete.getId(), ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT, 45);
//            constrainProdTotalPrice.connect(txtFullPrice.getId(), ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT, 0);
            constrainImgDelete.connect(imgDelete.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 5);
            constrainImgDelete.constrainWidth(imgDelete.getId(), 50);
            constrainImgDelete.constrainHeight(imgDelete.getId(), 50);

            // Prod name
            ConstraintSet constrainProdName = new ConstraintSet();
            constrainProdName.connect(txtProdName.getId(), ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT, 0);
            constrainProdName.connect(txtProdName.getId(), ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT, 0);
            constrainProdName.connect(txtProdName.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 7);
            constrainProdName.connect(txtProdName.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 7);

            // Prod image
            ConstraintSet constrainProdImage = new ConstraintSet();
            constrainProdImage.connect(imgProdImg.getId(), ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT, 0);
            constrainProdImage.connect(imgProdImg.getId(), ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT, 0);
            constrainProdImage.connect(imgProdImg.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 7);
            constrainProdImage.connect(imgProdImg.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 7);


            constrainCard.applyTo(clItemView);
            constrainLeft.applyTo(clBackground);
            constrainRight.applyTo(clBackground);
            constrainMiddle.applyTo(clBackground);
            constrainRightTop.applyTo(clRight);
            constrainRightBottom.applyTo(clRight);
            constrainProdBottomCount.applyTo(clRightBottom);
            constrainProdBottomMinus.applyTo(clRightBottom);
            constrainProdBottomPlus.applyTo(clRightBottom);
            constrainProdTotalPrice.applyTo(clRightTop);
            constrainImgDelete.applyTo(clRightTop);
            constrainProdName.applyTo(clMiddle);
            constrainProdImage.applyTo(clLeft);

           // itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
          //  if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }
    public MyCartAdapter(Context context, List<MyBuyProduct> products){

        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.mData = products;
    }

    // convenience method for getting data at click position
    MyBuyProduct getItem(int id) {
        return mData.get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(ItemBuyClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemBuyClickListener {
        void onItemRemoved(View view, int position);
        void onItemUpdated(View view, int position);
    }
}
