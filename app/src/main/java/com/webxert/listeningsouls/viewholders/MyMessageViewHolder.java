package com.webxert.listeningsouls.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.github.library.bubbleview.BubbleTextView;
import com.webxert.listeningsouls.R;

/**
 * Created by hp on 12/10/2018.
 */


public class MyMessageViewHolder extends RecyclerView.ViewHolder {
   public BubbleTextView message;

    public MyMessageViewHolder(View itemView) {
        super(itemView);
        message = (BubbleTextView) itemView.findViewById(R.id.message);
    }
}


