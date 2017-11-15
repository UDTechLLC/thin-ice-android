package com.udtech.thinice.ui;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.udtech.thinice.DeviceManager;
import com.udtech.thinice.R;
import com.udtech.thinice.ThinIceService;
import com.udtech.thinice.UserSessionManager;
import com.udtech.thinice.bluetooth.BluetoothLeService;
import com.udtech.thinice.eventbus.model.devices.DeviceChanged;
import com.udtech.thinice.model.Achievement;
import com.udtech.thinice.model.Day;
import com.udtech.thinice.model.users.User;
import com.udtech.thinice.ui.main.FragmentAchievements;
import com.udtech.thinice.ui.main.FragmentChangeRegistration;
import com.udtech.thinice.ui.main.FragmentControl;
import com.udtech.thinice.ui.main.FragmentDashBoard;
import com.udtech.thinice.ui.main.FragmentDialogAchievement;
import com.udtech.thinice.ui.main.FragmentSettings;
import com.udtech.thinice.ui.main.FragmentStatistics;
import com.udtech.thinice.ui.main.MenuHolder;
import com.udtech.thinice.utils.AchievementManager;
import com.udtech.thinice.utils.DateUtils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by Sofi on 16.11.2015.
 */
public class MainActivity extends SlidingFragmentActivity implements MenuHolder {
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;
    private LinearLayout menu;
    private int openedMenuItem;
    private ProgressDialog mProgressDialog;
    private boolean isOpened;
    private boolean isDayWasChanged = false;

