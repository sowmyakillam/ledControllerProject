package se.ju.students.malu1798.ledcontrollerproject;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import petrov.kristiyan.colorpicker.ColorPicker;

import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;

public class PickColorActivity extends AppCompatActivity
        implements SeekBar.OnSeekBarChangeListener {

    ViewHolder viewHolder = new ViewHolder();
    ArrayList<ImageView> a_imageButtons = new ArrayList<>();

    SeekBar seekB_red;
    SeekBar seekB_green;
    SeekBar seekB_blue;

    private int r = 255;
    private int g = 255;
    private int b = 255;
    private int brightness = 255;

    private int lastBrightnessState = 0;

    //Colors
    Colors colorsVar = new Colors();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_color);

        //Image Views
        View v_top = findViewById(R.id.layout_top);
        a_imageButtons.add((ImageView) findViewById(R.id.i_pC_led));
        a_imageButtons.add((ImageView) findViewById(R.id.i_pC_led_1));
        a_imageButtons.add((ImageView) findViewById(R.id.i_pC_led_2));
        a_imageButtons.add((ImageView) findViewById(R.id.i_pC_led_3));
        a_imageButtons.add((ImageView) findViewById(R.id.i_pC_led_4));
        a_imageButtons.add((ImageView) findViewById(R.id.i_pC_led_5));
        a_imageButtons.add((ImageView) findViewById(R.id.i_pC_led_6));
        a_imageButtons.add((ImageView) findViewById(R.id.i_pC_led_7));
        a_imageButtons.add((ImageView) findViewById(R.id.i_pC_led_8));
        a_imageButtons.add((ImageView) findViewById(R.id.i_pC_led_9));


        //Color RGB layouts
        viewHolder.v_header = findViewById(R.id.layout_top);
        viewHolder.v_r = findViewById(R.id.t_r);
        viewHolder.v_g = findViewById(R.id.t_g);
        viewHolder.v_b = findViewById(R.id.t_b);

        //Color RGB texts
        viewHolder.t_r = findViewById(R.id.t_r);
        viewHolder.t_g = findViewById(R.id.t_g);
        viewHolder.t_b = findViewById(R.id.t_b);

        //Seek bars
        viewHolder.seekB_brightness = (SeekBar) findViewById(R.id.seekBar_brightness);
        seekB_red = (SeekBar) findViewById(R.id.seekBar_r);
        seekB_green = (SeekBar) findViewById(R.id.seekBar_g);
        seekB_blue = (SeekBar) findViewById(R.id.seekBar_b);
        //Buttons
        Button b_mode = findViewById(R.id.b_mode);
        ImageButton b_colorPicker = findViewById(R.id.iB_color_picker);
        ImageButton b_saveColor = findViewById(R.id.iB_saveColor);
        Button b_profile = findViewById(R.id.b_profiles);
        //Toggle Button
        final ToggleButton t_b_on_off = findViewById(R.id.tB_on_off);
        t_b_on_off.setChecked(true);


        b_saveColor.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        System.out.println("save Color Image Button clicked");

                        //int to hex conversion
                        String hexColor = intColorToHexString(
                                colorConvertWithBrightness(getR()),
                                colorConvertWithBrightness(getG()),
                                colorConvertWithBrightness(getB()
                                ));

                        colorsVar.addColor(hexColor);

                    }
                }
        );

        b_mode.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Code here executes on main thread after user presses button
                        System.out.println("Button mode clicked");
                        Intent intent = new Intent(v.getContext(), ModeActivity.class);
                        startActivity(intent);
                    }
                });

        b_colorPicker.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Code here executes on main thread after user presses button
                        System.out.println("Button colorPicker clicked");
                        openColorPicker();
                    }
                });

        b_profile.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        System.out.println("Button Color profile clicked");
                        Intent intent = new Intent(v.getContext(), ColorProfileActivity.class);
                        startActivity(intent);
                    }
                }
        );

        t_b_on_off.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        System.out.println("Toggle Button clicked");
                        boolean onOff = t_b_on_off.isChecked();
                        if (onOff) {
                            //ON
                            if (lastBrightnessState == 0)
                                setBrightness(255);
                            else
                                setBrightness(lastBrightnessState);

                        } else {
                            //OFF
                            lastBrightnessState = getBrightness();
                            setBrightness(0);
                        }
                        updateSeekBars();
                        updateViewColors(getR(), getG(), getB());
                        viewHolder.seekB_brightness.setEnabled(onOff);
                    }
                }
        );

        /*
        final Handler handler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                //long running computation
                //final int result = compute();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        //runs on thread that created the handler.

                    }
                });
            }
        }).start();

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                //code to be executed in the new thread.

            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
        */

        viewHolder.seekB_brightness.setOnSeekBarChangeListener(this);
        seekB_red.setOnSeekBarChangeListener(this);
        seekB_green.setOnSeekBarChangeListener(this);
        seekB_blue.setOnSeekBarChangeListener(this);

        updateViewColors(getR(), getG(), getB());
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {


        switch (seekBar.getId()) {
            case R.id.seekBar_r:
                System.out.println("--SeekBar onChange red " + progress + fromUser + seekBar);
                setR(progress);
                break;
            case R.id.seekBar_g:
                System.out.println("--SeekBar onChange green");
                setG(progress);
                break;
            case R.id.seekBar_b:
                System.out.println("--SeekBar onChange blue");
                setB(progress);
                break;
            case R.id.seekBar_brightness:
                setBrightness(progress);
                //inverse
                //brightness = (brightness*(-1))+255;
                //setBrightness((progress*(-1))+255);
                System.out.println("--SeekBar onChange brightness " + getBrightness());
                break;
            default:
                System.out.println("--SeekBar onChange default");
                break;
        }
        updateViewColors(getR(), getG(), getB());
    }

    private void updateViewColors(int red, int green, int blue) {
        int red_minus_brightness = colorConvertWithBrightness(red);
        int green_minus_brightness = colorConvertWithBrightness(green);
        int blue_minus_brightness = colorConvertWithBrightness(blue);


        //viewHolder.v_header.setBackgroundColor(Color.rgb(red_minus_brightness, green_minus_brightness, blue_minus_brightness));
        viewHolder.v_header.setBackgroundColor(Color.WHITE);
        viewHolder.v_r.setBackgroundColor(Color.rgb(red_minus_brightness, 0, 0));
        viewHolder.v_g.setBackgroundColor(Color.rgb(0, green_minus_brightness, 0));
        viewHolder.v_b.setBackgroundColor(Color.rgb(0, 0, blue_minus_brightness));
        setTextColorVisible(red_minus_brightness, viewHolder.t_r);
        setTextColorVisible(green_minus_brightness, viewHolder.t_g);
        setTextColorVisible(blue_minus_brightness, viewHolder.t_b);
        for (ImageView imgView : a_imageButtons) {
            imgView.setColorFilter(Color.rgb(red_minus_brightness, green_minus_brightness, blue_minus_brightness));
        }

    }


    private void setTextColorVisible(int color, TextView tV) {
        int lowerLimit = 120;
        if (color < lowerLimit)
            tV.setTextColor(Color.WHITE);
        else
            tV.setTextColor(Color.BLACK);
    }

    private void updateSeekBars() {
        seekB_red.setProgress(getR());
        seekB_green.setProgress(getG());
        seekB_blue.setProgress(getB());
        viewHolder.seekB_brightness.setProgress(getBrightness());
    }


    private void openColorPicker() {
        String colorCode = "#258174";

        final ColorPicker cPicker = new ColorPicker(this);
        //final ArrayList<String> colors = new ArrayList<>();
        final ArrayList<String> colorsArr = colorsVar.getColors();
        /*colors.add("#258174");
        colors.add("#27AE60");
        colors.add("#3498DB");
        colors.add("#CB4335");
        colors.add("#34495E");
        colors.add("#F4D03F");*/

        cPicker.setColors(colorsArr)
                .setOnChooseColorListener(
                        new ColorPicker.OnChooseColorListener() {
                            @Override
                            public void onChooseColor(int position, int color) {
                                if (position != -1) {
                                    Log.d("COLOR", "color = " + position + " - " + color);
                                    System.out.println("Position: " + position + " Color: " + color);
                                    //setColorWithHex(viewHolder.colors.getColor(position));
                                    setColorWithHex(colorsArr.get(position));
                                    updateViewColors(getR(), getG(), getB());
                                    updateSeekBars();
                                } else
                                    return;
                            }

                            @Override
                            public void onCancel() {
                                //??
                                //finish();
                            }
                        })
                .setColumns(5)
                .setRoundColorButton(true)
                .show();
    }

    private int getColorFullRange(int procentage) {
        double fullRange = procentage * 2.55;
        return (int) fullRange;
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        switch (seekBar.getId()) {
            case R.id.seekBar_r:
                System.out.println("--SeekBar onStart red");
                break;
            case R.id.seekBar_g:
                System.out.println("--SeekBar onStart green");
                break;
            case R.id.seekBar_b:
                System.out.println("--SeekBar onStart blue");
                break;
            case R.id.seekBar_brightness:
                System.out.println("--SeekBar onStart brightness");
                break;
            default:
                System.out.println("--SeekBar onStart default");
                break;
        }
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        switch (seekBar.getId()) {
            case R.id.seekBar_r:
                System.out.println("--SeekBar onStop red");
                break;
            case R.id.seekBar_g:
                System.out.println("--SeekBar onStop green");
                break;
            case R.id.seekBar_b:
                System.out.println("--SeekBar onStop blue");
                break;
            case R.id.seekBar_brightness:
                System.out.println("--SeekBar onStop brightness");
                break;
            default:
                System.out.println("--SeekBar onStop default");
                break;
        }
    }

    public void modeButtonClicked(View view) {
        System.out.println("Button clicked");
        Intent intent = new Intent(this, ModeActivity.class);
        startActivity(intent);
    }

    @Override
    //if settings menu should show
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    //settings menu
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                /*handle*/
                return true;
            case R.id.action_profile:

                return false;
            case R.id.swich_tilt:

                return false;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }


    public int getR() {
        return r;
    }

    public void setR(int r) {
        this.r = r;
    }

    public int getG() {
        return g;
    }

    public void setG(int g) {
        this.g = g;
    }

    public int getB() {
        return b;
    }

    public void setB(int b) {
        this.b = b;
    }

    public int colorConvertWithBrightness(int color) {
        return getValueSetByBrightness(color);
    }


    public int getBrightness() {
        return brightness;
    }

    public void setBrightness(int brightness) {
        this.brightness = brightness;
    }

    public int getValueSetByBrightness(int in) {
        int out;
        //out = (int) ((in * 100 / (getBrightness() + 1))) / 100;
        out = (getBrightness() * in) / 255;
        System.out.println("out Value brightness converter: " + out);
        return out;
    }

    /**
     * Converts a hex string to a color. If it can't be converted null is returned.
     *
     * @param hex (i.e. #CCCCCCFF or CCCCCC)
     * @return Color
     */
    private void setColorWithHex(String hex) {
        String colorStr = hex;
        setR(Integer.valueOf(colorStr.substring(1, 3), 16));
        setG(Integer.valueOf(colorStr.substring(3, 5), 16));
        setB(Integer.valueOf(colorStr.substring(5, 7), 16));
    }

    /**
     * Converts a color to a hex string. If it can't be converted null is returned.
     *
     * @param r (i.e. 0 to 255)
     * @param g (i.e. 0 to 255)
     * @param b (i.e. 0 to 255)
     * @return String (i.e #000000 to #FFFFFF)
     */
    public String intColorToHexString(int r, int g, int b) {
        //int to hex conversion
        String hexColor = String.format("#%02X%02X%02X", r, g, b); //"#" + s_r + s_g + s_b;

        System.out.println("---------RESULT----------- hexColor" + hexColor);

        return hexColor;
    }


}
