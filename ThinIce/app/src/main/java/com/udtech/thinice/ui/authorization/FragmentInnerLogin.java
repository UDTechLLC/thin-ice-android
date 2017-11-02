package com.udtech.thinice.ui.authorization;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.udtech.thinice.R;
import com.udtech.thinice.UserSessionManager;
import com.udtech.thinice.model.users.User;
import com.udtech.thinice.ui.MainActivity;

import java.util.Iterator;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by JOkolot on 04.11.2015.
 */
public class FragmentInnerLogin extends Fragment {
    @Bind(R.id.email)
    EditText email;
    @Bind(R.id.pass)
    EditText pass;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_inner_login, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (email.requestFocus()) {
                            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.showSoftInput(email, InputMethodManager.SHOW_IMPLICIT);
                        }
                    }
                });
            }
        }).start();
        pass.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    Iterator<User> users = User.findAll(User.class);
                    boolean contains = false;
                    while (users.hasNext()) {
                        User dbUser = users.next();
                        if (dbUser.getEmail()!=null&&dbUser.getEmail().equals(email.getText().toString()) && pass.getText().toString().equals(dbUser.getPassword())) {
                            contains = true;
                            UserSessionManager.saveSession(dbUser, getContext());
                        }
                    }
                    if (!contains) {
                        showError("Wrong auth data");
                    } else {
                        Intent mainIntent = new Intent(getActivity(), MainActivity.class);
                        getActivity().startActivity(mainIntent);
                        getActivity().finish();
                    }
                }
                return false;
            }
        });
        pass.setOnKeyListener(new View.OnKeyListener() {
                                  public boolean onKey(View v, int keyCode, KeyEvent event) {
                                      if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                                          Iterator<User> users = User.findAll(User.class);
                                          boolean contains = false;
                                          while (users.hasNext()) {
                                              User dbUser = users.next();
                                              if (dbUser.getEmail() == email.getText().toString()) {
                                                  contains = true;
                                              }
                                          }
                                          if (!contains) {
                                              showError("Wrong auth data");
                                          } else {
                                              Intent mainIntent = new Intent(getActivity(), MainActivity.class);
                                              getActivity().startActivity(mainIntent);
                                              getActivity().finish();
                                          }

                                          return true;
                                      }
                                      return false;
                                  }
                              }

        );
        email.setOnFocusChangeListener(new View.OnFocusChangeListener()

                                       {
                                           @Override
                                           public void onFocusChange(View v, boolean hasFocus) {
                                               getView().findViewById(R.id.passErr).setVisibility(View.INVISIBLE);
                                               getView().findViewById(R.id.passStatus).setVisibility(View.INVISIBLE);
                                               getView().findViewById(R.id.emailErr).setVisibility(View.INVISIBLE);
                                               getView().findViewById(R.id.emailStatus).setVisibility(View.INVISIBLE);
                                           }
                                       }

        );
    }

    @OnClick(R.id.back)
    public void onBack() {
        if (getActivity() != null) {
            View view = getActivity().getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
            getActivity().onBackPressed();
        }
    }

    private void showError(String s) {
        ((TextView) getView().findViewById(R.id.email)).setText("");
        ((TextView) getView().findViewById(R.id.emailErr)).setText(s);
        getView().findViewById(R.id.emailErr).setVisibility(View.VISIBLE);
        getView().findViewById(R.id.emailStatus).setVisibility(View.VISIBLE);
        ((TextView) getView().findViewById(R.id.pass)).setText("");
        ((TextView) getView().findViewById(R.id.passErr)).setText(s);
        getView().findViewById(R.id.passErr).setVisibility(View.VISIBLE);
        getView().findViewById(R.id.passStatus).setVisibility(View.VISIBLE);
    }
}
