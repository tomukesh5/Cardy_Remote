package com.cardyapp.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cardyapp.Models.ConnectionDTO;
import com.cardyapp.Models.Userdata;
import com.cardyapp.R;
import com.cardyapp.Utils.AppConstants;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Priyanka on 12/31/2017.
 */

public class PendingRequestRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public interface PendingRequestBtnClickListener {
        void onClickedPendingBtn(String id, String action);
    }

    private List<Userdata> data = new ArrayList<>();
    private Context context;
    private LayoutInflater layoutInflater;
    private PendingRequestBtnClickListener pendingRequestBtnClickListener;
    private int expandedViewPosition = -1;

    public PendingRequestRecyclerViewAdapter(Context context, List<Userdata> data, PendingRequestBtnClickListener pendingRequestBtnClickListener) {
        this.context = context;
        this.data = data;
        this.layoutInflater = LayoutInflater.from(context);
        this.pendingRequestBtnClickListener = pendingRequestBtnClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = layoutInflater.inflate(R.layout.pending_request_row, viewGroup, false);
        view.setTag(R.layout.pending_request_row, data.get(i));
        return new PendingRequestRecyclerViewAdapter.PendingRequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        final Userdata connection = data.get(position);
        final PendingRequestRecyclerViewAdapter.PendingRequestViewHolder connectionViewHolder = (PendingRequestRecyclerViewAdapter.PendingRequestViewHolder) viewHolder;

        if (expandedViewPosition == position){
            connectionViewHolder.buttonLayout.setVisibility(View.VISIBLE);
        } else {
            connectionViewHolder.buttonLayout.setVisibility(View.GONE);
        }

        connectionViewHolder.mTvName.setText(connection.getFirstname() + " " + connection.getLastname());
        connectionViewHolder.mTvRole.setText(connection.getDesignation());

        connectionViewHolder.tvCurrentCompany.setText(connection.getCurcompany());
        connectionViewHolder.tvEmail.setText(connection.getPersonalemail());
        connectionViewHolder.tvContactNumber.setText(connection.getUser_mobile());

        connectionViewHolder.mTvAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pendingRequestBtnClickListener.onClickedPendingBtn(connection.getUserid(), AppConstants.PendingRequestAction.ACCEPT.name());
            }
        });
        connectionViewHolder.mTvReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pendingRequestBtnClickListener.onClickedPendingBtn(connection.getUserid(), AppConstants.PendingRequestAction.REJECT.name());
            }
        });
        connectionViewHolder.mTvAcceptAndReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pendingRequestBtnClickListener.onClickedPendingBtn(connection.getUserid(), AppConstants.PendingRequestAction.ACCEPT_AND_REVERT.name());
            }
        });

        connectionViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (connectionViewHolder.buttonLayout.getVisibility() == View.VISIBLE) {
                    connectionViewHolder.buttonLayout.setVisibility(View.GONE);
                    expandedViewPosition = -1;
                } else {
                    connectionViewHolder.buttonLayout.setVisibility(View.VISIBLE);
                    expandedViewPosition = position;
                }
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<Userdata> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    class PendingRequestViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.civ_profile)
        CircleImageView mCIVProfile;
        @BindView(R.id.tv_name)
        TextView mTvName;
        @BindView(R.id.tv_role)
        TextView mTvRole;
        @BindView(R.id.tv_accept)
        TextView mTvAccept;
        @BindView(R.id.tv_reject)
        TextView mTvReject;
        @BindView(R.id.tv_acceptAndRevert)
        TextView mTvAcceptAndReject;
        @BindView(R.id.tv_currentCompany)
        TextView tvCurrentCompany;
        @BindView(R.id.tv_email)
        TextView tvEmail;
        @BindView(R.id.tv_contactNumber)
        TextView tvContactNumber;
        @BindView(R.id.buttonContainer)
        LinearLayout buttonLayout;

        private PendingRequestViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
