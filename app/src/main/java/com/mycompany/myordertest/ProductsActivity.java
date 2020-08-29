package com.mycompany.myordertest;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mycompany.myordertest.adapters.MyProductsAdapter;
import com.mycompany.myordertest.constants.RAppConsts;
import com.mycompany.myordertest.rest.DTO.MyCategoryPOJO;
import com.mycompany.myordertest.rest.DTO.MyProductPOJO;
import com.mycompany.myordertest.rest.RRestClient;
import com.mycompany.myordertest.rest.RServerRequest;
import com.mycompany.myordertest.rest.response.MyProductsAPIResponse;
import com.mycompany.myordertest.utils.MyAppSession;
import com.mycompany.myordertest.utils.MyBounceInterpolator;
import com.mycompany.myordertest.utils.MyLocalCart;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductsActivity extends AppCompatActivity implements MyProductsAdapter.ProductClickListener {

    private static final String TAG = ProductsActivity.class.getName();

    private List<MyProductPOJO> allProducts;
    private GridView gridProducts;
    private MyProductsAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private MyCategoryPOJO mySelCategory;
    private TextView mTxtbadgeFav = null;
    private TextView mTxtBadgeCart = null;

    // create an action bar button
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // R.menu.mymenu is a reference to an xml file named mymenu.xml which should be inside your res/menu directory.
//        // If you don't have res/menu, just create a directory named "menu" inside res
//        getMenuInflater().inflate(R.menu.mymenu, menu);
//        return super.onCreateOptionsMenu(menu);
//    }

    // handle button activities
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.mybutton) {
            // do something here
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);

        Bundle bundle = getIntent().getExtras();
        //Type object = (Type) bundle.getSerializable("KEY");
        mySelCategory = (MyCategoryPOJO) bundle.getSerializable("CATEGORY_SELECTED");
        gridProducts = (GridView) findViewById(R.id.products_gridview);
        final View activityRootView = findViewById(R.id.products_cl_main);

        activityRootView.getViewTreeObserver()
                .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {


                        activityRootView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        Rect visibleRect = new Rect();

                        int mainW =  activityRootView.getWidth();

                        GridView gridView = (GridView)findViewById(R.id.products_gridview);
                        int noCols = 2;
                        int emptySpace = mainW / 10;//gridView.getHorizontalSpacing() * (noCols + 1);
                        gridView.setColumnWidth((mainW - emptySpace) / 2);
                        // gridView.

                        setRightTools();

                    }
                });

        makeGetSubCategories();

        ConstraintLayout clMain = findViewById(R.id.products_cl_main);
        clMain.setBackgroundColor(Color.LTGRAY);
    }


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

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ConstraintLayout clMain = new ConstraintLayout(this);
        ConstraintLayout clFrame = new ConstraintLayout(this);
        ImageView imgCart = new ImageView(this);
        ImageView imgFav = new ImageView(this);
        ImageView imgSearch = new ImageView(this);
        TextView txtTitle = new TextView(this);

        //Global
        mTxtbadgeFav = new TextView(this);
        mTxtBadgeCart = new TextView(this);
        clMain.setId(View.generateViewId());
        imgSearch.setId(View.generateViewId());
        imgFav.setId(View.generateViewId());
        imgCart.setId(View.generateViewId());
        clFrame.setId(View.generateViewId());
        txtTitle.setId(View.generateViewId());
        mTxtBadgeCart.setId(View.generateViewId());
        mTxtbadgeFav.setId(View.generateViewId());
        imgCart.setImageDrawable( ResourcesCompat.getDrawable(this.getResources(), R.drawable.cart2, null));
        imgFav.setImageDrawable( ResourcesCompat.getDrawable(this.getResources(), R.drawable.heart_favourite, null));
        imgSearch.setImageDrawable( ResourcesCompat.getDrawable(this.getResources(), R.drawable.search, null));
        imgCart.setColorFilter(Color.WHITE);
        imgSearch.setColorFilter(Color.WHITE);
        imgFav.setColorFilter(Color.WHITE);
        clMain.setBackgroundColor(Color.TRANSPARENT);
        txtTitle.setText(mySelCategory.label);
        txtTitle.setTextSize(20);
        txtTitle.setTypeface(null, Typeface.BOLD);
        txtTitle.setGravity(Gravity.CENTER_VERTICAL|Gravity.LEFT);
        txtTitle.setTextColor(this.getColor( R.color.colorWhite));
        GradientDrawable shapeButtonAdauga =  new GradientDrawable();
        shapeButtonAdauga.setCornerRadius( 18 );
        shapeButtonAdauga.setColor(Color.WHITE);
        mTxtbadgeFav.setBackground(shapeButtonAdauga);
        mTxtbadgeFav.setTextColor(Color.BLACK);
        mTxtbadgeFav.setText("3");
        mTxtbadgeFav.setTextSize(8);
        mTxtbadgeFav.setPadding(0, 0, 0, 0);
        mTxtbadgeFav.setGravity(Gravity.CENTER);
        mTxtbadgeFav.setIncludeFontPadding(false);

        mTxtBadgeCart.setBackground(shapeButtonAdauga);
        mTxtBadgeCart.setTextColor(Color.BLACK);
        mTxtBadgeCart.setText("" + cartSize);
        mTxtBadgeCart.setVisibility((cartSize > 0 )? View.VISIBLE : View.GONE);
        mTxtBadgeCart.setTextSize(8);
        mTxtBadgeCart.setPadding(0, 0, 0, 0);
        mTxtBadgeCart.setGravity(Gravity.CENTER);
        mTxtBadgeCart.setIncludeFontPadding(false);

        clFrame.addView(imgCart);
        clFrame.addView(imgFav);
        clFrame.addView(imgSearch);
        clFrame.addView(mTxtBadgeCart);
        clFrame.addView(mTxtbadgeFav);
        clFrame.addView(txtTitle);
        clMain.addView(clFrame);
        // Prod details

        final View activityRootView = findViewById(R.id.products_cl_top);
        int mainW =  activityRootView.getWidth();

        imgCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MyCartActivity.class);

                startActivity(intent);
            }
        });
        // setup image cart
        ConstraintSet constrainImgCart= new ConstraintSet();
        constrainImgCart.connect(imgCart.getId(), ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT, 50);
        constrainImgCart.connect(imgCart.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0);
        constrainImgCart.connect(imgCart.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0);
        constrainImgCart.constrainWidth(imgCart.getId(), 85);
        constrainImgCart.constrainHeight(imgCart.getId(), 75);

        // setup image favorite
        ConstraintSet constrainImgFav = new ConstraintSet();
        constrainImgFav.connect(imgFav.getId(), ConstraintSet.RIGHT, imgCart.getId(), ConstraintSet.LEFT, 20);
        constrainImgFav.connect(imgFav.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0);
        constrainImgFav.connect(imgFav.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0);
        constrainImgFav.constrainWidth(imgFav.getId(), 75);
        constrainImgFav.constrainHeight(imgFav.getId(), 75);

        // setup image search
        ConstraintSet constrainImgSearch = new ConstraintSet();
        constrainImgSearch.connect(imgSearch.getId(), ConstraintSet.RIGHT, imgFav.getId(), ConstraintSet.LEFT, 20);
        constrainImgSearch.connect(imgSearch.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0);
        constrainImgSearch.connect(imgSearch.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0);
        constrainImgSearch.constrainWidth(imgSearch.getId(), 75);
        constrainImgSearch.constrainHeight(imgSearch.getId(), 75);

        // setup txt title
        ConstraintSet constrainTxtTitle = new ConstraintSet();
        constrainTxtTitle.connect(txtTitle.getId(), ConstraintSet.RIGHT, imgSearch.getId(), ConstraintSet.LEFT, 20);
        constrainTxtTitle.connect(txtTitle.getId(), ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT, 100);
        constrainTxtTitle.connect(txtTitle.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0);
        constrainTxtTitle.connect(txtTitle.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0);

        // setup txt badge cart
        ConstraintSet constrainTxtBadgeCart = new ConstraintSet();
        constrainTxtBadgeCart.connect(mTxtBadgeCart.getId(), ConstraintSet.RIGHT, imgCart.getId(), ConstraintSet.RIGHT, 0);
        constrainTxtBadgeCart.connect(mTxtBadgeCart.getId(), ConstraintSet.TOP, imgCart.getId(), ConstraintSet.TOP, 0);
        constrainTxtBadgeCart.constrainWidth(mTxtBadgeCart.getId(), 35);
        constrainTxtBadgeCart.constrainHeight(mTxtBadgeCart.getId(), 35);

        // setup txt badge fav
        ConstraintSet constrainTxtBadgeFav = new ConstraintSet();
        constrainTxtBadgeFav.connect(mTxtbadgeFav.getId(), ConstraintSet.RIGHT, imgFav.getId(), ConstraintSet.RIGHT, 0);
        constrainTxtBadgeFav.connect(mTxtbadgeFav.getId(), ConstraintSet.TOP, imgFav.getId(), ConstraintSet.TOP, 0);
        constrainTxtBadgeFav.constrainWidth(mTxtbadgeFav.getId(), 35);
        constrainTxtBadgeFav.constrainHeight(mTxtbadgeFav.getId(), 35);



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
    View.OnClickListener itemClicked = new View.OnClickListener(){
        @Override
        public void onClick(View v) {



        }
    };
    private void populateCaroucel() {

        if (true){


            mAdapter = new MyProductsAdapter(this, allProducts);
            mAdapter.setProdClickListener(this);
            GridView gridView = (GridView)findViewById(R.id.products_gridview);
            gridView.setBackgroundColor(ResourcesCompat.getColor(this.getResources(), R.color.colorGrayBackground, null));

            gridView.setAdapter(mAdapter);

            return;

        }


    }


    public void setTitle(String title){
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TextView textView = new TextView(this);
        textView.setText(title);
        textView.setTextSize(20);
        textView.setTypeface(null, Typeface.BOLD);
        textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(this.getColor( R.color.colorWhite));
        textView.setBackgroundColor(Color.GREEN);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(textView);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorAccent, null)));

    }

    private void makeGetSubCategories(){
        RServerRequest serverReqTranslates = new RServerRequest(RAppConsts.kGetProducts);

        try {


            Call<MyProductsAPIResponse> cc = new RRestClient().
                    getApiMethods().
                    getProducts(
                            mySelCategory.id,
                            MyAppSession.getInstance().getCurrentUser().getUid());

            ProductsActivity da = this;
            cc.enqueue(new Callback<MyProductsAPIResponse>() {
                @Override
                public void onFailure(Call<MyProductsAPIResponse> call, Throwable t) {
                    System.out.println("callback- onFailure");
                }

                @Override
                public void onResponse(Call<MyProductsAPIResponse> call, Response<MyProductsAPIResponse> response) {

                    allProducts = response.body().data;
                    if(allProducts.size() == 0){
                        TextView txtNoProds = findViewById(R.id.products_txt_no_prods);
                        txtNoProds.setVisibility(View.VISIBLE);
                        txtNoProds.setText("There are no products for this category");
                        GridView grdView = findViewById(R.id.products_gridview);
                        grdView.setVisibility(View.GONE);
                    }else {
                        TextView txtNoProds = findViewById(R.id.products_txt_no_prods);
                        txtNoProds.setVisibility(View.GONE);
                        GridView grdView = findViewById(R.id.products_gridview);
                        grdView.setVisibility(View.VISIBLE);
                        populateCaroucel();
                    }


                }
            });

        } catch (com.google.gson.JsonSyntaxException ex) {

        }
    }


    @Override
    public void onBuyProduct(MyProductPOJO selProduct) {

        Log.d(TAG, "Product buy was click: " + selProduct.id);

        MyAppSession.getInstance().getLocalCart().addProduct(selProduct, 1);
        MyAppSession.getInstance().saveLocalCart();
        MyLocalCart myCart = MyAppSession.getInstance().getLocalCart();
        int cartSize = myCart.getCartSize();
        mTxtBadgeCart.setText("" + cartSize);

        final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.bounce);
        // Use bounce interpolator with amplitude 0.1 and frequency 15
        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.1, 5);
       // myAnim.setInterpolator(interpolator);
        mTxtBadgeCart.startAnimation(myAnim);

