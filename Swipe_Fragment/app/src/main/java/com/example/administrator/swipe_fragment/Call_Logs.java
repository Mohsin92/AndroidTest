package com.example.administrator.swipe_fragment;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.sip.SipAudioCall;
import android.net.sip.SipException;
import android.net.sip.SipManager;
import android.net.sip.SipProfile;
import android.net.sip.SipRegistrationListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.administrator.swipe_fragment.utility.CommonFunctions;
import com.example.administrator.swipe_fragment.utility.Constants;

import java.text.ParseException;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * <p/>
 * to handle interaction events.
 * Use the {@link Call_Logs#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Call_Logs extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ToggleButton pushToTalkButton;
    TextView labelView;
    TextView user_name, call_details;
    ImageView call_drop;
    public String sipAddress = null;

    public SipManager manager = null;
    public SipProfile me = null;
    public SipAudioCall call = null;
    public IncomingCallReceiver callReceiver;
    String callstatus;

    //   private OnFragmentInteractionListener mListener;

    public Call_Logs() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Call_Logs.
     */
    // TODO: Rename and change types and number of parameters
    public static Call_Logs newInstance(String param1, String param2) {
        Call_Logs fragment = new Call_Logs();
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


        View view = inflater.inflate(R.layout.fragment_call__logs, container, false);
        pushToTalkButton = (ToggleButton) view.findViewById(R.id.pushToTalk);
        labelView = (TextView) view.findViewById(R.id.sipLabel);
        user_name = (TextView) view.findViewById(R.id.user_name);
        call_details = (TextView) view.findViewById(R.id.call_details);
        call_drop = (ImageView) view.findViewById(R.id.call_drop);


        IntentFilter filter = new IntentFilter();
        filter.addAction("android.SipDemo.INCOMING_CALL");
        callReceiver = new IncomingCallReceiver();
        getActivity().registerReceiver(callReceiver, filter);

        // "Push to talk" can be a serious pain when the screen keeps turning off.
        // Let's prevent that.
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        initializeManager();

        if (!Constants.address.equals("")) {

            sipAddress = Constants.address;
            initiateCall();

            call_details.setText(Constants.address);
        }
        pushToTalkButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (call == null) {
                    return false;
                } else if (event.getAction() == MotionEvent.ACTION_DOWN && call != null && call.isMuted()) {
                    call.toggleMute();
                } else if (event.getAction() == MotionEvent.ACTION_UP && !call.isMuted()) {
                    call.toggleMute();
                }
                return false;
            }
        });

        call_drop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    if (call.isInCall() || call.isOnHold() || call.isMuted()) {
                        call.endCall();

                        updateStatus(call);

                    }
                    call.endCall();
                    updateStatus(call);


                    Log.e("called ended: ", "Else condition for end call executed...");

                } catch (SipException se) {
                    Log.d("Wa/onOptionsItem", "Error ending call.", se);
                }
                call.close();

            }
        });

        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        // When we get back from the preference setting Activity, assume
        // settings have changed, and re-login with new auth info.
        initializeManager();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (call != null) {
            call.close();
        }

        closeLocalProfile();

        if (callReceiver != null) {
            getActivity().unregisterReceiver(callReceiver);
        }
    }

    public void initializeManager() {
        if (manager == null) {
            manager = SipManager.newInstance(getActivity());
        }

        initializeLocalProfile();
    }

    /**
     * Logs you into your SIP provider, registering this device as the location to
     * send SIP calls to for your SIP address.
     */
    public void initializeLocalProfile() {
        if (manager == null) {
            return;
        }

        if (me != null) {
            closeLocalProfile();
        }


        String username = CommonFunctions.getPreference(getActivity(), Constants.username, "");
        String domain = CommonFunctions.getPreference(getActivity(), Constants.domains, "");
        String password = CommonFunctions.getPreference(getActivity(), Constants.password, "");
        String authuser = CommonFunctions.getPreference(getActivity(), Constants.authusername, "");
        String outbound = CommonFunctions.getPreference(getActivity(), Constants.outbound, "");
        final String dispname = CommonFunctions.getPreference(getActivity(), Constants.dispname, "");

        user_name.setText(username);

        if (username.length() == 0 || domain.length() == 0 || password.length() == 0) {
            Toast.makeText(getActivity(), "Please enter proper details", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            SipProfile.Builder builder = new SipProfile.Builder(username, domain);
            builder.setAuthUserName(authuser);
            builder.setOutboundProxy(outbound);
            builder.setDisplayName(dispname);
            builder.setPassword(password);
            me = builder.build();

            Intent i = new Intent();
            i.setAction("android.SipDemo.INCOMING_CALL");
            PendingIntent pi = PendingIntent.getBroadcast(getActivity(), 0, i, Intent.FILL_IN_DATA);
            manager.open(me, pi, null);


            // This listener must be added AFTER manager.open is called,
            // Otherwise the methods aren't guaranteed to fire.

            manager.setRegistrationListener(me.getUriString(), new SipRegistrationListener() {
                public void onRegistering(String localProfileUri) {
                    updateStatus("Registering with SIP Server...");
                }

                public void onRegistrationDone(String localProfileUri, long expiryTime) {
                    updateStatus("Ready:" + " ");
                }

                public void onRegistrationFailed(String localProfileUri, int errorCode,
                                                 String errorMessage) {
                    updateStatus("Registration failed.  Please check settings.");
                }
            });
        } catch (ParseException pe) {
            updateStatus("Connection Error.");
        } catch (SipException se) {
            updateStatus("Connection error.");
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Exception connection: ", e.toString());
        }
    }

    public void closeLocalProfile() {
        if (manager == null) {
            return;
        }
        try {
            if (me != null) {
                manager.close(me.getUriString());
            }
        } catch (Exception ee) {
            Log.d("Walkie/onDestroy", "Failed to close local profile.", ee);
        }
    }

    /**
     * Make an outgoing call.
     */
    public void initiateCall() {

        updateStatus(sipAddress);

        try {
            SipAudioCall.Listener listener = new SipAudioCall.Listener() {
                // Much of the client's interaction with the SIP Stack will
                // happen via listeners.  Even making an outgoing call, don't
                // forget to set up a listener to set things up once the call is established.
                @Override
                public void onCallEstablished(SipAudioCall call) {
                    call.startAudio();
                    call.setSpeakerMode(true);
                    call.toggleMute();
                    updateStatus(call);
                }

                @Override
                public void onCallEnded(SipAudioCall call) {
                    updateStatus("Ready.");
                }
            };


            call = manager.makeAudioCall(me.getUriString(), sipAddress, listener, 30);
            Log.e("/InitiateCall", "Call executed");

        } catch (Exception e) {
            Log.e("Walkie/InitiateCall", "Error when trying to close manager.", e);
            if (me != null) {
                try {
                    manager.close(me.getUriString());
                } catch (Exception ee) {
                    Log.e("WalkieTal/InitiateCall",
                            "Error when trying to close manager.", ee);
                    ee.printStackTrace();
                }
            }
            if (call != null) {
                call.close();
            }
        }
    }

    /**
     * Updates the status box at the top of the UI with a messege of your choice.
     *
     * @param status The String to display in the status box.
     */
    public void updateStatus(final String status) {
        // Be a good citizen.  Make sure UI changes fire on the UI thread.
        getActivity().runOnUiThread(new Runnable() {
            public void run() {

                labelView.setText(status);
            }
        });
    }

    public void updateStatus(SipAudioCall call) {
        String useName = call.getPeerProfile().getDisplayName();
        if (useName == null) {
            useName = call.getPeerProfile().getUserName();
        }
        //updateStatus(useName + "@" + call.getPeerProfile().getSipDomain());
        callstatus = useName + "@" + call.getPeerProfile().getSipDomain();
        call_details.setText(callstatus);
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
