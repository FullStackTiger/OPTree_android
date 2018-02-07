package com.tecocraft.optree.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tecocraft.optree.R;
import com.tecocraft.optree.adapter.SearchNameListAdapter;
import com.tecocraft.optree.global.CommonUtils;
import com.tecocraft.optree.global.Conts;
import com.tecocraft.optree.model.name.Code;
import com.tecocraft.optree.model.name.SearchNameModel;
import com.tecocraft.optree.rest.ApiClient;
import com.tecocraft.optree.rest.ApiInterface;

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


public class SearchByNameFragment extends Fragment {


    @BindView(R.id.rvList)
    public RecyclerView rvList;
    @BindView(R.id.etSearch)
    EditText etSearch;
    @BindView(R.id.tvError)
    public TextView tvError;
    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;

    private SearchNameListAdapter adapter;
    private List<Code> dataList = new ArrayList<>();
    Call<SearchNameModel> call;
    private ProgressDialog pd;
    private CommonUtils commonUtils;

    @Override
    public void onResume() {
        for (int i = 0; i < dataList.size(); i++) {
            if (Conts.checkAddedOrNot(dataList.get(i).getCodeName())) {
                Code c = dataList.get(i);
                c.setCardAdd(true);
                dataList.set(i, c);
            } else {
                Code c = dataList.get(i);
                c.setCardAdd(false);
                dataList.set(i, c);
            }
        }
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
        super.onResume();
    }

    public SearchByNameFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search_by_name, container, false);
        ButterKnife.bind(this, view);
        init();
        return view;
    }

    private void init() {

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        rvList.setLayoutManager(mLayoutManager);
        rvList.setItemAnimator(new DefaultItemAnimator());
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
    }

    private void getDataFromServer() {

        if (commonUtils.isNetworkAvailable()) {
            pd.show();
            ApiInterface apiService =
                    ApiClient.getClient().create(ApiInterface.class);
            call = apiService.getCodes();
            call.enqueue(new Callback<SearchNameModel>() {
                @Override
                public void onResponse(Call<SearchNameModel> call, Response<SearchNameModel> response) {
                    if (rvList == null)
                        return;
                    if (response.isSuccessful()) {
                        SearchNameModel model = response.body();
                        if (model.isSuccess()) {
                            dataList.clear();
                            dataList.addAll(model.getCodes());

                            for (int i = 0; i < dataList.size(); i++) {
                                if (Conts.checkAddedOrNot(dataList.get(i).getCodeName())) {
                                    Code c = dataList.get(i);
                                    c.setCardAdd(true);
                                    dataList.set(i, c);
                                } else {
                                    Code c = dataList.get(i);
                                    c.setCardAdd(false);
                                    dataList.set(i, c);
                                }

                                String search = dataList.get(i).getCodeName() + " " + dataList.get(i).getCodeDescription().replaceAll("[-+.^:,]", "") + " " +
                                        dataList.get(i).getCodeName();

                                String search1 = search.toLowerCase(Locale.getDefault());
                                String[] splited = search1.split("\\s+");
                                List<String> stringList = new ArrayList<String>(Arrays.asList(splited));

                                Code c = dataList.get(i);
                                c.setSearchString(search);
                                c.setSearchList(stringList);
                                dataList.set(i, c);
                            }

                            adapter = new SearchNameListAdapter(getActivity(), dataList, SearchByNameFragment.this);
                            rvList.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
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


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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


}
