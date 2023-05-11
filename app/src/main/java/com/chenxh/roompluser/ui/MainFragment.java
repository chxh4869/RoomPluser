package com.chenxh.roompluser.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;


import com.chenxh.roompluser.R;
import com.chenxh.roompluser.db.AppDataBases;
import com.chenxh.roompluser.db.entity.PayConfig;

import java.util.List;

public class MainFragment extends Fragment {

    private MainViewModel mViewModel;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    List<PayConfig> list;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_main, container, false);

        view.findViewById(R.id.create).setOnClickListener(v->{
            AppDataBases.getInstance().payConfigDao();
        });

        view.findViewById(R.id.btn_insert).setOnClickListener(v->{
            PayConfig config = new PayConfig();
            config.bankType = "ccb";
//            config.provinceId = new Random().nextInt();
            AppDataBases.getInstance().payConfigDao().addPayConfig(config);
        });

        view.findViewById(R.id.btn_query).setOnClickListener(v->{
            list = AppDataBases.getInstance().payConfigDao().getAllPayConfig();

        });

        view.findViewById(R.id.btn_updata).setOnClickListener(v->{
            if(list!=null&&list.size()>0) {
                PayConfig tmp = list.get(list.size()-1);
//                tmp.provinceId = new Random().nextInt();
                AppDataBases.getInstance().payConfigDao().updatePayConfig(tmp);
            }
        });

        view.findViewById(R.id.btn_delelte).setOnClickListener(v->{
            if(list!=null && list.size()>0) {
                PayConfig tmp = list.get(list.size()-1);
                AppDataBases.getInstance().payConfigDao().delPayConfig(tmp);
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        // TODO: Use the ViewModel
    }

}