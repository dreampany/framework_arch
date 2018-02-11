package com.dreampany.framework.data.api.swipe;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dreampany.framework.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by air on 11/7/17.
 */

public class DiscreteAdapter extends RecyclerView.Adapter<DiscreteAdapter.ViewHolder> {

    private final List<Item> items;

    public DiscreteAdapter() {
        items = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.item_discrete, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Item item = items.get(position);
        holder.title.setText(item.title);
        holder.description.setText(item.description);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(Item item) {
        int index = items.indexOf(item);
        if (index >= 0) {
            items.set(index, item);
            notifyItemChanged(index);
            return;
        }
        items.add(getItemCount(), item);
        notifyItemInserted(getItemCount());
    }

    public void addItems(Item... items) {
        List<Item> list = Arrays.asList(items);
        this.items.clear();
        this.items.addAll(list);
        notifyDataSetChanged();
    }

    public Item getItem(int position) {
        if (position < 0 || position >= getItemCount()) {
            return null;
        }
        return this.items.get(position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView description;

        ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.viewTitle);
            description = itemView.findViewById(R.id.viewDescription);
        }
    }
}
