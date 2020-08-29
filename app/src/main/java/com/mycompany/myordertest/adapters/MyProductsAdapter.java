package com.mycompany.myordertest.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.res.ResourcesCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.mycompany.myordertest.ProductsActivity;
import com.mycompany.myordertest.R;
import com.mycompany.myordertest.custom.RCaroucelProductCards;
import com.mycompany.myordertest.rest.DTO.MyProductPOJO;

import com.mycompany.myordertest.utils.MyAppSession;

import java.util.List;

import static com.mycompany.myordertest.constants.RAppConsts.BASE_URL;


public class MyProductsAdapter extends BaseAdapter implements RCaroucelProductCards.CardClickListener {


    private static final String TAG = MyProductsAdapter.class.getName();

    private final int TXT_TITLE_TAG = 1000;
    private  final int TXT_PRICE_TAG = 1001;
    private final int IMG_LOGO_TAG = 1002;
    private final int IMG_FAV_TAG = 1003;
    private final int IMG_CART_TAG = 1004;

    private List<MyProductPOJO> mData;
    private LayoutInflater mInflater;
    private ProductClickListener mProdClickListener;
    Context context = null;

    // 1
    public MyProductsAdapter(Context context, List<MyProductPOJO> categories) {
        this.context = context;
        this.mData = categories;
        this.mInflater = LayoutInflater.from(context);
    }

    // 2
    @Override
    public int getCount() {
        return mData.size();
    }



//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//
//    }
//
//    // inflates the cell layout from xml when needed
//    @Override
//    @NonNull
//    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//   //     RCaroucelProductCards newRow = new RCaroucelProductCards(context);
////        newRow.setMinWidth(200);
//      //  View view = mInflater.inflate(R.layout.products_lv_row, parent, false);
//       ConstraintLayout clFrame = new ConstraintLayout(context);
//
//        return new ViewHolder(clFrame);
//    }
//
//    // binds the data to the TextView in each cell

//    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        holder.txtProdPrice.setText("  " + mData.get(position).price);
//        holder.txtProdName.setText(mData.get(position).name);
//        MyProductPOJO product = mData.get(position);
//        String imgURL = BASE_URL + "clientapi/" + "getproductimage?" + "id=" + product.id;
//
//        Glide.with(context.getApplicationContext())
//                .asBitmap()
//                .load(imgURL)
//                .diskCacheStrategy(DiskCacheStrategy.NONE)
//                .skipMemoryCache(true)
//                .into(new SimpleTarget<Bitmap>() {
//                    @Override
//                    public void onResourceReady(Bitmap bitmap,
//                                                Transition<? super Bitmap> transition) {
//                       // holder.imgLogo.setImageBitmap(bitmap);
//                    }});
//
//        // treeObserver();
//
//        holder.txtTitle.setText(product.name);
//        holder.txtPrice.setText("" + product.price + " lei");
//        boolean found = false;
//        for (MyProductPOJO p: MyAppSession.getInstance().favouriteList) {
//            if (p.id == product.id) {
//                found = true;
//                break;
//            }
//        }
//        holder.caroucel.setSubCat(mypojo);
//        holder.caroucel.setClickListener(this);

//    }


    public int getItemCount() {
        return  mData.size();
    }



