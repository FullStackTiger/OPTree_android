package com.tecocraft.optree.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tecocraft.optree.R;
import com.tecocraft.optree.global.Conts;
import com.tecocraft.optree.global.SharedPref;
import com.tecocraft.optree.model.details.Addon;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;


public class AddonListAdapter extends RecyclerView.Adapter<AddonListAdapter.ViewHolder> {


    private Context mContext;
    private List<Addon> list;


    private String searchText = "";
    SharedPref sharedPref;


    public AddonListAdapter(Context mContext, List<Addon> list) {
        this.mContext = mContext;
        this.list = list;
        //    this.searchByNameFragment = searchByNameFragment;

        sharedPref = new SharedPref(mContext);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_add_on, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.position = position;
        final Addon model = list.get(position);
        holder.model = model;

        if (model.isCardAdd())
            holder.ivCart.setImageResource(R.drawable.cart_delete);
        else
            holder.ivCart.setImageResource(R.drawable.cart_add);

        holder.tvTitle.setText("" + colorChange(model.getRolename(), searchText));
        holder.tvSubTitle.setText("" + model.getDescription());

        holder.ivImageview.setVisibility(View.GONE);

        holder.ivCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (model.isCardAdd()) {
                    Conts.deleteCard(model.getRolename());
                    holder.ivCart.setImageResource(R.drawable.cart_add);

                    Addon c = list.get(position);
                    c.setCardAdd(false);
                    list.set(position,c);
                }else{
                    Conts.addCart(model.getRolename(),"","");
                    holder.ivCart.setImageResource(R.drawable.cart_delete);

                    Addon c = list.get(position);
                    c.setCardAdd(true);
                    list.set(position,c);
                }
            }
        });

    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        Addon model;
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

//                    mContext.startActivity(new Intent(mContext, DetailsActivity.class).putExtra(sharedPref.CODE_NAME, model.getCodeName()));
                }
            });
        }
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

}
