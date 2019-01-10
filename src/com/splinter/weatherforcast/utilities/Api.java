package com.splinter.weatherforcast.utilities;

public class Api {
	private static final String FORECAST_API_BASE_URL = "https://api.darksky.net/forecast/";
	private static final String FORECAST_API_KEY = "9ddcbca98bab559410fd7bbdffc14e68";

	public static String getForeCastUrl(double latitude, double longitude) {
		return FORECAST_API_BASE_URL + FORECAST_API_KEY + "/" + latitude + "," + longitude + "?units=si&lang=fr";
	}

}
