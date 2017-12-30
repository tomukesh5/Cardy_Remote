package com.cardyapp.Activities;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;

import com.cardyapp.Adapters.ConnectionsRecyclerViewAdapter;
import com.cardyapp.Models.ConnectionDTO;
import com.cardyapp.R;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class ConnectionListActivity extends BaseActivity {

    @BindView(R.id.rv_connection)
    public RecyclerView mRvConnections;

    private List<ConnectionDTO> list = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        generateHashkey();
        setAdapter();
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_connection_list;
    }

    public void generateHashkey(){
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.cardyapp",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());

                String str = Base64.encodeToString(md.digest(),
                        Base64.NO_WRAP);
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.d("Name not found", e.getMessage(), e);

        } catch (NoSuchAlgorithmException e) {
            Log.d("Error", e.getMessage(), e);
        }
    }

    private void setAdapter() {

        list.add(new ConnectionDTO("Atul Kulkarni", "Director","Google", "TED", "New York", "IT"));
        list.add(new ConnectionDTO("Atul Kulkarni", "Director","Google", "TED", "New York", "IT"));
        list.add(new ConnectionDTO("Atul Kulkarni", "Director","Google", "TED", "New York", "IT"));
        list.add(new ConnectionDTO("Atul Kulkarni", "Director","Google", "TED", "New York", "IT"));
        list.add(new ConnectionDTO("Atul Kulkarni", "Director","Google", "TED", "New York", "IT"));
        list.add(new ConnectionDTO("Atul Kulkarni", "Director","Google", "TED", "New York", "IT"));
        list.add(new ConnectionDTO("Atul Kulkarni", "Director","Google", "TED", "New York", "IT"));
        list.add(new ConnectionDTO("Atul Kulkarni", "Director","Google", "TED", "New York", "IT"));
        list.add(new ConnectionDTO("Atul Kulkarni", "Director","Google", "TED", "New York", "IT"));
        list.add(new ConnectionDTO("Atul Kulkarni", "Director","Google", "TED", "New York", "IT"));
        list.add(new ConnectionDTO("Atul Kulkarni", "Director","Google", "TED", "New York", "IT"));
        list.add(new ConnectionDTO("Atul Kulkarni", "Director","Google", "TED", "New York", "IT"));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ConnectionListActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        mRvConnections.setAdapter(new ConnectionsRecyclerViewAdapter(ConnectionListActivity.this, list));
        mRvConnections.setLayoutManager(linearLayoutManager);
    }
}
