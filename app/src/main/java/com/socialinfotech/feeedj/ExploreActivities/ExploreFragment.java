package com.socialinfotech.feeedj.ExploreActivities;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import com.amplitude.api.Amplitude;
import com.socialinfotech.feeedj.AppUtils.Constant;
import com.socialinfotech.feeedj.ApplicationActivities.SplashActivity;
import com.socialinfotech.feeedj.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class ExploreFragment extends Fragment {
    FragmentManager fm;
    FragmentTransaction fragmentTransaction;
    SharedPreferences sPref;
    SharedPreferences.Editor editor;
    String TOKEN;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Amplitude.getInstance().logEvent("Categories List");

         fm = getFragmentManager();
         fragmentTransaction = fm.beginTransaction();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_explore, container, false);
        ButterKnife.bind(this, view);
        sPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        editor = sPref.edit();
        TOKEN = sPref.getString(Constant.ACCESS_TOKEN,"");
        return view;
    }

    private void ShowwarningDialog() {
        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_login_warning);

        Button btn_ok = dialog.findViewById(R.id.btn_ok);
        Button btn_cnl = dialog.findViewById(R.id.btn_cnl);
        final Dialog finalDialog = dialog;
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalDialog.dismiss();
                startActivity(new Intent(getActivity(), SplashActivity.class));
            }
        });
        btn_cnl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalDialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.btn_telephone, R.id.btn_ariline, R.id.btn_bank, R.id.btn_electronics, R.id.btn_travel, R.id.btn_car, R.id.btn_resturant, R.id.btn_supermarket, R.id.btn_furniture, R.id.btn_cloths, R.id.btn_health, R.id.btn_other})
    public void onClick(View view) {
        Intent intent = new Intent(getActivity(), CategoryTabActivity.class);
        switch (view.getId()) {
            case R.id.btn_telephone:
                intent.putExtra("message", 1);
                intent.putExtra("title", "الاتصالات");
                break;
            case R.id.btn_ariline:
                intent.putExtra("message", 2);
                intent.putExtra("title", "الطيران");
                break;
            case R.id.btn_bank:
                intent.putExtra("message", 3);
                intent.putExtra("title", "البنوك");
                break;
            case R.id.btn_electronics:
                intent.putExtra("message", 4);
                intent.putExtra("title", "الإلكترونيات");
                break;
            case R.id.btn_travel:
                intent.putExtra("message", 5);
                intent.putExtra("title", "السفر");
                break;
            case R.id.btn_car:
                intent.putExtra("message", 6);
                intent.putExtra("title", "السيارات");
                break;
            case R.id.btn_resturant:
                intent.putExtra("message", 7);
                intent.putExtra("title", "المطاعم");
                break;
            case R.id.btn_supermarket:
                intent.putExtra("message", 8);
                intent.putExtra("title", "السوبرماركت");
                break;
            case R.id.btn_furniture:
                intent.putExtra("message", 9);
                intent.putExtra("title", "المفروشات");
                break;
            case R.id.btn_cloths:
                intent.putExtra("message", 10);
                intent.putExtra("title", "الملابس");
                break;
            case R.id.btn_health:
                intent.putExtra("message", 11);
                intent.putExtra("title", "الصحة");
                break;
            case R.id.btn_other:
                intent.putExtra("message", 12);
                intent.putExtra("title", "أشياء ثانية");
                break;
        }
        startActivity(intent);
    }
}
