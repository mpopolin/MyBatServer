package MyBPerfil;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Class representing the profile of the user.
 * 
 * @author Marcel
 *
 */
public class Profile {
	private String mId;
	private List<ProfileDay> mDays;

	public Profile(String id) {
		mId = id;
		mDays = new ArrayList<ProfileDay>();
	}

	public String getId() {
		return mId;
	}

	public void setId(String id) {
		this.mId = id;
	}

	public List<ProfileDay> getDays() {
		return mDays;
	}

	public void setDays(List<ProfileDay> days) {
		this.mDays = days;
	}

	public void addDay(List<ProfilePeriod> periods) {
		ProfileDay day = new ProfileDay();
		day.setPeriods(periods);
		mDays.add(day);
	}

	public JSONObject toJsonObject() {
		JSONObject response = new JSONObject();
		try {
			response.put("id", mId);
			response.put("daysCount", mDays.size());
			JSONArray days = new JSONArray();
			for (ProfileDay day : mDays) {
				days.put(day.toJsonObject());
			}
			response.put("days", days);
		} catch (JSONException e) {
			response = null;
		}

		return response;
	}
}
