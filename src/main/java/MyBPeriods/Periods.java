package MyBPeriods;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;

import MyBPerfil.ProfilePeriod;
import MyBPerfil.ProfileSensor;
import MyBResult.ResultHelper;
import MyBResult.TypeAnalyzer;
import MyBResult.Utils;

import com.mybitems.items.BaseItem;

/**
 * Class representing one period.
 * 
 * @author Marcel
 *
 */
public class Periods {

	enum Operation {
		MAX, MIN;
	}

	private int mId;

	// The type analyzer for each period
	private TypeAnalyzer mAnalyzer;

	// Array representing the quantity of items of the period
	private ArrayList<Object> mItems = new ArrayList<Object>();

	private PrintWriter mWriter;

	public Periods(PrintWriter writer, int id) {
		mWriter = writer;
		mAnalyzer = new TypeAnalyzer(writer);
		mId = id;
	}

	/**
	 * Get the {@link PerilPeriodo} for this period.
	 * 
	 * @return the {@link ProfilePeriod} of this period
	 */
	public ProfileSensor getPerfilSensor() {

		if (mAnalyzer != null) {
			return mAnalyzer.getPerfilSensor();
		}
		return null;
	}

	public void analyzeType(int type) {
		if (mAnalyzer != null && mItems != null) {
			mAnalyzer.analyzeType(type, mItems);
		}
	}

	/**
	 * Get the max or min timestamp based on operation. Return 0 if no log were
	 * found.
	 * 
	 * @param op
	 *            the {@link Operation} defined
	 * @return the value define
	 */
	// public long getMaxOrMin(Operation op) {
	// long min = Long.MAX_VALUE;
	// long max = Long.MIN_VALUE;
	// for (int i = 0; i < mItems.size(); i++) {
	// Object specificItem = mItems.get(i);
	// long timestamp = 0;
	// if (specificItem instanceof BaseItem) {
	// timestamp = ((BaseItem) specificItem).getTimestamp();
	// }
	// if (timestamp > max) {
	// max = timestamp;
	// }
	//
	// if (timestamp < min) {
	// min = timestamp;
	// }
	// }
	//
	// if (op == Operation.MAX) {
	// if (max == Long.MIN_VALUE) {
	// return 0;
	// }
	// return max;
	// } else {
	// if (min == Long.MAX_VALUE) {
	// return 0;
	// }
	// return min;
	// }
	// }

	public long getMaxOrMin(Operation op) {
		ArrayList<Object> ordenedItemsFromPeriod = getOrdenedItems();
		long first = 0;
		long last = 0;

		// Get the first and last times
		if (ordenedItemsFromPeriod != null && ordenedItemsFromPeriod.size() > 0) {
			if (ordenedItemsFromPeriod.get(0) instanceof BaseItem) {
				first = ((BaseItem) ordenedItemsFromPeriod.get(0))
						.getTimestamp();
			}

			if (ordenedItemsFromPeriod.get(ordenedItemsFromPeriod.size() - 1) instanceof BaseItem) {
				last = ((BaseItem) ordenedItemsFromPeriod
						.get(ordenedItemsFromPeriod.size() - 1)).getTimestamp();
			}
		}

		// Use the time that is not null
		Calendar time = Calendar.getInstance();
		if (op == Operation.MIN) {
			if (first != 0) {
				time.setTimeInMillis(first);
			} else
				return 0;
		}

		if (op == Operation.MAX) {
			if (last != 0) {
				time.setTimeInMillis(last);
			} else
				return 0;
		}

		if (ResultHelper.DOLOG) {
			mWriter.write("\nGet " + op.toString() + " time before set : "
					+ time.getTimeInMillis());

			if (op == Operation.MIN) {
				mWriter.write("\nGet " + op.toString() + " time before set : "
						+ Utils.getCurrentTimeStamp(first));
			} else {
				mWriter.write("\nGet " + op.toString() + " time before set : "
						+ Utils.getCurrentTimeStamp(last));
			}

		}
		// As we just want the day, set the rest of values to the defaults
		time.set(Calendar.HOUR_OF_DAY, mId * 4);
		if (ResultHelper.DOLOG) {
			mWriter.write("\nGet " + op.toString() + " time after add  " + mId
					* 4 + " hours " + time.getTimeInMillis());
		}

		time.set(Calendar.MINUTE, 0);
		if (ResultHelper.DOLOG) {
			mWriter.write("\nGet " + op.toString() + " time after add  " + 0
					+ " minutes " + time.getTimeInMillis());
		}

		time.set(Calendar.SECOND, 0);
		if (ResultHelper.DOLOG) {
			mWriter.write("\nGet " + op.toString() + " time after add  " + 0
					+ " seconds " + time.getTimeInMillis());
		}

		time.set(Calendar.MILLISECOND, 0);
		if (ResultHelper.DOLOG) {
			mWriter.write("\nGet " + op.toString() + " time after add  " + 0
					+ " milliseconds " + time.getTimeInMillis());
		}
		
		if (op == Operation.MAX) {
			time.add(Calendar.HOUR_OF_DAY, 4);
			if (ResultHelper.DOLOG) {
				mWriter.write("\nGet " + op.toString() + " time after add  "
						+ 4 + " hours " + time.getTimeInMillis());
			}
		}
		return time.getTimeInMillis();

	}

	/**
	 * Add all items to the period list.
	 * 
	 * @param itemsOfPeriod
	 *            the {@link ArrayList} of items
	 */
	public void addAll(ArrayList<Object> itemsOfPeriod) {
		if (mItems != null) {
			mItems.addAll(itemsOfPeriod);
		}
	}

	/**
	 * Get the size of the list of instances of the period.
	 * 
	 * @return the size of the list
	 */
	public int getItemSize() {
		if (mItems != null) {
			return mItems.size();
		}
		return 0;
	}

	/**
	 * Get the list of instances.
	 * 
	 * @return the {@link ArrayList} of the items
	 */
	public ArrayList<Object> getItems() {
		return mItems;
	}

	/**
	 * Get an item from the array list.
	 * 
	 * @param position
	 *            the position of the item
	 * @return the T item
	 */
	public Object getItem(int position) {
		if (mItems != null) {
			return mItems.get(0);
		}
		return null;
	}

	/**
	 * Add an item to the array list.
	 * 
	 * @param item
	 *            the item to be added
	 */
	public void add(Object item) {
		if (mItems != null) {
			mItems.add(item);
		}
	}

	public ArrayList<Object> getOrdenedItems() {
		ArrayList<Object> copyArrayList = new ArrayList<Object>(mItems);

		Comparator<Object> timeComparator = new Comparator<Object>() {

			@Override
			public int compare(Object o1, Object o2) {
				if (o1 instanceof BaseItem) {
					if (o2 instanceof BaseItem) {
						long time1 = ((BaseItem) o1).getTimestamp();
						long time2 = ((BaseItem) o2).getTimestamp();

						if (time1 - time2 == 0) {
							return 0;
						}

						return (time1 < time2 ? 1 : 0);
					}
				}
				return 0;
			}
		};

		Collections.sort(copyArrayList, timeComparator);
		return copyArrayList;
	}

}
