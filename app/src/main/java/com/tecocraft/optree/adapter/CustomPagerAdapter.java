package com.tecocraft.optree.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.tecocraft.optree.R;
import com.tecocraft.optree.global.CommonUtils;
import com.tecocraft.optree.model.details.Detail;
import com.tecocraft.optree.ui.WebviewActivity1;

import java.util.List;

public class CustomPagerAdapter extends PagerAdapter {

    private Context mContext;
    private List<Detail> detailList;
    CommonUtils commonUtils;

    public CustomPagerAdapter(Context context, List<Detail> detailList) {
        mContext = context;
        this.detailList = detailList;
        commonUtils = new CommonUtils(context);
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        final Detail modelObject = detailList.get(position);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.row_pager, collection, false);
        ImageView ivCodeDetail = (ImageView) layout.findViewById(R.id.ivCodeDetail);
        collection.addView(layout);


        Glide.with(mContext).load(modelObject.getImageUrl())
                .placeholder(R.drawable.no_available)
                .error(R.drawable.no_available)
                .into(ivCodeDetail);
        ivCodeDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (modelObject.getSiteUrl() != null && !modelObject.getSiteUrl().equals("")) {
                    if (commonUtils.isNetworkAvailable())
                    mContext.startActivity(new Intent(mContext, WebviewActivity1.class).putExtra("SiteUrl", modelObject.getSiteUrl()));
                    else
                        CommonUtils.alertDialog(mContext, mContext.getString(R.string.internet_disconnect));
                }

            }
        });

        return layout;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount() {
        return detailList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }




}