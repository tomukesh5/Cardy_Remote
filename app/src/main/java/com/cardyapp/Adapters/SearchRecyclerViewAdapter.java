package com.cardyapp.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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

public class SearchRecyclerViewAdapter extends RecyclerView.Adapter<ViewHolder> {

    private List<Userdata> data = new ArrayList<>();
    private Context context;
    private LayoutInflater layoutInflater;
    private boolean isFromConnectionListFragment = false;
    private List<String> selectedConnection = new ArrayList<>();

    public SearchRecyclerViewAdapter(Context context, List<Userdata> data, boolean isFromConnectionListFragment) {
        this.context = context;
        this.data = data;
        this.layoutInflater = LayoutInflater.from(context);
        this.isFromConnectionListFragment = isFromConnectionListFragment;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = layoutInflater.inflate(R.layout.search_row, viewGroup, false);
        view.setTag(R.layout.search_row, data.get(i));
        return new SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        final Userdata connection = data.get(position);
        final SearchViewHolder connectionViewHolder = (SearchViewHolder) viewHolder;

        if (!CommonUtil.isEmpty(connection.getProfilepic())) {
            Glide.with(context).load(context.getResources().getString(R.string.BASE_PROFILE_URL) + connection.getProfilepic()).signature(new StringSignature(new Date() + "")).error(setDefaultProfilePic()).into(connectionViewHolder.civProfile);
        } else {
            connectionViewHolder.civProfile.setImageResource(setDefaultProfilePic());
        }

        connectionViewHolder.mCBSend.setOnCheckedChangeListener(null);
        if (isFromConnectionListFragment) {
            connectionViewHolder.mCBSend.setVisibility(View.GONE);
        } else {
            connectionViewHolder.mCBSend.setVisibility(View.VISIBLE);
            if (selectedConnection.contains(connection.getUserid())) {
                connectionViewHolder.mCBSend.setChecked(true);
            } else {
                connectionViewHolder.mCBSend.setChecked(false);
            }
        }

        connectionViewHolder.mTvName.setText(connection.getFirstname() + " " + connection.getLastname());
        connectionViewHolder.mTvRole.setText(connection.getDesignation());
        connectionViewHolder.mCBSend.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    if (!selectedConnection.contains(connection.getUserid())) {
                        selectedConnection.add(connection.getUserid());
                    }
                } else {
                    if (selectedConnection.contains(connection.getUserid())) {
                        selectedConnection.remove(connection.getUserid());
                    }
                }
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

    class SearchViewHolder extends ViewHolder {

        @BindView(R.id.tv_name)
        TextView mTvName;
        @BindView(R.id.tv_role)
        TextView mTvRole;
        @BindView(R.id.cb_send)
        CheckBox mCBSend;
        @BindView(R.id.civ_profile)
        CircleImageView civProfile;

        private SearchViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public List<String> getSelectedUser() {
        return selectedConnection;
    }
}
