package com.tecocraft.optree.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tecocraft.optree.R;
import com.tecocraft.optree.fragment.CartFragment;
import com.tecocraft.optree.global.CommonUtils;
import com.tecocraft.optree.global.Conts;
import com.tecocraft.optree.global.SharedPref;
import com.tecocraft.optree.model.name.Code;
import com.tecocraft.optree.ui.DetailsActivity;
import com.tecocraft.optree.widget.BaseSwipListAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;


public class CartListAdapter extends BaseSwipListAdapter {


    private Context mContext;
    private List<Code> list;
    private CartFragment cartFragment;

    private List<Code> searchListFilter;
    private String searchText = "";
    SharedPref sharedPref;
    CommonUtils commonUtils;


    public CartListAdapter(Context mContext, List<Code> list, CartFragment cartFragment) {
        this.mContext = mContext;
        this.list = list;
        this.cartFragment = cartFragment;
        this.searchListFilter = new ArrayList<>();
        this.searchListFilter.addAll(list);
        sharedPref = new SharedPref(mContext);
        commonUtils = new CommonUtils(mContext);
    }

//    @Override
//    public int getItemViewType(int position) {
//        return position;
//    }

//    @Override
//    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_search_name, parent, false);
//        return new ViewHolder(itemView);
//    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Code getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = View.inflate(mContext,
                    R.layout.row_search_name, null);
            new ViewHolder(convertView);
        }

        final ViewHolder holder = (ViewHolder) convertView.getTag();

        holder.position = position;
        final Code model = getItem(position);
        holder.model = model;

        holder.tvTitle.setText("" + model.getCodeName());
        holder.tvSubTitle.setText("" + model.getCodeDescription());

        Glide.with(mContext).load(model.getDetailImg())
                .placeholder(R.drawable.no_available)
                .error(R.drawable.no_available)
                .into(holder.ivImageview);
        holder.ivCart.setImageResource(R.drawable.cart_delete);

        holder.ivCart.setVisibility(View.GONE);
        holder.ivCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                list.remove(position);
                for (int i = 0; i < searchListFilter.size(); i++) {
                    if (searchListFilter.get(i).getCodeName().equals(model.getCodeName())) {
                        searchListFilter.remove(i);
                        break;
                    }
                }

                Conts.deleteCard(model.getCodeName());
                notifyDataSetChanged();
            }

        });

        return convertView;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        Code model;
        int position;

        @BindView(R.id.ivImageview)
        ImageView ivImageview;
        @BindView(R.id.tvTitle)
        TextView tvTitle;
        @BindView(R.id.tvSubTitle)
        TextView tvSubTitle;
        @BindView(R.id.llRow)
        LinearLayout llRow;
        @BindView(R.id.ivCart)
        ImageView ivCart;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            llRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (commonUtils.isNetworkAvailable())
                        mContext.startActivity(new Intent(mContext, DetailsActivity.class).putExtra(sharedPref.CODE_NAME, model.getCodeName()));
                    else
                        CommonUtils.alertDialog(mContext, mContext.getString(R.string.internet_disconnect));
                }
            });
            itemView.setTag(this);
        }
    }

//    // Filter method
//    public void filter(String charText) {
//
//        charText = charText.toLowerCase(Locale.getDefault());
//        searchText = charText;
//        list.clear();
//        if (charText.length() == 0) {
//            list.addAll(searchListFilter);
//        } else {
//            String[] splited = charText.split("\\s+");
//            List<String> stringList = new ArrayList<String>(Arrays.asList(splited));
//            for (int i = 0; i < searchListFilter.size(); i++) {
//                int isMatched = 0;
//                for (int i1 = 0; i1 < stringList.size(); i1++) {
//                    if (searchListFilter.get(i).getSearchString().toLowerCase(Locale.getDefault()).contains(stringList.get(i1).toLowerCase(Locale.getDefault()))) {
//                        isMatched = isMatched + 1;
//                    }
//                }
//                if (isMatched == stringList.size())
//                    list.add(searchListFilter.get(i));
//            }
//        }
//
//        if (list.isEmpty()) {
//            cartFragment.swipewMenuListView.setVisibility(View.GONE);
//            cartFragment.tvError.setVisibility(View.VISIBLE);
//        } else {
//            cartFragment.tvError.setVisibility(View.GONE);
//            cartFragment.swipewMenuListView.setVisibility(View.VISIBLE);
//        }
//
//        notifyDataSetChanged();
//    }

    // Filter method
    public void filter(String charText) {
        searchText = charText;
        list.clear();
        if (charText.length() == 0) {
            list.addAll(searchListFilter);
        } else {
            String[] splited = charText.split("\\s+");
            List<String> stringList = new ArrayList<String>(Arrays.asList(splited));
            for (int i = 0; i < searchListFilter.size(); i++) {
                int isMatched = 0;
                for (int i1 = 0; i1 < stringList.size(); i1++) {
                    if (searchListFilter.get(i).getSearchString().toLowerCase(Locale.getDefault()).contains(stringList.get(i1).toLowerCase(Locale.getDefault()))) {
                        isMatched = isMatched + 1;
                    }
                }
                if (isMatched == stringList.size())
                    list.add(searchListFilter.get(i));
            }
        }

        if (list.isEmpty()) {
            cartFragment.swipewMenuListView.setVisibility(View.GONE);
            cartFragment.tvError.setVisibility(View.VISIBLE);
        } else {
            cartFragment.tvError.setVisibility(View.GONE);
            cartFragment.swipewMenuListView.setVisibility(View.VISIBLE);
        }

        notifyDataSetChanged();
    }

    // color change filter
    public SpannableStringBuilder colorChange(String current, String searchText) {
        try {
            SpannableStringBuilder sb = new SpannableStringBuilder(current);
            Pattern p = Pattern.compile(searchText, Pattern.CASE_INSENSITIVE);
            Matcher m = p.matcher(current);
            while (m.find()) {
                sb.setSpan(new ForegroundColorSpan(Color.parseColor("#FB5A14")), m.start(), m.end(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);

            }
            return sb;
        } catch (Exception e) {
            return new SpannableStringBuilder(current);
        }
    }

    public void removeItemSearch(String cname) {
        for (int i = 0; i < searchListFilter.size(); i++) {
            if (cname.equalsIgnoreCase(searchListFilter.get(i).getCodeName())) {
                searchListFilter.remove(i);
                return;
            }
        }
    }

}
