package com.example.ex2login.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ex2login.MainActivity;
import com.example.ex2login.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LobbyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LobbyFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FirebaseAuth mAuth;

    public LobbyFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LobbyFragment.
     */
    // TODO: Rename and change types and number of parameters


    public static LobbyFragment newInstance(String param1, String param2) {
        LobbyFragment fragment = new LobbyFragment();
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
        View view = inflater.inflate(R.layout.fragment_lobby, container, false);
        mAuth = FirebaseAuth.getInstance();

        Button buttonLogin = view.findViewById(R.id.buttonToLogin);
        Button buttonReg = view.findViewById(R.id.buttonToReg);
        EditText emailInput = view.findViewById(R.id.editTextTextEmailAddress);
        EditText passwordInput = view.findViewById(R.id.editTextTextPassword);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailInput.getText().toString();
                String password = passwordInput.getText().toString();

                if(email.isEmpty()){
                    emailInput.setError("Email address is required");
                    return;
                }

                if(password.isEmpty()){
                    passwordInput.setError("Password is required");
                    return;
                }

                MainActivity mainActivity = (MainActivity) getActivity();
                //mainActivity.getStudent("0536788372");
                mainActivity.login();

                Navigation.findNavController(view).navigate(R.id.action_lobbyFragment_to_calendarFragment);
            }
        });

        buttonReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //navigate to reg page
                Navigation.findNavController(view).navigate(R.id.action_lobbyFragment_to_registerFragment);
            }
        });

        return view;
    }
}