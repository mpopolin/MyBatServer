package MyBPeriods;

import java.io.PrintWriter;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import MyBPerfil.ProfilePeriod;
import MyBPeriods.Periods.Operation;
import MyBResult.ResultHelper;
import MyBResult.Utils;

import com.mybitems.items.BaseItem;
import com.mybitems.items.BaseItem.ItemType;

/**
 * Class representing one day.
 * 
 * @author Marcel
 *
 */
public class Day {

	// Array representing the quantity of items of the day
	private ArrayList<Object> mItems = new ArrayList<Object>();

	// The number of periods for one day
	public static final int MAX_PERIODS = 12;

	// List of Periods
	private ArrayList<Periods> mPeriods = new ArrayList<Periods>();

	private PrintWriter mWriter;

	public Day(ArrayList<Object> itemsOfDay, PrintWriter writer) {
		// set the items of the day
		mItems = itemsOfDay;
		mWriter = writer;
		for (int i = 0; i < Day.MAX_PERIODS; i++) {
			Periods perfilPeriodo = new Periods(writer, i);
			mPeriods.add(perfilPeriodo);
		}
	}

	private void logPeriodTimes() {
		if (ResultHelper.DOLOG) {
			int count = 0;

			mWriter.write("\n" + "Logando MAX e MIN de periodos de tamanho  : "
					+ mPeriods.size());
			for (Periods periodo : mPeriods) {
				mWriter.write("\n" + "Periodo ==> " + count + " Start : "
						+ periodo.getMaxOrMin(Operation.MIN) + " End : "
						+ periodo.getMaxOrMin(Operation.MAX));
				count++;
			}
			mWriter.write("\n");
		}
	}

	private void logAllPeriodsLogs() {
		if (ResultHelper.DOLOG) {
			int countPeriod = 0;

			mWriter.write("\n"
					+ "Logando FIRST e LAST de periodos de tamanho  : "
					+ mPeriods.size());
			for (Periods periodo : mPeriods) {
				ArrayList<Object> ordenedItemsFromPeriod = periodo
						.getOrdenedItems();

				long first = 0;
				long last = 0;

				if (ordenedItemsFromPeriod != null
						&& ordenedItemsFromPeriod.size() > 0) {
					if (ordenedItemsFromPeriod.get(0) instanceof BaseItem) {
						first = ((BaseItem) ordenedItemsFromPeriod.get(0))
								.getTimestamp();
					}

					if (ordenedItemsFromPeriod.get(ordenedItemsFromPeriod
							.size() - 1) instanceof BaseItem) {
						last = ((BaseItem) ordenedItemsFromPeriod
								.get(ordenedItemsFromPeriod.size() - 1))
								.getTimestamp();
					}

					mWriter.write("\n" + "Periodo " + countPeriod + " ===> "
							+ "First : " + first + " Last : " + last);

				} else {
					mWriter.write("\n" + "Periodo " + countPeriod + " ===> "
							+ "First : " + 0 + " Last : " + 0);
				}
				countPeriod++;
			}
			mWriter.write("\n");
		}
	}

	/**
	 * Return the profile for each period of this day.
	 * 
	 * @return the list of profile for each period of the day.
	 */
	public List<ProfilePeriod> getProfilePeriod() {
		if (ResultHelper.DOLOG) {
			mWriter.write("\n");
			mWriter.write("\n############################################------READING PERIOD LOGS------######################################################\n");
		}

		logPeriodTimes();
		logAllPeriodsLogs();
		ArrayList<ProfilePeriod> perfilPeriodos = new ArrayList<ProfilePeriod>();
		for (Periods periodo : mPeriods) {
			ProfilePeriod perfilPeriodo = new ProfilePeriod();
			perfilPeriodo.setmStart(periodo.getMaxOrMin(Operation.MIN));
			perfilPeriodo.setmEnd(periodo.getMaxOrMin(Operation.MAX));
			perfilPeriodo.setmConfig(periodo.getPerfilSensor());
			perfilPeriodos.add(perfilPeriodo);
		}

		return perfilPeriodos;
	}

	/** Do the analysis for each period of the day. */
	public void startPeriodAnalysis(ItemType type) {
		for (Periods periodo : mPeriods) {
			periodo.analyzeType(type.ordinal());
		}
	}

	/**
	 * Separate this day in periods and fulfill the array o periods.
	 */
	public void separateInPeriods() {
		// Build the periods
		if (mItems != null) {
			for (int i = 0; i < mItems.size(); i++) {
				Object specificItem = mItems.get(i);
				long timestamp = 0;
				if (specificItem instanceof BaseItem) {
					timestamp = ((BaseItem) specificItem).getTimestamp();
				}

				Calendar calendar = Calendar.getInstance();
				calendar.setTimeInMillis(timestamp);
				int hours = calendar.get(Calendar.HOUR_OF_DAY);

	
				
				if (hours >= 0 && hours < 2) {
					mPeriods.get(0).add(specificItem);
				} else if (hours >= 2 && hours < 4) {
					mPeriods.get(1).add(specificItem);
				} else if (hours >= 4 && hours < 6) {
					mPeriods.get(2).add(specificItem);
				} else if (hours >= 6 && hours < 8) {
					mPeriods.get(3).add(specificItem);
				} else if (hours >= 8 && hours < 10) {
					mPeriods.get(4).add(specificItem);
				} else if (hours >= 10 && hours < 12) {
					mPeriods.get(5).add(specificItem);
				} else if (hours >= 12 && hours < 14) {
					mPeriods.get(6).add(specificItem);
				} else if (hours >= 14 && hours < 16) {
					mPeriods.get(7).add(specificItem);
				} else if (hours >= 16 && hours < 18) {
					mPeriods.get(8).add(specificItem);
				} else if (hours >= 18 && hours < 20) {
					mPeriods.get(9).add(specificItem);
				} else if (hours >= 20 && hours < 22) {
					mPeriods.get(10).add(specificItem);
				}else if (hours >= 22 && hours < 24) {
					mPeriods.get(11).add(specificItem);
				}
			}
		}
	}

	/**
	 * Get the list of instances of the day.
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
}
