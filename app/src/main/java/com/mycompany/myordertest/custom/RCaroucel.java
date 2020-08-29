package com.mycompany.myordertest.custom;


import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.HorizontalScrollView;
import android.widget.TextView;
import com.mycompany.myordertest.R;
import java.util.ArrayList;
import java.util.List;

public class RCaroucel extends ConstraintLayout {

    private static final String TAG = RCaroucel.class.getName();

    public HorizontalScrollView horizontalParent = null;
//    public MenuActivity parentActivity = null;
    private int itemsCount = 5;
    private ConstraintLayout selectedCaroucelItem = null;
    private  ConstraintLayout prevCaroucelItem = null;
    private ConstraintLayout selectionFrame;
    private int frameH = 82;
    private int animationDuration = 500;
    private int frameColor = Color.parseColor("#FFAF33");
    private int selectedItemIdx = -1;
    private List<ConstraintLayout> categoriesList = new ArrayList<ConstraintLayout>();
    private List<String> itemNames = new ArrayList<String>(){{
        add("Item 0");
        add("Item 1");
        add("Item 2");
        add("Item 3");
        add("Item 4");
        add("Item 5");
        add("Item 0");

    }};
    private Context context = null;

    public int getItemsCount() {
        return itemsCount;

    }

    public void setItemsCount(int itemsCount) {
        this.itemsCount = itemsCount;
        populateCaroucel();
    }

    public List<String> getItemNames() {
        return itemNames;
    }

    public void setItemNames(List<String> itemNames) {
        this.itemNames = itemNames;
    }


    public RCaroucel(Context context) {
        super(context);
        init(context);
    }

    public RCaroucel(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public RCaroucel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public  void init(Context context) {
        this.context = context;
        this.setBackgroundColor(Color.CYAN);
        populateCaroucel();
    }

    public void DoSelectItem(int index, boolean animated){
        if (categoriesList.size() - 1 < index) {
            // Log(TAG, "DoSelectItem was called with invalid index [");
            return;
        }
        ConstraintLayout selectedView = this.categoriesList.get(index);
        CategoryTagClass ctc = (CategoryTagClass)selectedView.getTag();

        final Rect categoryBounds = new Rect();
        selectedView.getHitRect(categoryBounds);

        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        int width = metrics.widthPixels;
        final int xOffset = (width - categoryBounds.width()) / 2;

        int animationDurationX = (animated)? this.animationDuration : this.animationDuration;
        horizontalParent.post(new Runnable() {
            @Override
            public void run() {
                // scrollLogos.smoothScrollTo(categoryBounds.left - xOffset, 0);;
                ObjectAnimator.ofInt(horizontalParent, "scrollX",  categoryBounds.left - xOffset).setDuration(animationDuration).start();
            }
        });
        selectedCaroucelItem = (ConstraintLayout) selectedView;

        TextView ctv = (TextView) selectedCaroucelItem.getChildAt(0);
        if(ctv != null && animated){
            Integer colorFrom = Color.parseColor("#3B3A3A");
            Integer colorTo = Color.WHITE;
            ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
            colorAnimation.setDuration(animationDurationX);
            colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                @Override
                public void onAnimationUpdate(ValueAnimator animator) {
                    ctv.setTextColor((Integer)animator.getAnimatedValue());
                }

            });
            colorAnimation.start();

            int duration = 1000;  //miliseconds
            int offset = 0;      //fromListTop

//            parentActivity.scrollMenuTo(ctc.index);
        }

        if(prevCaroucelItem != null && prevCaroucelItem != selectedCaroucelItem && animated) {
            TextView utv = (TextView) prevCaroucelItem.getChildAt(0);
            if(utv != null){

                Integer colorFrom = Color.WHITE;
                Integer colorTo = Color.parseColor("#3B3A3A");
                ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
                colorAnimation.setDuration(animationDurationX);
                colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                    @Override
                    public void onAnimationUpdate(ValueAnimator animator) {
                        utv.setTextColor((Integer)animator.getAnimatedValue());
                    }

                });
                colorAnimation.start();

