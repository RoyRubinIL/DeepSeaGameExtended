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

    private int moveCountX = 0;
    private int moveCountY = 0;
    private long timestamp = 0l;

    private MoveCallback moveCallback;

    public MoveDetector(Context context, MoveCallback moveCallback) {
        this.sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        this.moveCallback = moveCallback;
        initEventListener();
    }

    public int getMoveCountX() {
        return moveCountX;
    }

    public int getMoveCountY() {
        return moveCountY;
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
        if (System.currentTimeMillis() - timestamp > 500){
            timestamp = System.currentTimeMillis();
            if (x > 6.0 || x < -6.0){
                moveCountX++;
                if (moveCallback != null){
//                    moveCallback.moveX();
                }
            }
//            if (y > 6.0 || y < -6.0){
//                moveCountY++;
//                if (moveCallback != null){
//                    moveCallback.moveY();
//                }
//            }
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
