package com.mycompany.myordertest.custom;


import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.res.ResourcesCompat;

import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.RotateAnimation;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.mycompany.myordertest.ProductsActivity;
import com.mycompany.myordertest.R;
import com.mycompany.myordertest.adapters.MyStoresAdapter;
import com.mycompany.myordertest.constants.RAppConsts;
import com.mycompany.myordertest.rest.DTO.MyProductPOJO;
import com.mycompany.myordertest.rest.DTO.MySubcategoryPOJO;
import com.mycompany.myordertest.rest.RRestClient;
import com.mycompany.myordertest.rest.RServerRequest;
import com.mycompany.myordertest.rest.response.MyProductsAPIResponse;
import com.mycompany.myordertest.utils.MyAppSession;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.mycompany.myordertest.constants.RAppConsts.BASE_URL;

public class RCaroucelProductCards extends ConstraintLayout {

    private static final String TAG = RCaroucel.class.getName();



    //Exported
    private OnClickListener itemSelectedListener = null;
    private MySubcategoryPOJO mySuCat = null;
    View overlayLeft = null;
    View overlayRight = null;
    ImageView imgLeft = null;
    ImageView imgRight = null;
    ConstraintLayout clTop = null;
    ConstraintSet clCats;
    Timer timer = new Timer();
    boolean isLeftArrowPressed = false;
    boolean isRightArrowPressed = false;
    final int caroucelDelayAnimation = 70;
    int imgsTotalWidth = 0;

    public HorizontalScrollView scrollLogos = null;
    ConstraintLayout constraintLoading = null;
    private int itemsCount = 5;
    private int frameH = 70;
    private int frameColor = Color.BLUE;
    private int loadedImgs = 0;
    private RCaroucelProductCards.CardClickListener mClickListener;
    private List<MyProductPOJO> allProducts = new ArrayList<MyProductPOJO>(){{

    }};
    public OnClickListener getItemSelectedListener() {
        return itemSelectedListener;
    }

    public void setItemSelectedListener(OnClickListener itemSelectedListener) {
        this.itemSelectedListener = itemSelectedListener;
    }

    private Context context = null;

    public int getItemsCount() {
        return itemsCount;

    }

    public void setSubCat(MySubcategoryPOJO subcatPojo){
        this.mySuCat = subcatPojo;
        makeGetProductsCall("" + subcatPojo.id);
    }
    public void setItemsCount(int itemsCount) {
        this.itemsCount = itemsCount;
        populateCaroucel();
    }

    public List<MyProductPOJO> getAllProducts() {
        return allProducts;
    }

    public void setAllProducts(List<MyProductPOJO> allProducts) {
        this.allProducts = allProducts;
        this.itemsCount = allProducts.size();
      //  populateCaroucel();
    }


    public RCaroucelProductCards(Context context) {
        super(context);
        init(context);
    }

