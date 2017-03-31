package com.unishop.menu;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;
import com.unishop.Closing.BoughtFragment;
import com.unishop.Closing.SoldFragment;
import com.unishop.R;

/**
 * Created by Daniel on 1/13/17.
 */

public class SettingsFragment extends Fragment {



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        BottomBar bottomBar = (BottomBar) view.findViewById(R.id.topBar);
        bottomBar.selectTabAtPosition(0);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                switch (tabId) {
                    case R.id.topbaritemone:
                        BoughtFragment boughtFragment = new BoughtFragment();
                        fragmentTransaction.replace(R.id.buysellContainer, boughtFragment);
                        fragmentTransaction.commit();
                        break;
                    case R.id.topbaritemtwo:
                        SoldFragment soldFragment = new SoldFragment();
                        fragmentTransaction.replace(R.id.buysellContainer, soldFragment);
                        fragmentTransaction.commit();
                        break;
                    default:
                        break;
                }
            }
        });

        return view;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