    public View getView(int position, View convertView, ViewGroup parent) {

        ConstraintLayout prodCard;

        ConstraintLayout clMain;
        TextView txtTitle;
        TextView txtPrice;

        ImageView imgLogo;
        ImageView imgFav;
        ImageView imgCart;

        prodCard = generateProductCard();
        txtTitle = prodCard.findViewWithTag(TXT_TITLE_TAG);
        txtPrice = prodCard.findViewWithTag(TXT_PRICE_TAG);
        imgLogo = prodCard.findViewWithTag(IMG_LOGO_TAG);
        imgCart = prodCard.findViewWithTag(IMG_CART_TAG);
        imgFav = prodCard.findViewWithTag(IMG_FAV_TAG);


        clMain = new ConstraintLayout(context);
        clMain.addView(prodCard);

        MyProductPOJO product = mData.get(position);
        txtTitle.setText(product.name);
        txtPrice.setText("" + product.price + " lei");
        String imgURL = BASE_URL + "clientapi/" + "getproductimage?" + "id=" + product.id + "&firebase_uid=" + MyAppSession.getInstance().getCurrentUser().getUid();

        Glide.with(context.getApplicationContext())
                .asBitmap()
                .load(imgURL)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap bitmap,
                                                Transition<? super Bitmap> transition) {
                       imgLogo.setImageBitmap(bitmap);
                    }});
        final View activityRootView = ((ProductsActivity) context).findViewById(R.id.products_cl_main);
        int mainW = activityRootView.getWidth();
        int mainH = activityRootView.getHeight();

        boolean found = false;
        for (MyProductPOJO p: MyAppSession.getInstance().getFavList()){
            if( p.id == product.id){
                found = true;
                break;
            }
        }
        if(found == false){
            imgFav.setImageDrawable( ResourcesCompat.getDrawable(context.getResources(), R.drawable.heart_empty, null));
        }else{
            imgFav.setImageDrawable( ResourcesCompat.getDrawable(context.getResources(), R.drawable.heart_red, null));
        }

        imgCart.setImageDrawable( ResourcesCompat.getDrawable(context.getResources(), R.drawable.basket, null));

        int emptySpace = mainW / 10;
        int newW = (mainW - emptySpace) / 2;
        int newH = (int)(newW * 1.5);

        // Layout button inside constraint
        ConstraintSet constrainCard = new ConstraintSet();
        constrainCard.connect(prodCard.getId(), ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT, 0);
        constrainCard.connect(prodCard.getId(), ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT, 0);
        constrainCard.connect(prodCard.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 20);
        constrainCard.connect(prodCard.getId(), ConstraintSet.BOTTOM,  ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 20);
        constrainCard.constrainHeight(prodCard.getId(), newH);
        constrainCard.constrainWidth(prodCard.getId(), newW);
        constrainCard.applyTo(clMain);
        clMain.setBackgroundColor(ResourcesCompat.getColor(context.getResources(), R.color.colorGrayBackground, null));


        final List<MyProductPOJO> favProducts = MyAppSession.getInstance().getFavList();
        imgFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d(TAG, "Heart was clicked: " + product.id);

                boolean found = false;
                int idx = 0;
                for (MyProductPOJO p: favProducts){
                    if( p.id == product.id){
                        MyAppSession.getInstance().getFavList().remove(idx);
                        found = true;
                        break;
                    }
                    idx++;
                }

                if ( found == false){
                    Log.d(TAG, "Product was not found in fav list: " + product.id);
                    imgFav.setImageDrawable( ResourcesCompat.getDrawable(context.getResources(), R.drawable.heart_red, null));
                    MyAppSession.getInstance().getFavList().add(product);
                }else{
                    Log.d(TAG, "Product was found in fav list: " + product.id);
                    imgFav.setImageDrawable( ResourcesCompat.getDrawable(context.getResources(), R.drawable.heart_empty, null));
                }
               // MyAppSession.getInstance().favouriteList = favProducts;
                MyAppSession.getInstance().saveLocalCart();
                mProdClickListener.onFavouriteClicked(product);
            }

        });

        imgCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProdClickListener.onBuyProduct(product);
            }
        });
        return clMain;

    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    // allows clicks events to be caught
    public void setProdClickListener(ProductClickListener productClickListener) {
        this.mProdClickListener = productClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ProductClickListener {
        void onItemClick(View view, MyProductPOJO selProd);
        void onFavouriteClicked(MyProductPOJO selProd);
        void onBuyProduct(MyProductPOJO selProduct);
    }

    @Override
    public void onProductClicked(View view, MyProductPOJO selProduct) {

        // Pass the object selected in caroucel to the products activity
        if (mProdClickListener != null) mProdClickListener.onItemClick(view, selProduct);
    }

    @Override
    public void onFavouriteClicked(MyProductPOJO selProduct){

        if (mProdClickListener != null) mProdClickListener.onFavouriteClicked(selProduct);
    }

    @Override
    public void onBuyProduct(MyProductPOJO selProduct) {
        if (mProdClickListener != null) mProdClickListener.onBuyProduct(selProduct);
    }

    private ConstraintLayout generateProductCard() {

        final ConstraintLayout clTop = new ConstraintLayout(context);
        final ConstraintLayout clMiddle = new ConstraintLayout(context);
        final ConstraintLayout clBottom = new ConstraintLayout(context);
        final ConstraintLayout clClick = new ConstraintLayout(context);

        clTop.setBackgroundColor(Color.parseColor("#FF0000"));
        clTop.setId(View.generateViewId());
        clMiddle.setBackgroundColor(Color.parseColor("#00FF00"));
        clMiddle.setId(View.generateViewId());
        clBottom.setBackgroundColor(Color.parseColor("#0000FF"));
        clBottom.setId(View.generateViewId());
        clClick.setBackgroundColor(Color.parseColor("#0000FF"));
        clClick.setId(View.generateViewId());

        Typeface roboRegular = Typeface.createFromAsset( context.getAssets(), "font/Roboto-Regular.ttf");
        Typeface roboBold = Typeface.createFromAsset( context.getAssets(), "font/Roboto-Bold.ttf");
        // Title
        final TextView txtTitle = new TextView(context);
        txtTitle.setId(View.generateViewId());
        txtTitle.setTag(TXT_TITLE_TAG);
        txtTitle.setClickable(false);
        txtTitle.setTypeface(roboRegular);


        // Price
        final TextView txtPrice = new TextView(context);
        txtPrice.setId(View.generateViewId());
        txtPrice.setTag(TXT_PRICE_TAG);
        txtPrice.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
        txtPrice.setTextColor(Color.RED);
        txtPrice.setTypeface(roboRegular);
        txtPrice.setClickable(false);
        txtPrice.setTextSize(20);

        clClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // mClickListener.onProductClicked(view, product);
            }
        });

        // Logo
        final ImageView imgLogo = new ImageView(context);
        imgLogo.setScaleType(ImageView.ScaleType.FIT_CENTER);
        imgLogo.setBackgroundColor(Color.parseColor("#00FF00FF"));
        imgLogo.setId(View.generateViewId());
        imgLogo.setTag(IMG_LOGO_TAG);

        // Favorite
        final ImageView imgFav = new ImageView(context);
        imgFav.setScaleType(ImageView.ScaleType.FIT_CENTER);
        imgFav.setBackgroundColor(Color.parseColor("#00FF00FF"));
        imgFav.setId(View.generateViewId());
        imgFav.setTag(IMG_FAV_TAG);


        // Cart
        final ImageView imgBuy = new ImageView(context);
        imgBuy.setScaleType(ImageView.ScaleType.FIT_CENTER);
        imgBuy.setBackgroundColor(Color.parseColor("#00FF00FF"));
        imgBuy.setId(View.generateViewId());
        imgBuy.setTag(IMG_CART_TAG);


        final ConstraintLayout clCardBackground = new ConstraintLayout(context);
        clCardBackground.setId(View.generateViewId());

        clCardBackground.addView(clTop);
        clCardBackground.addView(clMiddle);
        clCardBackground.addView(clBottom);
        clCardBackground.addView(clClick);
        clTop.addView(imgLogo);
        clMiddle.addView(txtTitle);
        clClick.addView(imgFav);
        clClick.addView(imgBuy);
        clBottom.addView(txtPrice);

        clTop.setBackgroundColor(Color.TRANSPARENT);
        clMiddle.setBackgroundColor(Color.TRANSPARENT);
        clBottom.setBackgroundColor(Color.TRANSPARENT);
        clClick.setBackgroundColor(Color.TRANSPARENT);

        GradientDrawable shape =  new GradientDrawable();
        shape.setCornerRadius( 18 );
        shape.setColor(Color.parseColor("#FFFFFF"));
        clCardBackground.setBackground(shape);
        clCardBackground.setElevation(10);
        //imgLogo.setPadding(pad, pad, pad, pad);
        imgLogo.setBackgroundColor(Color.parseColor("#ffffff"));

