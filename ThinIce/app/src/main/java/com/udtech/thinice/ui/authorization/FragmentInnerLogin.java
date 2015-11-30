package com.udtech.thinice.ui.authorization;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.udtech.thinice.R;
import com.udtech.thinice.model.users.User;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by JOkolot on 04.11.2015.
 */
public class FragmentInnerLogin extends Fragment {
    @Bind(R.id.email) EditText email;
    @Bind(R.id.pass) EditText pass;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_inner_login, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    getActivity().onBackPressed();
                    getActivity().onBackPressed();
                    return true;
                }
                return false;
            }
        });
        ButterKnife.bind(this, view);
        pass.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                if(s.equals("\n")){
                    List<User> users = User.find(User.class,"email = ?, pass = ?",email.getText().toString(),pass.getText().toString());
                    if(users!=null?users.size()>0:false){
                        User user = users.get(0);
                    }
                    else{
                        //todo show err
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }
}
