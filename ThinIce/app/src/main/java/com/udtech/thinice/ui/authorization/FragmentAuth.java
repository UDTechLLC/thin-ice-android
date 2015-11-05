package com.udtech.thinice.ui.authorization;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.core.models.User;
import com.udtech.thinice.R;
import com.udtech.thinice.UserSessionManager;
import com.udtech.thinice.model.users.FBUser;
import com.udtech.thinice.model.users.TUser;
import com.udtech.thinice.ui.LoginActivity;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by JOkolot on 04.11.2015.
 */
public class FragmentAuth extends Fragment {
    @Bind(R.id.login)
    View logIn;//inner login
    @Bind(R.id.fblogin)
    View logInFacebook;
    @Bind(R.id.twlogin)
    View logInTwitter;
    @Bind(R.id.twitter_login_button)
    TwitterLoginButton twitterLoginButton;
    private CallbackManager callbackManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        twitterLoginButton = new TwitterLoginButton(getActivity());
        return inflater.inflate(R.layout.fragment_select_login, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        callbackManager = CallbackManager.Factory.create();
        twitterLoginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                Twitter.getApiClient(result.data).getAccountService()
                        .verifyCredentials(true, false, new Callback<User>() {

                            @Override
                            public void failure(TwitterException e) {

                            }

                            @Override
                            public void success(Result<User> userResult) {
                                com.udtech.thinice.model.users.User innerUser = null;
                                User user = userResult.data;
                                List<TUser> tUsers = TUser.find(TUser.class, "twitter_id = ?", user.idStr);
                                if (tUsers != null ? tUsers.size() != 0 : false) {
                                    List<com.udtech.thinice.model.users.User> users = com.udtech.thinice.model.users.User.find(com.udtech.thinice.model.users.User.class, "t_user = ?", user.idStr);
                                    innerUser = users.get(0);

                                } else {
                                    TUser tUser = new TUser();
                                    tUser.setTwitterId(user.id);
                                    innerUser = new com.udtech.thinice.model.users.User(tUser);
                                    innerUser.setEmail(user.email);
                                    innerUser.setFirstName(user.name);
                                    innerUser.setLastName(user.screenName);
                                    innerUser.setImageUrl(user.profileImageUrl);
                                    innerUser.save();
                                }
                                saveSession(innerUser);
                            }

                        });
            }

            @Override
            public void failure(TwitterException e) {

            }
        });
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        GraphRequest.newMeRequest(
                                loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(JSONObject me, GraphResponse response) {
                                        if (response.getError() != null) {
                                            // handle error
                                        } else {
                                            com.udtech.thinice.model.users.User innerUser = null;
                                            long id = Long.parseLong(me.optString("id"));
                                            FBUser fbUser = null;
                                            List<FBUser> fbUsers = FBUser.find(FBUser.class, "fb_id = ?", id + "");
                                            if (fbUsers != null ? fbUsers.size() > 0 : false) {
                                                fbUser = fbUsers.get(0);
                                                innerUser = fbUser.getUser();
                                            } else {
                                                fbUser = new FBUser(id);
                                                innerUser = new com.udtech.thinice.model.users.User(fbUser);
                                                innerUser.setImageUrl("https://graph.facebook.com/" +id + "/picture?type=large");
                                                innerUser.setFirstName(me.optString("name").substring(0, me.optString("name").lastIndexOf(' ')));
                                                innerUser.setLastName(me.optString("name").substring(me.optString("name").lastIndexOf(' ')));
                                                innerUser.save();
                                            }
                                            saveSession(innerUser);
                                        }
                                        LoginManager.getInstance().logOut();
                                    }
                                }).executeAsync();
                    }

                    @Override
                    public void onCancel() {
                    }

                    @Override
                    public void onError(FacebookException exception) {
                    }
                });
    }

    @OnClick(R.id.login)
    public void showLogIn() {
        ((LoginActivity) getActivity()).showLoginScreen();
    }

    @OnClick(R.id.fblogin)
    public void showLogInFacebook() {
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile, email"));
    }

    @OnClick(R.id.twlogin)
    public void showLogInTwitter() {
        twitterLoginButton.performClick();
    }

    @OnClick(R.id.registration)
    public void pressRegistration() {
        ((LoginActivity) getActivity()).showRegistrationScreen();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        twitterLoginButton.onActivityResult(requestCode, resultCode, data);
    }

    private void saveSession(com.udtech.thinice.model.users.User user) {
        UserSessionManager.saveSession(user, getActivity());
    }
}
