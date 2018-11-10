package com.sc.sensorcube;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class SensorActivity extends AppCompatActivity {

    public Button startListenerButton;
    public Button stopListenerButton;
    public TextView sensorTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);

        startListenerButton = findViewById(R.id.sensor_start_button);
        stopListenerButton = findViewById(R.id.sensor_stop_button);
        sensorTextView = findViewById(R.id.sensor_text);

    }
}
