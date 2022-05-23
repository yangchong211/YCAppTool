package com.yc.selectviewlib;


import android.os.Bundle;

import androidx.annotation.CallSuper;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public abstract class SelectRecyclerViewAdapter<VH extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<VH> {

    private ArrayList<Integer> selectedIndices = new ArrayList<>();
    private SelectionListener selectionListener;
    private int lastCount = -1;
    private int maxSelectionCount = -1;

    private void fireSelectionListener() {
        if (this.lastCount != this.selectedIndices.size()) {
            this.lastCount = this.selectedIndices.size();
            if (this.selectionListener != null) {
                this.selectionListener.onDragSelectionChanged(this.lastCount);
            }

        }
    }

    protected SelectRecyclerViewAdapter() {
    }

    @CallSuper
    public void onBindViewHolder(VH holder, int position) {
        holder.itemView.setTag(holder);
    }

    public void setMaxSelectionCount(int maxSelectionCount) {
        this.maxSelectionCount = maxSelectionCount;
    }

    public void setSelectionListener(SelectionListener selectionListener) {
        this.selectionListener = selectionListener;
    }

    public void saveInstanceState(Bundle out) {
        this.saveInstanceState("selected_indices", out);
    }

    public void saveInstanceState(String key, Bundle out) {
        out.putSerializable(key, this.selectedIndices);
    }

    public void restoreInstanceState(Bundle in) {
        this.restoreInstanceState("selected_indices", in);
    }

    public void restoreInstanceState(String key, Bundle in) {
        if (in != null && in.containsKey(key)) {
            this.selectedIndices = (ArrayList)in.getSerializable(key);
            if (this.selectedIndices == null) {
                this.selectedIndices = new ArrayList();
            } else {
                this.fireSelectionListener();
            }
        }

    }

    public final void setSelected(int index, boolean selected) {
        if (!this.isIndexSelectable(index)) {
            selected = false;
        }

        if (selected) {
            if (!this.selectedIndices.contains(index) && (this.maxSelectionCount == -1 || this.selectedIndices.size() < this.maxSelectionCount)) {
                this.selectedIndices.add(index);
                this.notifyItemChanged(index);
            }
        } else if (this.selectedIndices.contains(index)) {
            this.selectedIndices.remove(index);
            this.notifyItemChanged(index);
        }

        this.fireSelectionListener();
    }

    public final boolean toggleSelected(int index) {
        boolean selectedNow = false;
        if (this.isIndexSelectable(index)) {
            if (this.selectedIndices.contains(index)) {
                this.selectedIndices.remove(index);
            } else if (this.maxSelectionCount == -1 || this.selectedIndices.size() < this.maxSelectionCount) {
                this.selectedIndices.add(index);
                selectedNow = true;
            }

            this.notifyItemChanged(index);
        }

        this.fireSelectionListener();
        return selectedNow;
    }

    public final void selectRange(int from, int to, int min, int max) {
        int i;
        if (from == to) {
            for(i = min; i <= max; ++i) {
                if (i != from) {
                    this.setSelected(i, false);
                }
            }

            this.fireSelectionListener();
        } else {
            if (to < from) {
                for(i = to; i <= from; ++i) {
                    this.setSelected(i, true);
                }

                if (min > -1 && min < to) {
                    for(i = min; i < to; ++i) {
                        if (i != from) {
                            this.setSelected(i, false);
                        }
                    }
                }

                if (max > -1) {
                    for(i = from + 1; i <= max; ++i) {
                        this.setSelected(i, false);
                    }
                }
            } else {
                for(i = from; i <= to; ++i) {
                    this.setSelected(i, true);
                }

                if (max > -1 && max > to) {
                    for(i = to + 1; i <= max; ++i) {
                        if (i != from) {
                            this.setSelected(i, false);
                        }
                    }
                }

                if (min > -1) {
                    for(i = min; i < from; ++i) {
                        this.setSelected(i, false);
                    }
                }
            }

            this.fireSelectionListener();
        }
    }

    protected boolean isIndexSelectable(int index) {
        return true;
    }

    public final void selectAll() {
        int max = this.getItemCount();
        this.selectedIndices.clear();

        for(int i = 0; i < max; ++i) {
            if (this.isIndexSelectable(i)) {
                this.selectedIndices.add(i);
            }
        }

        this.notifyDataSetChanged();
        this.fireSelectionListener();
    }

    public final void clearSelected() {
        this.selectedIndices.clear();
        this.notifyDataSetChanged();
        this.fireSelectionListener();
    }

    public final int getSelectedCount() {
        return this.selectedIndices.size();
    }

    public final Integer[] getSelectedIndices() {
        return (Integer[])this.selectedIndices.toArray(new Integer[this.selectedIndices.size()]);
    }

    public final boolean isIndexSelected(int index) {
        return this.selectedIndices.contains(index);
    }
}

