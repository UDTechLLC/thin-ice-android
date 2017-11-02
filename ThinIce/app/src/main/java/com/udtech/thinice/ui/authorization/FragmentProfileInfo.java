package com.udtech.thinice.ui.authorization;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.udtech.thinice.R;
import com.udtech.thinice.UserSessionManager;
import com.udtech.thinice.eventbus.model.user.UserInfoAdded;
import com.udtech.thinice.model.users.User;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by JOkolot on 30.11.2015.
 */
public class FragmentProfileInfo extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sign_in_info, container, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
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
        getView().findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
                getActivity().onBackPressed();
            }
        });
        getView().findViewById(R.id.weight).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.findViewById(R.id.weightErr).setVisibility(View.INVISIBLE);
                view.findViewById(R.id.weightStatus).setVisibility(View.INVISIBLE);
            }
        });
        view.findViewById(R.id.weight).setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                view.findViewById(R.id.weightErr).setVisibility(View.INVISIBLE);
                view.findViewById(R.id.weightStatus).setVisibility(View.INVISIBLE);
            }
        });
        view.findViewById(R.id.height).setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                view.findViewById(R.id.heightErr).setVisibility(View.INVISIBLE);
                view.findViewById(R.id.heightStatus).setVisibility(View.INVISIBLE);
            }
        });
        view.findViewById(R.id.height).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.findViewById(R.id.heightErr).setVisibility(View.INVISIBLE);
                view.findViewById(R.id.heightStatus).setVisibility(View.INVISIBLE);
            }
        });
        ((Spinner) view.findViewById(R.id.sex)).setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.item_spinner, R.id.tittle, Arrays.asList(new String[]{"Male", "Female"})));
        List<String> values = new ArrayList<>();
        for (int i = 1900; i < Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date())); i++) {
            values.add("" + i);
        }
        ((Spinner) view.findViewById(R.id.year)).setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.item_spinner, R.id.tittle, values));
        ((Spinner) view.findViewById(R.id.year)).setSelection(values.size() - 1);
        String[] months = new DateFormatSymbols().getMonths();
        ((Spinner) view.findViewById(R.id.months)).setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.item_spinner, R.id.tittle, months));
        values = new ArrayList<>();
        for (int i = 0; i < 31; i++) {
            values.add("" + (i + 1));
        }
        ((Spinner) view.findViewById(R.id.days)).setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.item_spinner, R.id.tittle, values));
        ((Spinner) view.findViewById(R.id.year)).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View childView, int position, long id) {
                int g = (int) (1900 + position);
                if (view.findViewById(R.id.days) != null)
                    if (g % 4 == 0 && ((g % 100 != 0) || (g % 400 == 0)) && ((Spinner) view.findViewById(R.id.months)).getSelectedItemPosition() == 1) {
                        int days = 29;
                        List<String> values = new ArrayList<>();
                        for (int i = 0; i < days; i++) {
                            values.add("" + (i + 1));
                        }
                        int temp = ((Spinner) view.findViewById(R.id.days)).getSelectedItemPosition();
                        ((Spinner) view.findViewById(R.id.days)).setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.item_spinner, R.id.tittle, values));
                        ((Spinner) view.findViewById(R.id.days)).setSelection(temp < days ? temp : days - 1);
                    } else if ((((Spinner) view.findViewById(R.id.months)).getSelectedItem()).equals("2")) {
                        int days = 28;
                        List<String> values = new ArrayList<>();
                        for (int i = 0; i < days; i++) {
                            values.add("" + (i + 1));
                        }
                        int temp = ((Spinner) view.findViewById(R.id.days)).getSelectedItemPosition();
                        ((Spinner) view.findViewById(R.id.days)).setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.item_spinner, R.id.tittle, values));
                        ((Spinner) view.findViewById(R.id.days)).setSelection(temp < days ? temp : days - 1);
                    }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ((Spinner) view.findViewById(R.id.months)).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View childview, int position, long id) {
                if (position == 1) {
                    int g = ((Spinner) view.findViewById(R.id.year)).getSelectedItemPosition();
                    if (g % 4 == 0 && ((g % 100 != 0) || (g % 400 == 0)) && ((Spinner) view.findViewById(R.id.months)).getSelectedItemPosition() == 1) {
                        int days = 29;
                        List<String> values = new ArrayList<>();
                        for (int i = 0; i < days; i++) {
                            values.add("" + (i + 1));
                        }
                        int temp = ((Spinner) view.findViewById(R.id.days)).getSelectedItemPosition();
                        ((Spinner) view.findViewById(R.id.days)).setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.item_spinner, R.id.tittle, values));
                        ((Spinner) view.findViewById(R.id.days)).setSelection(temp < days ? temp : days - 1);
                    } else {

                        int days = 28;
                        List<String> values = new ArrayList<>();
                        for (int i = 0; i < days; i++) {
                            values.add("" + (i + 1));
                        }
                        int temp = ((Spinner) view.findViewById(R.id.days)).getSelectedItemPosition();
                        ((Spinner) view.findViewById(R.id.days)).setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.item_spinner, R.id.tittle, values));
                        ((Spinner) view.findViewById(R.id.days)).setSelection(temp < days ? temp : days - 1);
                    }
                } else if (position == 3 || position == 5 || position == 8 || position == 10) {
                    int days = 30;
                    List<String> values = new ArrayList<>();
                    for (int i = 0; i < days; i++) {
                        values.add("" + (i + 1));
                    }
                    int temp = ((Spinner) view.findViewById(R.id.days)).getSelectedItemPosition();
                    ((Spinner) view.findViewById(R.id.days)).setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.item_spinner, R.id.tittle, values));
                    ((Spinner) view.findViewById(R.id.days)).setSelection(temp < days ? temp : days - 1);
                } else {
                    int days = 31;
                    List<String> values = new ArrayList<>();
                    for (int i = 0; i < days; i++) {
                        values.add("" + (i + 1));
                    }
                    if (((Spinner) view.findViewById(R.id.days)) != null) {
                        int temp = ((Spinner) view.findViewById(R.id.days)).getSelectedItemPosition();
                        ((Spinner) view.findViewById(R.id.days)).setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.item_spinner, R.id.tittle, values));
                        ((Spinner) view.findViewById(R.id.days)).setSelection(temp < days ? temp : days - 1);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        view.findViewById(R.id.save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = new User();
                user.setEmail(getArguments().getString("email"));
                user.setPassword(getArguments().getString("pass"));
                user.setFirstName(getArguments().getString("first_name", ""));
                user.setLastName(getArguments().getString("last_name", ""));
                user.setTwitterId(getArguments().getLong("twitterid", 0));
                user.setFacebookId(getArguments().getLong("facebookid", 0));
                user = collectData(user);
                if (user != null) {
                    user.save();
                    UserSessionManager.saveSession(user, getActivity());
                    EventBus.getDefault().post(new UserInfoAdded());
                }
            }
        });


    }

    public User collectData(User user) {
        if (!((EditText) getView().findViewById(R.id.weight)).getText().toString().equals("") && !((EditText) getView().findViewById(R.id.height)).getText().toString().equals("")) {
            user.setSex(((Spinner) getView().findViewById(R.id.sex)).getSelectedItemPosition() == 0);
            user.setWeight(Long.valueOf(((EditText) getView().findViewById(R.id.weight)).getText().toString()));
            user.setHeight(Long.valueOf(((EditText) getView().findViewById(R.id.height)).getText().toString()));
            int days = ((Spinner) getView().findViewById(R.id.days)).getSelectedItemPosition() + 1;
            int months = ((Spinner) getView().findViewById(R.id.months)).getSelectedItemPosition() + 1;
            int year = ((Spinner) getView().findViewById(R.id.year)).getSelectedItemPosition() + 1;
            try {
                user.setDateOfBirth(new SimpleDateFormat("d.M.yyyy").parse(days + "." + months + "." + year));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (user.getWeight() != 0 && user.getHeight() != 0)
                return user;
        } else {
            if (((EditText) getView().findViewById(R.id.weight)).getText().toString().equals("")) {
                showWeightError("Empty value");
            }
            if (((EditText) getView().findViewById(R.id.height)).getText().toString().equals("")) {
                showHeightError("Empty value");
            }
        }
        return null;
    }

    private void showWeightError(String string) {
        ((TextView) getView().findViewById(R.id.weight)).setText("");
        ((TextView) getView().findViewById(R.id.weightErr)).setText(string);
        getView().findViewById(R.id.weightErr).setVisibility(View.VISIBLE);
        getView().findViewById(R.id.weightStatus).setVisibility(View.VISIBLE);
        ((ImageView) getView().findViewById(R.id.weightStatus)).setImageDrawable(getActivity().getResources().getDrawable(string.equals("") ? R.mipmap.ic_accept : R.mipmap.ic_failed));
    }

    private void showHeightError(String string) {
        ((TextView) getView().findViewById(R.id.height)).setText("");
        ((TextView) getView().findViewById(R.id.heightErr)).setText(string);
        getView().findViewById(R.id.heightErr).setVisibility(View.VISIBLE);
        getView().findViewById(R.id.heightStatus).setVisibility(View.VISIBLE);
        ((ImageView) getView().findViewById(R.id.heightStatus)).setImageDrawable(getActivity().getResources().getDrawable(string.equals("") ? R.mipmap.ic_accept : R.mipmap.ic_failed));
    }
}