    @Override
    protected void onResume() {
        super.onResume();
        if (DeviceManager.getDevice() != null && !DeviceManager.getDevice().isDisabled())
            EventBus.getDefault().post(new DeviceChanged(DeviceManager.getDevice()));
        try {
            checkDay();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        isOpened = true;
        if (isDayWasChanged) {
            isDayWasChanged = false;
            getSupportFragmentManager().beginTransaction().replace(R.id.container, new FragmentDashBoard()).commit();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_COARSE_LOCATION: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Functionality limited");
                    builder.setMessage("Since location access has not been granted, this app will not be able to discover devices when in the background.");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

                        @Override
                        public void onDismiss(DialogInterface dialog) {
                        }

                    });
                    builder.show();
                }
                return;
            }
        }
    }

    private int getNavigationBarHeight() {
        Resources resources = getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return resources.getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("This app needs location access");
                builder.setMessage("Please grant location access so this app can detect device.");
                builder.setPositiveButton(android.R.string.ok, null);
                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    public void onDismiss(DialogInterface dialog) {
                        requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_COARSE_LOCATION);
                    }

                });
                builder.show();
            }
        }
        //initialising main app functional
        //startService(new Intent(this, ThinIceService.class));
        AchievementManager.getInstance(getApplicationContext());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.rgb(34, 46, 59));
        }
        setContentView(R.layout.activity_main);
        //add menu view and main view in container
        View view = getLayoutInflater().inflate(R.layout.menu, null, false);
        setBehindContentView(view);
        menu = (LinearLayout) view.findViewById(R.id.menu);
        SlidingMenu sm = getSlidingMenu();
        sm.setFadeDegree(0.35f);
        sm.setShadowWidthRes(R.dimen.shadow_width);
        sm.setShadowDrawable(R.drawable.shadow);
        sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
        sm.setOnOpenedListener(new SlidingMenu.OnOpenedListener() {
            @Override
            public void onOpened() {
                if (getCurrentFocus() != null)
                    if (getCurrentFocus().getWindowToken() != null) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(
                                getCurrentFocus().getWindowToken(), 0);
                    }
            }
        });
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        sm.setBehindWidth((int) (width * 0.4)); //Size of menu in percents 0.4 = 40%
        initMenu(menu);
        try {
            checkDay();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
        if (Build.MODEL.equalsIgnoreCase("nexus 6") || Build.MODEL.equalsIgnoreCase("nexus 5") || Build.MODEL.equalsIgnoreCase("nexus 7")) {
            int navBarHeight = getNavigationBarHeight();
            findViewById(R.id.container).setPadding(0, 0, 0, navBarHeight);
            findViewById(R.id.menu_frame).setPadding(0, 0, 0, navBarHeight);
        }
        AchievementManager.getInstance(getApplicationContext()).registrationCompleted(getApplicationContext());
    }

    @Override
    public void openPosition(int position) {
        resetMenuSelection(menu);
        selectMenuItem(menu, position);
    }

    @Override
    public void show() {
        showMenu();
    }

    @Override
    public void showMenuItem(int position) {
        if (DeviceManager.getDevice() != null && !DeviceManager.getDevice().isDisabled())
            EventBus.getDefault().post(new DeviceChanged(DeviceManager.getDevice()));
        if (position != openedMenuItem)
            switch (position) {
                case MenuHolder.SETTINGS: {
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, new FragmentSettings()).addToBackStack(null).commit();
                    break;
                }
                case MenuHolder.STATISTICS: {
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, new FragmentStatistics()).addToBackStack(null).commit();
                    break;
                }
                case MenuHolder.DASHBOARD: {
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, new FragmentDashBoard()).addToBackStack(null).commit();
                    break;
                }
                case MenuHolder.CONTROL: {
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, new FragmentControl()).addToBackStack(null).commit();
                    break;
                }
                case MenuHolder.ACHIEVEMENTS: {
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, new FragmentAchievements()).addToBackStack(null).commit();
                    break;
                }
                case MenuHolder.ACCOUNT: {
                    //getSupportFragmentManager().beginTransaction().replace(R.id.container, new FragmentAchievements()).addToBackStack(null).commit();
                    break;
                }
            }
    }

    private void initMenu(final LinearLayout menu) {
        int ii = 0;
        for (int i = 0; i < menu.getChildCount(); i++) {
            if (menu.getChildAt(i) instanceof FrameLayout) {
                final int finalIi = ii;
                menu.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openMenuItem(finalIi);
                    }
                });
                ii++;
            }
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.container, new FragmentDashBoard()).commit();
    }

    private void openMenuItem(int position) {
        if (DeviceManager.getDevice() != null && !DeviceManager.getDevice().isDisabled())
            EventBus.getDefault().post(new DeviceChanged(DeviceManager.getDevice()));
        if (position != openedMenuItem)
            switch (position) {
                case MenuHolder.SETTINGS: {
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, new FragmentSettings()).addToBackStack(null).commit();
                    break;
                }
                case MenuHolder.STATISTICS: {
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, new FragmentStatistics()).addToBackStack(null).commit();
                    break;
                }
                case MenuHolder.DASHBOARD: {
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, new FragmentDashBoard()).addToBackStack(null).commit();
                    break;
                }
                case MenuHolder.CONTROL: {
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, new FragmentControl()).addToBackStack(null).commit();
                    break;
                }
                case MenuHolder.ACHIEVEMENTS: {
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, new FragmentAchievements()).addToBackStack(null).commit();
                    break;
                }
                case MenuHolder.ACCOUNT: {
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, new FragmentChangeRegistration()).addToBackStack(null).commit();
                    break;
                }
            }
        toggle();
    }

    private void resetMenuSelection(LinearLayout menu) {
        for (int i = 0; i < menu.getChildCount(); i++) {
            if (menu.getChildAt(i) instanceof FrameLayout) {
                deselectMenuItem((FrameLayout) menu.getChildAt(i));
            }
        }
    }

    public void onEvent(Achievement achievement) {
        DialogFragment dialog = new FragmentDialogAchievement();
        Bundle bundle = new Bundle();
        bundle.putInt("id", achievement.getId());
        dialog.setArguments(bundle);
        dialog.show(getSupportFragmentManager(), null);
    }

    public void onEvent(final String log) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getBaseContext(), log, Toast.LENGTH_LONG).show();
            }
        });
    }

    public void onEvent(final Day day) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (openedMenuItem == MenuHolder.DASHBOARD && isOpened) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, new FragmentDashBoard()).commit();
                } else if (!isOpened) {
                    isDayWasChanged = true;
                }
            }
        });
    }

    private void deselectMenuItem(FrameLayout item) {
        TextView itemText = (TextView) (((LinearLayout) item.getChildAt(0)).getChildAt(1));
        itemText.setTextColor(getResources().getColor(R.color.textViewColor));
        item.setBackgroundColor(Color.argb(0, 0, 0, 0));
    }

    private void selectMenuItem(LinearLayout menu, int position) {
        openedMenuItem = position;
        FrameLayout item = null;
        for (int i = 0, ii = 0; i < menu.getChildCount() && ii <= position; i++) {
            if (menu.getChildAt(i) instanceof FrameLayout) {
                if (ii++ == position) {
                    item = (FrameLayout) menu.getChildAt(i);
                    break;
                }
            }
        }
        if (item != null) {
            TextView itemText = (TextView) (((LinearLayout) item.getChildAt(0)).getChildAt(1));
            itemText.setTextColor(getResources().getColor(R.color.colorAccent));
            item.setBackgroundColor(Color.argb(64, 0, 0, 0));
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        isOpened = false;
        if (DeviceManager.getDevice() != null && !DeviceManager.getDevice().isDisabled())
            EventBus.getDefault().post(new DeviceChanged(DeviceManager.getDevice()));
        AchievementManager.getInstance(getApplicationContext()).commit(getApplicationContext()); // committing for unexpected closing of app
    }

    private void checkDay() throws ParseException {// check is new day is already created and creating it if it is not
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        Iterator<Day> dayList = Day.findAll(Day.class);
        boolean created = false;
        User user = UserSessionManager.getSession(getApplicationContext());
        Date currentDate = new Date();
        while (dayList.hasNext()) {
            Day day = dayList.next();
            if (DateUtils.isSameDay(day.getDate(), currentDate)) {
                if (user.equals(day.getUser())) {
                    created = true;
                    break;
                }
            }
        }
        if (!created) {
            dayList = Day.findAll(Day.class);
            List<Day> days = new ArrayList<>();
            while (dayList.hasNext()) {
                days.add(dayList.next());
            }
            if (days.size() > 1) {
                Day lastDay = days.get(days.size() - 1);
                Calendar todayCal = new GregorianCalendar();
                todayCal.setTime(new Date());
                Calendar lastDate = new GregorianCalendar();
                lastDate.setTime(lastDay.getDate());
                for (int i = lastDate.get(Calendar.DAY_OF_YEAR) + 1; i < todayCal.get(Calendar.DAY_OF_YEAR); i++) {
                    Calendar calendar1 = new GregorianCalendar();
                    calendar.setTime(new Date());
                    calendar.set(Calendar.DAY_OF_YEAR, i);
                    Day day = new Day(UserSessionManager.getSession(getApplicationContext()));
                    day.setDate(calendar.getTime());
                    day.save();
                }

                new Day(UserSessionManager.getSession(getApplicationContext())).save();

            } else {
                new Day(UserSessionManager.getSession(getApplicationContext())).save();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //closing service< bluetooth connections and event bus registration
        stopService(new Intent(this, ThinIceService.class));
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


}
