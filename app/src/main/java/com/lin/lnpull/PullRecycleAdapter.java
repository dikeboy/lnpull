package com.lin.lnpull;

import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * author : leo
 * date   : 2018/10/2915:37
 */
public  abstract class PullRecycleAdapter<VH extends  PullRecycleAdapter.PullViewHolder> extends RecyclerView.Adapter<PullRecycleAdapter.PullViewHolder> {
    public static int VIEW_TYPE_HEADER=0;
    public static int VIEW_TYPE_CONTENT=1;
    public static int VIEW_TYPE_BOTTOM=2;
    protected PullToRecycleView pullToRecycleView;

    public PullRecycleAdapter(PullToRecycleView pullToRecycleView){
        this.pullToRecycleView = pullToRecycleView;
    }
    @NonNull
    @Override
    public PullViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if(i==VIEW_TYPE_HEADER){
            return new PullViewHolder(pullToRecycleView.getHeader());
        }
        else{
            return onCreateMViewHolder(viewGroup,i);
        }
    }
    @Override
    public void onBindViewHolder(@NonNull PullViewHolder pullViewHolder, int i) {
        if(i>0){
            onBindMViewHolder((VH)pullViewHolder,i-1);
        }
    }

    @Override
    public int getItemCount() {
        return getMItemCount()+1;
    }

    public abstract PullViewHolder onCreateMViewHolder(ViewGroup viewGroup, int i);
    public abstract void onBindMViewHolder(@NonNull VH pullViewHolder, int i);
    public abstract int getMItemCount();
    @Override
    public int getItemViewType(int position) {
        if(position==0){
            return  VIEW_TYPE_HEADER;
        }
        return VIEW_TYPE_CONTENT;
    }
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if(manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return getItemViewType(position) == VIEW_TYPE_HEADER
                            ? gridManager.getSpanCount() : 1;
                }
            });
        }
    }

    @Override
    public void onViewAttachedToWindow(@NonNull PullViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
        if(lp != null
                && lp instanceof StaggeredGridLayoutManager.LayoutParams) {
            StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
            p.setFullSpan(holder.getLayoutPosition() == 0);
        }
    }

    static  public class PullViewHolder extends  RecyclerView.ViewHolder{
        public PullViewHolder(View itemView){
            super(itemView);
        }
    }


}

