package com.tecocraft.optree.fragment;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tecocraft.optree.R;
import com.tecocraft.optree.adapter.CartListAdapter;
import com.tecocraft.optree.global.CommonUtils;
import com.tecocraft.optree.global.Conts;
import com.tecocraft.optree.model.name.Code;
import com.tecocraft.optree.model.name.SearchNameModel;
import com.tecocraft.optree.rest.ApiClient;
import com.tecocraft.optree.rest.ApiInterface;
import com.tecocraft.optree.ui.MainActivity;
import com.tecocraft.optree.widget.SwipeMenu;
import com.tecocraft.optree.widget.SwipeMenuCreator;
import com.tecocraft.optree.widget.SwipeMenuItem;
import com.tecocraft.optree.widget.SwipeMenuListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CartFragment extends Fragment {


    private static final String TAG = "CartFragment";
    @BindView(R.id.swipewMenuListView)
    public SwipeMenuListView swipewMenuListView;
    @BindView(R.id.etSearch)
    EditText etSearch;
    @BindView(R.id.tvError)
    public TextView tvError;
    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;


    private CartListAdapter adapter;

    private ProgressDialog pd;
    private CommonUtils commonUtils;
    Call<SearchNameModel> call;
    private List<Code> dataList = new ArrayList<>();
    public final List<Code> listCartTemp = new ArrayList<>();
    int totalCartSize = 0;


    public CartFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fargment_favorite, container, false);
        ButterKnife.bind(this, view);
        init();
        return view;
    }

    private void init() {

//        sharedPref = new SharedPref(getActivity());

//        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
//        rvList.setLayoutManager(mLayoutManager);
//        rvList.setItemAnimator(new DefaultItemAnimator());
        commonUtils = new CommonUtils(getActivity());

        pd = new ProgressDialog(getActivity());
        pd.setCancelable(false);
        pd.setMessage("Loading...");

        if (!Conts.listCart.isEmpty()) {
            getDataFromServer();
            totalCartSize = Conts.listCart.size();
        }

        tvError.setText("No card data Found..!!");


        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                if (adapter != null)
                    adapter.filter(etSearch.getText().toString().trim().toLowerCase());
            }
        });

        initSwipeMenu();

        ((MainActivity) getActivity()).ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Conts.listCart.isEmpty())
                    dialogForDelete();
            }
        });
    }


    private void dialogForDelete() {
        new AlertDialog.Builder(getActivity())
                .setMessage("Do you want to delete all codes?")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Conts.listCart.clear();
                        if (adapter != null)
                            adapter.notifyDataSetChanged();
                        // continue with delete
                        swipewMenuListView.setVisibility(View.GONE);
                        tvError.setVisibility(View.VISIBLE);
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private void setData() {

        for (int i = 0; i < Conts.listCart.size(); i++) {
            String search = Conts.listCart.get(i).getCodeName() + " " + Conts.listCart.get(i).getCodeDescription().replaceAll("[-+.^:,]", "") + " " +
                    Conts.listCart.get(i).getCodeName();

            String search1 = search.toLowerCase(Locale.getDefault());
            String[] splited = search1.split("\\s+");
            List<String> stringList = new ArrayList<String>(Arrays.asList(splited));

            Code c = Conts.listCart.get(i);
            c.setSearchString(search);
            c.setSearchList(stringList);
            Conts.listCart.set(i, c);
        }

        adapter = new CartListAdapter(getActivity(), Conts.listCart, CartFragment.this);
        swipewMenuListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();

        if (totalCartSize != Conts.listCart.size()) {
            if (!Conts.listCart.isEmpty()) {
                getDataFromServer();
                totalCartSize = Conts.listCart.size();
            } else {
                swipewMenuListView.setVisibility(View.GONE);
            }
        }
    }

//    @Override
//    public void onPause() {
//        super.onPause();
//        if (call != null)
//            call.cancel();
//
//        if (pd != null && pd.isShowing())
//            pd.dismiss();
//    }

    private void getDataFromServer() {
        if (commonUtils.isNetworkAvailable()) {
            pd.show();
            ApiInterface apiService =
                    ApiClient.getClient().create(ApiInterface.class);
            call = apiService.getCodes();
            call.enqueue(new Callback<SearchNameModel>() {
                @Override
                public void onResponse(Call<SearchNameModel> call, Response<SearchNameModel> response) {
                    if (swipewMenuListView == null)
                        return;
                    if (response.isSuccessful()) {
                        SearchNameModel model = response.body();
                        if (model.isSuccess()) {
                            dataList.clear();
                            dataList.addAll(model.getCodes());

                            for (int i = 0; i < Conts.listCart.size(); i++) {
                                Code c = checkTo(Conts.listCart.get(i).getCodeName());
                                if (c != null) {
                                    Code c1 = Conts.listCart.get(i);
                                    c1.setCodeDescription(c.getCodeDescription());
                                    c1.setDetailImg(c.getDetailImg());
                                    Conts.listCart.set(i, c1);
                                }

                            }

                            setData();

                            if (pd != null && pd.isShowing())
                                pd.dismiss();

                            if (dataList.isEmpty()) {
                                swipewMenuListView.setVisibility(View.GONE);
                                tvError.setVisibility(View.VISIBLE);
                            } else {
                                tvError.setVisibility(View.GONE);
                                swipewMenuListView.setVisibility(View.VISIBLE);
                            }


//                            adapter = new SearchNameListAdapter(getActivity(), dataList, SearchByNameFragment.this);
//                            rvList.setAdapter(adapter);
//                            adapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(getActivity(), "Please try again", Toast.LENGTH_SHORT).show();
                        }
                    }

                    if (pd != null && pd.isShowing())
                        pd.dismiss();


                }

                @Override
                public void onFailure(Call<SearchNameModel> call, Throwable t) {
                    // Log error here since request failed
                    Log.e(TAG, t.toString());
                    if (pd != null && pd.isShowing())
                        pd.dismiss();


                }
            });
        } else {
            CommonUtils.alertDialog(getActivity(), getString(R.string.internet_disconnect));
//            Toast.makeText(getActivity(), "Please check your internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    public Code checkTo(String cname) {
        for (int i = 0; i < dataList.size(); i++) {
            if (dataList.get(i).getCodeName().equalsIgnoreCase(cname))
                return dataList.get(i);
        }
        return null;
    }

    private void initSwipeMenu() {
        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getActivity());
                deleteItem.setBackground(android.R.color.holo_red_light);
                deleteItem.setWidth(commonUtils.dp2px(90));

                deleteItem.setIcon(R.drawable.delete);
                menu.addMenuItem(deleteItem);
            }
        };

        // set creator
        swipewMenuListView.setMenuCreator(creator);

        // Close Interpolator Animation
        swipewMenuListView.setCloseInterpolator(new BounceInterpolator());

        swipewMenuListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        // open
                        if (dataList.size() != position) {
                            removeItem(position);
                        }
                        break;
                }
                return false;
            }
        });

        // set SwipeListener
        swipewMenuListView.setOnSwipeListener(new SwipeMenuListView.OnSwipeListener() {

            @Override
            public void onSwipeStart(int position) {
                // swipe start
            }

            @Override
            public void onSwipeEnd(int position) {
                // swipe end
            }
        });

        // set MenuStateChangeListener
        swipewMenuListView.setOnMenuStateChangeListener(new SwipeMenuListView.OnMenuStateChangeListener() {
            @Override
            public void onMenuOpen(int position) {
                Log.e(TAG, "onMenuOpen");
            }

            @Override
            public void onMenuClose(int position) {
                Log.e(TAG, "onMenuClose");
            }
        });
    }

    public void removeItem(final int position) {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                adapter.removeItemSearch(Conts.listCart.get(position).getCodeName());
                Conts.deleteCard(Conts.listCart.get(position).getCodeName());
                adapter.notifyDataSetChanged();
            }
        }, 500);

    }
}
