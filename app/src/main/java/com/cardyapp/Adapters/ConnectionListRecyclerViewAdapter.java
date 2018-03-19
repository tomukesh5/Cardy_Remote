package com.cardyapp.Adapters;

import android.content.Context;
import android.icu.lang.UScript;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.cardyapp.Models.Userdata;
import com.cardyapp.R;
import com.cardyapp.Utils.CommonUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Priyanka on 3/2/2018.
 */

public class ConnectionListRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {

    private List<Userdata> data = new ArrayList<>();
    private List<Userdata> dataBackup = new ArrayList<>();
    private Context context;
    private LayoutInflater layoutInflater;

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

    public interface onClickConnectionButton {
        void onClickedCallButton(int position);
        void onClickedMailButton(int position);
        void onClickedWhatsappButton(int position);
        void onItemClicked(int position);
    }

    private onClickConnectionButton callback;

    public ConnectionListRecyclerViewAdapter(Context context, List<Userdata> data, onClickConnectionButton callback) {
        this.context = context;
        this.data = data;
        this.dataBackup.addAll(data);
        this.callback = callback;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = layoutInflater.inflate(R.layout.connection_row, viewGroup, false);
        view.setTag(R.layout.connection_row, data.get(i));
        return new ConnectionListRecyclerViewAdapter.ConnectionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        final Userdata connection = data.get(position);
        ConnectionViewHolder connectionViewHolder = (ConnectionViewHolder) viewHolder;

        connectionViewHolder.mTvName.setText(connection.getFirstname() + " " + connection.getLastname());
        connectionViewHolder.mTvRole.setText(connection.getDesignation());

        if (!CommonUtil.isEmpty(connection.getProfilepic())) {
            Glide.with(context).load(context.getResources().getString(R.string.BASE_PROFILE_URL) + connection.getProfilepic()).signature(new StringSignature(new Date() + "")).error(setDefaultProfilePic()).into(connectionViewHolder.civProfile);
        } else {
            connectionViewHolder.civProfile.setImageResource(setDefaultProfilePic());
        }

        connectionViewHolder.ivCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.onClickedCallButton(position);
            }
        });

        connectionViewHolder.ivMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.onClickedMailButton(position);
            }
        });

        connectionViewHolder.ivWhatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.onClickedWhatsappButton(position);
            }
        });

        connectionViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.onItemClicked(position);
            }
        });
    }

    private int setDefaultProfilePic() {
        return R.drawable.blank_profile;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<Userdata> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    class ConnectionViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_name)
        TextView mTvName;
        @BindView(R.id.tv_role)
        TextView mTvRole;
        @BindView(R.id.iv_call)
        ImageView ivCall;
        @BindView(R.id.iv_mail)
        ImageView ivMail;
        @BindView(R.id.iv_whatsapp)
        ImageView ivWhatsapp;
        @BindView(R.id.civ_profile)
        CircleImageView civProfile;

        private ConnectionViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
