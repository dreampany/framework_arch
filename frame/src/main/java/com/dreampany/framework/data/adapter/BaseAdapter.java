package com.dreampany.framework.data.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.common.collect.Maps;
import com.dreampany.framework.R;
import com.dreampany.framework.data.enums.Type;
import com.dreampany.framework.data.listener.RecyclerClickListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public abstract class BaseAdapter<T> extends RecyclerView.Adapter<BaseAdapter.BindingHolder> {

    /**
     * absolute adding, operation
     * relative notify, binding etc
     */

    protected static final int INVALID_INDEX = -1;
    protected static final int VIEW_TYPE_HEADER = 101;
    protected static final int VIEW_TYPE_FOOTER = 102;
    protected static final int VIEW_TYPE_EMPTY = 103;

    protected Map<Integer, Integer> itemLayoutIds; //view type -> layoutId
    protected Map<Integer, Integer> itemVariableIds; //view type -> layoutId
    protected final List<T> items;
    protected int headerLayoutId;
    protected int footerLayoutId;
    protected int emptyLayoutId;

    protected BaseAdapter() {
        itemLayoutIds = Maps.newConcurrentMap();
        itemVariableIds = Maps.newConcurrentMap();
        items = new ArrayList<>();
        headerLayoutId = INVALID_INDEX;
        footerLayoutId = INVALID_INDEX;
        emptyLayoutId = INVALID_INDEX;
    }

    public void addItemLayoutId(Type type, int itemLayoutId, int itemVariableId) {
        itemLayoutIds.put(type.ordinalValue(), itemLayoutId);
        itemVariableIds.put(type.ordinalValue(), itemVariableId);
    }

    public void setHeaderLayoutId(int headerLayoutId) {
        this.headerLayoutId = headerLayoutId;
    }

    public void setFooterLayoutId(int footerLayoutId) {
        this.footerLayoutId = footerLayoutId;
    }

    public void setEmptyLayoutId(int emptyLayoutId) {
        this.emptyLayoutId = emptyLayoutId;
    }

    public void clear() {
        headerLayoutId = INVALID_INDEX;
        footerLayoutId = INVALID_INDEX;
        emptyLayoutId = INVALID_INDEX;
        items.clear();
        notifyDataSetChanged();
    }


    public boolean isAbsoluteEmpty() {
        return items.isEmpty();
    }

    public boolean isRelativeEmpty() {
        return isAbsoluteEmpty() && headerLayoutId == INVALID_INDEX && footerLayoutId == INVALID_INDEX && emptyLayoutId == INVALID_INDEX;
    }

    public boolean isAbsoluteValid(int position) {
        return !(position <= INVALID_INDEX || position >= getAbsoluteCount());
    }

    public boolean isRelativeValid(int position) {
        return !(position <= INVALID_INDEX || position >= getItemCount());
    }

    protected void notifyItemChanged(T item) {
        int position = getRelativePosition(item);
        if (isRelativeValid(position)) {
            notifyItemChanged(position);
        }
    }

    protected void notifyItemInserted(T item) {
        int position = getRelativePosition(item);
        if (isRelativeValid(position)) {
            notifyItemInserted(position);
        }
    }

    protected int resolveToRelative(int position) {
        if (hasHeader()) {
            position += 1;
        }
        return position;
    }


    protected int nextAbsolutePosition(T item) {
        int index = getAbsolutePosition(item);
        return index == INVALID_INDEX ? 0 : index + 1;
    }

    protected int resolveToAbsolute(int position) {
        if (isHeader(position)) {
            return position;
        }

        int realPosition = position;
        if (hasHeader()) {
            realPosition -= 1;
        }
        return realPosition;
    }


    public T getItem(int position) {
        int realPosition = resolveToAbsolute(position);
        if (isAbsoluteValid(realPosition)) {
            return items.get(realPosition);
        }
        return null;
    }

    public List<T> getItems() {
        return items;
    }

    public int addItem(T item) { //absolute addition
        int position = nextAbsolutePosition(item);
        return addItem(position, item);
    }

    public void addItem(List<T> items) {
        for (T item : items) {
            addItem(item);
        }
    }

    public void addAll(List<T> items) {
        this.items.clear();
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    public int addItem(int position, T item) { //absolute addition
        items.add(position, item);
        notifyItemInserted(item);
        return position;
    }

    public boolean addItem(int position, List<T> items) { //absolute addition

        if (items == null || items.isEmpty()) return false;

        this.items.addAll(position, items);
        position = resolveToRelative(position);
        notifyItemRangeInserted(position, getItemCount());

        return true;
    }

    public int updateItem(T item) {
        int position = getAbsolutePosition(item);
        updateItem(position, item);
        return position;
    }

    public void updateItem(int position, T item) {
        if (isAbsoluteValid(position)) {
            items.set(position, item);
            notifyItemChanged(item);
        }
    }

    public void updateItem(T fromItem, T toItem) {
        int index = items.indexOf(toItem);
        notifyItemChanged(index);
    }

    public T removeItem(int position) {
        T item = items.remove(position);
        int realPosition = resolveToRelative(position);
        notifyItemRemoved(realPosition);
        return item;
    }

    public int removeItem(T item) {
        int position = getAbsolutePosition(item);
        if (isAbsoluteValid(position)) {
            removeItem(position);
            return position;
        }
        return -1;
    }

    public void moveItem(int fromPosition, int toPosition) {
        T item = items.remove(fromPosition);
        items.add(toPosition, item);
        notifyItemMoved(fromPosition, toPosition);
    }

    public int resolveItem(T item) {
        int position = updateItem(item); //absolute updating

        if (!isAbsoluteValid(position)) {
            position = addItem(item);
        }

        return position;
    }

    public int resolveItem(int position, T item) {
        int pos = updateItem(item);

        if (!isAbsoluteValid(pos)) {
            position = addItem(position, item);
        }

        return position;
    }

    public void resolveItem(List<T> items) {
        for (T t : items) {
            resolveItem(t);
        }
    }

    public int getAbsolutePosition(T item) {
        return items.indexOf(item);
    }

    public int getRelativePosition(T item) {
        int index = items.indexOf(item);
        if (hasHeader()) {
            index += 1;
        }
        return index;
    }

    protected boolean isHeader(int position) {
        return hasHeader() && position == 0;
    }

    protected boolean isFooter(int position) {
        if (!hasFooter()) {
            return false;
        }

        if (hasHeader()) {
            return position > getAbsoluteCount();
        }

        return position == getAbsoluteCount(); // last index excluding absolute items
    }

    public void animateTo(List<T> newItems) {
        applyAndAnimateRemovals(newItems);
        applyAndAnimateAdditions(newItems);
        applyAndAnimateMovedItems(newItems);
    }

    private void applyAndAnimateRemovals(List<T> newItems) {
        for (int i = items.size() - 1; i >= 0; i--) {
            final T item = items.get(i);
            if (!newItems.contains(item)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(List<T> newItems) {
        for (int i = 0, count = newItems.size(); i < count; i++) {
            final T item = newItems.get(i);
            if (!items.contains(item)) {
                addItem(i, item);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<T> newItems) {
        for (int toPosition = newItems.size() - 1; toPosition >= 0; toPosition--) {
            final T item = newItems.get(toPosition);
            final int fromPosition = items.indexOf(item);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }


    protected int getItemLayoutId(int viewType) {
        switch (viewType) {
            case VIEW_TYPE_HEADER:
                return headerLayoutId;
            case VIEW_TYPE_FOOTER:
                return footerLayoutId;
        }

        return itemLayoutIds.containsKey(viewType) ? itemLayoutIds.get(viewType) : INVALID_INDEX;
    }

    protected int getItemVariableId(int position) {
        if (isHeader(position) || isFooter(position)) {
            return INVALID_INDEX;
        }
        int viewType = getItemViewType(position);
        return itemVariableIds.containsKey(viewType) ? itemVariableIds.get(viewType) : INVALID_INDEX;
    }

    private boolean hasHeader() {
        return headerLayoutId != INVALID_INDEX;
    }

    private boolean hasFooter() {
        return footerLayoutId != INVALID_INDEX;
    }

    private boolean hasEmpty() {
        return emptyLayoutId != INVALID_INDEX;
    }

    protected int getEmptyLayoutId() {
        return R.layout.content_empty;
    }

    protected boolean equals(T leftItem, T rightItem) {
        return leftItem.equals(rightItem);
    }

    public boolean isEmptyItemLayoutView() {
        return (items.size() == 0 && getEmptyLayoutId() > 1);
    }

    public int compare(T left, T right) {
        return left.hashCode() > right.hashCode() ? 1 : 0;
    }

    public Comparator<T> getComparator() {
        return null;
    }

    public void sort() {
        Collections.sort(items, getComparator());
        notifyDataSetChanged();
    }

    public int getAbsoluteCount() {
        return items.size();
    }

    public void resolveSilently(List<T> items) {
        for (T item : items) {
            resolveSilently(item);
        }
    }

    public int resolveSilently(T item) {
        int position = updateSilently(item); //absolute updating

        if (!isAbsoluteValid(position)) {
            position = addSilently(item);
        }

        return position;
    }

    public int updateSilently(T item) {
        int position = getAbsolutePosition(item);
        updateSilently(position, item);
        return position;
    }

    public void updateSilently(int position, T item) {
        if (isAbsoluteValid(position)) {
            items.set(position, item);
        }
    }

    public int addSilently(T item) { //absolute addition
        int position = nextAbsolutePosition(item);
        return addSilently(position, item);
    }

    public int addSilently(int position, T item) { //absolute addition
        items.add(position, item);
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        if (isHeader(position)) {
            return VIEW_TYPE_HEADER;
        }
        if (isFooter(position)) {
            return VIEW_TYPE_FOOTER;
        }
        return INVALID_INDEX;
    }

    @Override
    public int getItemCount() { //this is relative count
        //return isEmptyItemLayoutView() ? 1 : items.size();
        int header = hasHeader() ? 1 : 0;
        int footer = hasFooter() ? 1 : 0;
        return header + getAbsoluteCount() + footer;
    }

    @Override
    public BindingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // View rowView = LayoutInflater.from(parent.getContext()).inflate(getItemLayoutId(viewType), parent, false);
        int itemLayoutId = getItemLayoutId(viewType);
        return new BindingHolder(parent, itemLayoutId);
    }

    @Override
    public void onBindViewHolder(BindingHolder holder, int position) {
        holder.getBinding().setVariable(getItemVariableId(position), getItem(position));
        holder.getBinding().executePendingBindings();
    }

    public RecyclerClickListener recyclerClickListener;

    public void setRecyclerClickListener(RecyclerClickListener recyclerClickListener) {
        this.recyclerClickListener = recyclerClickListener;
    }

    public static final class BindingHolder extends RecyclerView.ViewHolder {
        private final ViewDataBinding binding;

        BindingHolder(View rowView) {
            super(rowView);
            binding = DataBindingUtil.bind(rowView);
        }

        BindingHolder(ViewGroup parent, int layoutId) {
            this(LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false));
        }

        public ViewDataBinding getBinding() {
            return binding;
        }

        public Context getContext() {
            return itemView.getContext();
        }
    }
}
