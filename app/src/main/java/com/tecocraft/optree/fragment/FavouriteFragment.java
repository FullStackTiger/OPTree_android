package com.tecocraft.optree.fragment;

import android.app.ProgressDialog;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
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
import com.tecocraft.optree.adapter.FavouriteListAdapter;
import com.tecocraft.optree.global.CommonUtils;
import com.tecocraft.optree.global.SharedPref;
import com.tecocraft.optree.model.favourite.Favlist;
import com.tecocraft.optree.model.favourite.GetFavourite;
import com.tecocraft.optree.model.favourite.addFavourite;
import com.tecocraft.optree.rest.ApiClient;
import com.tecocraft.optree.rest.ApiInterface;
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

import static android.content.ContentValues.TAG;


public class FavouriteFragment extends Fragment {


    @BindView(R.id.swipewMenuListView)
    public SwipeMenuListView swipewMenuListView;
    @BindView(R.id.etSearch)
    EditText etSearch;
    @BindView(R.id.tvError)
    public TextView tvError;
    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;

    private FavouriteListAdapter adapter;
    private List<Favlist> dataList = new ArrayList<>();
    Call<GetFavourite> call;
    private ProgressDialog pd;
    private CommonUtils commonUtils;
    SharedPref sharedPref;
    Paint p = new Paint();


    public FavouriteFragment() {
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

        sharedPref = new SharedPref(getActivity());

//        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
//        rvList.setLayoutManager(mLayoutManager);
//        rvList.setItemAnimator(new DefaultItemAnimator());
        commonUtils = new CommonUtils(getActivity());

        pd = new ProgressDialog(getActivity());
        pd.setCancelable(false);
        pd.setMessage("Loading...");


        getDataFromServer();

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
                    adapter.filter(etSearch.getText().toString().trim());
            }
        });
        initSwipeMenu();

    }

    private void getDataFromServer() {

        if (commonUtils.isNetworkAvailable()) {
            pd.show();
            ApiInterface apiService =
                    ApiClient.getClient().create(ApiInterface.class);
            call = apiService.getFavourite("" + sharedPref.getDataFromPref(sharedPref.USER_ID));
            call.enqueue(new Callback<GetFavourite>() {
                @Override
                public void onResponse(Call<GetFavourite> call, Response<GetFavourite> response) {
                    if (swipewMenuListView == null || tvError == null)
                        return;

                    if (response.isSuccessful()) {
                        GetFavourite model = response.body();
                        if (model.getSuccess()) {
                            dataList.clear();
                            dataList.addAll(model.getFavlist());

                            for (int i = 0; i < dataList.size(); i++) {
                                String search = dataList.get(i).getCname() + " " + dataList.get(i).getDescription().replaceAll("[-+.^:,]", "") + " " +
                                        dataList.get(i).getCname();

                                String search1 = search.toLowerCase(Locale.getDefault());
                                String[] splited = search1.split("\\s+");
                                List<String> stringList = new ArrayList<String>(Arrays.asList(splited));

                                Favlist c = dataList.get(i);
                                c.setSearchString(search);
                                c.setSearchList(stringList);
                                dataList.set(i, c);
                            }

                            adapter = new FavouriteListAdapter(getActivity(), dataList, FavouriteFragment.this);
                            swipewMenuListView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(getActivity(), "Please try again", Toast.LENGTH_SHORT).show();
                        }
                    }

                    if (pd != null && pd.isShowing())
                        pd.dismiss();

                    if (dataList.isEmpty()) {
                        swipewMenuListView.setVisibility(View.GONE);
                        tvError.setVisibility(View.VISIBLE);
                    } else {
                        tvError.setVisibility(View.GONE);
                        swipewMenuListView.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onFailure(Call<GetFavourite> call, Throwable t) {
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


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onPause() {
        super.onPause();
//        if (call != null)
//            call.cancel();
//
//        if (pd != null && pd.isShowing())
//            pd.dismiss();
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
//                deleteItem.setTitle("Delete");

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
//                        Log.e(TAG, "Click O");
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
//                Log.e(TAG, "swipe start");
            }

            @Override
            public void onSwipeEnd(int position) {
                // swipe end
//                Log.e(TAG, "swipe end");
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

    public void removeItem(int position) {
        removeFavourite(dataList.get(position).getCname() + "", position);
    }

    private void removeFavourite(final String codeName, final int position) {
        if (commonUtils.isNetworkAvailable()) {
            pd.show();
            ApiInterface apiService =
                    ApiClient.getClient().create(ApiInterface.class);
            Call<addFavourite> call = apiService.removeFavourite("" + sharedPref.getDataFromPref(sharedPref.USER_ID), "" + codeName);
            call.enqueue(new Callback<addFavourite>() {
                @Override
                public void onResponse(Call<addFavourite> call, Response<addFavourite> response) {
                    if (response.isSuccessful()) {
                        addFavourite faveModel = response.body();
                        if (faveModel.getSuccess()) {
                            dataList.remove(position);

                            adapter.removeItemSearch(codeName);
                            if (dataList.isEmpty()) {
                                swipewMenuListView.setVisibility(View.GONE);
                                tvError.setVisibility(View.VISIBLE);
                            } else {
                                tvError.setVisibility(View.GONE);
                                swipewMenuListView.setVisibility(View.VISIBLE);
                            }
                            CommonUtils.alertDialog(getActivity(), getString(R.string.success_added_delete), "Success");
                        } else {
                            Toast.makeText(getActivity(), "Something went wrong...", Toast.LENGTH_SHORT).show();
                        }
                    }

                    adapter.notifyDataSetChanged();

                    if (pd != null && pd.isShowing())
                        pd.dismiss();
                }

                @Override
                public void onFailure(Call<addFavourite> call, Throwable t) {
                    // Log error here since request failed
                    if (pd != null && pd.isShowing())
                        pd.dismiss();


                }
            });
        } else {
            CommonUtils.alertDialog(getActivity(), getString(R.string.internet_disconnect));
//            Toast.makeText(getActivity(), "Please check your internet connection", Toast.LENGTH_SHORT).show();
        }
    }
}
