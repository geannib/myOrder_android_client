package com.mycompany.myordertest;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toolbar;

import com.mycompany.myordertest.adapters.MyCategoriesAdapter;
import com.mycompany.myordertest.constants.RAppConsts;
import com.mycompany.myordertest.rest.DTO.MyCategoryPOJO;
import com.mycompany.myordertest.rest.DTO.MyStoresPOJO;
import com.mycompany.myordertest.rest.RRestClient;
import com.mycompany.myordertest.rest.RServerRequest;
import com.mycompany.myordertest.rest.response.MyCategoriesAPIResponse;
import com.google.gson.Gson;
import com.mycompany.myordertest.utils.MyAppSession;
import com.mycompany.myordertest.utils.MyLocalCart;

import org.w3c.dom.Text;

import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoriesActivity extends AppCompatActivity {

    private static final String TAG = CategoriesActivity.class.getName();

    private List<MyCategoryPOJO> allCategories = null;
    private MyStoresPOJO mySelStore;

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
        setContentView(R.layout.activity_categories);


        Bundle bundle = getIntent().getExtras();
        //Type object = (Type) bundle.getSerializable("KEY");
         mySelStore = (MyStoresPOJO) bundle.getSerializable("STORE_SELECTED");
      //  setTitle(mySelStore.name);
       // setRightTools();
        GridView gridView = (GridView)findViewById(R.id.gridview);
        gridView.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.colorGrayBackground, null));



       // verified_btn.setOnClickListener(this);



        final MyCategoriesAdapter booksAdapter = new MyCategoriesAdapter(this, allCategories);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                MyCategoryPOJO book = allCategories.get(position);
               // book.toggleFavorite();

                // This tells the GridView to redraw itself
                // in turn calling your BooksAdapter's getView method again for each cell
                booksAdapter.notifyDataSetChanged();
            }
        });


        final View activityRootView = findViewById(R.id.categories_cl_main);
        activityRootView.getViewTreeObserver()
                .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                         @Override
                         public void onGlobalLayout() {


                             activityRootView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                             Rect visibleRect = new Rect();

                             int mainW =  activityRootView.getWidth();

                             GridView gridView = (GridView)findViewById(R.id.gridview);
                             int noCols = 2;
                             int emptySpace = mainW / 10;
                             gridView.setColumnWidth((mainW - emptySpace) / 3);
                            // gridView.

                             setRightTools();

                         }
                     });

        makeGetCategoriesCall();
    }



    private void setRightTools(){


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
        txtTitle.setText(mySelStore.name);
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

                startActivity(intent);
            }
        });
        final View activityRootView = findViewById(R.id.categories_cl_main);
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
    public void setTitle(String title){
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TextView textView = new TextView(this);
        textView.setText(title);
        textView.setTextSize(20);
        textView.setTypeface(null, Typeface.BOLD);
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(this.getColor( R.color.colorWhite));
        textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(textView);
    }

    private void makeGetCategoriesCall() {

        RServerRequest serverReqTranslates = new RServerRequest(RAppConsts.kGetCategories);
        String strGetCategories = "id=1"; // serverReqTranslates.getRequestString();
        RequestBody bodyTranslates =
                RequestBody.create(MediaType.parse("text/plain"), strGetCategories);
        try {

            Bundle bundle = getIntent().getExtras();
            //Type object = (Type) bundle.getSerializable("KEY");
            MyCategoryPOJO mySelCat = (MyCategoryPOJO) bundle.getSerializable("CATEGORY_SELECTED");

            Call<MyCategoriesAPIResponse> cc = null;
            if (mySelCat == null){
                cc = new RRestClient().
                        getApiMethods().
                        getstorecategories(
                                "" + mySelStore.id,
                                MyAppSession.getInstance().getCurrentUser().getUid()
                                );
            }else{
                cc = new RRestClient().
                        getApiMethods().
                        getstorecategories2(
                                "" + mySelStore.id,
                                mySelCat.id,
                                MyAppSession.getInstance().getCurrentUser().getUid()
                                );
            }


            CategoriesActivity da = this;
            cc.enqueue(new Callback<MyCategoriesAPIResponse>() {
                @Override
                public void onFailure(Call<MyCategoriesAPIResponse> call, Throwable t) {
                    System.out.println("callback- onFailure");
                }

                @Override
                public void onResponse(Call<MyCategoriesAPIResponse> call, Response<MyCategoriesAPIResponse> response) {

                    Gson gson = new Gson();
                    try {
                       allCategories = response.body().data;

                        GridView gridView = (GridView)findViewById(R.id.gridview);

                        MyCategoriesAdapter categoryAdapter = new MyCategoriesAdapter(da, allCategories);
                        gridView.setAdapter(categoryAdapter);

                        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView parent, View view, int position, long id) {
                                MyCategoryPOJO category = allCategories.get(position);
                                Log.d(TAG, "Category was clicked at position: " + position);
                                MyCategoryPOJO selCat = allCategories.get(position);

                                if (selCat.subcategories != null && selCat.subcategories.compareTo("0") == 0) {
                                    Intent intent = new Intent(getApplicationContext(), ProductsActivity.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("CATEGORY_SELECTED", selCat);
                                    intent.putExtras(bundle);

                                    startActivity(intent);
                                }else {

                                    Intent intent = new Intent(getApplicationContext(), CategoriesActivity.class);

                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("STORE_SELECTED", mySelStore);
                                    bundle.putSerializable("CATEGORY_SELECTED", selCat);
                                    intent.putExtras(bundle);

                                    startActivity ( intent );
                                }

                            }
                        });


                    } catch (Exception ex) {

                        Log.d(TAG, "OnResponse fail to convert string to corresponding class");
                        ex.printStackTrace();
                    }

                }
            });

        } catch (com.google.gson.JsonSyntaxException ex) {

        }
    }
}