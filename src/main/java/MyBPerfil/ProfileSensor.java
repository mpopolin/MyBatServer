package MyBPerfil;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Class representing the configuration of the sensors.
 * 
 * @author Marcel
 *
 */
public class ProfileSensor {

	// Bluetooth values
	private boolean mBtOn;
	private int mBtUsedItems;

	// GPS values
	private boolean mGpsOn;
	private int mGpsUsedItems;

	// WiFi values
	private boolean mWiFiOn;
	private int mWiFiUsedItems;

	// Mobile values
	private boolean mMobileOn;
	private int mMobileUsedItems;

	// Apps values
	private String mApps;

	// Screen values
	private float mScreenBrigthness;
	private float mScreenRefresh;

	// Battery values
	private float mBatteryLevel;
	private boolean mBatteryCharging;

	// Audio values
	private List<Integer> mVolumes;

	// Sync values
	private boolean mAutoSync;
	private long mAutoSyncTime;

	public ProfileSensor() {
		// Empty constructor
	}

	public int getBtUsed() {
		return mBtUsedItems;
	}

	public void setBtUsed(int used) {
		mBtUsedItems = used;
	}

	public int getWifiUsed() {
		return mWiFiUsedItems;
	}

	public void setWifiUsed(int used) {
		mWiFiUsedItems = used;
	}

	public int getGpsUsed() {
		return mGpsUsedItems;
	}

	public void setGpsUsed(int used) {
		mGpsUsedItems = used;
	}

	public int getMobileUsed() {
		return mMobileUsedItems;
	}

	public void setMobileUsed(int used) {
		mMobileUsedItems = used;
	}

	public boolean ismBtOn() {
		return mBtOn;
	}

	public void setmBtOn(boolean mBtOn) {
		this.mBtOn = mBtOn;
	}

	public boolean ismGpsOn() {
		return mGpsOn;
	}

	public void setmGpsOn(boolean mGpsOn) {
		this.mGpsOn = mGpsOn;
	}

	public boolean ismWiFiOn() {
		return mWiFiOn;
	}

	public void setmWiFiOn(boolean mWiFiOn) {
		this.mWiFiOn = mWiFiOn;
	}

	public boolean ismMobileOn() {
		return mMobileOn;
	}

	public void setmMobileOn(boolean mMobileOn) {
		this.mMobileOn = mMobileOn;
	}

	public String getmApps() {
		return mApps;
	}

	public void setmApps(String mApps) {
		this.mApps = mApps;
	}

	public float getmScreenBrigthness() {
		return mScreenBrigthness;
	}

	public void setmScreenBrigthness(float mScreenBrigthness) {
		this.mScreenBrigthness = mScreenBrigthness;
	}

	public float getmScreenRefresh() {
		return mScreenRefresh;
	}

	public void setmScreenRefresh(float mScreenRefresh) {
		this.mScreenRefresh = mScreenRefresh;
	}

	public float getmBatteryLevel() {
		return mBatteryLevel;
	}

	public void setmBatteryLevel(float mBatteryLevel) {
		this.mBatteryLevel = mBatteryLevel;
	}

	public boolean ismBatteryCharging() {
		return mBatteryCharging;
	}

	public void setmBatteryCharging(boolean mBatteryCharging) {
		this.mBatteryCharging = mBatteryCharging;
	}

	public List<Integer> getmVolumes() {
		return mVolumes;
	}

	public void setmVolumes(List<Integer> mVolumes) {
		this.mVolumes = mVolumes;
	}

	public boolean ismAutoSync() {
		return mAutoSync;
	}

	public void setmAutoSync(boolean mAutoSync) {
		this.mAutoSync = mAutoSync;
	}

	public long ismAutoSyncTime() {
		return mAutoSyncTime;
	}

	public void setmAutoSyncTime(long mAutoSyncTime) {
		this.mAutoSyncTime = mAutoSyncTime;
	}

	public JSONObject toJsonObject() throws JSONException {
		JSONObject response = new JSONObject();
		try {
			response.put("bt", mBtOn);
			response.put("btUsed", mBtUsedItems);
			response.put("gps", mGpsOn);
			response.put("gpsUsed", mGpsUsedItems);
			response.put("wifi", mWiFiOn);
			response.put("wifiUsed", mWiFiUsedItems);
			response.put("mobile", mMobileOn);
			response.put("mobileUsed", mMobileUsedItems);
			response.put("apps", mApps);
			response.put("brigth", mScreenBrigthness);
			response.put("refresh", mScreenRefresh);
			response.put("battery", mBatteryLevel);
			response.put("charging", mBatteryCharging);
			if (mVolumes != null && mVolumes.size() == 3) {
				response.put("call", mVolumes.get(0));
				response.put("ring", mVolumes.get(1));
				response.put("sound", mVolumes.get(2));
			}
			response.put("autosync", mAutoSync);
			response.put("synctime", mAutoSyncTime);
		} catch (JSONException e) {
			response = null;
			throw e;
		}
		return response;
	}
}
