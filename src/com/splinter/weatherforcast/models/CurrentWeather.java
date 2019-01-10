package com.splinter.weatherforcast.models;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CurrentWeather {
	private String _timezone;
	private long _time;
	private double _temperature;
	private double _humidity;
	private double _precipProbability;
	private String _summary;
	
	
	public String get_timezone() {
		return _timezone;
	}
	public void set_timezone(String _timezone) {
		this._timezone = _timezone;
	}
	public long get_time() {
		return _time;
	}
	public void set_time(long _time) {
		this._time = _time;
	}
	
	public String getFormattedTime() {
		Date date = new Date(get_time() * 1000L);
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
		formatter.setTimeZone(java.util.TimeZone.getTimeZone(get_timezone()));
		String timeSring = formatter.format(date);
		
		return timeSring;
	}
	
	
	public int get_temperature() {
		return (int) Math.round(_temperature);
	}
	public void set_temperature(double _temperature) {
		this._temperature = _temperature;
	}
	public double get_humidity() {
		return _humidity;
	}
	public void set_humidity(double _humidity) {
		this._humidity = _humidity;
	}
	public int get_precipProbability() {
		return (int) Math.round(_precipProbability);
	}
	public void set_precipProbability(double _precipProbability) {
		this._precipProbability = _precipProbability;
	}
	public String get_summary() {
		return _summary;
	}
	public void set_summary(String _summary) {
		this._summary = _summary;
	}
	
	
}
