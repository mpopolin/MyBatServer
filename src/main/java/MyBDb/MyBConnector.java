package MyBDb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import MyBPerfil.Profile;

import com.mybitems.items.AppItem;
import com.mybitems.items.ApplicationConstants;
import com.mybitems.items.AudioItem;
import com.mybitems.items.BatteryItem;
import com.mybitems.items.BluetoothItem;
import com.mybitems.items.ErrorItem;
import com.mybitems.items.GPSItem;
import com.mybitems.items.ScreenItem;
import com.mybitems.items.TelItem;
import com.mybitems.items.WiFiItem;

/**
 * Class responsible for handling all the connections and queries to the
 * database.
 * 
 * @author Marcel
 *
 */

public class MyBConnector {

	public static final String MYSQL_USERNAME = System
			.getenv("OPENSHIFT_MYSQL_DB_USERNAME");
	public static final String MYSQL_PASSWORD = System
			.getenv("OPENSHIFT_MYSQL_DB_PASSWORD");
	public static final String MYSQL_DATABASE_HOST = System
			.getenv("OPENSHIFT_MYSQL_DB_HOST");
	public static final String MYSQL_DATABASE_PORT = System
			.getenv("OPENSHIFT_MYSQL_DB_PORT");
	public static final String MYSQL_DATABASE_NAME = "marcel";

	private Connection mConn;

	private static MyBConnector mInstance;

	// /** Get the {@link MyBConnector} instance */
	// public static MyBConnector getInstance() {
	// if (mInstance == null) {
	// mInstance = new MyBConnector();
	// }
	// return mInstance;
	// }

	/**
	 * Constructor.
	 */
	public MyBConnector() {
		initConnection();
	}

	/**
	 * Builds the connection to DB.
	 */
	private void initConnection() {
		String url = "";

		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException cnfe) {
			cnfe.printStackTrace();
			mConn = null;
		}

