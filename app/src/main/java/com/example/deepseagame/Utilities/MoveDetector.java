package com.example.deepseagame.Utilities;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.example.deepseagame.Interfaces.MoveCallback;

public class MoveDetector {
    private SensorManager sensorManager;
    private Sensor sensor;
    private SensorEventListener sensorEventListener;
    private long timeStamp = 0l;
    private final double ACCELERATION = 2.0;
    private MoveCallback moveCallback;

    public MoveDetector(Context context, MoveCallback moveCallback) {
        this.sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        this.moveCallback = moveCallback;
        initEventListener();
    }


    private void initEventListener() {
        this.sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                float x = event.values[0];
                float y = event.values[1];
                calculateMove(x,y);
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
                // pass
            }
        };
    }

    private void calculateMove(float x, float y) {
        String dirX;
        String speedY;
        dirX = (x > 0) ? "Left" : (x < 0) ? "Right" : "";
        speedY = (y > 0) ? "Slow" : (y < 0) ? "Fast" : "";

        if (System.currentTimeMillis() - timeStamp > 500) {
            timeStamp = System.currentTimeMillis();
            if (x > ACCELERATION || x < -ACCELERATION) {
                if (moveCallback != null)
                    moveCallback.moveX(dirX);
            }
            if (y > ACCELERATION || y < -ACCELERATION) {
                if (moveCallback != null)
                    moveCallback.moveY(speedY);
            }
        }
    }


    public void start(){
        sensorManager.registerListener(
                sensorEventListener,
                sensor,
                SensorManager.SENSOR_DELAY_NORMAL
        );
    }

    public void stop(){
        sensorManager.unregisterListener(
                sensorEventListener,
                sensor
        );
    }
}
