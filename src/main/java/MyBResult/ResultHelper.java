package MyBResult;

import java.io.PrintWriter;
import java.util.ArrayList;

import MyBDb.MyBConnector;
import MyBPerfil.Profile;
import MyBPerfil.ProfileHelper;
import MyBPeriods.Day;

import com.mybitems.items.AppItem;
import com.mybitems.items.AudioItem;
import com.mybitems.items.BaseItem.ItemType;
import com.mybitems.items.BatteryItem;
import com.mybitems.items.BluetoothItem;
import com.mybitems.items.ErrorItem;
import com.mybitems.items.GPSItem;
import com.mybitems.items.ScreenItem;
import com.mybitems.items.TelItem;
import com.mybitems.items.WiFiItem;

/**
 * This class is the helper to parse and get information about the user data.
 * 
 * @author Marcel
 * 
 */
public class ResultHelper {

	/** Represent the user id */
	private String mUserId;

	/** Represent the profile of the user */
	private Profile mUserPerfil;

	private PrintWriter mWriter;
	/**
	 * Lists representing each sensor for the user
	 */
	private ArrayList<BatteryItem> mBatteryItems;
	private ArrayList<Day> mBatteryItemsDays;

	private ArrayList<BluetoothItem> mBluetoothItems;
	private ArrayList<Day> mBluetoothItemsDays;

	private ArrayList<AppItem> mAppItems;
	private ArrayList<Day> mAppsItemsDays;

	private ArrayList<AudioItem> mAudioItems;
	private ArrayList<Day> mAudioItemsDays;

	private ArrayList<ErrorItem> mErrorItems;
	private ArrayList<Day> mErrorItemsDays;

	private ArrayList<ScreenItem> mScreenItems;
	private ArrayList<Day> mScreenItemsDays;

	private ArrayList<GPSItem> mGpsItems;
	private ArrayList<Day> mGpsItemsDays;

	private ArrayList<WiFiItem> mWifiItems;
	private ArrayList<Day> mWifiItemsDays;

	private ArrayList<TelItem> mTelItems;
	private ArrayList<Day> mTelItemsDays;

	public static boolean DOLOG = false;

	public ResultHelper() {
		// Empty constructor
	}

	public ResultHelper(String userId, PrintWriter writer, boolean doLog) {
		mUserId = userId;
		mUserPerfil = new Profile(mUserId);
		mWriter = writer;
		DOLOG = true;

		String ret = readUserLogs();
		if (DOLOG) {
			mWriter.write(ret);
		}
	}

	/**
	 * Function responsible for reading all the user logs from the local
	 * database.
	 */
	public String readUserLogs() {
		// MyBConnector mConnector = MyBConnector.getInstance();
		MyBConnector mConnector = new MyBConnector();
		String ret = "inicio\n";
		try {
			if (mConnector != null) {
				ret += "\n############################################------READING USER LOGS------######################################################\n";

				for (ItemType type : ItemType.values()) {

					switch (type) {
					case BATTERY:
						mBatteryItems = mConnector
								.readBatteryInstances(mUserId);
						ret += "Battery : " + mBatteryItems.size() + "\n";
						break;

					case BLUETOOTH:
						mBluetoothItems = mConnector
								.readBluetoothInstances(mUserId);
						ret += "Bluetooth : " + mBluetoothItems.size() + "\n";
						break;

					case APP:
						mAppItems = mConnector.readAppInstances(mUserId);
						ret += "App : " + mAppItems.size() + "\n";
						break;

					case AUDIO:
						mAudioItems = mConnector.readAudioInstances(mUserId);
						ret += "Audio : " + mAudioItems.size() + "\n";
						break;

					case ERROR:
						mErrorItems = mConnector.readErrorInstances(mUserId);
						ret += "Error : " + mErrorItems.size() + "\n";
						break;

					case SCREEN:
						mScreenItems = mConnector.readScreenInstances(mUserId);
						ret += "Screen : " + mScreenItems.size() + "\n";
						break;

					case GPS:
						mGpsItems = mConnector.readGpsInstances(mUserId);
						ret += "Gps : " + mGpsItems.size() + "\n";
						break;

					case WIFI:
						mWifiItems = mConnector.readWifiInstances(mUserId);
						ret += "Wifi : " + mWifiItems.size() + "\n";
						break;

					case TEL:
						mTelItems = mConnector.readTelInstances(mUserId);
						ret += "Tel : " + mTelItems.size() + "\n";
						break;

					default:
						break;
					}
				}
			}
		} catch (Exception e) {
			ret += e.toString();
		}

		return ret;
	}

