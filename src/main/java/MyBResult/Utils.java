package MyBResult;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mybitems.items.BaseItem;

/**
 * Utility that has some utility methods and functions to help on the
 * development.
 * 
 * @author Marcel
 * 
 */
public class Utils {

	/**
	 * Order the array passed as parameter in ASC order.
	 * 
	 * @param array
	 *            the array to be ordered
	 * @return the {@link ArrayList} ordered
	 */
	public static ArrayList<JSONObject> sortArray(JSONArray array) {
		ArrayList<JSONObject> returnList = new ArrayList<JSONObject>();

		try {
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj1 = array.getJSONObject(i);
				long timestamp1 = obj1.getLong("timestamp");
				int position = 0;
				for (int j = 0; j < returnList.size(); j++) {
					JSONObject obj2 = returnList.get(j);
					long timestamp2 = obj2.getLong("timestamp");
					if (timestamp1 < timestamp2) {
						break;
					}
					position++;
				}
				returnList.add(position, obj1);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}

		return returnList;
	}
	
	/**
	 * Check if two items are in the same day
	 * 
	 * @param item1 the first {@link BaseItem}
	 * @param item2 the second {@link BaseItem}
	 * @return true if they are in the same day, false otherwise
	 */
	public static boolean isSameDay(BaseItem item1, BaseItem item2) {
		Date date1 = new Date(item1.getTimestamp());
		Date date2 = new Date(item2.getTimestamp());
		return isSameDay(date1, date2);
	}

	/**
	 * Check if two dates are in the same day
	 * 
	 * @param date1 the first date
	 * @param date2 the second date
	 * @return boolean indicating if they are in the same day
	 */
	public static boolean isSameDay(Date date1, Date date2) {
		Calendar cal1 = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		cal1.setTime(date1);
		cal2.setTime(date2);
		boolean sameDay = cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
				&& cal1.get(Calendar.DAY_OF_YEAR) == cal2
						.get(Calendar.DAY_OF_YEAR);
		return sameDay;
	}


	public static String getCurrentTimeStamp(long millis) {
		Calendar cal1 = Calendar.getInstance();
		cal1.setTimeInMillis(millis);
		SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");// dd/MM/yyyy
		String strDate = sdfDate.format(cal1.getTime());
		return strDate;
	}
}
