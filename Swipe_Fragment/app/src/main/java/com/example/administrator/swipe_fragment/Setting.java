package com.example.administrator.swipe_fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.administrator.swipe_fragment.utility.CommonFunctions;
import com.example.administrator.swipe_fragment.utility.Constants;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link } interface
 * to handle interaction events.
 * Use the {@link Setting#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Setting extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TextInputLayout input_layout_name, input_layout_domain, sip_layout_password, input_authuserName, input_layout_dispname;
    private EditText input_username_et, input_domain_et, input_password_et, input_authusername_et, input_outbound, input_dispname_et;

    private Button btn_signup;
    // private OnFragmentInteractionListener mListener;

    public Setting() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Setting.
     */
    // TODO: Rename and change types and number of parameters
    public static Setting newInstance(String param1, String param2) {
        Setting fragment = new Setting();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_setting, container, false);

        input_layout_name = (TextInputLayout) v.findViewById(R.id.input_layout_name);
        input_layout_domain = (TextInputLayout) v.findViewById(R.id.input_layout_domain);
        sip_layout_password = (TextInputLayout) v.findViewById(R.id.sip_layout_password);
        input_authuserName = (TextInputLayout) v.findViewById(R.id.input_authuserName);
        input_layout_dispname = (TextInputLayout) v.findViewById(R.id.input_layout_dispname);

        input_username_et = (EditText) v.findViewById(R.id.input_username_et);
        input_domain_et = (EditText) v.findViewById(R.id.input_domain_et);
        input_authusername_et = (EditText) v.findViewById(R.id.input_authusername_et);
        input_password_et = (EditText) v.findViewById(R.id.input_password_et);
        input_outbound = (EditText) v.findViewById(R.id.input_outbound);
        input_dispname_et = (EditText) v.findViewById(R.id.input_dispname_et);

        btn_signup = (Button) v.findViewById(R.id.btn_signup);

        btn_signup.setOnClickListener(Setting.this);


        if (CommonFunctions.getPreference(getActivity(), Constants.filldata, true)) {
            input_username_et.setText(CommonFunctions.getPreference(getActivity(), Constants.username, ""));
            input_domain_et.setText(CommonFunctions.getPreference(getActivity(), Constants.domains, ""));
            input_authusername_et.setText(CommonFunctions.getPreference(getActivity(), Constants.authusername, ""));
            input_password_et.setText(CommonFunctions.getPreference(getActivity(), Constants.password, ""));
            input_outbound.setText(CommonFunctions.getPreference(getActivity(), Constants.outbound, ""));
            input_dispname_et.setText(CommonFunctions.getPreference(getActivity(), Constants.dispname, ""));

        }


        return v;
    }

    @Override
    public void onClick(View v) {

        try {

            CommonFunctions.setPreference(getActivity(), Constants.username, input_username_et.getText().toString());
            CommonFunctions.setPreference(getActivity(), Constants.password, input_password_et.getText().toString());
            CommonFunctions.setPreference(getActivity(), Constants.domains, input_domain_et.getText().toString());
            CommonFunctions.setPreference(getActivity(), Constants.authusername, input_authusername_et.getText().toString());
            CommonFunctions.setPreference(getActivity(), Constants.outbound, input_outbound.getText().toString());
            CommonFunctions.setPreference(getActivity(), Constants.dispname, input_dispname_et.getText().toString());

            CommonFunctions.setPreference(getActivity(), Constants.filldata, true);

            Intent in = new Intent(getActivity(), MainActivity.class);
            in.putExtra("posi", 0);
            startActivity(in);

            getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
//    }

}
