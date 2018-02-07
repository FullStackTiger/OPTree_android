package com.tecocraft.optree.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.tecocraft.optree.R;
import com.tecocraft.optree.global.CommonUtils;
import com.tecocraft.optree.ui.MainActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;


public class HomeFragment extends Fragment {


    Button btnSearchByName, btnSearchByCategory, btnFavorite;
    CommonUtils commonUtils;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        ButterKnife.bind(this, view);

        commonUtils = new CommonUtils(getActivity());
        return view;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @OnClick({R.id.btnSearchByName, R.id.btnSearchByCategory, R.id.btnFavorite, R.id.btnCart, R.id.btnGlossary, R.id.ivLogo})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSearchByName:


                if (commonUtils.isNetworkAvailable())
                    ((MainActivity) getActivity()).replaceFragment(SearchByNameFragment.class);
                else
                    CommonUtils.alertDialog(getActivity(), getString(R.string.internet_disconnect));
                break;
            case R.id.btnSearchByCategory:
                if (commonUtils.isNetworkAvailable())
                    ((MainActivity) getActivity()).replaceFragment(CategoryFragment.class);
                else
                    CommonUtils.alertDialog(getActivity(), getString(R.string.internet_disconnect));
                break;
            case R.id.btnFavorite:
                if (commonUtils.isNetworkAvailable())
                    ((MainActivity) getActivity()).replaceFragment(FavouriteFragment.class);
                else
                    CommonUtils.alertDialog(getActivity(), getString(R.string.internet_disconnect));
                break;
            case R.id.btnCart:
                if (commonUtils.isNetworkAvailable())
                    ((MainActivity) getActivity()).replaceFragment(CartFragment.class);
                else
                    CommonUtils.alertDialog(getActivity(), getString(R.string.internet_disconnect));
                break;
            case R.id.btnGlossary:
                ((MainActivity) getActivity()).replaceFragment(GlosaryFragment.class);
                break;
            case R.id.ivLogo:
                CommonUtils.alertDialog(getActivity(), getString(R.string.home_logo_msg), getString(R.string.home_logo_title));
                break;
        }
    }
}
