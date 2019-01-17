package com.webxert.listeningsouls.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daasuu.bl.BubbleLayout;
import com.github.library.bubbleview.BubbleTextView;
import com.webxert.listeningsouls.R;

import org.w3c.dom.Text;

/**
 * Created by hp on 12/10/2018.
 */


public class MessageViewHolder extends RecyclerView.ViewHolder {

    public TextView message;
    public BubbleLayout message_layout;
    public FrameLayout frameLayout;
    public TextView who;

    public MessageViewHolder(View itemView) {
        super(itemView);
        frameLayout = itemView.findViewById(R.id.main_layout);
        message = (TextView) itemView.findViewById(R.id.message);
        message_layout = itemView.findViewById(R.id.message_layout);
        who = itemView.findViewById(R.id.who);
    }
}

