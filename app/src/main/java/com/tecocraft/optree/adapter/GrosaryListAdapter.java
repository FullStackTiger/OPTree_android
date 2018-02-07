package com.tecocraft.optree.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tecocraft.optree.R;
import com.tecocraft.optree.fragment.GlosaryFragment;
import com.tecocraft.optree.global.SharedPref;
import com.tecocraft.optree.model.GlosaryModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class GrosaryListAdapter extends RecyclerView.Adapter<GrosaryListAdapter.ViewHolder> {


    private Context mContext;
    private List<GlosaryModel> list;
    private GlosaryFragment searchByNameFragment;

    private List<GlosaryModel> searchListFilter;
    private String searchText = "";
    SharedPref sharedPref;


    public GrosaryListAdapter(Context mContext, List<GlosaryModel> list, GlosaryFragment searchByNameFragment) {
        this.mContext = mContext;
        this.list = list;
        this.searchByNameFragment = searchByNameFragment;
        this.searchListFilter = new ArrayList<>();
        this.searchListFilter.addAll(list);
        sharedPref = new SharedPref(mContext);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_grossary, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.position = position;
        final GlosaryModel model = list.get(position);
        holder.model = model;
        holder.tvTitle.setText(model.getTitle());
        holder.tvSubTitle.setText(model.getContent());

    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        GlosaryModel model;
        int position;


        @BindView(R.id.tvTitle)
        TextView tvTitle;
        @BindView(R.id.tvSubTitle)
        TextView tvSubTitle;
        @BindView(R.id.llRow)
        LinearLayout llRow;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);


            llRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }


}
