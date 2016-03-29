package com.example.administrator.swipe_fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.swipe_fragment.Database.DBHelper;

import java.util.ArrayList;

/**
 * Created by administrator on 14/3/16.
 */
public class DummyFragment extends Fragment {
    public static final String[] data = {"Cupcake", "Donut", "Eclair",
            "Froyo", "Gingerbread", "Honeycomb",
            "Icecream Sandwich", "Jelly Bean", "Kitkat", "Lollipop"};
    int color;
    DummyAdapter adpater;
    DummyAdapter.ViewHolder viewholder;
    DBHelper db;
    ArrayList<String> array_list = new ArrayList<String>();
    RecyclerView dummyfrag_scrollableview;
    private Menu activityMenu;
    private MenuItem curMenuItem;
    int posi;
    RecyclerView recyclerView;
    LinearLayout layout;
    private SparseBooleanArray selectedItems;
    public String Address = null;
    private static final int ADD1 = 1;
    private static DummyFragment mContext;


    public DummyFragment() {

    }

    @SuppressLint("ValidFragment")
    public DummyFragment(int color) {
        this.color = color;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dummy_fragment, container, false);

        final FrameLayout frameLayout = (FrameLayout) view.findViewById(R.id.dummyfrag_bg);

        frameLayout.setBackgroundColor(color);

        viewholder = new DummyAdapter.ViewHolder(view);
        mContext = this;

        recyclerView = (RecyclerView) view.findViewById(R.id.dummyfrag_scrollableview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getBaseContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        recyclerView.setClickable(true);
        recyclerView.setLongClickable(true);


        db = new DBHelper(getActivity());


        viewholder.SetOnItemLongClickListener(new DummyAdapter.ViewHolder.OnItemClickListener() {
            @Override
            public boolean onItemClick(View view, int position) {
                TextView id = (TextView) view.findViewById(R.id.forID);

                MainActivity act = new MainActivity();
                posi = Integer.parseInt(id.getText().toString());
                Toast.makeText(getActivity(), String.valueOf(posi), Toast.LENGTH_SHORT).show();
                view.startActionMode(modeCallBack);
                view.setSelected(true);

                layout = (LinearLayout) view.findViewById(R.id.contact_delete_ll);

                layout.setSelected(true);


                return true;
            }
        });



        adpater = new DummyAdapter(getActivity(), db.getAllCotacts());
        recyclerView.setAdapter(adpater);


        ((MainActivity) getActivity()).setFragmentRefreshListener(new MainActivity.FragmentRefreshListener() {
            @Override
            public void onRefresh() {

                // Refresh Your Fragment
                db = new DBHelper(getActivity());
                adpater = new DummyAdapter(getActivity(), db.getAllCotacts());
                recyclerView.setAdapter(adpater);
            }
        });

        return view;
    }

    public ActionMode.Callback modeCallBack = new ActionMode.Callback() {

        public void onDestroyActionMode(ActionMode mode) {
            mode = null;
            layout.setSelected(false);

        }

        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.setTitle("Options");
            mode.getMenuInflater().inflate(R.menu.menu_action_delete, menu);

            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            int id = item.getItemId();
            switch (id) {
                case R.id.action_delete_del:
                    //    int posi= adpater.getItemCount();
                    Toast.makeText(getActivity(), String.valueOf(posi), Toast.LENGTH_SHORT).show();
                    Toast.makeText(getActivity(), String.valueOf(db.numberOfRows()), Toast.LENGTH_SHORT).show();

                    db = new DBHelper(getActivity());
                    db.deleteContact(posi);

                    layout.setSelected(false);

                    adpater = new DummyAdapter(getActivity(), db.getAllCotacts());
                    recyclerView.setAdapter(adpater);

                    mode.finish();
                    break;
                case R.id.action_edit:
                    showInputDialog();
                    mode.finish();

                default:
                    return false;

            }

            return true;
        }
    };


    public void showInputDialog() {
        FragmentManager fm = getFragmentManager();
        DialogBox_fragment myDialogFragment = DialogBox_fragment.newInstance(String.valueOf(posi));
        myDialogFragment.show(fm, "dialog_fragment");


    }

    public static DummyFragment getInstance() {
        return mContext;
    }
}