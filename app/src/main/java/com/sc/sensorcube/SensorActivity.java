package com.sc.sensorcube;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SensorActivity extends AppCompatActivity {

    public Button startListenerButton;
    public Button stopListenerButton;
    public TextView sensorTextView;

    private SensorsManager sensor;

    private int rotation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);

        startListenerButton = findViewById(R.id.sensor_start_button);
        stopListenerButton = findViewById(R.id.sensor_stop_button);
        sensorTextView = findViewById(R.id.sensor_text);

        sensor = new SensorsManager(this);

        setupSensor();

        startListenerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sensor.startListener();
            }
        });
        stopListenerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sensor.stopListener();
            }
        });


    }
    private void setupSensor(){
        sensor = new SensorsManager(this);
        SensorsManager.SensorListener sensorListener = new SensorsManager.SensorListener() {
            @Override
            public void onNewParameters(double x, double y, double z) {
                rotation = getWindowManager().getDefaultDisplay().getRotation();
                sensor.setRotation(rotation);
                sensorTextView.setText("Rotation: " + rotation
                        + "\n X: " + x
                        + "\n Y: " + y
                        + "\n Z: " + z);
            }
        };
        sensor.setListener(sensorListener);
    }
}
