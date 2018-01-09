package com.cardyapp.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.cardyapp.Models.Userdata;
import com.cardyapp.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ConnectionsRecyclerViewAdapter extends RecyclerView.Adapter<ViewHolder> {

    private List<Userdata> data = new ArrayList<>();
    private Context context;
    private LayoutInflater layoutInflater;
    private boolean isFromConnectionListFragment = false;

    public ConnectionsRecyclerViewAdapter(Context context, List<Userdata> data, boolean isFromConnectionListFragment) {
        this.context = context;
        this.data = data;
        this.layoutInflater = LayoutInflater.from(context);
        this.isFromConnectionListFragment = isFromConnectionListFragment;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = layoutInflater.inflate(R.layout.connection_row, viewGroup, false);
        view.setTag(R.layout.connection_row, data.get(i));
        return new ConnectionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        final Userdata connection = data.get(position);
        final ConnectionViewHolder connectionViewHolder = (ConnectionViewHolder) viewHolder;

        if (isFromConnectionListFragment) {
            connectionViewHolder.mCBSend.setVisibility(View.GONE);
        } else {
            connectionViewHolder.mCBSend.setVisibility(View.VISIBLE);
        }
        connectionViewHolder.mTvName.setText(connection.getFirstname() + " " + connection.getLastname());
        connectionViewHolder.mTvRole.setText(connection.getDesignation());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<Userdata> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    class ConnectionViewHolder extends ViewHolder {

        @BindView(R.id.tv_name)
        TextView mTvName;
        @BindView(R.id.tv_role)
        TextView mTvRole;
        @BindView(R.id.cb_send)
        CheckBox mCBSend;

        private ConnectionViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
