package pt.ipp.estg.dwdn.pdm1.sensorsflg;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private TextView lightTextView;
    private TextView proximityTextView;
    private View view; //to change background color
    private SensorManager sensorManager;
    private Sensor lightSensor;
    private Sensor proximitySensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupViewBinding();
    }

    private void setupViewBinding() {
        lightTextView = findViewById(R.id.light_text_view);
        proximityTextView = findViewById(R.id.proximity_text_view);
        view = findViewById(R.id.main);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        if (lightSensor == null) {
            lightTextView.setText(R.string.error_no_sensor);
        } else {
            lightTextView.setText(getString(R.string.label_light, 0.0f));
        }

        if (proximitySensor == null) {
            proximityTextView.setText(R.string.error_no_sensor);
        } else {
            proximityTextView.setText(getString(R.string.label_proximity, 0.0f));
        }
    }

    protected void onResume(){
        super.onResume();
        sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, proximitySensor, SensorManager.SENSOR_DELAY_NORMAL);
    }
    protected void onPause(){
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.values == null || event.values.length == 0) {
            return;
        }
        int sensorType = event.sensor.getType();
        float value = event.values[0];
        switch (sensorType) {
            case Sensor.TYPE_LIGHT:
                lightTextView.setText(getString(R.string.label_light, value));
                updateBackground(value);
                break;
            case Sensor.TYPE_PROXIMITY:
                proximityTextView.setText(getString(R.string.label_proximity, value));
                updateFontSize(value);
                break;
        }
    }

    private void updateBackground(float lux) {
        int alpha = (int) (lux /40000 * 255);
        view.setBackgroundColor(Color.argb(alpha, 120, 200, 130));
    }

    private void updateFontSize(float distance) {
        lightTextView.setTextSize(16 + distance);
        proximityTextView.setTextSize(16 + distance);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}