	/**
	 * Main function that will tie together all the other ones created. This
	 * should always be called after reading the user file of logs. First of
	 * all, this function will separate these logs in days and periods. After
	 * this, it will make all the analysis available on this class.
	 * 
	 */
	public void startUserAnalysis() {

		// First of all, separate in days and also in periods, since each day
		// will be the responsible for this.
		separateByDays();

		mWriter.write("1");
		// Separate the days in periods
		separateInPeriods();
		mWriter.write("2");
		// Create the analysis for each type
		createAnalysis();
		mWriter.write("3");
		// Create user profile
		createUserProfile();
		mWriter.write("4");
		// Save profile
		saveProfile();
		mWriter.write("5");
	}

	private void saveProfile() {
		if (mUserPerfil != null) {
			// MyBConnector.getInstance().insertProfile(mUserPerfil);
			MyBConnector mConnector = new MyBConnector();
			mConnector.insertProfile(mUserPerfil);
		}
	}

	/**
	 * Method that separate the logs in days.
	 */
	private void separateByDays() {

		if (DOLOG) {
			mWriter.write("\n############################################------DAYS COUNT------######################################################\n");
		}
		for (ItemType type : ItemType.values()) {
			switch (type) {
			case BATTERY:
				if (mBatteryItems != null) {
					mBatteryItemsDays = new ArrayList<Day>();
					for (int i = 0; i < mBatteryItems.size(); i++) {
						BatteryItem item = mBatteryItems.get(i);
						boolean achou = false;
						if (mBatteryItemsDays.size() > 0) {
							for (int j = 0; j < mBatteryItemsDays.size(); j++) {
								BatteryItem specificItem = (BatteryItem) mBatteryItemsDays
										.get(j).getItem(0);
								if (Utils.isSameDay(item, specificItem)) {
									achou = true;
									mBatteryItemsDays.get(j).add(item);
								}
							}
						}
						if (!achou) {
							ArrayList<Object> dia = new ArrayList<Object>();
							dia.add(item);
							Day day = new Day(dia, mWriter);
							mBatteryItemsDays.add(day);
						}
					}
				}
				if (DOLOG) {
					mWriter.write("\n" + "Battery days : "
							+ mBatteryItemsDays.size() + "\n");
				}
				break;

			case BLUETOOTH:
				if (mBluetoothItems != null) {
					mBluetoothItemsDays = new ArrayList<Day>();
					for (int i = 0; i < mBluetoothItems.size(); i++) {
						BluetoothItem item = mBluetoothItems.get(i);
						boolean achou = false;
						if (mBluetoothItemsDays.size() > 0) {
							for (int j = 0; j < mBluetoothItemsDays.size(); j++) {
								BluetoothItem specificItem = (BluetoothItem) mBluetoothItemsDays
										.get(j).getItem(0);
								if (Utils.isSameDay(item, specificItem)) {
									achou = true;
									mBluetoothItemsDays.get(j).add(item);
								}
							}
						}
						if (!achou) {
							ArrayList<Object> dia = new ArrayList<Object>();
							dia.add(item);
							Day day = new Day(dia, mWriter);
							mBluetoothItemsDays.add(day);
						}
					}
				}
				if (DOLOG) {
					mWriter.write("\n" + "Bluetooth days : "
							+ mBluetoothItemsDays.size() + "\n");
				}
				break;

			case APP:
				if (mAppItems != null) {
					mAppsItemsDays = new ArrayList<Day>();
					for (int i = 0; i < mAppItems.size(); i++) {
						AppItem item = mAppItems.get(i);
						boolean achou = false;
						if (mAppsItemsDays.size() > 0) {
							for (int j = 0; j < mAppsItemsDays.size(); j++) {
								AppItem specificItem = (AppItem) mAppsItemsDays
										.get(j).getItem(0);
								if (Utils.isSameDay(item, specificItem)) {
									achou = true;
									mAppsItemsDays.get(j).add(item);
								}
							}
						}
						if (!achou) {
							ArrayList<Object> dia = new ArrayList<Object>();
							dia.add(item);
							Day day = new Day(dia, mWriter);
							mAppsItemsDays.add(day);
						}
					}
				}
				if (DOLOG) {
					mWriter.write("\n" + "Apps days : " + mAppsItemsDays.size()
							+ "\n");
				}
				break;

			case AUDIO:
				if (mAudioItems != null) {
					mAudioItemsDays = new ArrayList<Day>();
					for (int i = 0; i < mAudioItems.size(); i++) {
						AudioItem item = mAudioItems.get(i);
						boolean achou = false;
						if (mAudioItemsDays.size() > 0) {
							for (int j = 0; j < mAudioItemsDays.size(); j++) {
								AudioItem specificItem = (AudioItem) mAudioItemsDays
										.get(j).getItem(0);
								if (Utils.isSameDay(item, specificItem)) {
									achou = true;
									mAudioItemsDays.get(j).add(item);
								}
							}
						}
						if (!achou) {
							ArrayList<Object> dia = new ArrayList<Object>();
							dia.add(item);
							Day day = new Day(dia, mWriter);
							mAudioItemsDays.add(day);
						}
					}
				}
				if (DOLOG) {
					mWriter.write("\n" + "Audio days : "
							+ mAudioItemsDays.size() + "\n");
				}
				break;

			case ERROR:
				if (mErrorItems != null) {
					mErrorItemsDays = new ArrayList<Day>();
					for (int i = 0; i < mErrorItems.size(); i++) {
						ErrorItem item = mErrorItems.get(i);
						boolean achou = false;
						if (mErrorItemsDays.size() > 0) {
							for (int j = 0; j < mErrorItemsDays.size(); j++) {
								ErrorItem specificItem = (ErrorItem) mErrorItemsDays
										.get(j).getItem(0);
								if (Utils.isSameDay(item, specificItem)) {
									achou = true;
									mErrorItemsDays.get(j).add(item);
								}
							}
						}
						if (!achou) {
							ArrayList<Object> dia = new ArrayList<Object>();
							dia.add(item);
							Day day = new Day(dia, mWriter);
							mErrorItemsDays.add(day);
						}
					}
				}
				if (DOLOG) {
					mWriter.write("\n" + "Error days : "
							+ mErrorItemsDays.size() + "\n");
				}
				break;

			case SCREEN:
				if (mScreenItems != null) {
					mScreenItemsDays = new ArrayList<Day>();
					for (int i = 0; i < mScreenItems.size(); i++) {
						ScreenItem item = mScreenItems.get(i);
						boolean achou = false;
						if (mScreenItemsDays.size() > 0) {
							for (int j = 0; j < mScreenItemsDays.size(); j++) {
								ScreenItem specificItem = (ScreenItem) mScreenItemsDays
										.get(j).getItem(0);
								if (Utils.isSameDay(item, specificItem)) {
									achou = true;
									mScreenItemsDays.get(j).add(item);
								}
							}
						}
						if (!achou) {
							ArrayList<Object> dia = new ArrayList<Object>();
							dia.add(item);
							Day day = new Day(dia, mWriter);
							mScreenItemsDays.add(day);
						}
					}
				}
				if (DOLOG) {
					mWriter.write("\n" + "Screen days : "
							+ mScreenItemsDays.size() + "\n");
				}
				break;

			case GPS:
				if (mGpsItems != null) {
					mGpsItemsDays = new ArrayList<Day>();
					for (int i = 0; i < mGpsItems.size(); i++) {
						GPSItem item = mGpsItems.get(i);
						boolean achou = false;
						if (mGpsItemsDays.size() > 0) {
							for (int j = 0; j < mGpsItemsDays.size(); j++) {
								GPSItem specificItem = (GPSItem) mGpsItemsDays
										.get(j).getItem(0);
								if (Utils.isSameDay(item, specificItem)) {
									achou = true;
									mGpsItemsDays.get(j).add(item);
								}
							}
						}
						if (!achou) {
							ArrayList<Object> dia = new ArrayList<Object>();
							dia.add(item);
							Day day = new Day(dia, mWriter);
							mGpsItemsDays.add(day);
						}
					}
				}
				if (DOLOG) {
					mWriter.write("\n" + "GPS days : " + mGpsItemsDays.size()
							+ "\n");
				}
				break;

			case WIFI:
				if (mWifiItems != null) {
					mWifiItemsDays = new ArrayList<Day>();
					for (int i = 0; i < mWifiItems.size(); i++) {
						WiFiItem item = mWifiItems.get(i);
						boolean achou = false;
						if (mWifiItemsDays.size() > 0) {
							for (int j = 0; j < mWifiItemsDays.size(); j++) {
								WiFiItem specificItem = (WiFiItem) mWifiItemsDays
										.get(j).getItem(0);
								if (Utils.isSameDay(item, specificItem)) {
									achou = true;
									mWifiItemsDays.get(j).add(item);
								}
							}
						}
						if (!achou) {
							ArrayList<Object> dia = new ArrayList<Object>();
							dia.add(item);
							Day day = new Day(dia, mWriter);
							mWifiItemsDays.add(day);
						}
					}
				}
				if (DOLOG) {
					mWriter.write("\n" + "Wifi days : " + mWifiItemsDays.size()
							+ "\n");
				}
				break;

			case TEL:
				if (mTelItems != null) {
					mTelItemsDays = new ArrayList<Day>();
					for (int i = 0; i < mTelItems.size(); i++) {
						TelItem item = mTelItems.get(i);
						boolean achou = false;
						if (mTelItemsDays.size() > 0) {
							for (int j = 0; j < mTelItemsDays.size(); j++) {
								TelItem specificItem = (TelItem) mTelItemsDays
										.get(j).getItem(0);
								if (Utils.isSameDay(item, specificItem)) {
									achou = true;
									mTelItemsDays.get(j).add(item);
								}
							}
						}
						if (!achou) {
							ArrayList<Object> dia = new ArrayList<Object>();
							dia.add(item);
							Day day = new Day(dia, mWriter);
							mTelItemsDays.add(day);
						}
					}
				}
				if (DOLOG) {
					mWriter.write("\n" + "Tel days : " + mTelItemsDays.size()
							+ "\n");
				}
				break;

			default:
				break;
			}
		}
	}

