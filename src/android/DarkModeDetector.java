package at.coderabbit.darkmode;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * This class echoes a string called from JavaScript.
 */
public class DarkModeDetector extends CordovaPlugin implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor mSensor;
    private float maxLight = 100;
    private float maxRange = 1;

    /**
     * Constructor.
     */
    public DarkModeDetector() {
    }

    /**
     * Sets the context of the Command. This can then be used to do things like
     * get file paths associated with the Activity.
     *
     * @param cordova The context of the main Activity.
     * @param webView The CordovaWebView Cordova is running in.
     */
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
        this.sensorManager = (SensorManager) cordova.getActivity().getSystemService(Context.SENSOR_SERVICE);

        List<Sensor> list = this.sensorManager.getSensorList(Sensor.TYPE_LIGHT);
        if (list != null && list.size() > 0) {
            this.mSensor = list.get(0);
            this.maxRange =  this.mSensor.getMaximumRange();
            this.sensorManager.registerListener(this, this.mSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("getState")) {
            this.getState(callbackContext);
            return true;
        }
        return false;
    }

    private void getState(CallbackContext callbackContext) {
        callbackContext.success(String.valueOf(this.maxLight));
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        try {
            if(event.sensor.getType()==Sensor.TYPE_LIGHT) {

                float maxLight = 0;
                for (int i = 0; i < event.values.length; i++) {
                    if (maxLight < event.values[i]) {
                        maxLight = event.values[i];
                    }
                }
                this.maxLight = maxLight;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
