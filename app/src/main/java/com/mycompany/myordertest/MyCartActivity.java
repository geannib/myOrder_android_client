package com.mycompany.myordertest;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.mycompany.myordertest.adapters.MyCartAdapter;
import com.mycompany.myordertest.adapters.MyStoresAdapter;
import com.mycompany.myordertest.constants.RAppConsts;
import com.mycompany.myordertest.rest.RRestClient;
import com.mycompany.myordertest.rest.RServerRequest;
import com.mycompany.myordertest.rest.response.MySaveCartAPIResponse;
import com.mycompany.myordertest.rest.response.MyStoreAPIResponse;
import com.mycompany.myordertest.utils.MyAppSession;
import com.mycompany.myordertest.utils.MyBuyProduct;
import com.mycompany.myordertest.utils.MyLocalCart;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyCartActivity extends AppCompatActivity implements MyCartAdapter.ItemBuyClickListener {

    private static final String TAG = MyCartActivity.class.getName();

    private RecyclerView recyclerView;
    private MyCartAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_cart);


        recyclerView = findViewById(R.id.listView_cart);
        recyclerView.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.colorGrayBackground, null));

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        final View activityRootView = findViewById(R.id.cart_cl_main);
        MyCartActivity da = this;
        activityRootView.getViewTreeObserver()
                .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {


                        activityRootView.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                        MyLocalCart myCart = MyAppSession.getInstance().getLocalCart();
                        mAdapter = new MyCartAdapter(da, myCart.getAllProducts());
                        mAdapter.setClickListener(da);
                        recyclerView.setAdapter(mAdapter);
                        setRightTools();

                    }
                });
        TextView txtTrimite = findViewById(R.id.cart_txt_trimite);
        txtTrimite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeCartCall();
            }
        });
        updateTotalFields();

    }

    private void updateTotalFields(){

        List<MyBuyProduct> prods = MyAppSession.getInstance().getLocalCart().getAllProducts();

        double totalPrice = 0.0;
        for(MyBuyProduct p:  prods) {
            totalPrice += (p.price * p.quantity);
        }

        TextView total0 = findViewById(R.id.cart_summary_value_total0);
        TextView total0Lbl = findViewById(R.id.cart_summary_label_total0);
        TextView transportValue = findViewById(R.id.cart_summary_value_transport);
        TextView transportLbl = findViewById(R.id.cart_summary_label_transport);
        TextView totalValue = findViewById(R.id.cart_summary_value_total);
        TextView totalLbl = findViewById(R.id.cart_summary_label_total);

        total0.setGravity(Gravity.CENTER_VERTICAL| Gravity.RIGHT);
        transportValue.setGravity(Gravity.CENTER_VERTICAL| Gravity.RIGHT);
        totalValue.setGravity(Gravity.CENTER_VERTICAL| Gravity.RIGHT);

        total0.setText(String.format("%.2f RON", totalPrice));
        transportValue.setText("20 RON");
        totalValue.setText(String.format("%.2f RON", totalPrice + 20));    }

    private void setRightTools(){

//        Window window = this.getWindow();
//
//// clear FLAG_TRANSLUCENT_STATUS flag:
//        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//
//// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
//        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//
//// finally change the color
//        this.setStatusBarColor(ContextCompat.getColor(this, Color.RED));
//
        MyLocalCart myCart = MyAppSession.getInstance().getLocalCart();
        int cartSize = myCart.getCartSize();
        int favSize = MyAppSession.getInstance().getFavList().size();

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ConstraintLayout clMain = new ConstraintLayout(this);
        ConstraintLayout clFrame = new ConstraintLayout(this);
        ImageView imgCart = new ImageView(this);
        ImageView imgFav = new ImageView(this);
        ImageView imgSearch = new ImageView(this);
        TextView txtTitle = new TextView(this);
        TextView txtBadgeFav = new TextView(this);
        TextView txtBadgeCart = new TextView(this);
        clMain.setId(View.generateViewId());
        imgSearch.setId(View.generateViewId());
        imgFav.setId(View.generateViewId());
        imgCart.setId(View.generateViewId());
        clFrame.setId(View.generateViewId());
        txtTitle.setId(View.generateViewId());
        txtBadgeCart.setId(View.generateViewId());
        txtBadgeFav.setId(View.generateViewId());
        imgCart.setImageDrawable( ResourcesCompat.getDrawable(this.getResources(), R.drawable.cart2, null));
        imgFav.setImageDrawable( ResourcesCompat.getDrawable(this.getResources(), R.drawable.heart_favourite, null));
        imgSearch.setImageDrawable( ResourcesCompat.getDrawable(this.getResources(), R.drawable.search, null));
        imgCart.setColorFilter(Color.WHITE);
        imgSearch.setColorFilter(Color.WHITE);
        imgFav.setColorFilter(Color.WHITE);
        clMain.setBackgroundColor(Color.TRANSPARENT);
        txtTitle.setText("Cosul meu");
        txtTitle.setTextSize(20);
        txtTitle.setTypeface(null, Typeface.BOLD);
        txtTitle.setGravity(Gravity.CENTER_VERTICAL|Gravity.LEFT);
        txtTitle.setTextColor(this.getColor( R.color.colorWhite));
        GradientDrawable shapeButtonAdauga =  new GradientDrawable();
        shapeButtonAdauga.setCornerRadius( 18 );
        shapeButtonAdauga.setColor(Color.WHITE);
        txtBadgeFav.setBackground(shapeButtonAdauga);
        txtBadgeFav.setTextColor(Color.BLACK);
        txtBadgeFav.setText("" + favSize);
        txtBadgeFav.setTextSize(8);
        txtBadgeFav.setPadding(0, 0, 0, 0);
        txtBadgeFav.setGravity(Gravity.CENTER);
        txtBadgeFav.setIncludeFontPadding(false);

        txtBadgeCart.setBackground(shapeButtonAdauga);
        txtBadgeCart.setTextColor(Color.BLACK);
        txtBadgeCart.setText("" + cartSize);
//        txtBadgeCart.setAlpha(0);
        txtBadgeCart.setTextSize(8);
        txtBadgeCart.setPadding(0, 0, 0, 0);
        txtBadgeCart.setGravity(Gravity.CENTER);
        txtBadgeCart.setIncludeFontPadding(false);

        clFrame.addView(imgCart);
        clFrame.addView(imgFav);
        clFrame.addView(imgSearch);
        clFrame.addView(txtBadgeCart);
        clFrame.addView(txtBadgeFav);
        clFrame.addView(txtTitle);
        clMain.addView(clFrame);
        // Prod details

        imgCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MyCartActivity.class);
                //   Bundle bundle = new Bundle();
                //  bundle.putSerializable("CATEGORY_SELECTED", selCat);
                //  intent.putExtras(bundle);

                startActivity(intent);
            }
        });
        final View activityRootView = findViewById(R.id.cart_cl_main);
        int mainW =  activityRootView.getWidth();
        int mainH = activityRootView.getHeight();

        // setup image cart
        ConstraintSet constrainImgCart= new ConstraintSet();
        constrainImgCart.connect(imgCart.getId(), ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT, 50);
        constrainImgCart.connect(imgCart.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0);
        constrainImgCart.connect(imgCart.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0);
        constrainImgCart.constrainPercentHeight(imgCart.getId(), (float)0.8);
        constrainImgCart.setDimensionRatio(imgCart.getId(), "1.5:1");

        // setup image favorite
        ConstraintSet constrainImgFav = new ConstraintSet();
        constrainImgFav.connect(imgFav.getId(), ConstraintSet.RIGHT, imgCart.getId(), ConstraintSet.LEFT, 20);
        constrainImgFav.connect(imgFav.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0);
        constrainImgFav.connect(imgFav.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0);
        constrainImgFav.constrainPercentHeight(imgFav.getId(), (float)1.1);
        constrainImgFav.setDimensionRatio(imgFav.getId(), "1:1");

        // setup image search
        ConstraintSet constrainImgSearch = new ConstraintSet();
        constrainImgSearch.connect(imgSearch.getId(), ConstraintSet.RIGHT, imgFav.getId(), ConstraintSet.LEFT, 20);
        constrainImgSearch.connect(imgSearch.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0);
        constrainImgSearch.connect(imgSearch.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0);
        constrainImgSearch.constrainPercentHeight(imgSearch.getId(), (float)0.8);
        constrainImgSearch.setDimensionRatio(imgSearch.getId(), "1:1");

        // setup txt title
        ConstraintSet constrainTxtTitle = new ConstraintSet();
        constrainTxtTitle.connect(txtTitle.getId(), ConstraintSet.RIGHT, imgSearch.getId(), ConstraintSet.LEFT, 20);
        constrainTxtTitle.connect(txtTitle.getId(), ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT, 100);
        constrainTxtTitle.connect(txtTitle.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0);
        constrainTxtTitle.connect(txtTitle.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0);

        // setup txt badge cart
        ConstraintSet constrainTxtBadgeCart = new ConstraintSet();
        constrainTxtBadgeCart.connect(txtBadgeCart.getId(), ConstraintSet.RIGHT, imgCart.getId(), ConstraintSet.RIGHT, 0);
        constrainTxtBadgeCart.connect(txtBadgeCart.getId(), ConstraintSet.TOP, imgCart.getId(), ConstraintSet.TOP, 0);
        constrainTxtBadgeCart.constrainWidth(txtBadgeCart.getId(), 35);
        constrainTxtBadgeCart.constrainHeight(txtBadgeCart.getId(), 35);

        // setup txt badge fav
        ConstraintSet constrainTxtBadgeFav = new ConstraintSet();
        constrainTxtBadgeFav.connect(txtBadgeFav.getId(), ConstraintSet.RIGHT, imgFav.getId(), ConstraintSet.RIGHT, 0);
        constrainTxtBadgeFav.connect(txtBadgeFav.getId(), ConstraintSet.TOP, imgFav.getId(), ConstraintSet.TOP, 0);
        constrainTxtBadgeFav.constrainWidth(txtBadgeFav.getId(), 35);
        constrainTxtBadgeFav.constrainHeight(txtBadgeFav.getId(), 35);



        ConstraintSet constrainFrame = new ConstraintSet();
        constrainFrame.connect(clFrame.getId(), ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT, 0);
        constrainFrame.connect(clFrame.getId(), ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT, 0);
        constrainFrame.connect(clFrame.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0);
        constrainFrame.connect(clFrame.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0);
        constrainFrame.constrainWidth(clFrame.getId(), mainW);
        constrainFrame.constrainHeight(clFrame.getId(), 75);


        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(clMain);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorSelgrosRed, null)));

        constrainImgCart.applyTo(clFrame);
        constrainImgFav.applyTo(clFrame);
        constrainImgSearch.applyTo(clFrame);
        constrainTxtBadgeCart.applyTo(clFrame);
        constrainTxtBadgeFav.applyTo(clFrame);
        constrainTxtTitle.applyTo(clFrame);
        constrainFrame.applyTo(clMain);
    }

    private void makeCartCall()
    {

        String jsonCart = "";
        Gson gson = new Gson();
        List<MyBuyProduct> allProducts = MyAppSession.getInstance().getLocalCart().getAllProducts();
        jsonCart = gson.toJson(allProducts);
        RServerRequest serverReqTranslates = new RServerRequest(RAppConsts.kGetProducts);
        String strTranslates = serverReqTranslates.getRequestString();
        RequestBody bodyTranslates =
                RequestBody.create(MediaType.parse("text/plain"), strTranslates);
        try {

            Map<String, String > newMap= new HashMap<>();
            newMap.put("3", "4");
            Map<String, String > newMap2= new HashMap<>();
            newMap.put("5", "6");


            Call<MySaveCartAPIResponse> cc = new RRestClient()
                    .getApiMethods()
                    .saveCart2( "3",
                            "some notes",
                            MyAppSession.getInstance().getCurrentUser().getUid(),
                            jsonCart );

            MyCartActivity da = this;
            cc.enqueue(new Callback<MySaveCartAPIResponse>() {
                @Override
                public void onFailure(Call<MySaveCartAPIResponse> call, Throwable t) {
                    System.out.println("callback- onFailure");
                    new AlertDialog.Builder(da)
                            .setTitle("Error")
                            .setMessage(t.getLocalizedMessage())

                            // Specifying a listener allows you to take an action before dismissing the dialog.
                            // The dialog is automatically dismissed when a dialog button is clicked.
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Continue with delete operation
                                }
                            })
                            // A null listener allows the button to dismiss the dialog and take no further action.
                            .setNegativeButton(android.R.string.no, null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }

                @Override
                public void onResponse(Call<MySaveCartAPIResponse> call, Response<MySaveCartAPIResponse> response) {


                    try {
                        //String str = "{\"resType\":\"success\",\"resMessage\":\"best message\",\"data\":[{\"Id\":1,\"Name\":\"Selgros\",\"Description\":\"bla\",\"Image\":\"path\",\"Orderfee\":20,\"Deliverytime\":90,\"Minimumorder\":100,\"Idstoretype\":1}]}";
//                        MySaveCartAPIResponse r = gson.fromJson(response.body(), MySaveCartAPIResponse.class);
                        int cartId = response.body().data;
                        new AlertDialog.Builder(da)
                        .setTitle("Success")
                        .setMessage("Cosul a fost trimis la server")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Continue with delete operation
                            }
                        })

                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
//                        mStoresAdapter = new MyStoresAdapter(da, allStores);
//                        mStoresAdapter.setClickListener(da);
//                        rvStores.setAdapter(mStoresAdapter);
//                        rvStores.invalidate();

                    } catch (Exception ex) {

                        Log.d(TAG, "OnResponse fail to convert string to corresponding class");
                        ex.printStackTrace();
                    }

                }
            });

        } catch (com.google.gson.JsonSyntaxException ex){

        }
    }

    @Override
    public void onItemRemoved(View view, int position) {

        MyLocalCart myCart = MyAppSession.getInstance().getLocalCart();
        mAdapter = new MyCartAdapter(this, myCart.getAllProducts());
        mAdapter.setClickListener(this);
        recyclerView.setAdapter(mAdapter);
        updateTotalFields();
    }

    @Override
    public void onItemUpdated(View view, int position) {

      updateTotalFields();
    }
}