	/**
	 * Method that separate each day in periods.
	 */
	private void separateInPeriods() {
		for (ItemType type : ItemType.values()) {
			switch (type) {
			case BATTERY:
				for (Day day : mBatteryItemsDays) {
					day.separateInPeriods();
				}
				break;

			case BLUETOOTH:
				for (Day day : mBluetoothItemsDays) {
					day.separateInPeriods();
				}
				break;

			case APP:
				for (Day day : mAppsItemsDays) {
					day.separateInPeriods();
				}
				break;

			case AUDIO:
				for (Day day : mAudioItemsDays) {
					day.separateInPeriods();
				}

				break;

			case ERROR:
				for (Day day : mErrorItemsDays) {
					day.separateInPeriods();
				}
				break;

			case SCREEN:
				for (Day day : mScreenItemsDays) {
					day.separateInPeriods();
				}
				break;

			case GPS:
				for (Day day : mGpsItemsDays) {
					day.separateInPeriods();
				}
				break;

			case WIFI:
				for (Day day : mWifiItemsDays) {
					day.separateInPeriods();
				}

				break;

			case TEL:
				for (Day day : mTelItemsDays) {
					day.separateInPeriods();
				}
				break;

			default:
				break;
			}
		}

	}

	/**
	 * Method that create the analysis of the periods of the user.
	 */
	private void createAnalysis() {
		for (ItemType type : ItemType.values()) {
			switch (type) {
			case BATTERY:
				for (Day day : mBatteryItemsDays) {
					day.startPeriodAnalysis(type);
				}
				break;

			case BLUETOOTH:
				for (Day day : mBluetoothItemsDays) {
					day.startPeriodAnalysis(type);
				}
				break;

			case APP:
				for (Day day : mAppsItemsDays) {
					day.startPeriodAnalysis(type);
				}
				break;

			case AUDIO:
				for (Day day : mAudioItemsDays) {
					day.startPeriodAnalysis(type);
				}

				break;

			case ERROR:
				for (Day day : mErrorItemsDays) {
					day.startPeriodAnalysis(type);
				}
				break;

			case SCREEN:
				for (Day day : mScreenItemsDays) {
					day.startPeriodAnalysis(type);
				}
				break;

			case GPS:
				for (Day day : mGpsItemsDays) {
					day.startPeriodAnalysis(type);
				}
				break;

			case WIFI:
				for (Day day : mWifiItemsDays) {
					day.startPeriodAnalysis(type);
				}

				break;

			case TEL:
				for (Day day : mTelItemsDays) {
					day.startPeriodAnalysis(type);
				}
				break;

			default:
				break;
			}
		}

	}

