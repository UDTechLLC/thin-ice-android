package com.udtech.thinice.ui.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.udtech.thinice.R;
import com.udtech.thinice.UserSessionManager;
import com.udtech.thinice.eventbus.model.user.SaveUser;
import com.udtech.thinice.model.users.User;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * Created by JOkolot on 05.11.2015.
 */
public class FragmentAccount extends UserDataForm {
    @Bind(R.id.email)
    EditText email;
    @Bind(R.id.pass)
    EditText pass;
    @Bind(R.id.passConfirm)
    EditText passConfirm;
    @Bind(R.id.emailErr)
    TextView emailError;
    @Bind(R.id.passErr)
    TextView passError;
    @Bind(R.id.passConfirmErr)
    TextView passConfirmError;
    @Bind(R.id.emailStatus)
    ImageView emailStatus;
    @Bind(R.id.passStatus)
    ImageView passStatus;
    @Bind(R.id.passConfirmStatus)
    ImageView passConfirmStatus;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_account_account, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        User user = UserSessionManager.getSession(getContext());
        if (user != null) {
            email.setText(user.getEmail());
        }
        email.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (emailStatus.getVisibility() == View.VISIBLE) {
                    emailError.setVisibility(View.INVISIBLE);
                    email.setHint("Email");
                    emailStatus.setVisibility(View.INVISIBLE);
                }
                return false;
            }
        });
        pass.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (passStatus.getVisibility() == View.VISIBLE) {
                    passError.setVisibility(View.INVISIBLE);
                    pass.setHint("Password");
                    passStatus.setVisibility(View.INVISIBLE);
                }
                return false;
            }
        });
        passConfirm.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (passConfirmStatus.getVisibility() == View.VISIBLE) {
                    passConfirm.setHint("Confirm Password");
                    passConfirmError.setVisibility(View.INVISIBLE);
                    passConfirmStatus.setVisibility(View.INVISIBLE);
                }
                return false;
            }
        });
    }

    @OnClick(R.id.save)
    void save() {
        EventBus.getDefault().post(new SaveUser(true));
    }

    @Override
    public User collectData(User user) {
        String email, pass, passConfirm;
        email = this.email.getText().toString();
        pass = this.pass.getText().toString();
        passConfirm = this.passConfirm.getText().toString();
        if (email != null ? email.equals("") : true) {
            showEmailError("Empty e-mail.");
        }
        if (pass != null ? pass.equals("") : true) {
            showPassError("Field empty.");
        } else if (passConfirm != null ? passConfirm.equals("") || !passConfirm.equals(pass) : true) {
            showPassConfirmError("Password doesn't match.");
        } else {
            user.setEmail(email);
            user.setPassword(pass);
            return user;
        }
        return null;
    }

    private void showEmailError(String string) {
        email.setText("");
        emailError.setText(string);
        email.setHint("");
        emailError.setVisibility(View.VISIBLE);
        emailStatus.setVisibility(View.VISIBLE);
        emailStatus.setImageDrawable(getActivity().getResources().getDrawable(string.equals("") ? R.mipmap.ic_accept : R.mipmap.ic_failed));
    }

    private void showPassError(String string) {
        passError.setText(string);
        pass.setHint("");
        pass.setText("");
        passConfirm.setText("");
        passError.setVisibility(View.VISIBLE);
        passStatus.setVisibility(View.VISIBLE);
        passStatus.setImageDrawable(getActivity().getResources().getDrawable(string.equals("") ? R.mipmap.ic_accept : R.mipmap.ic_failed));
    }

    private void showPassConfirmError(String string) {
        passConfirmError.setText(string);
        passConfirm.setHint("");
        passConfirm.setText("");
        pass.setText("");
        passConfirmError.setVisibility(View.VISIBLE);
        passConfirmStatus.setVisibility(View.VISIBLE);
        passConfirmStatus.setImageDrawable(getActivity().getResources().getDrawable(string.equals("") ? R.mipmap.ic_accept : R.mipmap.ic_failed));
    }
}
