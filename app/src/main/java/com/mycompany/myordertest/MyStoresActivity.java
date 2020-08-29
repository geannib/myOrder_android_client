package com.mycompany.myordertest;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Menu;
import android.view.ViewTreeObserver;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.mycompany.myordertest.adapters.MyAddressesAdapter;
import com.mycompany.myordertest.adapters.MyStoresAdapter;
import com.mycompany.myordertest.constants.RAppConsts;
import com.mycompany.myordertest.rest.DTO.MyAddressPOJO;
import com.mycompany.myordertest.rest.DTO.MyStoresPOJO;
import com.mycompany.myordertest.rest.RRestClient;
import com.mycompany.myordertest.rest.RServerRequest;
import com.mycompany.myordertest.rest.response.MyAddressesAPIResponse;
import com.mycompany.myordertest.rest.response.MyPushTokenAPIResponse;
import com.mycompany.myordertest.rest.response.MyStoreAPIResponse;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.mycompany.myordertest.utils.MyAppSession;
import com.mycompany.myordertest.utils.MyLocalCart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyStoresActivity extends AppCompatActivity implements MyStoresAdapter.ItemClickListener {

    private static final String TAG = MyStoresActivity.class.getName();

    private AppBarConfiguration mAppBarConfiguration;
    private RecyclerView rvStores;
    private RecyclerView rvAddresses;
    private MyStoresAdapter mStoresAdapter;
    private MyAddressesAdapter myAddressesAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<MyStoresPOJO> allStores = null;
    private List<MyAddressPOJO> allAddresses = null;

// ...
// Initialize Firebase Auth

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        Toolbar toolbar = findViewById(R.id.toolbar);
        MyAppSession.getInstance().init(this);

        // Initialize Firebase Auth

        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        rvStores = (RecyclerView) findViewById(R.id.listView_stores);
        rvStores.setBackgroundColor(getResources().getColor(R.color.colorGrayBackground));

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        rvStores.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        rvStores.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)

        final View activityRootView = findViewById(R.id.drawer_layout);
        activityRootView.getViewTreeObserver()
                .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        activityRootView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        setRightTools();

                    }
                });


        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();
                        makePushCall(token);
                        // Log and toast
                       // String msg = getString(R.string.msg_token_fmt, token);
                        Log.d(TAG, token);
                        Toast.makeText(MyStoresActivity.this, token, Toast.LENGTH_SHORT).show();
                    }
                });
        makeStoresCall();
        makeAddressesCall();
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.drawer, menu);
//        return true;
//    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }



    private void makeAddressesCall() {
        RServerRequest serverReqAddresses = new RServerRequest(RAppConsts.kGetAddresses);
        String strTranslates = serverReqAddresses.getRequestString();
        RequestBody bodyTranslates =
                RequestBody.create(MediaType.parse("text/plain"), strTranslates);
        try {


            Call<MyAddressesAPIResponse> cc = new RRestClient()
                    .getApiMethods()
                    .getAddresses(
                            //"L7wZvLHT89f0SR2LMpzIqTn4Ka13");
                            MyAppSession.getInstance().getCurrentUser().getUid());

            MyStoresActivity da = this;
            cc.enqueue(new Callback<MyAddressesAPIResponse>() {
                @Override
                public void onFailure(Call<MyAddressesAPIResponse> call, Throwable t) {
                    System.out.println("callback- onFailure");
                }

                @Override
                public void onResponse(Call<MyAddressesAPIResponse> call, Response<MyAddressesAPIResponse> response) {

                    Gson gson = new Gson();
                    try {
                        //String str = "{\"resType\":\"success\",\"resMessage\":\"best message\",\"data\":[{\"Id\":1,\"Name\":\"Selgros\",\"Description\":\"bla\",\"Image\":\"path\",\"Orderfee\":20,\"Deliverytime\":90,\"Minimumorder\":100,\"Idstoretype\":1}]}";
                        //  MyStoreAPIResponse r = gson.fromJson(response.body(), MyStoreAPIResponse.class);
                        allAddresses = response.body().data;

                       // MyAddressesDialog cdd=new MyAddressesDialog(da,allAddresses);
//                        setRightTools();


                    } catch (Exception ex) {

                        Log.d(TAG, "OnResponse fail to convert string to corresponding class");
                        ex.printStackTrace();
                    }

                }
            });

        } catch (com.google.gson.JsonSyntaxException ex){

        }
    }



    private void makeStoresCall(){

        RServerRequest serverReqTranslates = new RServerRequest(RAppConsts.kGetProducts);
        String strTranslates = serverReqTranslates.getRequestString();
        RequestBody bodyTranslates =
                RequestBody.create(MediaType.parse("text/plain"), strTranslates);
        try {


            Call<MyStoreAPIResponse> cc = new RRestClient()
                    .getApiMethods()
                    .getStores("1",
                            MyAppSession.getInstance().getCurrentUser().getUid());

            MyStoresActivity da = this;
            cc.enqueue(new Callback<MyStoreAPIResponse>() {
                @Override
                public void onFailure(Call<MyStoreAPIResponse> call, Throwable t) {
                    System.out.println("callback- onFailure");
                }

                @Override
                public void onResponse(Call<MyStoreAPIResponse> call, Response<MyStoreAPIResponse> response) {

                    Gson gson = new Gson();
                    try {
                        //String str = "{\"resType\":\"success\",\"resMessage\":\"best message\",\"data\":[{\"Id\":1,\"Name\":\"Selgros\",\"Description\":\"bla\",\"Image\":\"path\",\"Orderfee\":20,\"Deliverytime\":90,\"Minimumorder\":100,\"Idstoretype\":1}]}";
                      //  MyStoreAPIResponse r = gson.fromJson(response.body(), MyStoreAPIResponse.class);
                        allStores = response.body().data;
                        mStoresAdapter = new MyStoresAdapter(da, allStores);
                        mStoresAdapter.setClickListener(da);
                        rvStores.setAdapter(mStoresAdapter);
                        rvStores.invalidate();

                    } catch (Exception ex) {

                        Log.d(TAG, "OnResponse fail to convert string to corresponding class");
                        ex.printStackTrace();
                    }

                }
            });

        } catch (com.google.gson.JsonSyntaxException ex){

        }
    }

    private void makePushCall(String token){

        RServerRequest serverReqTranslates = new RServerRequest(RAppConsts.kGetProducts);
        String strTranslates = serverReqTranslates.getRequestString();
        RequestBody bodyTranslates =
                RequestBody.create(MediaType.parse("text/plain"), strTranslates);
        try {


            Call<MyPushTokenAPIResponse> cc = new RRestClient()
                    .getApiMethods()
                    .updateToken(MyAppSession.getInstance().getCurrentUser().getUid(),
                            token);

            MyStoresActivity da = this;
            cc.enqueue(new Callback<MyPushTokenAPIResponse>() {
                @Override
                public void onFailure(Call<MyPushTokenAPIResponse> call, Throwable t) {
                    System.out.println("callback- onFailure");
                }

                @Override
                public void onResponse(Call<MyPushTokenAPIResponse> call, Response<MyPushTokenAPIResponse> response) {

                    Gson gson = new Gson();
                    try {
                        //  MyStoreAPIResponse r = gson.fromJson(response.body(), MyStoreAPIResponse.class);
                        List<String> result = response.body().data;

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
    public void onItemClick(View view, int position) {
        Log.d(TAG, "Item was clicked at position: " + position);

        MyStoresPOJO selStore = allStores.get(position);
        Intent intent = new Intent(getApplicationContext(), CategoriesActivity.class);
        String storeId = "" + selStore.id;
        Bundle bundle = new Bundle();
        bundle.putSerializable("STORE_SELECTED", selStore);
        intent.putExtras(bundle);

        startActivity ( intent );

    }

    private void setRightTools() {

        Toolbar toolbar = findViewById(R.id.toolbar);
        TextView dummyTxt = new TextView(this);
        dummyTxt.setText("     Addresses");
        dummyTxt.setTextColor(Color.WHITE);
        dummyTxt.setTextSize(20);
        toolbar.addView(dummyTxt);
        MyStoresActivity da = this;
        dummyTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentManager fm = getSupportFragmentManager();
                MyAddressesDialog editNameDialogFragment = MyAddressesDialog.newInstance(allAddresses, da);
                editNameDialogFragment.show(fm, "dummy");
            }
        });
//        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
//        getSupportActionBar().setCustomView(dummyTxt);
//        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorAccent, null)));
    }
    private void setRightTools2(){


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
        txtTitle.setText("Addresses");
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

        clFrame.setBackgroundColor(Color.YELLOW);
        clMain.setBackgroundColor(Color.BLUE);
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
        final View activityRootView = findViewById(R.id.drawer_layout);
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


        TextView dummyTxt = new TextView(this);
        dummyTxt.setText("olala");
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(dummyTxt);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorAccent, null)));

        constrainImgCart.applyTo(clFrame);
        constrainImgFav.applyTo(clFrame);
        constrainImgSearch.applyTo(clFrame);
        constrainTxtBadgeCart.applyTo(clFrame);
        constrainTxtBadgeFav.applyTo(clFrame);
        constrainTxtTitle.applyTo(clFrame);
        constrainFrame.applyTo(clMain);
    }
}