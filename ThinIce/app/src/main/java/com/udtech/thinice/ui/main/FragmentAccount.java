package com.udtech.thinice.ui.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.udtech.thinice.R;
import com.udtech.thinice.model.users.User;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by JOkolot on 05.11.2015.
 */
public class FragmentAccount extends UserDataForm{
    @Bind(R.id.email) EditText email;
    @Bind(R.id.pass) EditText pass;
    @Bind(R.id.passConfirm) EditText passConfirm;
    @Bind(R.id.emailErr) TextView emailError;
    @Bind(R.id.passErr) TextView passError;
    @Bind(R.id.passConfirmErr) TextView passConfirmError;
    @Bind(R.id.emailStatus) ImageView emailStatus;
    @Bind(R.id.passStatus) ImageView passStatus;
    @Bind(R.id.passConfirmStatus) ImageView passConfirmStatus;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_account_account, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (emailStatus.getVisibility() == View.VISIBLE) {
                    emailError.setVisibility(View.INVISIBLE);
                    emailStatus.setVisibility(View.INVISIBLE);
                }
            }
        });
        pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pass.getVisibility()==View.VISIBLE){
                    pass.setVisibility(View.INVISIBLE);
                    pass.setVisibility(View.INVISIBLE);
                }
            }
        });
        passConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(passConfirm.getVisibility()==View.VISIBLE){
                    passConfirm.setVisibility(View.INVISIBLE);
                    passConfirm.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    @Override
    public User collectData(User user) {
        String email, pass,passConfirm;
        email = this.email.getText().toString();
        this.email.setText("");
        pass = this.pass.getText().toString();
        this.pass.setText("");
        passConfirm = this.passConfirm.getText().toString();
        if(email!= null?email.equals(""):true){
            showEmailError("Empty e-mail.");
        }
        if(pass != null?pass.equals(""):true) {
            showPassError("Empty password.");
        }else if(passConfirm != null?passConfirm.equals(""):true){
            showPassConfirmError("Empty confirm password.");
        }else{
            user.setEmail(email);
            user.setPassword(pass);
            return user;
        }
        return null;
    }
    private void showEmailError(String string){
        emailError.setText(string);
        emailError.setVisibility(View.VISIBLE);
        emailStatus.setVisibility(View.VISIBLE);
        emailStatus.setImageDrawable(getActivity().getResources().getDrawable(string.equals("")?R.mipmap.ic_accept:R.mipmap.ic_failed));
    }
    private void showPassError(String string){
        passError.setText(string);
        passError.setVisibility(View.VISIBLE);
        passStatus.setVisibility(View.VISIBLE);
        passStatus.setImageDrawable(getActivity().getResources().getDrawable(string.equals("")?R.mipmap.ic_accept:R.mipmap.ic_failed));
    }
    private void showPassConfirmError(String string){
        passConfirmError.setText(string);
        passConfirmError.setVisibility(View.VISIBLE);
        passConfirmStatus.setVisibility(View.VISIBLE);
        passConfirmStatus.setImageDrawable(getActivity().getResources().getDrawable(string.equals("")?R.mipmap.ic_accept:R.mipmap.ic_failed));
    }
}
