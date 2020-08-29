package com.mycompany.myordertest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.res.ResourcesCompat;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mycompany.myordertest.rest.DTO.MyProductPOJO;
import com.mycompany.myordertest.rest.DTO.MyStoresPOJO;

public class ProductActivity extends AppCompatActivity {

    private static final String TAG = ProductsActivity.class.getName();

    MyProductPOJO selectedProd;
    private int quantity = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        Bundle bundle = getIntent().getExtras();
        selectedProd = (MyProductPOJO) bundle.getSerializable("PRODUCT_SELECTED");

        doLayout();
    }

    private void doLayout(){

        ConstraintLayout clMain = findViewById(R.id.cl_product_main);
        ConstraintLayout clProdDetails = new ConstraintLayout(this);
        clProdDetails.setId(View.generateViewId());
        ConstraintLayout clProdRecenzii = new ConstraintLayout(this);
        clProdRecenzii.setId(View.generateViewId());
        ConstraintLayout clBottom = new ConstraintLayout(this);
        clBottom.setId(View.generateViewId());

        // top
        ConstraintLayout clTopTop = new ConstraintLayout(this);
        clTopTop.setId(View.generateViewId());
        ConstraintLayout clTopMiddle = new ConstraintLayout(this);
        clTopMiddle.setId(View.generateViewId());
        ConstraintLayout clTopBottom = new ConstraintLayout(this);
        clTopBottom.setId(View.generateViewId());
        TextView txtProdName = new TextView(this);
        txtProdName.setId(View.generateViewId());
        clTopBottom.setId(View.generateViewId());
        TextView txtAdaugaInstructiuni = new TextView(this);
        txtAdaugaInstructiuni.setId(View.generateViewId());
        TextView txtProdPrice = new TextView(this);
        txtProdPrice.setId(View.generateViewId());
        ImageView imgInstructiuni = new ImageView(this);
        imgInstructiuni.setId(View.generateViewId());
        ConstraintLayout clTopBottomInstructiuni = new ConstraintLayout(this);
        clTopBottomInstructiuni.setId(View.generateViewId());
        txtProdName.setText(selectedProd.name);
        txtProdPrice.setText("" + selectedProd.price + "lei");
        txtProdName.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
        txtProdPrice.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
        txtProdPrice.setTextColor(Color.RED);
        txtAdaugaInstructiuni.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_HORIZONTAL);
        txtAdaugaInstructiuni.setText("Instructiuni pentru alegerea produsului");
        imgInstructiuni.setImageDrawable( ResourcesCompat.getDrawable(this.getResources(), R.drawable.instructiuni, null));




        // bottom
        ConstraintLayout clBottomLeft = new ConstraintLayout(this);
        clBottomLeft.setId(View.generateViewId());
        ConstraintLayout clBottomRight = new ConstraintLayout(this);
        clBottomRight.setId(View.generateViewId());
        TextView txtBottomMinus = new TextView(this);
        txtBottomMinus.setId(View.generateViewId());
        TextView txtBottomCount = new TextView(this);
        txtBottomCount.setId(View.generateViewId());
        TextView txtBottomPlus = new TextView(this);
        txtBottomPlus.setId(View.generateViewId());
        TextView txtBottomAdauga = new TextView(this);
        txtBottomAdauga.setId(View.generateViewId());
        txtBottomMinus.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        txtBottomPlus.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        txtBottomCount.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        txtBottomAdauga.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        txtBottomCount.setText("1");
        txtBottomMinus.setText("-");
        txtBottomPlus.setText("+");
        txtBottomAdauga.setText("Adauga in cos");
        txtBottomAdauga.setTextColor(Color.WHITE);
        txtBottomAdauga.setBackgroundColor(Color.LTGRAY);

        GradientDrawable shape =  new GradientDrawable();
        shape.setCornerRadius( 18 );
        shape.setStroke(2, Color.BLACK);
        shape.setColor(Color.WHITE);
        clBottomLeft.setBackground(shape);

        GradientDrawable shapeButtonAdauga =  new GradientDrawable();
        shapeButtonAdauga.setCornerRadius( 18 );
        shapeButtonAdauga.setColor(Color.LTGRAY);
        txtBottomAdauga.setBackground(shapeButtonAdauga);

        GradientDrawable shapeBottomInstructiuni =  new GradientDrawable();
        shapeBottomInstructiuni.setCornerRadius( 18 );
        shapeBottomInstructiuni.setColor(Color.WHITE);
        clTopBottomInstructiuni.setBackground(shapeBottomInstructiuni);
        clTopBottomInstructiuni.setElevation(15);

        Typeface faceBold = Typeface.createFromAsset( this.getAssets(), "font/Roboto-Bold.ttf");
        Typeface faceRegular = Typeface.createFromAsset( this.getAssets(), "font/Roboto-Regular.ttf");
        if(faceBold != null && faceRegular != null){
            txtBottomAdauga.setTypeface(faceBold);
            txtBottomCount.setTypeface(faceBold);
            txtBottomMinus.setTypeface(faceBold);
            txtBottomPlus.setTypeface(faceBold);
            txtProdName.setTypeface(faceRegular);
            txtProdPrice.setTypeface(faceRegular);
            txtAdaugaInstructiuni.setTypeface(faceBold);
        }

        txtBottomAdauga.setTextSize(20);
        txtBottomCount.setTextSize(20);
        txtBottomMinus.setTextSize(20);
        txtBottomPlus.setTextSize(20);
        txtAdaugaInstructiuni.setTextSize(20);


        txtBottomMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quantity = quantity - 1;
                txtBottomCount.setText("" + quantity);
                Log.d(TAG, "D: Minus was clicked: " + quantity);
            }
        });

        txtBottomPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quantity = quantity + 1;
                txtBottomCount.setText("" + quantity);
                Log.d(TAG, "D: Plus was clicked: " + quantity);
            }
        });

        txtBottomAdauga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "D: Adauga was clicked: " + quantity);
            }
        });



        clMain.addView(clProdDetails);
        clMain.addView(clProdRecenzii);
        clMain.addView(clBottom);
        clBottom.addView(clBottomLeft);
        clBottom.addView(clBottomRight);
        clBottomLeft.addView(txtBottomPlus);
        clBottomLeft.addView(txtBottomMinus);
        clBottomLeft.addView(txtBottomCount);
        clBottomRight.addView(txtBottomAdauga);
        clProdDetails.addView(clTopTop);
        clProdDetails.addView(clTopMiddle);
        clProdDetails.addView(clTopBottom);
        clTopBottom.addView(clTopBottomInstructiuni);
        clTopMiddle.addView(txtProdName);
        clTopMiddle.addView(txtProdPrice);
        clTopBottomInstructiuni.addView(imgInstructiuni);
        clTopBottomInstructiuni.addView(txtAdaugaInstructiuni);

