package com.example.administrator.swipe_fragment;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.swipe_fragment.utility.Constants;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by administrator on 14/3/16.
 */

public class DummyAdapter extends RecyclerView.Adapter<DummyAdapter.ViewHolder> {
    Activity activity;
    ArrayList<HashMap<String, String>> arrayhashmap = new ArrayList<HashMap<String, String>>();
    private SparseBooleanArray selectedItems;
    LayoutInflater inflater;
    public static ViewHolder.OnItemClickListener mItemClickListener;

    public DummyAdapter(Activity activity, ArrayList<HashMap<String, String>> arrayhashmap) {
        this.activity = activity;
        this.arrayhashmap = arrayhashmap;
        inflater = (LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        HashMap<String, String> array_list = new HashMap<String, String>();
        array_list = arrayhashmap.get(position);

        // holder.contact_delete_ll.setSelected(selectedItems.get(position, false));

        holder.listitem_name.setText(array_list.get("name"));
        holder.forID.setText(array_list.get("id"));
        holder.contact_call_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String address = String.valueOf(holder.listitem_name.getText());
                Toast.makeText(activity, address, Toast.LENGTH_SHORT).show();
                Constants.address = address; // store in address contant for Global vairable

                Intent in = new Intent(activity, MainActivity.class);
                in.putExtra("posi", 1);
                activity.startActivity(in);

                activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });


    }


    @Override
    public int getItemCount() {
        return arrayhashmap.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        public TextView listitem_name, forID;
        public LinearLayout contact_delete_ll;
        public ImageView contact_call_iv;

        public ViewHolder(View v) {
            super(v);
            listitem_name = (TextView) v.findViewById(R.id.listitem_name);
            forID = (TextView) v.findViewById(R.id.forID);
            contact_delete_ll = (LinearLayout) v.findViewById(R.id.contact_delete_ll);
            contact_call_iv = (ImageView) v.findViewById(R.id.contact_call_iv);

            v.setOnLongClickListener(this);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (contact_delete_ll.isSelected() == true) {

                        contact_delete_ll.setSelected(false);
                    }
                }
            });


        }

        @Override
        public boolean onLongClick(View v) {

            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v, getAdapterPosition());
            }

            return true;
        }

        public interface OnItemClickListener {
            boolean onItemClick(View view, int position);
        }

        public void SetOnItemLongClickListener(OnItemClickListener mItemClickListenerw) {
            mItemClickListener = mItemClickListenerw;
        }
    }

}
