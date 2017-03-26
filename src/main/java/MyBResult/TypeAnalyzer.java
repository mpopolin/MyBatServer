package MyBResult;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import MyBPerfil.ProfileSensor;

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
 * This class is the responsible for analyzing the data of a determinate type.
 * 
 * @author Marcel
 * 
 */
public class TypeAnalyzer {

	/** Indicate the number of apps to show as result */
	private static final int TOP_APPS = 5;

	private ProfileSensor mPerfilSensor;

	PrintWriter mWriter;

	public TypeAnalyzer(PrintWriter w) {
		mPerfilSensor = new ProfileSensor();
		mWriter = w;
	}

	/**
	 * Get the perfil sensor for the
	 * 
	 * @return
	 */
	public ProfileSensor getPerfilSensor() {
		return mPerfilSensor;
	}

	/** Enum to indicate the possible item types that we have on the project */
	public void analyzeType(int type, ArrayList<Object> typeArray) {
		// TODO: Change this
		mPerfilSensor.setmAutoSync(true);
		mPerfilSensor.setmAutoSyncTime(40);

		ItemType itemType = ItemType.values()[type];
		switch (itemType) {

		case AUDIO:
			if (typeArray != null) {
				int ringAvg = 0;
				int callAvg = 0;
				int musicAvg = 0;
				int usedItems = 0;
				for (int i = 0; i < typeArray.size(); i++) {
					AudioItem item = (AudioItem) typeArray.get(i);

					if (item.isUsed()) {
						usedItems++;
					} else {
						ringAvg += item.getRingVolume();
						musicAvg += item.getMusicVolume();
						callAvg += item.getCallVolume();

					}

				}
				ArrayList<Integer> volumes = new ArrayList<Integer>();

				if (typeArray.size() - usedItems > 0) {

					volumes.add(ringAvg / (typeArray.size() - usedItems));

					volumes.add(callAvg / (typeArray.size() - usedItems));

					volumes.add(musicAvg / (typeArray.size() - usedItems));
				} else {
					volumes.add(0);
					volumes.add(0);
					volumes.add(0);
				}
				if (ResultHelper.DOLOG) {
					mWriter.write("\n" + "Volume 0 : " + volumes.get(0));
					mWriter.write("\n" + "Volume 1 : " + volumes.get(1));
					mWriter.write("\n" + "Volume 2 : " + volumes.get(2));
				}
				mPerfilSensor.setmVolumes(volumes);
			}

			break;
		case BATTERY:
			if (typeArray != null) {

				if (typeArray.size() > 0) {
					int chargingItems = 0;
					int usedItems = 0;
					int batAvg = 0;
					BatteryItem item = (BatteryItem) typeArray.get(0);

					for (int i = 0; i < typeArray.size(); i++) {
						item = (BatteryItem) typeArray.get(i);
						batAvg += item.getBatteryLevel();

						if (item.isUsed()) {
							usedItems++;
						} else {
							chargingItems++;
						}

					}

					mPerfilSensor.setmBatteryLevel(batAvg / typeArray.size());

					if (ResultHelper.DOLOG) {
						mWriter.write("\n" + "Battery charging : "
								+ chargingItems);

						mWriter.write("\n" + "Battery level : " + batAvg
								/ typeArray.size());

					}
					if (chargingItems >= typeArray.size() / 2) {
						mPerfilSensor.setmBatteryCharging(true);
					} else {
						mPerfilSensor.setmBatteryCharging(false);
					}

				}
			}
			break;

		case SCREEN:
			if (typeArray != null) {
				if (typeArray.size() > 0) {

					int brigthAvg = 0;
					int refreshAvg = 0;

					ScreenItem item = (ScreenItem) typeArray.get(0);
					for (int i = 0; i < typeArray.size(); i++) {
						item = (ScreenItem) typeArray.get(i);

						if (item.isUsed()) {	
							brigthAvg += item.getBrightness();
							refreshAvg += item.getRefreshRate();
						}

					}

					mPerfilSensor.setmScreenBrigthness(brigthAvg	
							/ typeArray.size());

					mPerfilSensor.setmScreenRefresh(refreshAvg
							/ typeArray.size());

					if (ResultHelper.DOLOG) {
						mWriter.write("\n" + "Screen bright: " + brigthAvg
								/ typeArray.size());

						mWriter.write("\n" + "Screen refresh: " + refreshAvg
								/ typeArray.size());
					}
				}
			}
			break;
		case TEL:
			if (typeArray != null) {
				int usedItems = 0;
				for (int i = 0; i < typeArray.size(); i++) {

					TelItem item = (TelItem) typeArray.get(i);

					if (item.isUsed()) {
						usedItems++;
					}
				}
				mPerfilSensor.setMobileUsed(usedItems);
				if (usedItems >= typeArray.size() / 2) {
					mPerfilSensor.setmMobileOn(true);
				} else {
					mPerfilSensor.setmMobileOn(false);
				}

				if (ResultHelper.DOLOG) {
					mWriter.write("\n" + "Tel used: " + usedItems);
					mWriter.write("\n" + "Tel total: " + typeArray.size());
				}

			}
			break;

		case APP:
			if (typeArray != null) {
				// Get the hash map of apps and occurrences
				HashMap<String, Integer> apps = getApps(typeArray);
				// Add on UI
				String[] topApps = getTopApps(TOP_APPS, apps);
				String appsString = "";
				for (int i = 0; i < topApps.length; i++) {
					appsString += topApps[i];
				}

				mPerfilSensor.setmApps(appsString);
			}
			break;

		case WIFI:
			if (typeArray != null) {
				int usedItems = 0;
				for (int i = 0; i < typeArray.size(); i++) {

					WiFiItem item = (WiFiItem) typeArray.get(i);

					if (item.isUsed()) {
						usedItems++;
					}
				}
				mPerfilSensor.setWifiUsed(usedItems);
				if (usedItems >= typeArray.size() / 2) {
					mPerfilSensor.setmWiFiOn(true);
				} else {
					mPerfilSensor.setmWiFiOn(false);
				}

				if (ResultHelper.DOLOG) {
					mWriter.write("\n" + "Wifi used: " + usedItems);
					mWriter.write("\n" + "Wifi total: " + typeArray.size());
				}

			}
			break;
		case ERROR:
			if (typeArray != null) {
				for (int i = 0; i < typeArray.size(); i++) {
					ErrorItem item = (ErrorItem) typeArray.get(i);
				}
			}

			break;
		case GPS:
			if (typeArray != null) {
				boolean isStarted = false;
				int usedItems = 0;
				for (int i = 0; i < typeArray.size(); i++) {

					GPSItem item = (GPSItem) typeArray.get(i);

					if (item != null) {
						if (item.isUsed()) {
							if (item.getmState() == 1) {
								isStarted = true;
							}
							if (isStarted) {
								usedItems++;
							}
						} else {
							isStarted = false;
						}
					}
				}
				mPerfilSensor.setGpsUsed(usedItems);
				if (usedItems >= typeArray.size() / 2) {
					if (ResultHelper.DOLOG) {
						mWriter.write("true");
					}
					mPerfilSensor.setmGpsOn(true);
				} else {
					if (ResultHelper.DOLOG) {
						mWriter.write("false");
					}
					mPerfilSensor.setmGpsOn(false);
				}

				if (ResultHelper.DOLOG) {
					mWriter.write("\n" + "GPS used: " + usedItems);
					mWriter.write("\n" + "GPS total: " + typeArray.size());
				}
			}
			break;
		case BLUETOOTH:
			if (typeArray != null) {
				int usedItems = 0;
				for (int i = 0; i < typeArray.size(); i++) {
					BluetoothItem item = (BluetoothItem) typeArray.get(i);
					if (item != null) {
						if (item.isUsed()) {
							usedItems++;
						}
					}
				}

				mPerfilSensor.setBtUsed(usedItems);
				if (usedItems >= typeArray.size() / 2) {
					mPerfilSensor.setmBtOn(true);
				} else {
					mPerfilSensor.setmBtOn(false);
				}
				if (ResultHelper.DOLOG) {
					mWriter.write("\n" + "BT used: " + usedItems);
					mWriter.write("\n" + "BT total: " + typeArray.size());
				}
			}
			break;
		default:
			break;
		}
	}