    public RCaroucelProductCards(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public RCaroucelProductCards(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public  void init(Context context) {
        this.context = context;
        populateCaroucel();
    }


    private void manageTimer(){
        if(isLeftArrowPressed == false && isRightArrowPressed == false && timer != null){
            timer.cancel();
            timer.purge();
            timer = null;
            Log.i(TAG, "Both left and right arrwo are up: timer stopped");
        }else{
            TimerTask repeatedTask = new TimerTask() {
                public void run() {

                    Log.i(TAG, "run method from timer was called");
                    if(isLeftArrowPressed){
                        moveCaroucel(true);
                    }
                    if(isRightArrowPressed){
                        moveCaroucel(false);
                    }
                }
            };
            if (timer != null) {
                timer.cancel();
                timer.purge();
                timer = null;
            }
            timer = new Timer("Timer");

            long delay  = 0L;
            long period = caroucelDelayAnimation;
            Log.i(TAG, "Timer will start");
            timer.scheduleAtFixedRate(repeatedTask, delay, period);
        }
    }

    private void setLoadingPerspective(){

        this.setBackgroundColor(Color.TRANSPARENT);
        constraintLoading = new ConstraintLayout(context);
        constraintLoading.setBackgroundColor(Color.parseColor("#CCFFFFFF"));//(Color.parseColor("#55FFFFFF"));
        constraintLoading.setId(View.generateViewId());
        this.addView(constraintLoading);

        ConstraintSet loadingSet = new ConstraintSet();

        loadingSet.connect(constraintLoading.getId(), ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT, 5);
        loadingSet.connect(constraintLoading.getId(), ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT, 5);
        loadingSet.connect(constraintLoading.getId(), ConstraintSet.BOTTOM,ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0);
        loadingSet.connect(constraintLoading.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0);
        loadingSet.applyTo(this);

        // Title text
        TextView txtTitle = new TextView(context);
        txtTitle.setId(View.generateViewId());
        txtTitle.setBackgroundColor(Color.TRANSPARENT);
        txtTitle.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        txtTitle.setText("");
        txtTitle.setTextSize(17);
        txtTitle.setPadding(0, 0, 0, 0);
        txtTitle.setLayoutParams(new ConstraintLayout.LayoutParams(30, ViewGroup.LayoutParams.WRAP_CONTENT));
        constraintLoading.addView(txtTitle);

        Typeface faceLight = Typeface.createFromAsset( context.getAssets(), "font/Roboto-Light.ttf");
        txtTitle.setTextColor(Color.parseColor("#525253"));
        if(faceLight != null){
            txtTitle.setTypeface(faceLight);
        }

        ConstraintSet txtCats = new ConstraintSet();
        txtCats.constrainWidth(txtTitle.getId(), ConstraintSet.WRAP_CONTENT);
        txtCats.connect(txtTitle.getId(), ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT, 0);
        txtCats.connect(txtTitle.getId(), ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT, 60);
        txtCats.connect(txtTitle.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 30);
        txtCats.applyTo(constraintLoading);


        //Close image
        // Reload image
        ImageView imgClose = new ImageView(context);
        imgClose.setId(View.generateViewId());
        imgClose.setScaleType(ImageView.ScaleType.CENTER_CROP);
      //  imgClose.setBackgroundResource(R.drawable.close);
        constraintLoading.addView(imgClose);

        ConstraintSet imgLoadingCats0 = new ConstraintSet();
        imgLoadingCats0.constrainHeight(imgClose.getId(), 55);
        imgLoadingCats0.constrainWidth(imgClose.getId(), 55);
        imgLoadingCats0.connect(imgClose.getId(), ConstraintSet.RIGHT, constraintLoading.getId(), ConstraintSet.RIGHT, 50);
        imgLoadingCats0.connect(imgClose.getId(), ConstraintSet.TOP, constraintLoading.getId(), ConstraintSet.TOP, 50);
        imgLoadingCats0.applyTo(constraintLoading);

        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                removeAllViews();
            }
        });

        // Reload image
        ImageView imgLoading = new ImageView(context);
        imgLoading.setId(View.generateViewId());
        imgLoading.setScaleType(ImageView.ScaleType.CENTER_CROP);
        //imgLoading.setBackgroundResource(R.drawable.reload2);
        constraintLoading.addView(imgLoading);

        ConstraintSet imgLoadingCats = new ConstraintSet();
        imgLoadingCats.constrainHeight(imgLoading.getId(), 100);
        imgLoadingCats.constrainWidth(imgLoading.getId(), 100);
        imgLoadingCats.connect(imgLoading.getId(), ConstraintSet.LEFT, constraintLoading.getId(), ConstraintSet.LEFT, 0);
        imgLoadingCats.connect(imgLoading.getId(), ConstraintSet.RIGHT, constraintLoading.getId(), ConstraintSet.RIGHT, 0);
        imgLoadingCats.connect(imgLoading.getId(), ConstraintSet.BOTTOM, constraintLoading.getId(), ConstraintSet.BOTTOM, 0);
        imgLoadingCats.connect(imgLoading.getId(), ConstraintSet.TOP, constraintLoading.getId(), ConstraintSet.TOP, 0);
        imgLoadingCats.applyTo(constraintLoading);

