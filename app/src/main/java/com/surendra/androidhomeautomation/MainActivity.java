package com.surendra.androidhomeautomation;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.surendra.androidhomeautomation.fragment.NumberPickerFragment;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        NumberPickerFragment.OnCompleteListener {

    private static final int REQ_CODE_SPEECH_INPUT = 100;

    private Bulb redBulb, greenBulb, blueBulb;

    private TextView timerRed;
    private TextView timerBlue;
    private TextView timerGreen;

    private CountDownTimer timer1;
    private CountDownTimer timer2;
    private CountDownTimer timer3;

    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                promptSpeechInput();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Timer Texts
        timerRed = (TextView) findViewById(R.id.timer_red);
        timerBlue = (TextView) findViewById(R.id.timer_blue);
        timerGreen = (TextView) findViewById(R.id.timer_green);

        //Initializes the bulbs
        initBulb();

        //Set OnClickListener
        findViewById(R.id.redCard).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redBulb.toggle();
            }
        });

        findViewById(R.id.blueCard).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                blueBulb.toggle();
            }
        });

        findViewById(R.id.greenCard).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                greenBulb.toggle();
            }
        });

        findViewById(R.id.redCard).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                NumberPickerFragment newFragment = new NumberPickerFragment();
                //newFragment.show(getActivity().getFragmentManager(), DIALOG_TIME);
                newFragment.setId('r');
                newFragment.setSwitchState(redBulb.mIsOn);
                newFragment.show(MainActivity.this.getSupportFragmentManager(), "Red");
                return true;
            }
        });

        findViewById(R.id.blueCard).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                NumberPickerFragment newFragment = new NumberPickerFragment();
                //newFragment.show(getActivity().getFragmentManager(), DIALOG_TIME);
                newFragment.setId('b');
                newFragment.setSwitchState(blueBulb.mIsOn);
                newFragment.show(MainActivity.this.getSupportFragmentManager(), "Blue");
                return true;
            }
        });

        findViewById(R.id.greenCard).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                NumberPickerFragment newFragment = new NumberPickerFragment();
                //newFragment.show(getActivity().getFragmentManager(), DIALOG_TIME);
                newFragment.setId('g');
                newFragment.setSwitchState(greenBulb.mIsOn);
                newFragment.show(MainActivity.this.getSupportFragmentManager(), "Green");
                return true;
            }
        });
    }

    private void initBulb() {
        redBulb = new Bulb('r', (ImageView) findViewById(R.id.red_bulb_img),
                (TextView) findViewById(R.id.red_bulb_state), R.drawable.red_bulb);

        greenBulb = new Bulb('g', (ImageView) findViewById(R.id.green_bulb_image),
                (TextView) findViewById(R.id.green_bulb_state), R.drawable.green_bulb);

        blueBulb = new Bulb('b', (ImageView) findViewById(R.id.blue_bulb_img),
                (TextView) findViewById(R.id.blue_bulb_state), R.drawable.blue_bulb);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT:
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    String val = result.get(0);
                    Toast.makeText(MainActivity.this, val, Toast.LENGTH_LONG).show();
                    //TODO: add timer functionality to automatically turn on/off after some time
                    val = val.toLowerCase();
                    if (val.contains("all")) {
                        if (val.contains(" on")) {
                            redBulb.turnOn();
                            blueBulb.turnOn();
                            greenBulb.turnOn();
                        } else if (val.contains(" off")) {
                            redBulb.turnOff();
                            blueBulb.turnOff();
                            greenBulb.turnOff();
                        }
                    } else if (val.contains(" and")) {
                        if (val.contains(" on")) {
                            if (val.contains(" red"))
                                redBulb.turnOn();
                            if (val.contains(" blue"))
                                blueBulb.turnOn();
                            if (val.contains(" green"))
                                greenBulb.turnOn();
                        } else if (val.contains(" off")) {
                            if (val.contains(" red"))
                                redBulb.turnOff();
                            if (val.contains(" blue"))
                                blueBulb.turnOff();
                            if (val.contains(" green"))
                                greenBulb.turnOff();
                        }
                    } else if (val.contains("red")) {
                        if (val.contains(" on")) {
                            redBulb.turnOn();
                        } else if (val.contains(" off")) {
                            redBulb.turnOff();
                        }
                    } else if (val.contains("blue")) {
                        if (val.contains(" on")) {
                            blueBulb.turnOn();
                        } else if (val.contains(" off")) {
                            blueBulb.turnOff();
                        }
                    } else if (val.contains("green")) {
                        if (val.contains(" on")) {
                            greenBulb.turnOn();
                        } else if (val.contains(" off")) {
                            greenBulb.turnOff();
                        }
                    }
                    break;
                }
        }
    }

    private void promptSpeechInput() {
        //Showing google speech input dialog
        //Simply takes user's speech input and returns it to same activity
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);         //Considers input in free form English
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.speech_prompt));          //Text prompt to show to user when asking them to speak

        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException ex) {
            Toast.makeText(MainActivity.this, R.string.speech_not_supported, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.action_speech) {
            promptSpeechInput();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_help) {
            Intent helpIntent = new Intent(MainActivity.this, HelpActivity.class);
            startActivity(helpIntent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onComplete(char id, int time, final boolean toTurnOn) {
        //Sets the bulb
        Bulb bulb = null;
        TextView timerText = null;
        if (id == 'r') {
            bulb = redBulb;
            timerText = timerRed;
        }
        if (id == 'g') {
            bulb = greenBulb;
            timerText = timerGreen;
        }
        if (id == 'b') {
            bulb = blueBulb;
            timerText = timerBlue;
        }

        //A final timerText and bulb to be used by timer
        final TextView finalTimerText = timerText;
        final Bulb finalBulb = bulb;

        //if appropriate bulb not found exit
        if (bulb == null) return;

        //If the state of bulb is equal to the toTurnOn then exit
        if (toTurnOn == bulb.isBulbOn()) return;

        if (time > 0) {
            switch (id) {
                case 'r':
                    //Cancel the previous timer
                    if (timer1 != null)
                        timer1.cancel();
                    //Creates new timer with the new time
                    timer1 = new CountDownTimer(time, 1000) {
                        public void onTick(long millisUntilFinished) {
                            finalTimerText.setText(formatTimerValue(toTurnOn, millisUntilFinished));
                        }

                        public void onFinish() {
                            finalTimerText.setText("");
                            if (toTurnOn) {
                                finalBulb.turnOn();
                            } else {
                                finalBulb.turnOff();
                            }
                        }
                    }.start();
                    break;
                case 'g':
                    //Cancel the previous timer
                    if (timer2 != null)
                        timer2.cancel();
                    //Creates new timer with the new time
                    timer2 = new CountDownTimer(time, 1000) {
                        public void onTick(long millisUntilFinished) {
                            finalTimerText.setText(formatTimerValue(toTurnOn, millisUntilFinished));
                        }

                        public void onFinish() {
                            finalTimerText.setText("");
                            if (toTurnOn) {
                                finalBulb.turnOn();
                            } else {
                                finalBulb.turnOff();
                            }
                        }
                    }.start();
                    break;
                case 'b':
                    //Cancel the previous timer
                    if (timer3 != null)
                        timer3.cancel();
                    //Creates new timer with the new time
                    timer3 = new CountDownTimer(time, 1000) {
                        public void onTick(long millisUntilFinished) {
                            finalTimerText.setText(formatTimerValue(toTurnOn, millisUntilFinished));
                        }

                        public void onFinish() {
                            finalTimerText.setText("");
                            if (toTurnOn) {
                                finalBulb.turnOn();
                            } else {
                                finalBulb.turnOff();
                            }
                        }
                    }.start();
                    break;
            }
        }
    }

    private String formatTimerValue(boolean toOn, long millis) {
        StringBuilder builder = new StringBuilder();
        builder.append(millis / 1000);
        builder.append("s to turn ");
        if (toOn) {
            builder.append(" On.");
        } else {
            builder.append(" Off.");
        }
        return builder.toString();
    }
}
