package com.udtech.thinice.ui.main.cards;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.text.format.DateUtils;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.udtech.thinice.DeviceManager;
import com.udtech.thinice.R;
import com.udtech.thinice.eventbus.model.cards.ShowBackCard;
import com.udtech.thinice.eventbus.model.cards.ShowFrontCard;
import com.udtech.thinice.eventbus.model.devices.DeviceChanged;
import com.udtech.thinice.model.Day;
import com.udtech.thinice.model.Settings;
import com.udtech.thinice.model.devices.Device;
import com.udtech.thinice.protocol.CaloryesUtils;
import com.udtech.thinice.utils.SessionManager;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import de.greenrobot.event.EventBus;

/**
 * Created by JOkolot on 18.11.2015.
 */
public class CardView extends FrameLayout {
    private GestureDetector gdt;
    private View frontSide;
    private Day day;
    private volatile int dayId = 0;
    private float calories;
    private Settings settings;
    private float totalCalories;

    public CardView(Context context) {
        this(context, null);
    }

    public CardView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setVisibility(INVISIBLE);
        settings = new Settings().fetch(getContext());
        frontSide = View.inflate(getContext(), R.layout.item_dashboard_day, null);
        frontSide.findViewById(R.id.switchCard).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                switchCards();
            }
        });
        this.addView(frontSide);

        EventBus.getDefault().register(this);
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        EventBus.getDefault().unregister(this);
    }

    public void setDay(Day day) {
        if (com.udtech.thinice.utils.DateUtils.isToday(day.getDate())) {

            gdt = new GestureDetector(getContext(), new GestureListener());
            this.setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    gdt.onTouchEvent(event);
                    return true;
                }
            });
        }
        this.day = day;
        ((TextView) findViewById(R.id.timeTotal)).setText(new SimpleDateFormat("HH:mm").format(new Date(
                (long) (1 / (SessionManager.getManager().getCurrentCaloriesRatePerSecond()))
        )));
        update((int) (long) day.getId());
        checkTasks();
        ((TextView) findViewById(R.id.date)).setText(new SimpleDateFormat("MMMM dd, yyyy").format(day.getDate()));
        if (com.udtech.thinice.utils.DateUtils.isToday(day.getDate())) {
            ((TextView) findViewById(R.id.day)).setText(DateUtils.getRelativeTimeSpanString(day.getDate().getTime(), new Date().getTime(), DateUtils.DAY_IN_MILLIS));
        } else {
            findViewById(R.id.switchCard).setVisibility(GONE);
        }
        GradientDrawable bgShape = (GradientDrawable) findViewById(R.id.header).getBackground();
        bgShape.setColor(day.getHeaderColor());
    }

    private void checkTasks() {
        ((Activity) getContext()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                settings = settings.fetch(getContext());
                int temp = 0, count = 0;
                Device tShirt = DeviceManager.getDevice();
                if (tShirt != null)
                    temp = (int) tShirt.getTemperature();
                else {
                    temp = day.getLastTemp();
                }
                ((TextView) findViewById(R.id.temperature)).setText((Math.round(settings.isTemperature() ? Settings.convertTemperatureToFaringeite(temp) : temp))
                        + (settings.isTemperature() ? "°F" : "°C"));
                ((TextView) findViewById(R.id.value_gym)).setText(day.getGymHours() + "");
                ((TextView) findViewById(R.id.value_carbs)).setText(day.getCarbsConsumed()+"");
                ((TextView) findViewById(R.id.value_food)).setText(day.getJunkFood() + "");
                ((TextView) findViewById(R.id.value_proteins)).setText(day.gethProteinMeals() + "");
                ((TextView) findViewById(R.id.value_sleep)).setText(day.getHoursSlept() + "");
                ((TextView) findViewById(R.id.value_water)).setText(Math.round(settings.isVolume() ? day.getWaterIntake() / 28.3495 : day.getWaterIntake()) + "");
            }
        });

    }

    public void switchCards() {
        EventBus.getDefault().post(new ShowBackCard(day));
    }

    public void reverseSwitchCards() {
        EventBus.getDefault().post(new ShowBackCard(day, true));
    }

    public void onEvent(DeviceChanged event) {
        checkTasks();
    }

    public void onEvent(ShowFrontCard event) {
        checkTasks();
    }

    public void onEvent(Settings settings) {
        ((Activity) getContext()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                checkTasks();
            }
        });
    }

    public void update(final int dayId) {
        if (!FragmentFrontCard.isMoving()) {
            if (com.udtech.thinice.utils.DateUtils.isToday(day.getDate())) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final int targetCalories = (int) SessionManager.getManager().getTargetCalories();
                        final int spendedCalories = (int) SessionManager.getManager().getSpendedCallories();
                        final long spendedTime = SessionManager.getManager().getSpended();
                        final long targetTime = (long) SessionManager.getManager().getTargetTime();
                        ((Activity) getContext()).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ((ProgressBar) findViewById(R.id.progressBar)).setMax(targetCalories);
                                ((ProgressBar) findViewById(R.id.progressBar)).setProgress(spendedCalories);
                                ((TextView) findViewById(R.id.time_spended)).setText(new SimpleDateFormat("H:m.s").format(new Date(spendedTime)));
                                ((TextView) findViewById(R.id.calories)).setText(spendedCalories + " Cal");
                                if (DeviceManager.getDevice() != null) {
                                    if (DeviceManager.getDevice().isDisabled() || !com.udtech.thinice.utils.DateUtils.isToday(day.getDate())) {
                                        if (targetCalories < spendedCalories) {
                                            ((TextView) findViewById(R.id.timeTotal)).setText("achieved!");
                                            findViewById(R.id.textView19).setVisibility(GONE);
                                        } else if (day.getLastTemp() == 20) {
                                            ((TextView) findViewById(R.id.timeTotal)).setText("comfort");
                                            findViewById(R.id.textView19).setVisibility(GONE);
                                        } else {
                                            Calendar calendar = Calendar.getInstance();
                                            calendar.setTimeInMillis(targetTime);
                                            ((TextView) findViewById(R.id.timeTotal)).setText(calendar.get(Calendar.HOUR)+":"+(calendar.get(Calendar.MINUTE)+1));
                                            findViewById(R.id.textView19).setVisibility(VISIBLE);
                                        }
                                    } else {
                                        if (targetCalories < spendedCalories) {
                                            ((TextView) findViewById(R.id.timeTotal)).setText("achieved!");
                                            findViewById(R.id.textView19).setVisibility(GONE);
                                        } else if (DeviceManager.getDevice().getTemperature() == 20) {
                                            ((TextView) findViewById(R.id.timeTotal)).setText("comfort");
                                            findViewById(R.id.textView19).setVisibility(GONE);
                                        } else {
                                            ((TextView) findViewById(R.id.timeTotal)).setText(new SimpleDateFormat("HH:mm").format(new Date(targetTime)));
                                            findViewById(R.id.textView19).setVisibility(VISIBLE);
                                        }
                                    }
                                } else {
                                    if (targetCalories < spendedCalories) {
                                        ((TextView) findViewById(R.id.timeTotal)).setText("achieved!");
                                        findViewById(R.id.textView19).setVisibility(GONE);
                                    } else if (day.getLastTemp() == 20) {
                                        ((TextView) findViewById(R.id.timeTotal)).setText("comfort");
                                        findViewById(R.id.textView19).setVisibility(GONE);
                                    } else {
                                        ((TextView) findViewById(R.id.timeTotal)).setText(new SimpleDateFormat("HH:mm").format(new Date(targetTime)));
                                        findViewById(R.id.textView19).setVisibility(VISIBLE);
                                    }
                                }
                                setVisibility(VISIBLE);
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        update(dayId);
                                    }
                                }, 1000);
                            }
                        });
                    }
                }).start();

            } else {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final int targetCalories = (int) SessionManager.getManager().getTargetCalories();
                        final int totalCalories = (int) day.getTotalCalories();
                        final long totalTime = day.getTotalTime();
                        final int lastTemp = day.getLastTemp();
                        ((Activity) getContext()).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ((ProgressBar) findViewById(R.id.progressBar)).setMax(targetCalories);
                                ((ProgressBar) findViewById(R.id.progressBar)).setProgress(totalCalories);
                                ((TextView) findViewById(R.id.time_spended)).setText(new SimpleDateFormat("H:m.s").format(new Date(totalTime)));
                                ((TextView) findViewById(R.id.calories)).setText(totalCalories + " Cal");
                                ((TextView) findViewById(R.id.timeTotal)).setText(new SimpleDateFormat("HH:mm").format(new Date((long) (totalTime + 1000 * (targetCalories - totalCalories) / CaloryesUtils.getBurningSpeedPerSecond(lastTemp)))));
                                setVisibility(VISIBLE);
                            }
                        });
                    }
                }).start();
            }
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    update(dayId);
                }
            }, 1000);
        }
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        private static final int SWIPE_MIN_DISTANCE = 30;
        private static final int SWIPE_THRESHOLD_VELOCITY = 60;

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (e1.getY() - e2.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
                return false; // Bottom to top
            } else if (e2.getY() - e1.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
                return false; // Top to bottom
            }
            if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                reverseSwitchCards();
                return false; // Right to left
            } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                switchCards();
                return false; // Right to left
            }


            return true;
        }

    }
}
