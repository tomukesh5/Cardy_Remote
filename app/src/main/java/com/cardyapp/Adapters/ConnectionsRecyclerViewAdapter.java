package com.cardyapp.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.cardyapp.Models.ConnectionDTO;
import com.cardyapp.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ConnectionsRecyclerViewAdapter extends RecyclerView.Adapter<ViewHolder> {

    private List<ConnectionDTO> data = new ArrayList<>();
    private Context context;
    private LayoutInflater layoutInflater;
    private ConnectionViewHolder previousHolder;

    public ConnectionsRecyclerViewAdapter(Context context, List<ConnectionDTO> data) {
        this.context = context;
        this.data = data;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = layoutInflater.inflate(R.layout.connection_row, viewGroup, false);
        view.setTag(R.layout.connection_row, data.get(i));
        return new ConnectionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        final ConnectionDTO connection = data.get(position);
        final ConnectionViewHolder connectionViewHolder = (ConnectionViewHolder) viewHolder;

        connectionViewHolder.mTLDetails.setVisibility(View.GONE);
        connectionViewHolder.mLLBtnLayout.setVisibility(View.GONE);
        previousHolder = null;

        connectionViewHolder.mTvName.setText(connection.getName());
        connectionViewHolder.mTvCompanyName.setText(connection.getCompanyName());

        connectionViewHolder.mIvExpand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (previousHolder != null) {
                    previousHolder.mTLDetails.setVisibility(View.GONE);
                    previousHolder.mLLBtnLayout.setVisibility(View.GONE);
                }
                if (previousHolder != connectionViewHolder) {
                    if (connectionViewHolder.mTLDetails.getVisibility() == View.VISIBLE) {
                        connectionViewHolder.mTLDetails.setVisibility(View.GONE);
                        connectionViewHolder.mLLBtnLayout.setVisibility(View.GONE);
                        notifyItemChanged(position);
                        previousHolder = null;
                    } else {
                        connectionViewHolder.mTLDetails.setVisibility(View.VISIBLE);
                        connectionViewHolder.mLLBtnLayout.setVisibility(View.VISIBLE);
                        previousHolder = connectionViewHolder;
                    }
                } else {
                    previousHolder = null;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<ConnectionDTO> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    class ConnectionViewHolder extends ViewHolder {

        @BindView(R.id.tv_name)
        TextView mTvName;
        @BindView(R.id.tv_companyName)
        TextView mTvCompanyName;
        @BindView(R.id.tv_designation)
        TextView mTvDesignation;
        @BindView(R.id.tv_previousOrganization)
        TextView mTvPreviousOrganization;
        @BindView(R.id.tv_location)
        TextView mTvLocation;
        @BindView(R.id.tv_sector)
        TextView mTvSector;
        @BindView(R.id.iv_expand)
        ImageView mIvExpand;
        @BindView(R.id.tl_details)
        TableLayout mTLDetails;
        @BindView(R.id.ll_btn_layout)
        LinearLayout mLLBtnLayout;

        private ConnectionViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
