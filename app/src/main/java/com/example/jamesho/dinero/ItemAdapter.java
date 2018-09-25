package com.example.jamesho.dinero;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.telecom.Call;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by jamesho on 2018-09-24.
 */

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder>{

    // This specifies the number of cards
    private int numberOfCards = 10;

    public ItemAdapter(Context context) {
    }

    // Provide a reference to the views for each item
    public static class ItemViewHolder extends RecyclerView.ViewHolder implements RecyclerView.OnClickListener {
        //public TextView mTextView;
        TextView titleTv;
        public ItemViewHolder(View itemView) {
            super(itemView);
            //This references the textView on the card
            titleTv = itemView.findViewById(R.id.info_text);
        }

        @Override
        public void onClick(View v) {

        }
    }
    @NonNull
    @Override
    public ItemAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Create new views (invoked by the layout manager)
        Context context = parent.getContext();
        // this references the layout of the card view
        int layoutIdForMenuItem = R.layout.card_view;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutIdForMenuItem, parent, shouldAttachToParentImmediately);
        ItemViewHolder viewHolder = new ItemViewHolder(view);
        return viewHolder;
    }
    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull ItemAdapter.ItemViewHolder holder, int position) {
        holder.titleTv.setText(String.valueOf(position));
    }
    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return numberOfCards;
    }
}