        final RotateAnimation rotateAnim = new RotateAnimation(0.0f, 360,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        rotateAnim.setDuration(500);
        rotateAnim.setFillAfter(false);
        rotateAnim.setRepeatCount(-1);
        imgLoading.startAnimation(rotateAnim);

    }
    private void addNavImgages(){

        overlayLeft = new View(context);
        overlayRight = new View(context);
        imgLeft = new ImageView(context);
        imgRight = new ImageView(context);

        imgLeft.setId(View.generateViewId());
        imgRight.setId(View.generateViewId());
        overlayLeft.setId(View.generateViewId());
        overlayRight.setId(View.generateViewId());
        imgLeft.setScaleType(ImageView.ScaleType.CENTER_CROP);
       // imgLeft.setBackgroundResource(R.drawable.back_black);
       // imgRight.setBackgroundResource(R.drawable.back_black_right);
        overlayLeft.setBackgroundColor(Color.parseColor("#00FFFFFF"));
        overlayRight.setBackgroundColor(Color.parseColor("#00FFFFFF"));


        this.addView(overlayLeft);
        this.addView(overlayRight);
        this.addView(imgLeft);
        this.addView(imgRight);
        overlayLeft.setVisibility(INVISIBLE);
        overlayRight.setVisibility(INVISIBLE);

        overlayRight.setTag(new String("overlayRight"));
        overlayLeft.setTag(new String("overlayLeft"));
//        this.setBackgroundColor(Color.RED);
//        imgRight.setBackgroundColor(Color.RED);
//        overlayRight.setBackgroundColor(Color.GREEN);

//        final RotateAnimation rotateAnim = new RotateAnimation(0.0f, 180,
//                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
//                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
//
//        rotateAnim.setDuration(3000);
//        rotateAnim.setFillAfter(true);
//        imgRight.setAnimation(rotateAnim);
//        rotateAnim.start();

        ConstraintSet imgLeftCats = new ConstraintSet();
        imgLeftCats.constrainHeight(imgLeft.getId(), 70);
        imgLeftCats.constrainWidth(imgLeft.getId(), 70);
        imgLeftCats.connect(imgLeft.getId(), ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT, 5);
        imgLeftCats.connect(imgLeft.getId(), ConstraintSet.BOTTOM, scrollLogos.getId(), ConstraintSet.BOTTOM, 0);
        imgLeftCats.connect(imgLeft.getId(), ConstraintSet.TOP, scrollLogos.getId(), ConstraintSet.TOP, 0);
        imgLeftCats.applyTo(this);

        ConstraintSet imgRighttCats = new ConstraintSet();
        imgRighttCats.constrainHeight(imgRight.getId(), 70);
        imgRighttCats.constrainWidth(imgRight.getId(),70);
        imgRighttCats.connect(imgRight.getId(), ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT, 0);
        imgRighttCats.connect(imgRight.getId(), ConstraintSet.BOTTOM, scrollLogos.getId(), ConstraintSet.BOTTOM, 0);
        imgRighttCats.connect(imgRight.getId(), ConstraintSet.TOP, scrollLogos.getId(), ConstraintSet.TOP, 0);
        imgRighttCats.applyTo(this);

        ConstraintSet overlayLeftCats = new ConstraintSet();
        overlayLeftCats.constrainWidth(overlayLeft.getId(), 100);
        overlayLeftCats.connect(overlayLeft.getId(), ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT, 0);
        overlayLeftCats.connect(overlayLeft.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 7);
        overlayLeftCats.connect(overlayLeft.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 7);
        overlayLeftCats.applyTo(this);


        ConstraintSet overlayRightCats = new ConstraintSet();
        overlayRightCats.constrainWidth(overlayRight.getId(), 100);
        overlayRightCats.connect(overlayRight.getId(), ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT, 0);
        overlayRightCats.connect(overlayRight.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 7);
        overlayRightCats.connect(overlayRight.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 7);
        overlayRightCats.applyTo(this);

        overlayLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // moveCaroucel(true);
            }
        });
        overlayRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // moveCaroucel(false);
            }
        });
        OnTouchListener touchListener = new OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event){

                String senderTag =  (String)v.getTag();
                Log.i(TAG, "touchListener called [" + event.getAction() + "]");
                if ((event.getAction() == MotionEvent.ACTION_DOWN)){
                    if (senderTag.compareToIgnoreCase("overlayLeft") == 0) {
                        isLeftArrowPressed = true;
                        Log.i(TAG, "Left overlay was " + isLeftArrowPressed);
                    }
                    if (senderTag.compareToIgnoreCase("overlayRight") == 0) {
                        isRightArrowPressed = true;
                        Log.i(TAG, "Right overlay was " + isRightArrowPressed);
                    }
                    manageTimer();
                }

                if ((event.getAction() == MotionEvent.ACTION_UP)){
                    if (senderTag.compareToIgnoreCase("overlayLeft") == 0) {
                        isLeftArrowPressed = false;
                        Log.i(TAG, "Left overlay was " + isLeftArrowPressed);
                    }
                    if (senderTag.compareToIgnoreCase("overlayRight") == 0) {
                        isRightArrowPressed = false;
                        Log.i(TAG, "Right overlay was " + isRightArrowPressed);
                    }
                }



                return true;
            }
        };
        overlayLeft.setOnTouchListener(touchListener);
        overlayRight.setOnTouchListener(touchListener);
    }
    private  void moveCaroucel(boolean direction){

        int idx = 150 * ((direction == true)? -1 : 1);
        scrollLogos.post(new Runnable() {
            @Override
            public void run() {
                int newPos = scrollLogos.getScrollX() + idx;
                if (newPos < 0) {
                    newPos = 0;
                }
                ObjectAnimator.ofInt(scrollLogos, "scrollX",  newPos).setDuration(caroucelDelayAnimation).start();
                if (newPos == 0 ){
                    imgLeft.setVisibility(INVISIBLE);
                } else {
                    imgLeft.setVisibility(VISIBLE);
                }
            }
        });
    }

    private void addHeader(){

//        this.setBackgroundColor(Color.YELLOW);
        clTop = new ConstraintLayout(context);
        this.addView(clTop);
        clTop.setId(View.generateViewId());
        clTop.setBackgroundColor(Color.TRANSPARENT);

        ConstraintSet topCats = new ConstraintSet();
        topCats.constrainHeight(clTop.getId(), 100);
        topCats.connect(clTop.getId(), ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT, 0);
        topCats.connect(clTop.getId(), ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT, 0);
        topCats.connect(clTop.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0);
        topCats.applyTo(this);

        TextView txtTitle = new TextView(context);
        txtTitle.setId(View.generateViewId());
        txtTitle.setBackgroundColor(Color.TRANSPARENT);
        txtTitle.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        txtTitle.setText(this.mySuCat.label);
        txtTitle.setTextSize(19);
        txtTitle.setPadding(0, 0, 0, 0);
        txtTitle.setLayoutParams(new ConstraintLayout.LayoutParams(30, ViewGroup.LayoutParams.WRAP_CONTENT));
        clTop.addView(txtTitle);

        TextView txtDots = new TextView(context);
        txtDots.setId(View.generateViewId());
        txtDots.setBackgroundColor(Color.TRANSPARENT);
        txtDots.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        txtDots.setText("...");
        txtDots.setTextSize(19);
        txtDots.setPadding(0, 0, 0, 0);
        //txtDots.setLayoutParams(new ConstraintLayout.LayoutParams(30, ViewGroup.LayoutParams.WRAP_CONTENT));
        clTop.addView(txtDots);

        Typeface faceLight = Typeface.createFromAsset( context.getAssets(), "font/Roboto-Light.ttf");
        Typeface faceBold = Typeface.createFromAsset( context.getAssets(), "font/Roboto-Bold.ttf");
        txtTitle.setTextColor(Color.parseColor("#525253"));
        if(faceLight != null){
            txtTitle.setTypeface(faceBold);
        }

        ConstraintSet txtCats = new ConstraintSet();
        txtCats.constrainWidth(txtTitle.getId(), ConstraintSet.WRAP_CONTENT);
        txtCats.connect(txtTitle.getId(), ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT, 20);
        txtCats.connect(txtTitle.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0);
        txtCats.connect(txtTitle.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0);
        txtCats.applyTo(clTop);

        ConstraintSet txtDotsConstraints = new ConstraintSet();
        txtDotsConstraints.constrainWidth(txtDots.getId(), ConstraintSet.WRAP_CONTENT);
        txtDotsConstraints.connect(txtDots.getId(), ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT, 50);
        txtDotsConstraints.connect(txtDots.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0);
        txtDotsConstraints.connect(txtDots.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0);
        txtDotsConstraints.applyTo(clTop);

        final ImageView imgReload = new ImageView(context);
        // imgReload.setBackgroundResource(R.drawable.reload2);
//        Glide
//                .with(context)
//                .load(R.drawable.reload2)
//                .centerCrop()
//                .placeholder(R.drawable.reload2)
//                .into(imgReload);
        imgReload.setId(View.generateViewId());
        clTop.addView(imgReload);

        //txtTitle.setBackgroundColor(Color.YELLOW);
        ConstraintSet imgCats = new ConstraintSet();
        imgCats.constrainHeight(imgReload.getId(), 40);
        imgCats.constrainWidth(imgReload.getId(), 40);
        imgCats.connect(imgReload.getId(), ConstraintSet.LEFT, txtTitle.getId(), ConstraintSet.RIGHT, 20);
        imgCats.connect(imgReload.getId(), ConstraintSet.TOP, txtTitle.getId(), ConstraintSet.TOP, 0);
        imgCats.connect(imgReload.getId(), ConstraintSet.BOTTOM, txtTitle.getId(), ConstraintSet.BOTTOM, 0);
        imgCats.applyTo(clTop);

        View overlay = new View(context);
        overlay.setId(View.generateViewId());
        overlay.setBackgroundColor(Color.TRANSPARENT);
        clTop.addView(overlay);
        ConstraintSet overlayCats = new ConstraintSet();
        overlayCats.constrainHeight(overlay.getId(), 70);
        overlayCats.constrainWidth(overlay.getId(), 70);
        overlayCats.connect(overlay.getId(), ConstraintSet.LEFT, txtTitle.getId(), ConstraintSet.RIGHT, 10);
        overlayCats.connect(overlay.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0);
        overlayCats.connect(overlay.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0);
        overlayCats.applyTo(clTop);

        overlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                final RotateAnimation rotateAnim = new RotateAnimation(0.0f, 360,
                        RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                        RotateAnimation.RELATIVE_TO_SELF, 0.5f);
                rotateAnim.setDuration(500);
                rotateAnim.setFillAfter(false);
                rotateAnim.setRepeatCount(-1);
                imgReload.startAnimation(rotateAnim);
            }
        });


    }
    private void populateCaroucel() {


        this.imgsTotalWidth = 0;
        loadedImgs = 0;

        if(allProducts.size() == 0){

            this.removeAllViews();
            setLoadingPerspective();
            return;

        }else{
            ValueAnimator anim = ValueAnimator.ofInt(constraintLoading.getMeasuredHeight(), -100);
            anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    int val = (Integer) valueAnimator.getAnimatedValue();
                    ViewGroup.LayoutParams layoutParams = constraintLoading.getLayoutParams();
                    layoutParams.height = val;
                    constraintLoading.setLayoutParams(layoutParams);
                }
            });
            anim.setDuration(1000);
            anim.start();

        }

        this.removeAllViews();
        // Finally, set the button background drawable
        //this.setBackground(layerDrawable);


        scrollLogos = new HorizontalScrollView(context);
        scrollLogos.setVisibility(INVISIBLE);
        scrollLogos.setSmoothScrollingEnabled(true);
        this.addView(scrollLogos);
        this.setBackgroundColor(Color.LTGRAY);
        scrollLogos.setId(View.generateViewId());
        scrollLogos.setBackgroundColor(Color.TRANSPARENT);
        scrollLogos.setHorizontalScrollBarEnabled(false);


        scrollLogos.setOnScrollChangeListener(new OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {



            }
        });

        ConstraintLayout clItems = new ConstraintLayout(context);
        clItems.setId(View.generateViewId());

        clItems.setMinimumWidth(300);
        clItems.setMinimumHeight(100);
        clItems.setBackgroundColor(Color.LTGRAY);
