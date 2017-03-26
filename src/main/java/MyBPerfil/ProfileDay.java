package MyBPerfil;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Class representing the configuration of a specific day.
 * 
 * @author Marcel
 *
 */
public class ProfileDay {

	int mDayOfWeek;
	private List<ProfilePeriod> mPeriods;

	public ProfileDay() {
		// Do nothing for now
	}

	public void setPeriods(List<ProfilePeriod> periods) {
		mPeriods = periods;
		createDayName();
	}

	private void createDayName() {
		if (mPeriods != null && !mPeriods.isEmpty()) {
			ProfilePeriod period = mPeriods.get(0);
			if (period != null) {
				long start = period.getmStart();
				long end = period.getmEnd();
				long toUse = 0;

				// Just use if we have a valid start or end
				if (start != 0) {
					toUse = start;
				} else if (end != 0) {
					toUse = end;
				}

				if (toUse != 0) {
					Date date = new Date(toUse);
					Calendar c = Calendar.getInstance();
					c.setTime(date);
					mDayOfWeek = c.get(Calendar.DAY_OF_WEEK);
					// if (c.get(Calendar.MONDAY) == dayOfWeek)
					// setDayName("Monday");
					// else if (c.get(Calendar.TUESDAY) == dayOfWeek)
					// setDayName("Tuesday");
					// else if (c.get(Calendar.WEDNESDAY) == dayOfWeek)
					// setDayName("Wednesday");
					// else if (c.get(Calendar.THURSDAY) == dayOfWeek)
					// setDayName("Thursday");
					// else if (c.get(Calendar.FRIDAY) == dayOfWeek)
					// setDayName("Friday");
					// else if (c.get(Calendar.SATURDAY) == dayOfWeek)
					// setDayName("Saturday");
					// else if (c.get(Calendar.SUNDAY) == dayOfWeek)
					// setDayName("Sunday");
				}

			}
		}
	}

	public JSONObject toJsonObject() {
		JSONObject response = new JSONObject();
		try {
			response.put("dayName", mDayOfWeek);
			response.put("periodsCount", mPeriods.size());
			JSONArray periods = new JSONArray();
			for (ProfilePeriod period : mPeriods) {
				periods.put(period.toJsonObject());
			}
			response.put("periods", periods);
		} catch (JSONException e) {
			response = null;
		}

		return response;
	}
}
