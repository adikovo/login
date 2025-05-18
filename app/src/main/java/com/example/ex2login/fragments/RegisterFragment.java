package com.example.ex2login.fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import com.example.ex2login.MainActivity;
import com.example.ex2login.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegisterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;

    private String mParam2;

    private FirebaseDatabase database;
    private DatabaseReference usersRef;
    private EditText usernameInput;
    private EditText passwordInput;
    private EditText passwordConfInput;
    private EditText phoneInput;

    public RegisterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegisterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RegisterFragment newInstance(String param1, String param2) {
        RegisterFragment fragment = new RegisterFragment();
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
        database = FirebaseDatabase.getInstance();
        usersRef = database.getReference("users");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        
        Button buttonReg = view.findViewById(R.id.buttonRegister);
        usernameInput = view.findViewById(R.id.editTextEmailAddressReg);
        passwordInput = view.findViewById(R.id.editTextPasswordReg);
        passwordConfInput = view.findViewById(R.id.editTextPasswordConfirmationReg);
        phoneInput = view.findViewById(R.id.editTextPhoneReg);

        buttonReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameInput.getText().toString().trim();
                String password = passwordInput.getText().toString();
                String passwordConf = passwordConfInput.getText().toString();
                String phone = phoneInput.getText().toString().trim();

                if(username.isEmpty()){
                    usernameInput.setError("Username is required");
                    return;
                }

                if(password.isEmpty()){
                    passwordInput.setError("Password is required");
                    return;
                }

                if(passwordConf.isEmpty()){
                    passwordConfInput.setError("Password confirmation is required");
                    return;
                }

                if(!password.equals(passwordConf)){
                    passwordConfInput.setError("Passwords don't match!");
                    return;
                }

                if(phone.isEmpty()){
                    phoneInput.setError("Phone number is required!");
                    return;
                }

                MainActivity mainActivity = (MainActivity) getActivity();
                if(mainActivity != null) {
                    mainActivity.register();
                }
            }
        });

        return view;
    }
}