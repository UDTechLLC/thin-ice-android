package com.udtech.thinice.ui.authorization;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.udtech.thinice.R;
import com.udtech.thinice.UserSessionManager;
import com.udtech.thinice.eventbus.model.user.CreatedUser;
import com.udtech.thinice.model.users.User;

import java.util.Iterator;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * Created by JOkolot on 30.11.2015.
 */
public class FragmentCreateProfile extends Fragment {
    @BindView(R.id.email)
    EditText email;
    @BindView(R.id.pass)
    EditText pass;
    @BindView(R.id.passConfirm)
    EditText passConfirm;
    @BindView(R.id.emailErr)
    TextView emailError;
    @BindView(R.id.passErr)
    TextView passError;
    @BindView(R.id.passConfirmErr)
    TextView passConfirmError;
    @BindView(R.id.emailStatus)
    ImageView emailStatus;
    @BindView(R.id.passStatus)
    ImageView passStatus;
    @BindView(R.id.passConfirmStatus)
    ImageView passConfirmStatus;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sign_in, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((TextView) view.findViewById(R.id.agreements)).setText(Html.fromHtml(
                "By clicking \"Create Account\" you acknowledge that you understand and accept the <a href = \"https://en.wikipedia.org/wiki/Terms_of_service\">Terms of Use</a> and read the <a href = \"https://en.wikipedia.org/wiki/Privacy_policy\">Privacy Policy</a>"));
        ((TextView) view.findViewById(R.id.agreements)).setMovementMethod(LinkMovementMethod.getInstance());
        view.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        ButterKnife.bind(this, view);
        User user = UserSessionManager.getSession(getContext());
        if (user != null) {
            email.setText(user.getEmail());
        }
        email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (emailStatus.getVisibility() == View.VISIBLE) {
                    emailError.setVisibility(View.INVISIBLE);
                    email.setHint("Email");
                    emailStatus.setVisibility(View.INVISIBLE);
                }
            }
        });
        pass.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (passStatus.getVisibility() == View.VISIBLE) {
                    passError.setVisibility(View.INVISIBLE);
                    pass.setHint("Password");
                    passStatus.setVisibility(View.INVISIBLE);
                }
            }
        });
        passConfirm.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (passConfirmStatus.getVisibility() == View.VISIBLE) {
                    passConfirm.setHint("Confirm Password");
                    passConfirmError.setVisibility(View.INVISIBLE);
                    passConfirmStatus.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    @OnClick(R.id.save)
    void save() {
        User user = collectData(new User());
        if (user != null) {
            Iterator<User> userIterator = User.findAll(User.class);
            while (userIterator.hasNext()) {
                User iteratorUser = userIterator.next();
                if (iteratorUser.getEmail() != null)
                    if (iteratorUser.getEmail().equals(user.getEmail())) {
                        showEmailError("Email is already being used.");
                        return;
                    }
            }
            EventBus.getDefault().post(new CreatedUser(user));
        }
    }

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
            Iterator<User> users = User.findAll(User.class);
            boolean exist = false;
            user.setEmail(email);
            user.setPassword(pass);
            while (users.hasNext()) {
                User temp = users.next();
                try {
                    if (temp.getEmail().equals(user.getEmail())) {
                        showEmailError("Email already exists");
                        exist = true;
                        break;
                    }


                } catch (Exception e) {
                }
            }
            if (!exist)
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
