package com.mycompany.myordertest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.mycompany.myordertest.adapters.MyAddressesAdapter;
import com.mycompany.myordertest.rest.DTO.MyAddressPOJO;

import java.util.List;

public class MyAddressesDialog extends DialogFragment {

    private RecyclerView rvAddresses;
    private RecyclerView.LayoutManager layoutManager;
    private List<MyAddressPOJO> addresses;
    private Context context;

    public MyAddressesDialog(List<MyAddressPOJO> addresses, Context context) {
        super();
        this.addresses = addresses;
        this.context = context;
    }

    public static MyAddressesDialog newInstance(List<MyAddressPOJO> addresses, Context context) {
        MyAddressesDialog frag = new MyAddressesDialog( addresses, context);
        Bundle args = new Bundle();
//        args.putString("title", title);
//        frag.setArguments(args);
        return frag;
    }
    private RecyclerView mRecyclerView;
    // this method create view for your Dialog
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //inflate layout with recycler view
        View v = inflater.inflate(R.layout.activity_my_addresses_dialog, container, false);
        mRecyclerView =  v.findViewById(R.id.listView_addresses);

        mRecyclerView.setBackgroundColor(getResources().getColor(R.color.colorGrayBackground));

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);
        MyAddressesAdapter myAddressesAdapter = new MyAddressesAdapter(context, addresses);
        mRecyclerView.setAdapter(myAddressesAdapter);
        mRecyclerView.invalidate();
        return v;
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        MyAddressesAdapter myAddressesAdapter = new MyAddressesAdapter(getContext(), addresses);
//        mRecyclerView.setAdapter(myAddressesAdapter);
//        mRecyclerView.invalidate();
//        myAddressesAdapter.notifyDataSetChanged();
//
//    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get field from view

//        // Fetch arguments from bundle and set title
//        String title = getArguments().getString("title", "Enter Name");
//        getDialog().setTitle(title);

        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }
//    @NonNull
//    @Override
//    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
//
//        rvAddresses = new RecyclerView(getContext());
//            // you can use LayoutInflater.from(getContext()).inflate(...) if you have xml layout
//        rvAddresses.setLayoutManager(new LinearLayoutManager(getContext()));
//        rvAddresses.setBackgroundColor(Color.RED);
//        MyAddressesAdapter myAddressesAdapter = new MyAddressesAdapter(getContext(), addresses);
//        rvAddresses.setAdapter(myAddressesAdapter);
//        rvAddresses.invalidate();
//
//
//        return new AlertDialog.Builder(getActivity())
//                .setTitle("Hi")
//                .setView(rvAddresses)
//                .setPositiveButton(android.R.string.ok,
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int whichButton) {
//                                // do something
//                            }
//                        }
//                ).create();
//    }

//    @Override
//        public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_my_addresses_dialog);
//
//        rvAddresses = findViewById(R.id.listView_addresses);
//
//        rvAddresses.setBackgroundColor(getContext().getResources().getColor(R.color.colorGrayBackground));
//
//        rvAddresses.setHasFixedSize(true);
//
//        layoutManager = new LinearLayoutManager(getContext());
//
//        rvAddresses.setLayoutManager(layoutManager);
//        MyAddressesAdapter myAddressesAdapter = new MyAddressesAdapter(getContext(), addresses);
//        rvAddresses.setAdapter(myAddressesAdapter);
//        rvAddresses.invalidate();
//
//
//    }


}