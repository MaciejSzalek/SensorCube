package com.sc.sensorcube;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * Created by Maciej Szalek on 2018-10-09.
 */

public class SensorsManager implements SensorEventListener {


    public interface SensorListener{
        void onNewParameters(float x, float y, float z);
    }

    private Sensor mAccelerometer;
    private SensorManager mSensorManager;
    private SensorListener listener;

    private float x, y, z;
    private static final float ALPHA = 0.99f;

    public SensorsManager(Context context){
        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
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
        float[] rotationMatrix = new float[16];
        rotationMatrix = lowPassFilter(event.values.clone(), rotationMatrix);
        SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values);

        float[] remappedRotationMatrix = new float[16];
        remappedRotationMatrix = lowPassFilter(event.values.clone(), remappedRotationMatrix);
        SensorManager.remapCoordinateSystem(rotationMatrix,
                SensorManager.AXIS_X,
                SensorManager.AXIS_Z,
                remappedRotationMatrix);

        float[] orientation = new float[3];
        SensorManager.getOrientation(remappedRotationMatrix, orientation);
        for(int i = 0; i<3; i++ ){
            orientation[i] = (float) (Math.toDegrees(orientation[i]));
        }
        x = orientation[0];
        y = orientation[1];
        z = orientation[2];

        if(listener !=null){
            listener.onNewParameters(x, y, z);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

}
