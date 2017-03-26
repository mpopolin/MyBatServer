package MyBPerfil;

import java.util.ArrayList;
import java.util.List;

import MyBPeriods.Day;

import com.mybitems.items.BaseItem.ItemType;

/**
 * Utility class used to create the user profile.
 * 
 * @author Marcel
 *
 */
public class ProfileHelper {

	ArrayList<ProfilePeriod> mPeriodos = new ArrayList<ProfilePeriod>();

	public ProfileHelper() {
		for (int i = 0; i < Day.MAX_PERIODS; i++) {
			ProfilePeriod perfilPeriodo = new ProfilePeriod();
			mPeriodos.add(perfilPeriodo);
		}
	}

	/**
	 * Get the list of periods.
	 * 
	 * @return the list of periods.
	 */
	public List<ProfilePeriod> getPeriods() {
		return mPeriodos;
	}

	/**
	 * Copy the day configuration to the list of periods based on its type.
	 * 
	 * @param day
	 *            the day to be copied
	 */
	public void copyDayConfig(Day day, ItemType type) {

		// Safety cast for the
		int count = 0;

		switch (type) {
		case BATTERY:
			count = 0;
			ArrayList<ProfilePeriod> batteryPeriods = (ArrayList<ProfilePeriod>) day
					.getProfilePeriod();
			for (ProfilePeriod p : batteryPeriods) {
				if (count < mPeriodos.size()) {
					mPeriodos
							.get(count)
							.getmConfig()
							.setmBatteryCharging(
									p.getmConfig().ismBatteryCharging());

					mPeriodos.get(count).setmStart(p.getmStart());
					mPeriodos.get(count).setmEnd(p.getmEnd());

					mPeriodos
							.get(count)
							.getmConfig()
							.setmBatteryLevel(p.getmConfig().getmBatteryLevel());

					mPeriodos.get(count).getmConfig()
							.setmAutoSync(p.getmConfig().ismAutoSync());

					mPeriodos.get(count).getmConfig()
							.setmAutoSyncTime(p.getmConfig().ismAutoSyncTime());
				}

				count++;
			}

			break;
		case BLUETOOTH:
			count = 0;

			ArrayList<ProfilePeriod> bluetoothPeriods = (ArrayList<ProfilePeriod>) day
					.getProfilePeriod();
			for (ProfilePeriod p : bluetoothPeriods) {
				if (count < mPeriodos.size()) {
					mPeriodos.get(count).getmConfig()
							.setmBtOn(p.getmConfig().ismBtOn());
					mPeriodos.get(count).getmConfig()
							.setBtUsed(p.getmConfig().getBtUsed());
					mPeriodos.get(count).setmStart(p.getmStart());
					mPeriodos.get(count).setmEnd(p.getmEnd());
				}

				count++;
			}

			break;
		case GPS:
			count = 0;

			ArrayList<ProfilePeriod> gpsPeriods = (ArrayList<ProfilePeriod>) day
					.getProfilePeriod();
			for (ProfilePeriod p : gpsPeriods) {
				if (count < mPeriodos.size()) {
					mPeriodos.get(count).getmConfig()
							.setmGpsOn(p.getmConfig().ismGpsOn());
					mPeriodos.get(count).getmConfig()
							.setGpsUsed(p.getmConfig().getGpsUsed());
					mPeriodos.get(count).setmStart(p.getmStart());
					mPeriodos.get(count).setmEnd(p.getmEnd());
				}

				count++;
			}

			break;
		case WIFI:
			count = 0;

			ArrayList<ProfilePeriod> wifiPeriods = (ArrayList<ProfilePeriod>) day
					.getProfilePeriod();
			for (ProfilePeriod p : wifiPeriods) {
				if (count < mPeriodos.size()) {
					mPeriodos.get(count).getmConfig()
							.setmWiFiOn(p.getmConfig().ismWiFiOn());
					mPeriodos.get(count).getmConfig()
							.setWifiUsed(p.getmConfig().getWifiUsed());
					mPeriodos.get(count).setmStart(p.getmStart());
					mPeriodos.get(count).setmEnd(p.getmEnd());
				}

				count++;
			}

			break;
		case TEL:
			count = 0;

			ArrayList<ProfilePeriod> telPeriods = (ArrayList<ProfilePeriod>) day
					.getProfilePeriod();
			for (ProfilePeriod p : telPeriods) {
				if (count < mPeriodos.size()) {
					mPeriodos.get(count).getmConfig()
							.setmMobileOn(p.getmConfig().ismMobileOn());
					mPeriodos.get(count).getmConfig()
							.setMobileUsed(p.getmConfig().getMobileUsed());
					mPeriodos.get(count).setmStart(p.getmStart());
					mPeriodos.get(count).setmEnd(p.getmEnd());
				}

				count++;
			}

			break;
		case SCREEN:
			count = 0;

			ArrayList<ProfilePeriod> screenPeriods = (ArrayList<ProfilePeriod>) day
					.getProfilePeriod();
			for (ProfilePeriod p : screenPeriods) {
				if (count < mPeriodos.size()) {
					mPeriodos
							.get(count)
							.getmConfig()
							.setmScreenBrigthness(
									p.getmConfig().getmScreenBrigthness());

					mPeriodos
							.get(count)
							.getmConfig()
							.setmScreenRefresh(
									p.getmConfig().getmScreenRefresh());
					mPeriodos.get(count).setmStart(p.getmStart());
					mPeriodos.get(count).setmEnd(p.getmEnd());
				}

				count++;
			}

			break;
		case AUDIO:
			count = 0;

			ArrayList<ProfilePeriod> audioPeriods = (ArrayList<ProfilePeriod>) day
					.getProfilePeriod();
			for (ProfilePeriod p : audioPeriods) {
				if (count < mPeriodos.size()) {
					mPeriodos.get(count).getmConfig()
							.setmVolumes(p.getmConfig().getmVolumes());
					mPeriodos.get(count).setmStart(p.getmStart());
					mPeriodos.get(count).setmEnd(p.getmEnd());
				}

				count++;
			}

			break;

		case APP:
			count = 0;

			ArrayList<ProfilePeriod> appsPeriods = (ArrayList<ProfilePeriod>) day
					.getProfilePeriod();
			for (ProfilePeriod p : appsPeriods) {
				if (count < mPeriodos.size()) {
					mPeriodos.get(count).getmConfig()
							.setmApps(p.getmConfig().getmApps());

					mPeriodos.get(count).setmStart(p.getmStart());
					mPeriodos.get(count).setmEnd(p.getmEnd());
				}

				count++;
			}

			break;

		default:
			break;
		}

	}
}