//        clMain.setBackgroundColor(Color.parseColor("#123456"));
//        clProdDetails.setBackgroundColor(Color.parseColor("#FF0000"));
//        clProdRecenzii.setBackgroundColor(Color.parseColor("#00FF00"));
//        clBottom.setBackgroundColor(Color.YELLOW);
//        clBottomLeft.setBackgroundColor(Color.MAGENTA);
//        clBottomRight.setBackgroundColor(Color.CYAN);
//        txtBottomMinus.setBackgroundColor(Color.RED);
//        txtBottomCount.setBackgroundColor(Color.GREEN);
//        txtBottomPlus.setBackgroundColor(Color.BLUE);
//        clTopTop.setBackgroundColor(Color.RED);
//        clTopMiddle.setBackgroundColor(Color.GREEN);
//        clTopBottom.setBackgroundColor(Color.BLUE);

        clMain.setBackgroundColor(Color.WHITE);
        clProdDetails.setBackgroundColor(Color.WHITE);
        clProdRecenzii.setBackgroundColor(Color.WHITE);
        clBottom.setBackgroundColor(Color.WHITE);
        clBottomRight.setBackgroundColor(Color.WHITE);
        txtBottomMinus.setBackgroundColor(Color.TRANSPARENT);
        txtBottomCount.setBackgroundColor(Color.TRANSPARENT);
        clTopTop.setBackgroundColor(Color.TRANSPARENT);
        clTopMiddle.setBackgroundColor(Color.TRANSPARENT);
        clTopBottom.setBackgroundColor(Color.TRANSPARENT);

        // Prod details
        ConstraintSet constrainProdDetails= new ConstraintSet();
        constrainProdDetails.connect(clProdDetails.getId(), ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT, 0);
        constrainProdDetails.connect(clProdDetails.getId(), ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT, 0);
        constrainProdDetails.connect(clProdDetails.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0);
        constrainProdDetails.constrainPercentHeight(clProdDetails.getId(), (float) 0.43);

        // Prod recenzii
        ConstraintSet constrainProdRecenzii = new ConstraintSet();
        constrainProdRecenzii.connect(clProdRecenzii.getId(), ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT, 0);
        constrainProdRecenzii.connect(clProdRecenzii.getId(), ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT, 0);
        constrainProdRecenzii.connect(clProdRecenzii.getId(), ConstraintSet.TOP, clProdDetails.getId(), ConstraintSet.BOTTOM, 0);
        constrainProdRecenzii.connect(clProdRecenzii.getId(), ConstraintSet.BOTTOM, clBottom.getId(), ConstraintSet.BOTTOM, 0);

        // Prod Botoom
        ConstraintSet constrainProdBottom= new ConstraintSet();
        constrainProdBottom.connect(clBottom.getId(), ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT, 0);
        constrainProdBottom.connect(clBottom.getId(), ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT, 0);
        constrainProdBottom.connect(clBottom.getId(), ConstraintSet.BOTTOM,  ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0);
        constrainProdBottom.constrainPercentHeight(clBottom.getId(), (float) 0.1);

        // Prod details - top: image
        ConstraintSet constrainTopTop = new ConstraintSet();
        constrainTopTop.connect(clTopTop.getId(), ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT, 0);
        constrainTopTop.connect(clTopTop.getId(), ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT, 0);
        constrainTopTop.connect(clTopTop.getId(), ConstraintSet.TOP,  ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0);
        constrainTopTop.constrainPercentHeight(clTopTop.getId(), (float) 0.5);

        //Prod details - middle: name and price
        ConstraintSet constrainTopMiddle = new ConstraintSet();
        constrainTopMiddle.connect(clTopMiddle.getId(), ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT, 0);
        constrainTopMiddle.connect(clTopMiddle.getId(), ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT, 0);
        constrainTopMiddle.connect(clTopMiddle.getId(), ConstraintSet.TOP,  clTopTop.getId(), ConstraintSet.BOTTOM, 0);
        constrainTopMiddle.constrainPercentHeight(clTopMiddle.getId(), (float) 0.25);

        // Prod details - middle: txt name
        ConstraintSet constrainTopMiddleTxtName = new ConstraintSet();
        constrainTopMiddleTxtName.connect(txtProdName.getId(), ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT, 0);
        constrainTopMiddleTxtName.connect(txtProdName.getId(), ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT, 0);
        constrainTopMiddleTxtName.connect(txtProdName.getId(), ConstraintSet.TOP,  ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0);
        constrainTopMiddleTxtName.constrainPercentWidth(txtProdName.getId(), (float) 0.9);
        constrainTopMiddleTxtName.constrainPercentHeight(txtProdName.getId(), (float) 0.5);

        // Prod details - middle: txt price
        ConstraintSet constrainTopMiddleTxtPrice = new ConstraintSet();
        constrainTopMiddleTxtPrice.connect(txtProdPrice.getId(), ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT, 0);
        constrainTopMiddleTxtPrice.connect(txtProdPrice.getId(), ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT, 0);
        constrainTopMiddleTxtPrice.connect(txtProdPrice.getId(), ConstraintSet.BOTTOM,  ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0);
        constrainTopMiddleTxtPrice.constrainPercentWidth(txtProdPrice.getId(), (float) 0.9);
        constrainTopMiddleTxtPrice.constrainPercentHeight(txtProdPrice.getId(), (float) 0.5);

        //Prod details bottom: instructiuni
        ConstraintSet constrainTopBottom = new ConstraintSet();
        constrainTopBottom.connect(clTopBottom.getId(), ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT, 0);
        constrainTopBottom.connect(clTopBottom.getId(), ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT, 0);
        constrainTopBottom.connect(clTopBottom.getId(), ConstraintSet.TOP,  clTopMiddle.getId(), ConstraintSet.BOTTOM, 0);
        constrainTopBottom.constrainPercentHeight(clTopBottom.getId(), (float) 0.25);

        // Prod details bottom: button instructiuni - image + label
        ConstraintSet constrainTopBottomInstructiuni = new ConstraintSet();
        constrainTopBottomInstructiuni.connect(clTopBottomInstructiuni.getId(), ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT, 0);
        constrainTopBottomInstructiuni.connect(clTopBottomInstructiuni.getId(), ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT, 0);
        constrainTopBottomInstructiuni.connect(clTopBottomInstructiuni.getId(), ConstraintSet.TOP,  ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0);
        constrainTopBottomInstructiuni.connect(clTopBottomInstructiuni.getId(), ConstraintSet.BOTTOM,  ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0);
        constrainTopBottomInstructiuni.constrainPercentHeight(clTopBottomInstructiuni.getId(), (float) 0.8);
        constrainTopBottomInstructiuni.constrainPercentWidth(clTopBottomInstructiuni.getId(), (float) 0.9);

        //Prod details bottom: instructiuni - txt instructiuni
        ConstraintSet constrainTopBottomInstructiuniTxt = new ConstraintSet();
        constrainTopBottomInstructiuniTxt.connect(txtAdaugaInstructiuni.getId(), ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT, 0);
        constrainTopBottomInstructiuniTxt.connect(txtAdaugaInstructiuni.getId(), ConstraintSet.TOP,  ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0);
        constrainTopBottomInstructiuniTxt.connect(txtAdaugaInstructiuni.getId(), ConstraintSet.BOTTOM,  ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0);
        constrainTopBottomInstructiuniTxt.constrainPercentWidth(txtAdaugaInstructiuni.getId(), (float) 0.72);

        //Prod details bottom: instructiuni - img instructiuni
        ConstraintSet constrainTopBottomInstructiuniImg = new ConstraintSet();
        constrainTopBottomInstructiuniImg.connect(imgInstructiuni.getId(), ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT, 0);
        constrainTopBottomInstructiuniImg.connect(imgInstructiuni.getId(), ConstraintSet.TOP,  ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0);
        constrainTopBottomInstructiuniImg.connect(imgInstructiuni.getId(), ConstraintSet.BOTTOM,  ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0);
        constrainTopBottomInstructiuniImg.constrainPercentWidth(imgInstructiuni.getId(), (float) 0.25);

        // Prod bottom left
        ConstraintSet constrainProdBottomLeft= new ConstraintSet();
        constrainProdBottomLeft.connect(clBottomLeft.getId(), ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT, 20);
        constrainProdBottomLeft.connect(clBottomLeft.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0);
        constrainProdBottomLeft.connect(clBottomLeft.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0);
        constrainProdBottomLeft.constrainPercentWidth(clBottomLeft.getId(), (float) 0.45);
        constrainProdBottomLeft.constrainPercentHeight(clBottomLeft.getId(), (float) 0.9);

        // Prod bottom right
        ConstraintSet constrainProdBottomRight= new ConstraintSet();
        constrainProdBottomRight.connect(clBottomRight.getId(), ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT, 20);
        constrainProdBottomRight.connect(clBottomRight.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0);
        constrainProdBottomRight.connect(clBottomRight.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0);
        constrainProdBottomRight.constrainPercentWidth(clBottomRight.getId(), (float) 0.45);
        constrainProdBottomRight.constrainPercentHeight(clBottomRight.getId(), (float) 0.9);

        // Prod bottom left - img minus
        ConstraintSet constrainProdBottomLeftImgMinus= new ConstraintSet();
        constrainProdBottomLeftImgMinus.connect(txtBottomMinus.getId(), ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT, 7);
        constrainProdBottomLeftImgMinus.connect(txtBottomMinus.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 7);
        constrainProdBottomLeftImgMinus.connect(txtBottomMinus.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 7);
        constrainProdBottomLeftImgMinus.constrainPercentWidth(txtBottomMinus.getId(), (float) 0.33);

        // Prod bottom left - img plus
        ConstraintSet constrainProdBottomLeftImgPlus= new ConstraintSet();
        constrainProdBottomLeftImgPlus.connect(txtBottomPlus.getId(), ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT, 7);
        constrainProdBottomLeftImgPlus.connect(txtBottomPlus.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 7);
        constrainProdBottomLeftImgPlus.connect(txtBottomPlus.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 7);
        constrainProdBottomLeftImgPlus.constrainPercentWidth(txtBottomPlus.getId(), (float) 0.33);

        // Prod bottom left - txt count
        ConstraintSet constrainProdBottomLeftImgCount = new ConstraintSet();
        constrainProdBottomLeftImgCount.connect(txtBottomCount.getId(), ConstraintSet.RIGHT, txtBottomPlus.getId(), ConstraintSet.LEFT, 0);
        constrainProdBottomLeftImgCount.connect(txtBottomCount.getId(), ConstraintSet.LEFT, txtBottomMinus.getId(), ConstraintSet.RIGHT, 0);
        constrainProdBottomLeftImgCount.connect(txtBottomCount.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 7);
        constrainProdBottomLeftImgCount.connect(txtBottomCount.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 7);

        // Prod bottom Right - txt comanda
        ConstraintSet constrainProdBottomRightComanda = new ConstraintSet();
        constrainProdBottomRightComanda.connect(txtBottomAdauga.getId(), ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT, 0);
        constrainProdBottomRightComanda.connect(txtBottomAdauga.getId(), ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT, 0);
        constrainProdBottomRightComanda.connect(txtBottomAdauga.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0);
        constrainProdBottomRightComanda.connect(txtBottomAdauga.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0);
        constrainProdBottomRightComanda.constrainPercentHeight(txtBottomAdauga.getId(), (float) 0.9);
        constrainProdBottomRightComanda.constrainPercentWidth(txtBottomAdauga.getId(), (float) 0.9);


        constrainProdDetails.applyTo(clMain);
        constrainProdRecenzii.applyTo(clMain);
        constrainProdBottom.applyTo(clMain);
        constrainProdBottomLeft.applyTo(clBottom);
        constrainProdBottomRight.applyTo(clBottom);
        constrainProdBottomLeftImgMinus.applyTo(clBottomLeft);
        constrainProdBottomLeftImgPlus.applyTo(clBottomLeft);
        constrainProdBottomLeftImgCount.applyTo(clBottomLeft);
        constrainProdBottomRightComanda.applyTo(clBottomRight);
        constrainTopTop.applyTo(clProdDetails);
        constrainTopMiddle.applyTo(clProdDetails);
        constrainTopBottom.applyTo(clProdDetails);
        constrainTopBottomInstructiuni.applyTo(clTopBottom);
        constrainTopMiddleTxtName.applyTo(clTopMiddle);
        constrainTopMiddleTxtPrice.applyTo(clTopMiddle);
        constrainTopBottomInstructiuniImg.applyTo(clTopBottomInstructiuni);
        constrainTopBottomInstructiuniTxt.applyTo(clTopBottomInstructiuni);

    }
    @Override
    protected void onResume() {
        super.onResume();

        TextView demo = findViewById(R.id.product_txt_demo);
        demo.setText("Aici se va cumpara: " + selectedProd.name);
    }
}