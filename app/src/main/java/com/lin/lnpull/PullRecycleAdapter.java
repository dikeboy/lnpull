package com.lin.lnpull;

import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
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
    protected PullRecycleView pullRecycleView;
    private boolean  needShowMore  = false;

    public PullRecycleAdapter(PullRecycleView pullRecycleView){
        this.pullRecycleView = pullRecycleView;
    }
    @NonNull
    @Override
    public PullViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if(i==VIEW_TYPE_HEADER){
            return new PullViewHolder(pullRecycleView.getListView().getHeader());
        }
        else if(i==VIEW_TYPE_BOTTOM){
            return new PullViewHolder(pullRecycleView.getMoreLayout());
        }
        else{
            return onCreateMViewHolder(viewGroup,i);
        }
    }
    @Override
    public void onBindViewHolder(@NonNull PullViewHolder pullViewHolder, int i) {
        if(i>0&&i<=getMItemCount()){
            onBindMViewHolder((VH)pullViewHolder,i-1);
        }
    }

    @Override
    public int getItemCount() {
        if(needShowMore){
            return getMItemCount()+2;
        }
        else{
            return getMItemCount()+1;
        }
    }

    public abstract PullViewHolder onCreateMViewHolder(ViewGroup viewGroup, int i);
    public abstract void onBindMViewHolder(@NonNull VH pullViewHolder, int i);
    public abstract int getMItemCount();
    @Override
    public int getItemViewType(int position) {
        if(position==0){
            return  VIEW_TYPE_HEADER;
        }
        else if(position>getMItemCount()){
            return VIEW_TYPE_BOTTOM;
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
                    if(getItemViewType(position) == VIEW_TYPE_HEADER){
                        return gridManager.getSpanCount();
                    }
                    if(needShowMore){
                        if(getMItemCount()%2==0){
                            if(getItemViewType(position) == VIEW_TYPE_BOTTOM){
                                return gridManager.getSpanCount();
                            }
                        }
                        else{
                            if(position>=getMItemCount()){
                                return gridManager.getSpanCount();
                            }
                        }
                    }
                    return 1;
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
            RecyclerView.LayoutParams lp =new RecyclerView.LayoutParams( RecyclerView.LayoutParams.MATCH_PARENT,RecyclerView.LayoutParams.WRAP_CONTENT);
            itemView.setLayoutParams(lp);
        }
    }


    public void showMore(boolean needShowMore){
        Log.e("lin","showMore");
       this.needShowMore = needShowMore;
       notifyDataSetChanged();
    }
}

