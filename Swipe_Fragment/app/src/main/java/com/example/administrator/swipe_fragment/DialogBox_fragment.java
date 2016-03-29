package com.example.administrator.swipe_fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.example.administrator.swipe_fragment.Database.DBHelper;

/**
 * Created by administrator on 22/3/16.
 */
public class DialogBox_fragment extends DialogFragment {

    DBHelper db;

    public DialogBox_fragment() {
    }
    //testol...

    public static DialogBox_fragment newInstance(String title) {
        DialogBox_fragment frag = new DialogBox_fragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onActivityCreated(Bundle arg0) {
        super.onActivityCreated(arg0);
        getDialog().getWindow()
                .getAttributes().windowAnimations = R.style.DialogAnimation;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final String title = getArguments().getString("title");
        String dbAddName;
        db = new DBHelper(getActivity());
        dbAddName = String.valueOf(db.getData(Integer.parseInt(title)));

        LayoutInflater factory = LayoutInflater.from(getActivity());
        final View textBoxView = factory.inflate(R.layout.call_address_dialog, null);
        DummyFragment.getInstance().layout.setSelected(true);
        final EditText textField = (EditText)
                (textBoxView.findViewById(R.id.calladdress_edit));
        textField.setText(dbAddName);

        return new android.support.v7.app.AlertDialog.Builder(getActivity())
                .setTitle("Update Contact")
                .setView(textBoxView)
                .setPositiveButton(
                        android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {

                                String address = textField.getText().toString();
                                // initiateCall();

                                db.updateContact(Integer.valueOf(title), address);

                                DummyFragment.getInstance().layout.setSelected(false);


                                DummyFragment.getInstance().adpater = new DummyAdapter(getActivity(), db.getAllCotacts());
                                DummyFragment.getInstance().recyclerView.setAdapter(DummyFragment.getInstance().adpater);
                            }
                        })
                .setNegativeButton(
                        android.R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                // Noop.
                                DummyFragment.getInstance().layout.setSelected(false);
                            }
                        })

                .create();

    }
}
