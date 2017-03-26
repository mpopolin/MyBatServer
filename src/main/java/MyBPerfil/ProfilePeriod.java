package MyBPerfil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Class representing the profile of each period of the user.
 * 
 * @author Marcel
 *
 */
public class ProfilePeriod {
	private long mStart = 0;
	private long mEnd = 0;
	private ProfileSensor mConfig;

	public ProfilePeriod() {
		mStart = 0; 
		mEnd = 0;
		mConfig = new ProfileSensor();
	}

	public long getmStart() {
		return mStart;
	}

	public void setmStart(long start) {
		// First value being set
		if (mStart == 0) {
			mStart = start;
		} else {
			if (start < mStart) {
				mStart = start;
			}
		}
	}

	public long getmEnd() {
		return mEnd;
	}

	public void setmEnd(long end) {
		// First value being set
		if (mEnd == 0) {
			mEnd = end;
		} else {
			if (end > mEnd) {
				mEnd = end;
			}
		}
	}

	public ProfileSensor getmConfig() {
		return mConfig;
	}

	public void setmConfig(ProfileSensor mConfig) {
		this.mConfig = mConfig;
	}

	public JSONObject toJsonObject() throws JSONException {

		JSONObject response = new JSONObject();
		try {
			response.put("start", mStart);
			response.put("end", mEnd);
			response.put("configuration", mConfig.toJsonObject());
		} catch (JSONException e) {
			response = null;
			throw e;
		}
		return response;

	}
}