	/**
	 * Get the apps and number of instances.
	 * 
	 * @param arrayResponse
	 *            the array containing the data
	 * @return the hash map containing the name of the apps and how many times
	 *         it appeared
	 */
	private HashMap<String, Integer> getApps(ArrayList<Object> arrayResponse) {
		HashMap<String, Integer> apps = new HashMap<String, Integer>();

		try {
			for (int i = 0; i < arrayResponse.size(); i++) {

				Object object = arrayResponse.get(i);
				// App type
				if (object instanceof AppItem) {
					String listOfApps = ((AppItem) object).mAppsString;
					listOfApps = listOfApps.replace("[", "");
					listOfApps = listOfApps.replace("]", "");
					String[] arrayOfApps = listOfApps.split(",");
					for (int j = 0; j < arrayOfApps.length; j++) {
						Integer qnt = apps.get(arrayOfApps[j]);
						if (qnt == null) {
							apps.put(arrayOfApps[j], 1);
						} else {
							int a = qnt + 1;
							apps.put(arrayOfApps[j], a);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return apps;
	}

	/**
	 * Get a specified number of apps, ordered by its number of instances.
	 * 
	 * @param howMany
	 *            the limit of apps
	 * @param apps
	 *            the @link{HashMap} of apps
	 * @return the array containing the top apps, according to number of apps
	 */
	private String[] getTopApps(int howMany, HashMap<String, Integer> apps) {
		String[] topApps = new String[howMany];
		for (int i = 0; i < howMany; i++) {
			int max = 0;
			String app = null;
			for (Entry<String, Integer> key : apps.entrySet()) {
				if (key.getValue() > max) {
					max = key.getValue();
					app = key.getKey();
				}
			}
			topApps[i] = app;
			apps.remove(app);
		}
		return topApps;
	}
}
