package com.tecocraft.optree.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tecocraft.optree.R;
import com.tecocraft.optree.adapter.CategoryListAdapter;
import com.tecocraft.optree.global.CommonUtils;
import com.tecocraft.optree.global.Conts;
import com.tecocraft.optree.model.category.CategoryModel;
import com.tecocraft.optree.model.category.Data;
import com.tecocraft.optree.model.details.TreeModel;
import com.tecocraft.optree.rest.ApiClient;
import com.tecocraft.optree.rest.ApiInterface;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;


public class CategoryFragment extends Fragment implements View.OnClickListener {

    @BindView(R.id.rvList)
    public RecyclerView rvList;
    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;
    @BindView(R.id.llCateName)
    LinearLayout llCateName;

    private CategoryListAdapter adapter;
    private List<Data> dataList = new ArrayList<>();
    private List<Data> dataList_temp = new ArrayList<>();
    Call<CategoryModel> call;
    private ProgressDialog pd;
    private CommonUtils commonUtils;

    List<TreeModel> treeModelList = new ArrayList<>();

    Gson gson = new Gson();

    public CategoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_category, container, false);
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
        pd.setMessage("Loading...");
        pd.setCancelable(false);


        getDataFromServer();

    }

    private void getDataFromServer() {

        if (commonUtils.isNetworkAvailable()) {
            pd.show();
            ApiInterface apiService =
                    ApiClient.getClient().create(ApiInterface.class);
            call = apiService.getCategory();
            call.enqueue(new Callback<CategoryModel>() {
                @Override
                public void onResponse(Call<CategoryModel> call, Response<CategoryModel> response) {
                    if (rvList == null)
                        return;
                    if (response.isSuccessful()) {
                        CategoryModel model = response.body();
                        if (model.isSuccess()) {
                            dataList.clear();
                            dataList.addAll(model.getDataList());
                            dataList_temp.addAll(model.getDataList());

//                            TreeModel treeModel = new TreeModel();
//                            treeModel.setRoleName("");
//                            treeModel.setDataList(dataList);
//                            treeModelList.add(treeModel);

                            adapter = new CategoryListAdapter(getActivity(), dataList, CategoryFragment.this);
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
                public void onFailure(Call<CategoryModel> call, Throwable t) {
                    // Log error here since request failed
                    Log.e(TAG, t.toString());
                    if (pd != null && pd.isShowing())
                        pd.dismiss();
                }
            });
        } else {
            CommonUtils.alertDialog(getActivity(),getString(R.string.internet_disconnect));
//            Toast.makeText(getActivity(), "Please check your internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResume() {
        for (int i = 0; i < dataList.size(); i++) {
            if (Conts.checkAddedOrNot(dataList.get(i).getRoleName())) {
                Data c = dataList.get(i);
                c.setCardAdded(true);
                dataList.set(i, c);
            } else {
                Data c = dataList.get(i);
                c.setCardAdded(false);
                dataList.set(i, c);
            }
        }
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
        super.onResume();
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


    public void getSetData(final String rolename, final List<Data> newData, List<Data> oldData) {
        TreeModel treeModel = new TreeModel();
        treeModel.setRoleName(rolename);
        treeModel.setDataInJson("" + gson.toJson(oldData));
        treeModel.setDataArrayList(oldData);
        treeModelList.add(treeModel);

        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View child = inflater.inflate(R.layout.category_child, null);
        TextView textView = (TextView) child.findViewById(R.id.tvCatTitle);
        textView.setText(rolename);
        child.setTag(treeModelList.size());
        child.setOnClickListener(this);
        llCateName.addView(child);

        dataList.clear();
        dataList.addAll(newData);

        for (int i = 0; i < dataList.size(); i++) {

            if (dataList.get(i).getDataList() != null && !dataList.get(i).getDataList().isEmpty()) {
//                categoryFragment.getSetData(model.getRoleName(), model.getDataList(), list);
            } else {
//                mContext.startActivity(new Intent(mContext, DetailsActivity.class).putExtra(sharedPref.CODE_NAME, model.getRoleName()));
                if (Conts.checkAddedOrNot(dataList.get(i).getRoleName())) {
                    Data c = dataList.get(i);
                    c.setCardAdded(true);
                    dataList.set(i, c);
                } else {
                    Data c = dataList.get(i);
                    c.setCardAdded(false);
                    dataList.set(i, c);
                }
            }
        }
        adapter.notifyDataSetChanged();
    }

    private void getClickData(int position) {

//        Log.e("-->", "tree 2nd time : " + treeModelList.size());

        List<Data> splashList = new ArrayList<>();
//        for (int z = 0; z < treeModelList.size(); z++) {
//            splashList = new Gson().fromJson(treeModelList.get(z).getDataInJson(), new TypeToken<List<Data>>() {
//            }.getType());
//            Log.e("-->", "size of : " + treeModelList.get(z).getRoleName() + " =================" + treeModelList.get(z).getDataArrayList().size() + "-------------" + splashList.size());
//        }

        List<TreeModel> treeModelListTemp = new ArrayList<>();
        dataList.clear();
        for (int i = 0; i < treeModelList.size(); i++) {
//            if (i <= position){
//                treeModelListTemp.add(treeModelList.get(i));
//            }

//            Log.e("-->>", "position " + position + "   " + i);
            if (i == position) {
//                Log.e("-->>", "True in in");
//                Log.e("-->>", "True in in " + splashList.size());
//                List<Data> oldDataList = new Gson().fromJson(treeModelList.get(i).getDataInJson(), new TypeToken<List<Data>>() {
//                }.getType());
                splashList = new Gson().fromJson(treeModelList.get(i).getDataInJson(), new TypeToken<List<Data>>() {
                }.getType());
                dataList.addAll(splashList);
            }

            if (i < position) {
                treeModelListTemp.add(treeModelList.get(i));
            }
        }
        adapter.notifyDataSetChanged();

        treeModelList.clear();
        treeModelList.addAll(treeModelListTemp);

        if (((LinearLayout) llCateName).getChildCount() > 0)
            ((LinearLayout) llCateName).removeAllViews();

        for (int i = 0; i < treeModelList.size(); i++) {
            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View child = inflater.inflate(R.layout.category_child, null);
            TextView textView = (TextView) child.findViewById(R.id.tvCatTitle);
            textView.setText(treeModelList.get(i).getRoleName());
            child.setTag(i + 1);
            child.setOnClickListener(this);
            llCateName.addView(child);
        }
    }

    private void getSearchCatName(List<Data> dataList, String catName) {
        for (int i = 0; i < dataList.size(); i++) {
            if (dataList.get(i).getRoleName().equalsIgnoreCase(catName)) {
                this.dataList.clear();
                this.dataList.addAll(dataList);
                adapter.notifyDataSetChanged();
                return;
            }
            getSearchCatName(dataList.get(i).getDataList(), catName);
        }
    }

    @Override
    public void onClick(View v) {
        int tag = (int) v.getTag();
//        getSearchCatName(dataList_temp, tag);
        getClickData(tag - 1);
    }
}