	/**
	 * Create the user profile based on the periods created.
	 */
	private void createUserProfile() {

		// Get the configuration of all days
		for (int i = 0; i < getSmallerDay(); i++) {
			ProfileHelper profileHelper = new ProfileHelper();
			if (i < mBatteryItemsDays.size()) {
				profileHelper.copyDayConfig(mBatteryItemsDays.get(i),
						ItemType.BATTERY);
			}

			if (i < mBluetoothItemsDays.size()) {
				profileHelper.copyDayConfig(mBluetoothItemsDays.get(i),
						ItemType.BLUETOOTH);
			}

			if (i < mAppsItemsDays.size()) {
				profileHelper
						.copyDayConfig(mAppsItemsDays.get(i), ItemType.APP);
			}

			if (i < mAudioItemsDays.size()) {
				profileHelper.copyDayConfig(mAudioItemsDays.get(i),
						ItemType.AUDIO);
			}

			if (i < mWifiItemsDays.size()) {
				profileHelper.copyDayConfig(mWifiItemsDays.get(i),
						ItemType.WIFI);
			}

			if (i < mGpsItemsDays.size()) {
				profileHelper.copyDayConfig(mGpsItemsDays.get(i), ItemType.GPS);
			}

			if (i < mScreenItemsDays.size()) {
				profileHelper.copyDayConfig(mScreenItemsDays.get(i),
						ItemType.SCREEN);
			}

			if (i < mTelItemsDays.size()) {
				profileHelper.copyDayConfig(mTelItemsDays.get(i), ItemType.TEL);
			}

			mUserPerfil.addDay(profileHelper.getPeriods());
		}

	}

	/**
	 * Let's use always the smaller day present on our types TODO: Should we
	 * behave like this?
	 * 
	 * @return the smaller day
	 */
	private int getSmallerDay() {

		int min = mBatteryItemsDays.size();

		if (mBluetoothItemsDays.size() < min) {
			min = mBluetoothItemsDays.size();
		}
		if (mAppsItemsDays.size() < min) {
			min = mAppsItemsDays.size();
		}
		if (mAudioItemsDays.size() < min) {
			min = mAudioItemsDays.size();
		}
		if (mWifiItemsDays.size() < min) {
			min = mWifiItemsDays.size();
		}
		if (mGpsItemsDays.size() < min) {
			min = mGpsItemsDays.size();
		}
		if (mScreenItemsDays.size() < min) {
			min = mScreenItemsDays.size();
		}
		if (mTelItemsDays.size() < min) {
			min = mTelItemsDays.size();
		}
		if (DOLOG) {
			mWriter.write("\nMin days : " + min + "\n");
		}
		return min;
	}
}