//        String imgURL = BASE_URL + "clientapi/" + "getproductimage?" + "id=" + product.id;
//
//        Glide.with(context.getApplicationContext())
//                .asBitmap()
//                .load(imgURL)
//                .diskCacheStrategy(DiskCacheStrategy.NONE)
//                .skipMemoryCache(true)
//                .into(new SimpleTarget<Bitmap>() {
//                    @Override
//                    public void onResourceReady(Bitmap bitmap,
//                                                Transition<? super Bitmap> transition) {
//                        imgLogo.setImageBitmap(bitmap);
//                    }});
//
//       // treeObserver();
//
//        txtTitle.setText(product.name);
//        txtPrice.setText("" + product.price + " lei");
//        boolean found = false;
//        for (MyProductPOJO p: MyAppSession.getInstance().favouriteList){
//            if( p.id == product.id){
//                found = true;
//                break;
//            }
//        }
//        if(found == false){
//            imgFav.setImageDrawable( ResourcesCompat.getDrawable(context.getResources(), R.drawable.heart_empty, null));
//        }else{
//            imgFav.setImageDrawable( ResourcesCompat.getDrawable(context.getResources(), R.drawable.heart_red, null));
//        }
//
//        imgBuy.setImageDrawable( ResourcesCompat.getDrawable(context.getResources(), R.drawable.basket2, null));


        // imgFav.setImageDrawable(getResources().getDrawable(R.drawable.heart));

        // Layout button inside constraint
        ConstraintSet constraintTop = new ConstraintSet();
        constraintTop.connect(clTop.getId(), ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT, 0);
        constraintTop.connect(clTop.getId(), ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT, 0);
        constraintTop.connect(clTop.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0);
        constraintTop.connect(clTop.getId(), ConstraintSet.BOTTOM, clMiddle.getId(), ConstraintSet.TOP, 0);
        constraintTop.constrainPercentHeight(clTop.getId(), (float) 0.55);

        ConstraintSet constraintMiddle = new ConstraintSet();
        constraintMiddle.connect(clMiddle.getId(), ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT, 0);
        constraintMiddle.connect(clMiddle.getId(), ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT, 0);
        constraintMiddle.connect(clMiddle.getId(), ConstraintSet.TOP, clTop.getId(), ConstraintSet.BOTTOM, 0);
        constraintMiddle.constrainPercentHeight(clMiddle.getId(), (float) 0.3);

        ConstraintSet constraintBottom = new ConstraintSet();
        constraintBottom.connect(clBottom.getId(), ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT, 0);
        constraintBottom.connect(clBottom.getId(), ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT, 0);
        constraintBottom.connect(clBottom.getId(), ConstraintSet.TOP, clMiddle.getId(), ConstraintSet.BOTTOM, 0);
        constraintBottom.constrainPercentHeight(clBottom.getId(), (float) 0.15);

        // Click
        ConstraintSet constraintClick = new ConstraintSet();
        constraintClick.connect(clClick.getId(), ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT, 0);
        constraintClick.connect(clClick.getId(), ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT, 0);
        constraintClick.connect(clClick.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0);
        constraintClick.connect(clClick.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0);

        //Logo
        ConstraintSet constraintImgLogo = new ConstraintSet();
        constraintImgLogo.connect(imgLogo.getId(), ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT, 0);
        constraintImgLogo.connect(imgLogo.getId(), ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT, 0);
        constraintImgLogo.connect(imgLogo.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0);
        constraintImgLogo.connect(imgLogo.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0);
        constraintImgLogo.constrainPercentHeight(imgLogo.getId(), (float) 0.95);
        constraintImgLogo.constrainPercentWidth(imgLogo.getId(), (float) 0.95);

        // Title
        ConstraintSet constraintTxtTitle = new ConstraintSet();
        constraintTxtTitle.connect(txtTitle.getId(), ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT, 0);
        constraintTxtTitle.connect(txtTitle.getId(), ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT, 0);
        constraintTxtTitle.connect(txtTitle.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0);
        constraintTxtTitle.connect(txtTitle.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0);
        constraintTxtTitle.constrainPercentHeight(txtTitle.getId(), (float) 0.8);
        constraintTxtTitle.constrainPercentWidth(txtTitle.getId(), (float) 0.8);

        // Price txt
        ConstraintSet constraintTxtPrice = new ConstraintSet();
        constraintTxtPrice.connect(txtPrice.getId(), ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT, 0);
        constraintTxtPrice.connect(txtPrice.getId(), ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT, 0);
        constraintTxtPrice.connect(txtPrice.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0);
        constraintTxtPrice.connect(txtPrice.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0);
        constraintTxtPrice.constrainPercentHeight(txtPrice.getId(), (float) 0.8);
        constraintTxtPrice.constrainPercentWidth(txtPrice.getId(), (float) 0.7);


        // Fav img
        ConstraintSet constraintImgFav = new ConstraintSet();
        constraintImgFav.connect(imgFav.getId(), ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT, 17);
        constraintImgFav.connect(imgFav.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 17);
        // constraintImgFav.connect(imgFav.getId(), ConstraintSet.TOP, txtPrice.getId(), ConstraintSet.TOP, 0);
        constraintImgFav.constrainPercentHeight(imgFav.getId(), (float) 0.13);
        constraintImgFav.constrainPercentWidth(imgFav.getId(), (float) 0.13);

        // Buy img
        ConstraintSet constraintImgBuy = new ConstraintSet();
        constraintImgBuy.connect(imgBuy.getId(), ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT, 13);
        constraintImgBuy.connect(imgBuy.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0);
        constraintImgBuy.constrainPercentHeight(imgBuy.getId(), (float) 0.2);
        constraintImgBuy.constrainPercentWidth(imgBuy.getId(), (float) 0.2);

        constraintTop.applyTo(clCardBackground);
        constraintMiddle.applyTo(clCardBackground);
        constraintBottom.applyTo(clCardBackground);
        constraintClick.applyTo(clCardBackground);
        constraintImgLogo.applyTo(clTop);
        constraintTxtTitle.applyTo(clMiddle);
        constraintTxtPrice.applyTo(clBottom);
        constraintImgFav.applyTo(clClick);
        constraintImgBuy.applyTo(clClick);




        return clCardBackground;
    }
}
