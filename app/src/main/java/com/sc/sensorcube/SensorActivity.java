package com.sc.sensorcube;

import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
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
    private GLSurfaceView mGlSurfaceView;

    private int rotation;
    private float xRot;
    private float yRot;
    private float zRot;
    private float xSpeed;
    private float ySpeed;
    private float zSpeed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);

        startListenerButton = findViewById(R.id.sensor_start_button);
        stopListenerButton = findViewById(R.id.sensor_stop_button);
        sensorTextView = findViewById(R.id.sensor_text);
        mGlSurfaceView = findViewById(R.id.gl_surface_view);

        sensor = new SensorsManager(this);
        setupSensor();

        mGlSurfaceView.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        mGlSurfaceView.setZOrderOnTop(true);
        mGlSurfaceView.setRenderer(new GLRenderer(true,
                SensorActivity.this.getApplicationContext()));
        mGlSurfaceView.getHolder().setFormat(PixelFormat.RGBA_8888);

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
            public void onNewParameters(float x, float y, float z) {
                rotation = getWindowManager().getDefaultDisplay().getRotation();
                sensorTextView.setText("\n X: " + x
                        + "\n Y: " + y
                        + "\n Z: " + z);
            }
        };
        sensor.setListener(sensorListener);
    }
    protected void onResume(){
        super.onResume();
    }
}
