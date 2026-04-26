package com.example.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.model.Snack;

import java.util.ArrayList;

public class SnackAdapter extends BaseAdapter {

    public interface OnQuantityChangeListener {
        void onQuantityChanged();
    }

    private final Context context;
    private final ArrayList<Snack> snacks;
    private final int[] quantities;
    private final OnQuantityChangeListener listener;

    public SnackAdapter(Context context, ArrayList<Snack> snacks, OnQuantityChangeListener listener) {
        this.context = context;
        this.snacks = snacks;
        this.quantities = new int[snacks.size()];
        this.listener = listener;
    }

    public int getTotalCost() {
        int total = 0;
        for (int i = 0; i < snacks.size(); i++) {
            total += quantities[i] * snacks.get(i).getPrice();
        }
        return total;
    }

    public int getQuantity(int index) {
        return quantities[index];
    }

    @Override
    public int getCount() { return snacks.size(); }

    @Override
    public Object getItem(int position) { return snacks.get(position); }

    @Override
    public long getItemId(int position) { return position; }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_snack, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Snack snack = snacks.get(position);

        // Resolve image: prefer imageResId, fall back to name-based lookup
        if (snack.getImageResId() != 0) {
            holder.imgSnack.setImageResource(snack.getImageResId());
        } else if (snack.getImageName() != null && !snack.getImageName().isEmpty()) {
            int resId = context.getResources().getIdentifier(
                    snack.getImageName(), "drawable", context.getPackageName());
            if (resId != 0) {
                holder.imgSnack.setImageResource(resId);
            }
        }

        holder.tvName.setText(snack.getName());
        holder.tvDesc.setText(snack.getDescription());
        holder.tvPrice.setText("$" + snack.getPrice());
        holder.tvQty.setText(String.valueOf(quantities[position]));

        final int pos = position;

        holder.btnPlus.setOnClickListener(v -> {
            quantities[pos]++;
            holder.tvQty.setText(String.valueOf(quantities[pos]));
            listener.onQuantityChanged();
        });

        holder.btnMinus.setOnClickListener(v -> {
            if (quantities[pos] > 0) {
                quantities[pos]--;
                holder.tvQty.setText(String.valueOf(quantities[pos]));
                listener.onQuantityChanged();
            }
        });

        return convertView;
    }

    static class ViewHolder {
        ImageView imgSnack;
        TextView tvName, tvDesc, tvPrice, tvQty;
        Button btnPlus, btnMinus;

        ViewHolder(View view) {
            imgSnack = view.findViewById(R.id.imgSnack);
            tvName = view.findViewById(R.id.tvSnackName);
            tvDesc = view.findViewById(R.id.tvSnackDesc);
            tvPrice = view.findViewById(R.id.tvSnackPrice);
            tvQty = view.findViewById(R.id.tvSnackQty);
            btnPlus = view.findViewById(R.id.btnSnackPlus);
            btnMinus = view.findViewById(R.id.btnSnackMinus);
        }
    }
}
