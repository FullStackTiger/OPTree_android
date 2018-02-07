package com.tecocraft.optree.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tecocraft.optree.R;
import com.tecocraft.optree.fragment.CategoryFragment;
import com.tecocraft.optree.fragment.SearchByNameFragment;
import com.tecocraft.optree.global.CommonUtils;
import com.tecocraft.optree.global.Conts;
import com.tecocraft.optree.global.SharedPref;
import com.tecocraft.optree.model.category.Data;
import com.tecocraft.optree.ui.DetailsActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class CategoryListAdapter extends RecyclerView.Adapter<CategoryListAdapter.ViewHolder> {


    private Context mContext;
    private List<Data> list;
    private CategoryFragment categoryFragment;
    private SearchByNameFragment searchByNameFragment;

    private List<Data> searchListFilter;
    private String searchText = "";
    SharedPref sharedPref;
    CommonUtils commonUtils;


    public CategoryListAdapter(Context mContext, List<Data> list, CategoryFragment categoryFragment) {
        this.mContext = mContext;
        this.list = list;
        this.categoryFragment = categoryFragment;
        this.searchByNameFragment = searchByNameFragment;
        this.searchListFilter = new ArrayList<>();
        this.searchListFilter.addAll(list);
        sharedPref = new SharedPref(mContext);
        commonUtils = new CommonUtils(mContext);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_category, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.position = position;
        final Data model = list.get(position);
        holder.model = model;

        holder.tvTitle.setText("" + model.getRoleName());


        if (model.isCardAdded())
            holder.ivCart.setImageResource(R.drawable.cart_delete);
        else
            holder.ivCart.setImageResource(R.drawable.cart_add);

        if (model.getDataList() != null && !model.getDataList().isEmpty()) {
            holder.ivCart.setVisibility(View.GONE);
        } else {
            holder.ivCart.setVisibility(View.VISIBLE);
        }

        holder.ivCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (model.isCardAdded()) {
                    Conts.deleteCard(model.getRoleName());
                    holder.ivCart.setImageResource(R.drawable.cart_add);

                    Data c = list.get(position);
                    c.setCardAdded(false);
                    list.set(position, c);
                } else {
                    Conts.addCart(model.getRoleName(), "", "");
                    holder.ivCart.setImageResource(R.drawable.cart_delete);

                    Data c = list.get(position);
                    c.setCardAdded(true);
                    list.set(position, c);
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        Data model;
        int position;


        @BindView(R.id.tvTitle)
        TextView tvTitle;
        @BindView(R.id.ivCart)
        ImageView ivCart;

        @BindView(R.id.llRow)
        LinearLayout llRow;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            llRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (model.getDataList() != null && !model.getDataList().isEmpty()) {
                        categoryFragment.getSetData(model.getRoleName(), model.getDataList(), list);
                    } else {
                        if (commonUtils.isNetworkAvailable())
                            mContext.startActivity(new Intent(mContext, DetailsActivity.class).putExtra(sharedPref.CODE_NAME, model.getRoleName()));
                        else
                            CommonUtils.alertDialog(mContext, mContext.getString(R.string.internet_disconnect));
                    }
                }
            });
        }
    }
}