                // ctv.setTextColor(Color.WHITE);

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        //utv.setTextColor(Color.parseColor("#3B3A3A"));
                    }
                }, animationDuration/2);


            }

        }
        prevCaroucelItem = (ConstraintLayout)selectedView;
        Log.d(TAG, "Category: "+ctc.index+" was clicked");

        final ConstraintLayout transitionsContainer = this;
        int newX = categoryBounds.left - 10;
        int newW = categoryBounds.width() + 20;
        if (categoryBounds.width() > 0 ) {
            AutoTransition autoTransition = new AutoTransition();
            autoTransition.setDuration(animationDuration);
            TransitionManager.beginDelayedTransition(transitionsContainer, autoTransition);
            ConstraintSet constraintsCats = new ConstraintSet();
            constraintsCats.constrainHeight(selectionFrame.getId(), frameH);
            constraintsCats.constrainWidth(selectionFrame.getId(), newW);
            constraintsCats.connect(selectionFrame.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0);
            constraintsCats.connect(selectionFrame.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0);
            constraintsCats.connect(selectionFrame.getId(), ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT, newX);
            constraintsCats.applyTo(transitionsContainer);
        }
    }

    private void treeObserver(){

        final View view = this;
        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {

                view.getViewTreeObserver().removeOnGlobalLayoutListener(this);


                Rect r = new Rect();
                view.getWindowVisibleDisplayFrame(r);

                int rootViewHeight = view.getRootView().getHeight();
                if (selectedItemIdx == -1 && itemNames.size() > 0)
                    DoSelectItem(0, false);



            }
        });
    }
    private void populateCaroucel() {

        this.removeAllViews();
        this.categoriesList.clear();


        selectionFrame = new ConstraintLayout(context);
        selectionFrame.setBackgroundColor(this.frameColor);
        selectionFrame.setId(View.generateViewId());

        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.RECTANGLE);
        shape.setColor(frameColor);
      //  shape.setCornerRadius(this.frameH / 2);
        selectionFrame.setBackground(shape);
        this.addView(selectionFrame);


        this.setBackgroundColor(Color.parseColor("#00FF0000"));
        if (    context == null ||
                itemsCount == 0 ||
                itemNames.size() == 0) {
            return;
        }



        ConstraintLayout prevCat = null;

        int idx = 0;
        for (String strCat: itemNames) {

            ConstraintSet constraintsCats = new ConstraintSet();
            ConstraintLayout newCat = generateCategory(strCat, "category"+idx, idx == itemNames.size() - 1 );
            this.categoriesList.add(newCat);
            selectedCaroucelItem = newCat;
            newCat.setTag(new CategoryTagClass(strCat, idx++));
            this.addView(newCat);

            newCat.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    CategoryTagClass ctc = (CategoryTagClass)v.getTag();

                    DoSelectItem(ctc.index, true);
                }
            });

            constraintsCats.constrainHeight(newCat.getId(), ConstraintSet.WRAP_CONTENT);
            constraintsCats.constrainWidth(newCat.getId(), ConstraintSet.WRAP_CONTENT);
            if(idx == itemNames.size())
                constraintsCats.connect(newCat.getId(), ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT, 40);
            constraintsCats.connect(newCat.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0);
            constraintsCats.connect(newCat.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0);

            if (prevCat == null) {

                constraintsCats.connect(newCat.getId(), ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT, 40);

            }else {

                constraintsCats.connect(newCat.getId(), ConstraintSet.LEFT, prevCat.getId(), ConstraintSet.RIGHT, 10);

            }
            prevCat = newCat;

            constraintsCats.applyTo(this);
        }
//        selectionFrame.bringToFront();
        // Layout button inside constraint
        final Rect categoryBounds = new Rect();
        selectedCaroucelItem.getHitRect(categoryBounds);
        if (categoryBounds.width() > 0 ) {
            ConstraintSet constraintsFrame = new ConstraintSet();
            constraintsFrame.constrainHeight(selectionFrame.getId(), this.frameH);
            constraintsFrame.constrainWidth(selectionFrame.getId(), categoryBounds.width() + 5);
            constraintsFrame.connect(selectionFrame.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0);
            constraintsFrame.connect(selectionFrame.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0);
            constraintsFrame.connect(selectionFrame.getId(), ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT, 40);
            final Handler handler = new Handler();
            final ConstraintLayout thisOut = this;
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    constraintsFrame.applyTo(thisOut);
                }
            }, animationDuration);
        }

        treeObserver();

    }


    private ConstraintLayout generateCategory(String name, String id, boolean isLast) {

        Typeface faceSemiBold = Typeface.createFromAsset( context.getAssets(), "font/Roboto-Regular.ttf");

        TextView txtCategory = new TextView(context);
        txtCategory.setText(name.toUpperCase());
        txtCategory.setTextSize(15);
        txtCategory.setBackgroundColor(Color.parseColor("#00FFFFFF"));
        txtCategory.setTextColor(Color.parseColor("#3B3A3A"));
        txtCategory.setGravity(Gravity.CENTER_VERTICAL);
        txtCategory.setId(View.generateViewId());
        if(faceSemiBold != null){
            txtCategory.setTypeface(faceSemiBold);
        }

        ConstraintLayout layoutCat = new ConstraintLayout(context);
        layoutCat.setBackgroundColor(Color.parseColor("#00FFFFFF"));
        layoutCat.setId(View.generateViewId());
        layoutCat.addView(txtCategory);

        // Layout button inside constraint
        ConstraintSet constraintsBut = new ConstraintSet();
        constraintsBut.connect(txtCategory.getId(), ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT, 25);
        constraintsBut.connect(txtCategory.getId(), ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT, (isLast)? 25 : 25);
        constraintsBut.connect(txtCategory.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0);
        constraintsBut.connect(txtCategory.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0);
        constraintsBut.constrainHeight(txtCategory.getId(), ConstraintSet.WRAP_CONTENT);
        constraintsBut.constrainWidth(txtCategory.getId(), ConstraintSet.WRAP_CONTENT);

        constraintsBut.applyTo(layoutCat);


        return layoutCat;
    }

    private class CategoryTagClass {

        public
        String name;
        int index;

        public CategoryTagClass(String name, int index){
            this.name = name;
            this.index = index;
        }
    }
}
