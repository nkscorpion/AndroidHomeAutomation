package com.surendra.androidhomeautomation;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
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
import com.surendra.androidhomeautomation.myClass.Light;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        NumberPickerFragment.OnCompleteListener {

    private static final int REQ_CODE_SPEECH_INPUT = 100;

    private Light redLight;
    private Light blueLight;
    private Light greenLight;

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

        //Initializes the Lights
        initLights();

        //Set OnClickListener
        findViewById(R.id.redCard).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redLight.toggle();
            }
        });

        findViewById(R.id.blueCard).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                blueLight.toggle();
            }
        });

        findViewById(R.id.greenCard).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                greenLight.toggle();
            }
        });

        findViewById(R.id.redCard).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                NumberPickerFragment newFragment = new NumberPickerFragment();
                //newFragment.show(getActivity().getFragmentManager(), DIALOG_TIME);
                newFragment.setId('r');
                newFragment.setSwitchState(redLight.isOn());
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
                newFragment.setSwitchState(blueLight.isOn());
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
                newFragment.setSwitchState(greenLight.isOn());
                newFragment.show(MainActivity.this.getSupportFragmentManager(), "Green");
                return true;
            }
        });
    }

    private void initLights() {
        redLight = new Light('r', R.drawable.red_bulb, (TextView) findViewById(R.id.red_bulb_state),
                (ImageView) findViewById(R.id.red_bulb_img), (TextView) findViewById(R.id.timer_red));

        blueLight = new Light('b', R.drawable.blue_bulb, (TextView) findViewById(R.id.blue_bulb_state),
                (ImageView) findViewById(R.id.blue_bulb_img), (TextView) findViewById(R.id.timer_blue));

        greenLight = new Light('g', R.drawable.green_bulb, (TextView) findViewById(R.id.green_bulb_state),
                (ImageView) findViewById(R.id.green_bulb_image), (TextView) findViewById(R.id.timer_green));
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
                            redLight.turnOn();
                            blueLight.turnOn();
                            greenLight.turnOn();
                        } else if (val.contains(" off")) {
                            redLight.turnOff();
                            blueLight.turnOff();
                            greenLight.turnOff();
                        }
                    } else if (val.contains(" and")) {
                        if (val.contains(" on")) {
                            if (val.contains(" red"))
                                redLight.turnOn();
                            if (val.contains(" blue"))
                                blueLight.turnOn();
                            if (val.contains(" green"))
                                greenLight.turnOn();
                        } else if (val.contains(" off")) {
                            if (val.contains(" red"))
                                redLight.turnOff();
                            if (val.contains(" blue"))
                                blueLight.turnOff();
                            if (val.contains(" green"))
                                greenLight.turnOff();
                        }
                    } else if (val.contains("red")) {
                        if (val.contains(" on")) {
                            redLight.turnOn();
                        } else if (val.contains(" off")) {
                            redLight.turnOff();
                        }
                    } else if (val.contains("blue")) {
                        if (val.contains(" on")) {
                            blueLight.turnOn();
                        } else if (val.contains(" off")) {
                            blueLight.turnOff();
                        }
                    } else if (val.contains("green")) {
                        if (val.contains(" on")) {
                            greenLight.turnOn();
                        } else if (val.contains(" off")) {
                            greenLight.turnOff();
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
        if (time > 0) {
            switch (id) {
                case 'r':
                    redLight.setTimer(time, toTurnOn);
                    break;
                case 'g':
                    greenLight.setTimer(time, toTurnOn);
                    break;
                case 'b':
                    blueLight.setTimer(time, toTurnOn);
                    break;
            }
        }
    }
}
