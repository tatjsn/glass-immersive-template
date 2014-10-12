package com.example.testglass;

import android.util.SparseArray;

public class Config {
	int interval;
	int stringId;

	private static Config create(int interval, int stringId) {
		Config config = new Config();
		config.interval = interval;
		config.stringId = stringId;
		return config;
	}

	private static SparseArray<Config> MAP = new SparseArray<Config>();
	static {
		MAP.append(R.id.action_interval_5s, Config.create(5 * 1000, R.string.action_interval_5s));
		MAP.append(R.id.action_interval_10s, Config.create(10 * 1000, R.string.action_interval_10s));
		MAP.append(R.id.action_interval_30s, Config.create(30 * 1000, R.string.action_interval_30s));
		MAP.append(R.id.action_interval_60s, Config.create(60 * 1000, R.string.action_interval_60s));
		MAP.append(R.id.action_interval_5m, Config.create(5 * 60 * 1000, R.string.action_interval_5m));
		MAP.append(R.id.action_interval_10m, Config.create(10 * 60 * 1000, R.string.action_interval_10m));
		MAP.append(R.id.action_interval_30m, Config.create(30 * 60 * 1000, R.string.action_interval_30m));
		MAP.append(R.id.action_interval_60m, Config.create(60 * 60 * 1000, R.string.action_interval_60m));
		MAP.append(R.id.action_interval_stop, Config.create(0, R.string.action_interval_stop));
	}

	static Config get(int itemId) {
		return MAP.get(itemId);
	}
}