		if (mConn == null) {
			try {
				url = "jdbc:mysql://" + MYSQL_DATABASE_HOST + ":"
						+ MYSQL_DATABASE_PORT + "/" + MYSQL_DATABASE_NAME;
				mConn = DriverManager.getConnection(url, MYSQL_USERNAME,
						MYSQL_PASSWORD);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public String getProfile(String userId) {
		try {
			String query = "select * from userProfile where userId = '"
					+ userId + "'";

			PreparedStatement statement = mConn.prepareStatement(query);

			ResultSet resultSet = statement.executeQuery();
			if (resultSet != null) {

				while (resultSet.next()) {
					String json = resultSet.getString("profileJson");
					return json;
				}
			}
		} catch (SQLException e) {
			return e.toString();
		}
		return "";
	}

	public void insertProfile(Profile userProfile) {
		if (mConn != null) {
			try {
				Statement statement = mConn.createStatement();

				String delSql = "DELETE FROM userProfile WHERE userId='"
						+ userProfile.getId() + "'";

				statement.execute(delSql);

				String sql = "INSERT INTO userProfile VALUES ( " + "'"
						+ userProfile.getId() + "' , '"
						+ userProfile.toJsonObject().toString() + "')";

				statement.execute(sql);

			} catch (SQLException e) {
				// Do nothing...
			}
		}

	}

	public String insertInstances(JSONArray array) {
		String ret = "";
		if (array != null) {
			Statement statement = null;

			if (mConn != null) {
				try {
					statement = mConn.createStatement();
				} catch (SQLException e1) {
					ret += e1.toString();
					statement = null;
					e1.printStackTrace();
				}
			}

			if (statement != null) {

				for (int i = 0; i < array.length(); i++) {
					String sql = "";
					try {
						JSONObject object = array.getJSONObject(i);
						if (object != null) {
							int type = object.getInt("type");
							String id = object.getString("id");
							switch (type) {
							case 0:
								// app
								AppItem item = new AppItem(id);
								item.fromJson(object);
								sql = "INSERT INTO " + AppItem.TABLE_NAME
										+ " VALUES ( " + "'" + id + "' , '"
										+ item.mAppsString + "' , "
										+ object.getLong("timestamp") + ")";

								break;

							case 1:
								// audio
								AudioItem audioitem = new AudioItem(id);
								audioitem.fromJson(object);
								sql = "INSERT INTO " + AudioItem.TABLE_NAME
										+ " VALUES ( " + "'" + id + "' , "
										+ audioitem.isSpeakerOn() + " , "
										+ audioitem.getmMode() + " , "
										+ audioitem.isMusicOn() + " , "
										+ audioitem.getMusicVolume() + " , "
										+ audioitem.getCallVolume() + " , "
										+ audioitem.getRingVolume() + " , "
										+ audioitem.getTimestamp() + ")";
								break;

							case 2:
								// battery
								BatteryItem batteryitem = new BatteryItem(id);
								batteryitem.fromJson(object);

								int isCharging = object
										.getInt(ApplicationConstants.charging);
								int chargePlug = object
										.getInt(ApplicationConstants.plug);

								sql = "INSERT INTO " + BatteryItem.TABLE_NAME
										+ " VALUES ( " + "'" + id + "' , "
										+ batteryitem.getBatteryLevel() + " , "
										+ batteryitem.getBatteryScale() + " , "
										+ batteryitem.getIsPlugged() + " , "
										+ batteryitem.getBatteryHealth()
										+ " , '" + batteryitem.getBatteryTec()
										+ "' , " + batteryitem.getBatteryTemp()
										+ " , "
										+ batteryitem.getBatteryVoltage()
										+ " , " + chargePlug + " , "
										+ isCharging + " , "
										+ batteryitem.getTimestamp() + ")";

								break;

							case 3:
								// bt
								BluetoothItem btitem = new BluetoothItem(id);
								btitem.fromJson(object);
								sql = "INSERT INTO " + BluetoothItem.TABLE_NAME
										+ " VALUES ( " + "'" + id + "' , "
										+ btitem.getState() + " , "
										+ btitem.getConnections() + " , "
										+ btitem.getPreviousState() + " , "
										+ btitem.getActualConnectionState()
										+ " , "
										+ btitem.getPreviousConnectionState()
										+ " , " + btitem.getTimestamp() + ")";
								break;

							case 4:
								// error

								sql = "INSERT INTO " + ErrorItem.TABLE_NAME
										+ " VALUES ( " + "'" + id + "' , '"
										+ object.getString("error") + "' , "
										+ object.getLong("timestamp") + ")";

								break;

							case 5:
								// gps
								GPSItem gpsitem = new GPSItem(id);
								gpsitem.fromJson(object);
								sql = "INSERT INTO " + GPSItem.TABLE_NAME
										+ " VALUES ( " + "'" + id + "' , "
										+ gpsitem.getmState() + " , "
										+ gpsitem.getTimestamp() + ")";
								break;

							case 6:
								// screen
								ScreenItem screenitem = new ScreenItem(id);
								screenitem.fromJson(object);
								sql = "INSERT INTO " + ScreenItem.TABLE_NAME
										+ " VALUES ( " + "'" + id + "' , "
										+ screenitem.getHeight() + " , "
										+ screenitem.getWidth() + " , "
										+ screenitem.getScreenState() + " , "
										+ screenitem.getBrightness() + " , "
										+ screenitem.getRefreshRate() + " , "
										+ screenitem.getOrientation() + " , "
										+ screenitem.getTimestamp() + ")";
								break;

							case 7:
								// tel
								TelItem telitem = new TelItem(id);
								telitem.fromJson(object);
								sql = "INSERT INTO " + TelItem.TABLE_NAME
										+ " VALUES ( " + "'" + id + "' , "
										+ telitem.getmNetworkType() + " , "
										+ telitem.getmDataState() + " , "
										+ telitem.getmDataActivity() + " , "
										+ telitem.getmCallState() + " , "
										+ telitem.getSignalStrenghtValue()
										+ " , " + telitem.getmTxBytes() + " , "
										+ telitem.getmRxBytes() + " , "
										+ telitem.getTimestamp() + ")";
								break;

							case 8:
								// wifi
								WiFiItem wifiitem = new WiFiItem(id);
								wifiitem.fromJson(object);

								int wifistate = object
										.getInt(ApplicationConstants.wifiState);

								int wifipreviousstate = object
										.getInt(ApplicationConstants.wifiPreviousState);

								int linkspeed = object
										.getInt(ApplicationConstants.linkspeed);

								int ss = object
										.getInt(ApplicationConstants.sinalstrength);

								String mac = object
										.getString(ApplicationConstants.mac);

								sql = "INSERT INTO " + WiFiItem.TABLE_NAME
										+ " VALUES ( " + "'" + id + "' , "
										+ wifiitem.getConnectionState() + " , "
										+ wifistate + " , " + wifipreviousstate
										+ " , '" + mac + "' , " + linkspeed
										+ " , " + ss + " , "
										+ wifiitem.getTxBytes() + " , "
										+ wifiitem.getRxBytes() + " , "
										+ wifiitem.getTimestamp() + ")";
								break;
							}

						}

					} catch (Exception e) {
						ret += e.toString();
						sql = "";
					}

					if (sql != "") {
						try {
							statement.addBatch(sql);
						} catch (SQLException e) {
							ret += e.toString();
							e.printStackTrace();
						}
					}

				}
			}

			// After for - insert all
			if (statement != null && mConn != null) {
				try {
					int[] retornos = statement.executeBatch();

					if (retornos != null) {
						int retInt = 0;
						for (int i = 0; i < retornos.length; i++) {
							retInt += retornos[i];
						}
						ret = retInt + "";
					}
					statement.close();
				} catch (SQLException e) {
					ret += e.toString();
					e.printStackTrace();
				}
			}

		}
		return ret;
	}

	/**
	 * Read the battery instances of the user.
	 * 
	 * @param mUserId
	 *            the user ID
	 * @return {@link ArrayList} of {@link BatteryItem}
	 */
	public ArrayList<BatteryItem> readBatteryInstances(String mUserId) {
		ArrayList<BatteryItem> arrayList = new ArrayList<BatteryItem>();
		if (mConn != null) {

			try {
				String query = "select * from "
						+ BatteryItem.TABLE_NAME
						+ " where "
						+ BatteryItem.TABLE_NAME
						+ ".mId = '"
						+ mUserId
						+ "'"
						+ "AND mTimestamp > 1443322800000 AND mTimestamp < 1443927599000";

				PreparedStatement statement = mConn.prepareStatement(query);

				ResultSet resultSet = statement.executeQuery();
				if (resultSet != null) {

					while (resultSet.next()) {
						BatteryItem item = new BatteryItem(mUserId);
						item.setBatteryHealth(resultSet
								.getInt("mBatteryHealth"));
						item.setBatteryLevel(resultSet.getInt("mBatteryLevel"));
						item.setBatteryScale(resultSet.getInt("mBatteryScale"));
						item.setBatteryTec(resultSet.getString("mBatteryTec"));
						item.setBatteryTemp(resultSet.getInt("mBatteryTemp"));
						item.setBatteryVoltage(resultSet
								.getInt("mBatteryVoltage"));
						item.setChargePlug(resultSet.getInt("chargePlug"));
						item.setIsCharging(resultSet.getInt("isCharging"));
						item.setIsPlugged(resultSet.getInt("mIsPlugged"));
						item.setTimestamp(resultSet.getLong("mTimestamp"));

						if (item.getTimestamp() > 0) {
							arrayList.add(item);
						}
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return arrayList;

	}

	/**
	 * Read the bluetooth instances of the user.
	 * 
	 * @param mUserId
	 *            the user ID
	 * @return {@link ArrayList} of {@link BluetoothItem}
	 */
	public ArrayList<BluetoothItem> readBluetoothInstances(String mUserId) {
		ArrayList<BluetoothItem> arrayList = new ArrayList<BluetoothItem>();
		if (mConn != null) {
			try {
				PreparedStatement statement = mConn
						.prepareStatement("select * from "
								+ BluetoothItem.TABLE_NAME
								+ " where mId = '"
								+ mUserId
								+ "'"
								+ "AND mTimestamp > 1443322800000 AND mTimestamp < 1443927599000");

				ResultSet resultSet = statement.executeQuery();
				if (resultSet != null) {
					while (resultSet.next()) {
						BluetoothItem item = new BluetoothItem(mUserId);
						item.setActualConnectionState(resultSet
								.getInt("mActualConnectionState"));
						item.setConnections(resultSet.getInt("mConnections"));
						item.setPreviousConnectionState(resultSet
								.getInt("mPreviousConnectionState"));
						item.setPreviousState(resultSet
								.getInt("mPreviousState"));
						item.setState(resultSet.getInt("mState"));
						item.setTimestamp(resultSet.getLong("mTimestamp"));
						if (item.getTimestamp() > 0) {
							arrayList.add(item);
						}

					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return arrayList;

	}

	/**
	 * Read the app instances of the user.
	 * 
	 * @param mUserId
	 *            the user ID
	 * @return {@link ArrayList} of {@link AppItem}
	 */
	public ArrayList<AppItem> readAppInstances(String mUserId) {
		ArrayList<AppItem> arrayList = new ArrayList<AppItem>();
		if (mConn != null) {
			try {
				PreparedStatement statement = mConn
						.prepareStatement("select * from "
								+ AppItem.TABLE_NAME
								+ " where mId = '"
								+ mUserId
								+ "'"
								+ "AND mTimestamp > 1443322800000 AND mTimestamp < 1443927599000");

				ResultSet resultSet = statement.executeQuery();
				if (resultSet != null) {
					while (resultSet.next()) {
						AppItem item = new AppItem(mUserId);
						item.setAppsString(resultSet.getString("mApps"));
						item.setTimestamp(resultSet.getLong("mTimestamp"));
						if (item.getTimestamp() > 0) {
							arrayList.add(item);
						}
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return arrayList;

	}

	/**
	 * Read the audio instances of the user.
	 * 
	 * @param mUserId
	 *            the user ID
	 * @return {@link ArrayList} of {@link AudioItem}
	 */
	public ArrayList<AudioItem> readAudioInstances(String mUserId) {
		ArrayList<AudioItem> arrayList = new ArrayList<AudioItem>();
		if (mConn != null) {
			try {
				PreparedStatement statement = mConn
						.prepareStatement("select * from "
								+ AudioItem.TABLE_NAME
								+ " where mId = '"
								+ mUserId
								+ "'"
								+ "AND mTimestamp > 1443322800000 AND mTimestamp < 1443927599000");

				ResultSet resultSet = statement.executeQuery();
				if (resultSet != null) {
					while (resultSet.next()) {

						AudioItem item = new AudioItem(mUserId);
						item.setCallVolume(resultSet.getInt("callVolume"));
						item.setMode(resultSet.getInt("mMode"));
						item.setmTimeStamp(resultSet.getLong("mTimestamp"));
						item.setMusicOn(resultSet.getBoolean("musicOn"));
						item.setMusicVolume(resultSet.getInt("musicVolume"));
						item.setRingVolume(resultSet.getInt("ringVolume"));
						item.setSpeakerOn(resultSet.getBoolean("mSpeakerOn"));

						if (item.getTimestamp() > 0) {
							arrayList.add(item);
						}
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return arrayList;
	}

	/**
	 * Read the error instances of the user.
	 * 
	 * @param mUserId
	 *            the user ID
	 * @return {@link ArrayList} of {@link ErrorItem}
	 */
	public ArrayList<ErrorItem> readErrorInstances(String mUserId) {
		ArrayList<ErrorItem> arrayList = new ArrayList<ErrorItem>();
		if (mConn != null) {
			try {
				PreparedStatement statement = mConn
						.prepareStatement("select * from "
								+ ErrorItem.TABLE_NAME
								+ " where mId = '"
								+ mUserId
								+ "'"
								+ "AND mTimestamp > 1443322800000 AND mTimestamp < 1443927599000");

				ResultSet resultSet = statement.executeQuery();
				if (resultSet != null) {
					while (resultSet.next()) {
						String error = resultSet.getString("mError");
						ErrorItem item = new ErrorItem(mUserId, error);
						item.setTimestamp(resultSet.getLong("mTimestamp"));
						if (item.getTimestamp() > 0) {
							arrayList.add(item);
						}
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return arrayList;

	}

	/**
	 * Read the screen instances of the user.
	 * 
	 * @param mUserId
	 *            the user ID
	 * @return {@link ArrayList} of {@link ScreenItem}
	 */
	public ArrayList<ScreenItem> readScreenInstances(String mUserId) {
		ArrayList<ScreenItem> arrayList = new ArrayList<ScreenItem>();
		if (mConn != null) {
			try {
				PreparedStatement statement = mConn
						.prepareStatement("select * from "
								+ ScreenItem.TABLE_NAME
								+ " where mId = '"
								+ mUserId
								+ "'"
								+ "AND mTimestamp > 1443322800000 AND mTimestamp < 1443927599000");

				ResultSet resultSet = statement.executeQuery();
				if (resultSet != null) {
					while (resultSet.next()) {
						ScreenItem item = new ScreenItem(mUserId);
						item.setBrightness(resultSet.getInt("mBrightness"));
						item.setHeight(resultSet.getFloat("mHeight"));
						item.setOrientation(resultSet.getInt("mOrientation"));
						item.setRefreshRate(resultSet.getFloat("mRefreshRate"));
						item.setScreenState(resultSet.getInt("mScreenState"));
						item.setWidth(resultSet.getFloat("mWidth"));
						item.setTimestamp(resultSet.getLong("mTimestamp"));
						if (item.getTimestamp() > 0) {
							arrayList.add(item);
						}
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return arrayList;

	}

	/**
	 * Read the gps instances of the user.
	 * 
	 * @param mUserId
	 *            the user ID
	 * @return {@link ArrayList} of {@link GpsItem}
	 */
	public ArrayList<GPSItem> readGpsInstances(String mUserId) {
		ArrayList<GPSItem> arrayList = new ArrayList<GPSItem>();
		if (mConn != null) {
			try {
				PreparedStatement statement = mConn
						.prepareStatement("select * from "
								+ GPSItem.TABLE_NAME
								+ " where mId = '"
								+ mUserId
								+ "'"
								+ "AND mTimestamp > 1443322800000 AND mTimestamp < 1443927599000");

				ResultSet resultSet = statement.executeQuery();
				if (resultSet != null) {
					while (resultSet.next()) {
						GPSItem item = new GPSItem(mUserId);
						item.setmState(resultSet.getInt("mState"));
						item.setTimestamp(resultSet.getLong("mTimestamp"));
						if (item.getTimestamp() > 0) {
							arrayList.add(item);
						}

					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return arrayList;

	}

	/**
	 * Read the Wifi instances of the user.
	 * 
	 * @param mUserId
	 *            the user ID
	 * @return {@link ArrayList} of {@link WifiItem}
	 */
	public ArrayList<WiFiItem> readWifiInstances(String mUserId) {
		ArrayList<WiFiItem> arrayList = new ArrayList<WiFiItem>();
		if (mConn != null) {
			try {
				PreparedStatement statement = mConn
						.prepareStatement("select * from "
								+ WiFiItem.TABLE_NAME
								+ " where mId = '"
								+ mUserId
								+ "'"
								+ "AND mTimestamp > 1443322800000 AND mTimestamp < 1443927599000");

				ResultSet resultSet = statement.executeQuery();
				if (resultSet != null) {
					while (resultSet.next()) {
						WiFiItem item = new WiFiItem(mUserId);
						item.setConnectionState(resultSet
								.getInt("mConnectionState"));
						item.setLinkSpeed(resultSet.getInt("mLinkSpeed"));
						item.setMacAddress(resultSet.getString("mMacAddress"));
						item.setRxBytes(resultSet.getLong("mRxBytes"));
						item.setSinalStrenght(resultSet
								.getInt("mSinalStrength"));
						item.setTimestamp(resultSet.getLong("mTimestamp"));
						item.setTxBytes(resultSet.getLong("mTxBytes"));
						item.setWiFiPreviousState(resultSet
								.getInt("mWiFiPreviousState"));
						item.setWiFiState(resultSet.getInt("mWiFiState"));

						if (item.getTimestamp() > 0) {
							arrayList.add(item);
						}
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return arrayList;

	}

	/**
	 * Read the telephony instances of the user.
	 * 
	 * @param mUserId
	 *            the user ID
	 * @return {@link ArrayList} of {@link TelItem}
	 */
	public ArrayList<TelItem> readTelInstances(String mUserId) {
		ArrayList<TelItem> arrayList = new ArrayList<TelItem>();
		if (mConn != null) {
			try {
				PreparedStatement statement = mConn
						.prepareStatement("select * from "
								+ TelItem.TABLE_NAME
								+ " where mId = '"
								+ mUserId
								+ "'"
								+ "AND mTimestamp > 1443322800000 AND mTimestamp < 1443927599000");

				ResultSet resultSet = statement.executeQuery();
				if (resultSet != null) {
					while (resultSet.next()) {
						TelItem item = new TelItem(mUserId);
						item.setmCallState(resultSet.getInt("mCallState"));
						item.setmDataActivity(resultSet.getInt("mDataActivity"));
						item.setmDataState(resultSet.getInt("mDataState"));
						item.setmNetworkType(resultSet.getInt("mNetworkType"));
						item.setmRxBytes(resultSet.getLong("mRxBytes"));
						item.setmTxBytes(resultSet.getLong("mTxBytes"));
						item.setSignalStrengthValue(resultSet
								.getInt("mSignalStrengthValue"));
						item.setTimestamp(resultSet.getLong("mTimestamp"));

						if (item.getTimestamp() > 0) {
							arrayList.add(item);
						}
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return arrayList;

	}
}