//        scrollLogos.setBackgroundColor(Color.GREEN);
        scrollLogos.addView(clItems);


        if (context == null ||
                itemsCount == 0 ||
                allProducts.size() == 0) {
            return;
        }


        ConstraintLayout prevCat = null;
        int idx = 0;
        final int index = idx;
        ConstraintSet clConstraints = new ConstraintSet();
        clConstraints.connect(clItems.getId(), ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT, 40);
        clConstraints.connect(clItems.getId(), ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT, 40);
        clConstraints.applyTo(this);

        for (MyProductPOJO tmpProd : allProducts) {
if(idx == 10) {break;}

            ConstraintLayout newProdCard = generateProductCard(tmpProd);

            newProdCard.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    mClickListener.onProductClicked(view, allProducts.get(index));
                }
            });
            newProdCard.setTag(new String(""+idx));
            Log.d(TAG, "A new category was added: " + this.allProducts.get(idx));

            clItems.addView(newProdCard);


            final ConstraintLayout transitionsContainer = this;
            newProdCard.setOnClickListener(itemSelectedListener);
            ConstraintSet constraintsCats = new ConstraintSet();

            final View activityRootView = ((ProductsActivity)context).findViewById(R.id.products_cl_main);
            int mainW =  activityRootView.getWidth();
            int mainH = activityRootView.getHeight();

            int test = scrollLogos.getHeight();
            int emptySpace = mainH / 10;
            int cardH = (mainH - emptySpace) / 2;
            int cardW = (int)((cardH - 220) * (1.7 / 3.0));

            constraintsCats.constrainWidth(newProdCard.getId(), cardW);
            if(idx == allProducts.size()) {
                constraintsCats.connect(newProdCard.getId(), ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT, 40);
            }
            constraintsCats.connect(newProdCard.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 10);
            constraintsCats.connect(newProdCard.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 10);

            idx++;

            if (prevCat == null) {

                constraintsCats.connect(newProdCard.getId(), ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT, 40);

            }else {

                constraintsCats.connect(newProdCard.getId(), ConstraintSet.LEFT, prevCat.getId(), ConstraintSet.RIGHT, 70);

            }
            prevCat = newProdCard;

            constraintsCats.applyTo(clItems);
            //if (idx == 1) break;
        }

        float[] or = new float[]{75,75,75,75,75,75,75,75};

        treeObserver();

    }

    private ConstraintLayout generateProductCard(MyProductPOJO product) {

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

        // Title
        final TextView txtTitle = new TextView(context);
        txtTitle.setId(View.generateViewId());
        txtTitle.setClickable(false);


        // Price
        final TextView txtPrice = new TextView(context);
        txtPrice.setId(View.generateViewId());
        txtPrice.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
        txtPrice.setTextColor(Color.RED);
        txtPrice.setTypeface(roboRegular);
        txtPrice.setClickable(false);

        clClick.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickListener.onProductClicked(view, product);
            }
        });

        // Logo
        final ImageView imgLogo = new ImageView(context);
        imgLogo.setScaleType(ImageView.ScaleType.FIT_CENTER);
        imgLogo.setBackgroundColor(Color.parseColor("#00FF00FF"));
        imgLogo.setId(View.generateViewId());

        // Favorite
        final ImageView imgFav = new ImageView(context);
        imgFav.setScaleType(ImageView.ScaleType.FIT_CENTER);
        imgFav.setBackgroundColor(Color.parseColor("#00FF00FF"));
        imgFav.setId(View.generateViewId());

        final List<MyProductPOJO> favProducts = MyAppSession.getInstance().getFavList();
        imgFav.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickListener.onFavouriteClicked(product);
                boolean found = false;
                int idx = 0;
                for (MyProductPOJO p: favProducts){
                    if( p.id == product.id){
                        favProducts.remove(idx);
                        found = true;
                        break;
                    }
                    idx++;
                }

                if ( found == false){
                    Log.d(TAG, "Product was not found in fav list: " + product.id);
                    imgFav.setImageDrawable( ResourcesCompat.getDrawable(context.getResources(), R.drawable.heart_red, null));
                    favProducts.add(product);
                }else{
                    Log.d(TAG, "Product was found in fav list: " + product.id);
                    imgFav.setImageDrawable( ResourcesCompat.getDrawable(context.getResources(), R.drawable.heart_empty, null));
                }
               // MyAppSession.getInstance().favouriteList = favProducts;
            }



        });
        // Favorite
        final ImageView imgBuy = new ImageView(context);
        imgBuy.setScaleType(ImageView.ScaleType.FIT_CENTER);
        imgBuy.setBackgroundColor(Color.parseColor("#00FF00FF"));
        imgBuy.setId(View.generateViewId());

        imgBuy.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickListener.onBuyProduct(product);
            }
        });



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

        String imgURL = BASE_URL + "clientapi/" + "getproductimage?" + "id=" + product.id + "firebase_uid=" + MyAppSession.getInstance().getCurrentUser().getUid();

        Glide.with(getContext().getApplicationContext())
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

                        treeObserver();

                        txtTitle.setText(product.name);
                        txtPrice.setText("" + product.price + " lei");
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

                        imgBuy.setImageDrawable( ResourcesCompat.getDrawable(context.getResources(), R.drawable.basket2, null));


                       // imgFav.setImageDrawable(getResources().getDrawable(R.drawable.heart));

                        // Layout button inside constraint
                        ConstraintSet constraintTop = new ConstraintSet();
                        constraintTop.connect(clTop.getId(), ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT, 0);
                        constraintTop.connect(clTop.getId(), ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT, 0);
                        constraintTop.connect(clTop.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0);
                        constraintTop.connect(clTop.getId(), ConstraintSet.BOTTOM, clMiddle.getId(), ConstraintSet.TOP, 0);
                        constraintTop.constrainPercentHeight(clTop.getId(), (float) 0.33);

                        ConstraintSet constraintMiddle = new ConstraintSet();
                        constraintMiddle.connect(clMiddle.getId(), ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT, 0);
                        constraintMiddle.connect(clMiddle.getId(), ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT, 0);
                        constraintMiddle.connect(clMiddle.getId(), ConstraintSet.TOP, clTop.getId(), ConstraintSet.BOTTOM, 0);
                        constraintMiddle.constrainPercentHeight(clMiddle.getId(), (float) 0.33);

                        ConstraintSet constraintBottom = new ConstraintSet();
                        constraintBottom.connect(clBottom.getId(), ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT, 0);
                        constraintBottom.connect(clBottom.getId(), ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT, 0);
                        constraintBottom.connect(clBottom.getId(), ConstraintSet.TOP, clMiddle.getId(), ConstraintSet.BOTTOM, 0);
                        constraintBottom.constrainPercentHeight(clBottom.getId(), (float) 0.33);

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
                        constraintImgLogo.constrainPercentHeight(imgLogo.getId(), (float) 0.8);
                        constraintImgLogo.constrainPercentWidth(imgLogo.getId(), (float) 0.7);

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
                        constraintImgFav.connect(imgFav.getId(), ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT, 7);
                        constraintImgFav.connect(imgFav.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 7);
                       // constraintImgFav.connect(imgFav.getId(), ConstraintSet.TOP, txtPrice.getId(), ConstraintSet.TOP, 0);
                        constraintImgFav.constrainPercentHeight(imgFav.getId(), (float) 0.2);
                        constraintImgFav.constrainPercentWidth(imgFav.getId(), (float) 0.2);

                        // Buy img
                        ConstraintSet constraintImgBuy = new ConstraintSet();
                        constraintImgBuy.connect(imgBuy.getId(), ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT, 13);
                        constraintImgBuy.connect(imgBuy.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 13);
                        constraintImgBuy.constrainPercentHeight(imgBuy.getId(), (float) 0.3);
                        constraintImgBuy.constrainPercentWidth(imgBuy.getId(), (float) 0.3);

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

    private void treeObserver(){

        Log.d(TAG, "treeObserver was called");
        final ConstraintLayout clThis = this;
        clThis.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {

                clThis.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                Rect r = new Rect();
                clThis.getWindowVisibleDisplayFrame(r);
                int clHeight = clThis.getHeight() / 4;
                int imgHeight = clThis.getHeight() / 2;

                //addNavImgages();
                addHeader();

                clCats = new ConstraintSet();
                clCats.connect(scrollLogos.getId(), ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT, 8);
                clCats.connect(scrollLogos.getId(), ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT, 8);
                clCats.connect(scrollLogos.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 80);
                clCats.connect(scrollLogos.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 140);

                clCats.applyTo(clThis);
            }
        });
    }

    private void makeGetProductsCall(String subcatId) {

        RServerRequest serverReqTranslates = new RServerRequest(RAppConsts.kGetProducts);

        try {


            Call<MyProductsAPIResponse> cc = new RRestClient().
                    getApiMethods().
                    getProducts(
                            subcatId,
                            MyAppSession.getInstance().getCurrentUser().getUid());

            ProductsActivity da = (ProductsActivity) context;
            cc.enqueue(new Callback<MyProductsAPIResponse>() {
                @Override
                public void onFailure(Call<MyProductsAPIResponse> call, Throwable t) {
                    System.out.println("callback- onFailure");
                }

                @Override
                public void onResponse(Call<MyProductsAPIResponse> call, Response<MyProductsAPIResponse> response) {

                   allProducts = response.body().data;
                   populateCaroucel();


                }
            });

        } catch (com.google.gson.JsonSyntaxException ex) {

        }
    }

    // parent activity will implement this method to respond to click events
    public interface CardClickListener {
        void onProductClicked(View view, MyProductPOJO selProduct);
        void onFavouriteClicked(MyProductPOJO selProduct);
        void onBuyProduct(MyProductPOJO selProduct);
    }
    // allows clicks events to be caught
    public void setClickListener(RCaroucelProductCards.CardClickListener cardClickListener) {
        this.mClickListener = cardClickListener;
    }
}
