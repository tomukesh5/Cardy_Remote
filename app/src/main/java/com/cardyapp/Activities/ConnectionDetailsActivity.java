package com.cardyapp.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.cardyapp.Models.Userdata;
import com.cardyapp.R;
import com.cardyapp.Utils.IntentExtras;

import butterknife.BindView;
import butterknife.OnClick;

import static com.cardyapp.Utils.AppConstants.F_GENDER;

/**
 * Created by Priyanka on 3/3/2018.
 */

public class ConnectionDetailsActivity extends BaseActivity {

    @BindView(R.id.tv_fullName)
    TextView tvFullName;
    @BindView(R.id.tv_currentCompany)
    TextView tvCurrentCompany;
    @BindView(R.id.tv_designation)
    TextView tvDesignation;
    @BindView(R.id.tv_previousCompany)
    TextView tvPreviousCompany;
    @BindView(R.id.tv_gender)
    TextView tvGender;

    private Userdata userdata;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getIntent().hasExtra(IntentExtras.CONNECTION_DTO)){
            userdata = (Userdata) getIntent().getExtras().get(IntentExtras.CONNECTION_DTO);
        }

        setDataToUI();
    }

    private void setDataToUI() {
        if (userdata == null){
            setTitle("Details");
            return;
        }

        setTitle(userdata.getFullname());

        tvFullName.setText(userdata.getFullname());
        tvCurrentCompany.setText(userdata.getCurcompany());
        tvDesignation.setText(userdata.getDesignation());
        tvPreviousCompany.setText(userdata.getPrevcompany());

        if (userdata.getGender() != null && userdata.getGender().equalsIgnoreCase(F_GENDER)) {
            tvGender.setText(getText(R.string.Female));
        } else {
            tvGender.setText(getText(R.string.Male));
        }
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_connection_details;
    }

    @OnClick({R.id.tv_email, R.id.tv_call})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_email:
                String str = "to@email.com";
                if (userdata != null)
                    str = userdata.getUser_email();
                Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                emailIntent.setType("plain/text");
                emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{str});
                emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject");
                emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Text");
                startActivity(Intent.createChooser(emailIntent, "Send mail..."));
                break;
            case R.id.tv_call:
                String number = "tel:9999999999";
                if (userdata != null)
                    number = "tel:" + userdata.getMobileno();
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse(number));
                startActivity(intent);
                break;
        }
    }
}
