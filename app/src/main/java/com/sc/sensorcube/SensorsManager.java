package com.sc.sensorcube;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.Surface;

/**
 * Created by Maciej Szalek on 2018-10-09.
 */

public class SensorsManager implements SensorEventListener {


    public interface SensorListener{
        void onNewParameters(double x, double y, double z);
    }

    private Sensor mAccelerometer;
    private SensorManager mSensorManager;
    private SensorListener listener;

    private double x, y, z;
    private int rotation;
    static final float ALPHA = 0.99f;

    public SensorsManager(Context context){
        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    public int getRotation(){
        return rotation;
    }
    public void setRotation(int rotation){
        this.rotation = rotation;
    }

    public void startListener(){
        mSensorManager.registerListener(this,
                mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void stopListener(){
        mSensorManager.unregisterListener(this);
    }

    public void setListener(SensorListener l){
        this.listener = l;
    }

    protected float[] lowPassFilter(float[] input, float[] output){
        if(output == null){
            return input;
        }
        for(int i=0; i<input.length; i++){
            output[i] = output[i] * ALPHA + (1.0f - ALPHA) * input[i];
        }
        return output;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        final float alpha = (float) 0.9;
        final float gravity[] = new float[3];

        gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
        gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
        gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];

        switch (getRotation()) {
            case Surface.ROTATION_0:
                x = event.values[0] - gravity[0];
                y = event.values[1] - gravity[1];
                z = event.values[2] - gravity[2];
                break;
            case Surface.ROTATION_90:
                x = -(event.values[1] - gravity[1]);
                y = event.values[0] - gravity[0];
                z = event.values[2] - gravity[2];
                break;
            case Surface.ROTATION_180:
                x = -(event.values[0] - gravity[0]);
                y = -(event.values[1] - gravity[1]);
                z = event.values[2] - gravity[2];
                break;
            case Surface.ROTATION_270:
                x = event.values[1] - gravity[1];
                y = -(event.values[0] - gravity[0]);
                z = event.values[2] - gravity[2];

        }
        if(listener !=null){
            listener.onNewParameters(x, y, z);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

}