//        new AlertDialog.Builder(this)
//                .setTitle("Great, produsul a fost adaugat in cos :)")
//                .setMessage(selProduct.name + " " + selProduct.id)
//
//                // Specifying a listener allows you to take an action before dismissing the dialog.
//                // The dialog is automatically dismissed when a dialog button is clicked.
//                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        // Continue with delete operation
//                    }
//                })
//
//                // A null listener allows the button to dismiss the dialog and take no further action.
//                .setNegativeButton(android.R.string.no, null)
//                .setIcon(android.R.drawable.ic_dialog_alert)
//                .show();
    }

    @Override
    public void onFavouriteClicked(MyProductPOJO selProd) {

        Log.d(TAG, "Product favourite was click: " + selProd.id);

        int favSize = MyAppSession.getInstance().getFavList().size();
        mTxtbadgeFav.setText("" + favSize);

//        mAdapter = new MyProductsAdapter(this, allSubcats);
//        mAdapter.setClickListener(this);
//        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onItemClick(View view, MyProductPOJO selectedProd) {
        Log.d(TAG, "Item from products list was clicked at position: " + selectedProd.id);
        Intent productIntent = new Intent(getApplicationContext(), ProductActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("PRODUCT_SELECTED", selectedProd);
        productIntent.putExtras(bundle);
        startActivity ( productIntent );
//
//        Toast toast = Toast.makeText(this.getApplicationContext(), position, Toast.LENGTH_LONG);
//        toast.show();
    }
}