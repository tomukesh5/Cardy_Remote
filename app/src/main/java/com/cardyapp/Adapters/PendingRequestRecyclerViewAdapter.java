package com.cardyapp.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.cardyapp.Models.ConnectionDTO;
import com.cardyapp.Models.Userdata;
import com.cardyapp.R;
import com.cardyapp.Utils.AppConstants;
import com.cardyapp.Utils.CommonUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Priyanka on 12/31/2017.
 */

public class PendingRequestRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {

    public interface PendingRequestBtnClickListener {
        void onClickedPendingBtn(String id, String action);
    }

    private List<Userdata> data = new ArrayList<>();
    private List<Userdata> dataBackup = new ArrayList<>();
    private Context context;
    private LayoutInflater layoutInflater;
    private PendingRequestBtnClickListener pendingRequestBtnClickListener;
    private int expandedViewPosition = -1;

    public PendingRequestRecyclerViewAdapter(Context context, List<Userdata> data, PendingRequestBtnClickListener pendingRequestBtnClickListener) {
        this.context = context;
        this.data = data;
        this.dataBackup.addAll(data);
        this.layoutInflater = LayoutInflater.from(context);
        this.pendingRequestBtnClickListener = pendingRequestBtnClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = layoutInflater.inflate(R.layout.pending_request_row, viewGroup, false);
        view.setTag(R.layout.pending_request_row, data.get(i));
        return new PendingRequestRecyclerViewAdapter.PendingRequestViewHolder(view);
    }

    private int setDefaultProfilePic() {
        return R.drawable.blank_profile;
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

        if (!CommonUtil.isEmpty(connection.getProfilepic())) {
            Glide.with(context).load(context.getResources().getString(R.string.BASE_PROFILE_URL) + connection.getProfilepic()).signature(new StringSignature(new Date() + "")).error(setDefaultProfilePic()).into(connectionViewHolder.civProfile);
        } else {
            connectionViewHolder.civProfile.setImageResource(setDefaultProfilePic());
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

    @Override
    public Filter getFilter() {
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                FilterResults result = new FilterResults();
                if(null != constraint && !constraint.toString().isEmpty()) {
                    ArrayList<Userdata> filter = new ArrayList<Userdata>();
                    ArrayList<Userdata> items = new ArrayList<Userdata>();
                    synchronized (this) {
                        items.addAll(dataBackup);
                    }
                    for (Userdata item : items) {
                        if((item.getFirstname()+item.getLastname()).toLowerCase().contains(constraint) ||
                                (item.getDesignation()).toLowerCase().contains(constraint)) {
                            filter.add(item);
                        }
                    }
                    result.count = filter.size();
                    result.values = filter;
                } else {
                    synchronized (this) {
                        result.count = dataBackup.size();
                        result.values = dataBackup;
                    }
                }

                return result;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if(results.count == 0) {
                    notifyDataSetChanged();
                    data = new ArrayList<>();
                } else {
                    data = (List<Userdata>) results.values;
                    notifyDataSetChanged();
                }
            }
        };
    }

    class PendingRequestViewHolder extends RecyclerView.ViewHolder {

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
        @BindView(R.id.civ_profile)
        CircleImageView civProfile;

        private PendingRequestViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
