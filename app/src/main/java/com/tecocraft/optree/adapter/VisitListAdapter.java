package com.tecocraft.optree.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tecocraft.optree.R;
import com.tecocraft.optree.model.DrawerItemModel;
import com.tecocraft.optree.ui.MainActivity;

import java.util.List;


public class VisitListAdapter extends RecyclerView.Adapter<VisitListAdapter.ViewHolder> {

    private Context mContext;
    private List<DrawerItemModel> list;


    public VisitListAdapter(Context mContext, List<DrawerItemModel> list) {
        this.mContext = mContext;
        this.list = list;


    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_row_navigation, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.position = position;
        final DrawerItemModel model = list.get(position);
        holder.model = model;

        holder.tvTitle.setText("" + model.getName());
        holder.ivImageview.setImageResource(model.getImage());


    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        DrawerItemModel model;
        int position;

        LinearLayout llRow;
        TextView tvTitle;
        ImageView ivImageview;

        public ViewHolder(View itemView) {
            super(itemView);

            llRow = (LinearLayout) itemView.findViewById(R.id.llRow);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            ivImageview = (ImageView) itemView.findViewById(R.id.ivImageview);
            llRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainActivity) mContext).drawerItemClick(position);

                }
            });
        }
    }


}
