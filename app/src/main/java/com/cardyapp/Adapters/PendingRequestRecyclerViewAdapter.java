package com.cardyapp.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cardyapp.Models.ConnectionDTO;
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
        void onClickedPendingBtn(int pos, String action);
    }

    private List<ConnectionDTO> data = new ArrayList<>();
    private Context context;
    private LayoutInflater layoutInflater;
    private PendingRequestBtnClickListener pendingRequestBtnClickListener;

    public PendingRequestRecyclerViewAdapter(Context context, List<ConnectionDTO> data, PendingRequestBtnClickListener pendingRequestBtnClickListener) {
        this.context = context;
        this.data = data;
        this.layoutInflater = LayoutInflater.from(context);
        this.pendingRequestBtnClickListener = pendingRequestBtnClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = layoutInflater.inflate(R.layout.connection_row, viewGroup, false);
        view.setTag(R.layout.pending_request_row, data.get(i));
        return new PendingRequestRecyclerViewAdapter.PendingRequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        final ConnectionDTO connection = data.get(position);
        final PendingRequestRecyclerViewAdapter.PendingRequestViewHolder connectionViewHolder = (PendingRequestRecyclerViewAdapter.PendingRequestViewHolder) viewHolder;

        connectionViewHolder.mTvName.setText(connection.getName());
        connectionViewHolder.mTvRole.setText(connection.getCompanyName());

        connectionViewHolder.mTvAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pendingRequestBtnClickListener.onClickedPendingBtn(position, AppConstants.PendingRequestAction.ACCEPT.name());
            }
        });
        connectionViewHolder.mTvReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pendingRequestBtnClickListener.onClickedPendingBtn(position, AppConstants.PendingRequestAction.REJECT.name());
            }
        });
        connectionViewHolder.mTvAcceptAndReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pendingRequestBtnClickListener.onClickedPendingBtn(position, AppConstants.PendingRequestAction.ACCEPT_AND_REVERT.name());
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

        private PendingRequestViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
