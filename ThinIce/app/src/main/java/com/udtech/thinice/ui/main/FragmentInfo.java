package com.udtech.thinice.ui.main;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.udtech.thinice.R;
import com.udtech.thinice.UserSessionManager;
import com.udtech.thinice.eventbus.model.user.SaveUser;
import com.udtech.thinice.model.Settings;
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
 * Created by JOkolot on 05.11.2015.
 */
public class FragmentInfo extends UserDataForm {
    private Settings settings;
    private HeightController controller;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_account_info, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new SaveUser(false));
            }
        });
        settings = new Settings();
        settings.fetch(getActivity());
        controller = new HeightController(view.findViewById(R.id.ft));
        if (settings.isWeight()) {
            ((TextView) view.findViewById(R.id.textView8)).setText("Weight(lb)*");
        } else {
            ((TextView) view.findViewById(R.id.textView8)).setText("Weight(kg)*");
        }
        if (settings.isLenght()) {
            view.findViewById(R.id.height).setVisibility(View.GONE);
            view.findViewById(R.id.ft).setVisibility(View.VISIBLE);
            ((TextView) view.findViewById(R.id.textView9)).setText("Height(ft'in\")*");
        } else {
            view.findViewById(R.id.height).setVisibility(View.VISIBLE);
            view.findViewById(R.id.ft).setVisibility(View.GONE);
            ((TextView) view.findViewById(R.id.textView9)).setText("Height(cm)*");
        }
        ((Spinner) view.findViewById(R.id.sex)).setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.item_spinner, R.id.tittle, Arrays.asList(new String[]{"Male", "Female"})));
        List<String> values = new ArrayList<>();
        for (int i = 1900; i < Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date())); i++) {
            values.add("" + i);
        }
        ((Spinner) view.findViewById(R.id.year)).setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.item_spinner, R.id.tittle, values));
        String[] months = new DateFormatSymbols().getMonths();
        ((Spinner) view.findViewById(R.id.months)).setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.item_spinner, R.id.tittle, months));
        values = new ArrayList<>();
        for (int i = 0; i < 31; i++) {
            values.add("" + (i + 1));
        }
        ((Spinner) view.findViewById(R.id.days)).setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.item_spinner, R.id.tittle, values));
        ((Spinner) view.findViewById(R.id.year)).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int g = (int) (1900 + position);
                if (getView().findViewById(R.id.days) != null)
                    if (g % 4 == 0 && ((g % 100 != 0) || (g % 400 == 0)) && ((Spinner) getView().findViewById(R.id.months)).getSelectedItemPosition() == 1) {
                        int days = 29;
                        List<String> values = new ArrayList<>();
                        for (int i = 0; i < days; i++) {
                            values.add("" + (i + 1));
                        }
                        int temp = ((Spinner) getView().findViewById(R.id.days)).getSelectedItemPosition();
                        ((Spinner) getView().findViewById(R.id.days)).setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.item_spinner, R.id.tittle, values));
                        ((Spinner) getView().findViewById(R.id.days)).setSelection(temp < days ? temp : days - 1);
                    } else if ((((Spinner) getView().findViewById(R.id.months)).getSelectedItem()).equals("2")) {
                        int days = 28;
                        List<String> values = new ArrayList<>();
                        for (int i = 0; i < days; i++) {
                            values.add("" + (i + 1));
                        }
                        int temp = ((Spinner) getView().findViewById(R.id.days)).getSelectedItemPosition();
                        ((Spinner) getView().findViewById(R.id.days)).setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.item_spinner, R.id.tittle, values));
                        ((Spinner) getView().findViewById(R.id.days)).setSelection(temp < days ? temp : days - 1);
                    }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ((Spinner) view.findViewById(R.id.months)).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 1) {
                    int g = ((Spinner) getView().findViewById(R.id.year)).getSelectedItemPosition();
                    if (g % 4 == 0 && ((g % 100 != 0) || (g % 400 == 0)) && ((Spinner) getView().findViewById(R.id.months)).getSelectedItemPosition() == 1) {
                        int days = 29;
                        List<String> values = new ArrayList<>();
                        for (int i = 0; i < days; i++) {
                            values.add("" + (i + 1));
                        }
                        int temp = ((Spinner) getView().findViewById(R.id.days)).getSelectedItemPosition();
                        ((Spinner) getView().findViewById(R.id.days)).setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.item_spinner, R.id.tittle, values));
                        ((Spinner) getView().findViewById(R.id.days)).setSelection(temp < days ? temp : days - 1);
                    } else {

                        int days = 28;
                        List<String> values = new ArrayList<>();
                        for (int i = 0; i < days; i++) {
                            values.add("" + (i + 1));
                        }
                        int temp = ((Spinner) getView().findViewById(R.id.days)).getSelectedItemPosition();
                        ((Spinner) getView().findViewById(R.id.days)).setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.item_spinner, R.id.tittle, values));
                        ((Spinner) getView().findViewById(R.id.days)).setSelection(temp < days ? temp : days - 1);
                    }
                } else if (position == 3 || position == 5 || position == 8 || position == 10) {
                    int days = 30;
                    List<String> values = new ArrayList<>();
                    for (int i = 0; i < days; i++) {
                        values.add("" + (i + 1));
                    }
                    int temp = ((Spinner) getView().findViewById(R.id.days)).getSelectedItemPosition();
                    ((Spinner) getView().findViewById(R.id.days)).setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.item_spinner, R.id.tittle, values));
                    ((Spinner) getView().findViewById(R.id.days)).setSelection(temp < days ? temp : days - 1);
                } else {
                    int days = 31;
                    List<String> values = new ArrayList<>();
                    for (int i = 0; i < days; i++) {
                        values.add("" + (i + 1));
                    }
                    if (((Spinner) getView().findViewById(R.id.days)) != null) {
                        int temp = ((Spinner) getView().findViewById(R.id.days)).getSelectedItemPosition();
                        ((Spinner) getView().findViewById(R.id.days)).setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.item_spinner, R.id.tittle, values));
                        ((Spinner) getView().findViewById(R.id.days)).setSelection(temp < days ? temp : days - 1);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        User user = UserSessionManager.getSession(getContext());
        if (user != null) {
            ((EditText) getView().findViewById(R.id.first_name)).setText(user.getFirstName());
            ((EditText) getView().findViewById(R.id.last_name)).setText(user.getLastName());
            ((EditText) getView().findViewById(R.id.weight)).setText((Math.round(settings.isWeight() ? Settings.convertWeight(user.getWeight()) : user.getWeight())) + "");
            if (settings.isLenght()) {
                int inches = Math.round(Settings.convertLenght(user.getHeight()));
                 getView().findViewById(R.id.height).setVisibility(View.GONE);
                (getView().findViewById(R.id.ft)).setVisibility(View.VISIBLE);
                controller.inches.setText(inches % 12+"");
                controller.feet.setText(inches / 12+"");
            }
            ((EditText) getView().findViewById(R.id.height)).setText( Math.round(user.getHeight()) + "");
            ((Spinner) getView().findViewById(R.id.days)).setSelection(Integer.parseInt(new SimpleDateFormat("d").format(user.getDateOfBirth())) - 1);
            ((Spinner) getView().findViewById(R.id.months)).setSelection(Integer.parseInt(new SimpleDateFormat("M").format(user.getDateOfBirth())) - 1);
            ((Spinner) getView().findViewById(R.id.year)).setSelection(Integer.parseInt(new SimpleDateFormat("yyyy").format(user.getDateOfBirth())) - 1);
            ((Spinner) view.findViewById(R.id.sex)).setSelection(user.getSex()?0:1);
        }
    }

    @Override
    public User collectData(User user) {
        user.setFirstName(((EditText) getView().findViewById(R.id.first_name)).getText().toString());
        user.setLastName(((EditText) getView().findViewById(R.id.last_name)).getText().toString());
        if (settings.isWeight()) {
            user.setWeight(Settings.deconvertWeight(Long.valueOf(((EditText) getView().findViewById(R.id.weight)).getText().toString())));
        } else {
            user.setWeight(Long.valueOf(((EditText) getView().findViewById(R.id.weight)).getText().toString()));
        }
        if (settings.isLenght()) {
            long result;
            result = Long.parseLong(((EditText)getView().findViewById(R.id.foot)).getText().toString())*12;
            result +=Long.parseLong(((EditText)getView().findViewById(R.id.inches)).getText().toString());
            user.setHeight(Settings.deconvertLenght(result));
        } else {
            user.setHeight(Long.valueOf(((EditText) getView().findViewById(R.id.height)).getText().toString()));
        }
        user.setSex(((Spinner) getView().findViewById(R.id.sex)).getSelectedItemPosition()==0);
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
        return null;
    }

    private class HeightController {
        EditText feet, inches;

        public HeightController(View parent) {
            feet = (EditText) parent.findViewById(R.id.foot);
            inches = (EditText) parent.findViewById(R.id.inches);
            parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    feet.requestFocus();
                    final InputMethodManager inputMethodManager = (InputMethodManager) getContext()
                            .getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.showSoftInput(feet, InputMethodManager.SHOW_IMPLICIT);
                }
            });
            inches.addTextChangedListener(new TextWatcher() {
                private int oldValue = 0;

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    try {
                        int value = Integer.parseInt(s.toString());
                        if (value > 12) {
                            if (value / 10 == oldValue) {
                                inches.setText(value % (oldValue * 10) + "");
                            } else if (value % 100 == oldValue) {
                                inches.setText(value / 100 + "");
                            } else if (value % 10 == oldValue) {
                                inches.setText(value / 10 + "");
                            } else {
                                inches.setText((value % 100) / 10 + "");
                            }
                        }
                        oldValue = Integer.parseInt(inches.getText().toString());
                    } catch (NumberFormatException e) {
                    }
                }
            });
            feet.addTextChangedListener(new TextWatcher() {
                int oldValue = 0;

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    try {
                        int value = Integer.parseInt(s.toString());

                        if (value > 10) {
                            if (value / 10 != oldValue) {
                                feet.setText(value / 10 + "");
                            } else {
                                feet.setText(value % 10 + "");
                            }
                        }
                        inches.requestFocus();
                        final InputMethodManager inputMethodManager = (InputMethodManager) getContext()
                                .getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputMethodManager.showSoftInput(feet, InputMethodManager.SHOW_IMPLICIT);
                        oldValue = Integer.parseInt(feet.getText().toString());
                    } catch (NumberFormatException e) {
                    }
                }
            });
        }
    }
}
