package com.splinter.weatherforcast;

import java.awt.Dimension;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JFrame;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;

import com.splinter.weatherforcast.utilities.Alert;
import com.splinter.weatherforcast.utilities.Api;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class MainFrame extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8921112008679215898L;

	private static final String GENERIC_ERROR_MSG = "Oops an error has occurred, please try again";
	private static final String INTERNET_CONNECTIVITY_ERROR_MSG = "Please check your internet connection";

	public MainFrame(String title) {
		super(title);

		double latitude = 37.8267;
		double longitude = -122.4233;

		/**
		 * Asynchronous GET
		 */
		OkHttpClient client = new OkHttpClient();
		Request request = new Request.Builder().url(Api.getForeCastUrl(latitude, longitude)).build();
		client.newCall(request)
				// Anonymous Class Callback
				.enqueue(new Callback() {

					@Override
					public void onResponse(Call arg0, Response response) {
						try (ResponseBody body = response.body()) {
							if (response.isSuccessful()) {
								String jsonData = body.string();
								JSONObject forecast;
								forecast = (JSONObject) JSONValue.parseWithException(jsonData);
								String timezone = (String) forecast.get("timezone");

								JSONObject currently = (JSONObject) forecast.get("currently");
								long time = (long) currently.get("time");
								double temperature = Double.parseDouble(currently.get("temperature") + "");
								double hummidity = (double) currently.get("humidity");
								double precipProbability = Double.parseDouble(currently.get("precipProbability") + "");
								String summary = (String) currently.get("summary");
								
								Date date = new Date(time*1000L);
								SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss z"); 
								formatter.setTimeZone(java.util.TimeZone.getTimeZone("Europe/Paris")); 
								String timeSring = formatter.format(date);
								
								System.out.println(timeSring);
							} else {
								Alert.error(MainFrame.this, GENERIC_ERROR_MSG);
							}
						} catch (ParseException | IOException e) {
							Alert.error(MainFrame.this, GENERIC_ERROR_MSG);
						}
					}

					@Override
					public void onFailure(Call call, IOException e) {
						Alert.error(MainFrame.this, INTERNET_CONNECTIVITY_ERROR_MSG);

					}
				});
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(500, 500);
	}

	@Override
	public Dimension getMinimumSize() {
		return getPreferredSize();
	}

	@Override
	public Dimension getMaximumSize() {
		return getPreferredSize();
	}

	/**
	 * Synchronous GET with an internal class
	 */
	/*
	 * String forecastUrl=
	 * String.format("https://api.darksky.net/forecast/%s/%f,%f", apiKey, latitude,
	 * longitude);
	 * 
	 * class ForeCastWorker extends SwingWorker<String, Void> {
	 * 
	 * private String _forecastUrl;
	 * 
	 * public ForeCastWorker(String forecastUrl) { this._forecastUrl = forecastUrl;
	 * }
	 * 
	 * 
	 * @Override protected String doInBackground() throws Exception { OkHttpClient
	 * httpClient = new OkHttpClient(); Request request = new Request.Builder()
	 * .url(_forecastUrl) .build(); try { Response response =
	 * httpClient.newCall(request) .execute(); // Response verification
	 * if(response.isSuccessful()) { return response.body().string(); } } catch
	 * (Exception e) { System.err.println("Error: " + e); } return null; }
	 * 
	 * @Override protected void done() { try { // CallBack GET method
	 * System.out.println(get()); } catch (InterruptedException | ExecutionException
	 * e) { System.err.println("Error: " + e); } }
	 * 
	 * }
	 */

	/**
	 * Synchronous GET with an anonymous class
	 */
	/*
	 * new SwingWorker<String, Void>() {
	 * 
	 * @Override protected String doInBackground() throws Exception { OkHttpClient
	 * httpClient = new OkHttpClient(); Request request = new Request.Builder()
	 * .url(forecastUrl) .build(); try { Response response =
	 * httpClient.newCall(request) .execute(); return response.body().string(); }
	 * catch (Exception e) { System.err.println("Error: " + e); } return null; }
	 * 
	 * // when the task is done
	 * 
	 * @Override protected void done() { try { System.out.println(get()); } catch
	 * (InterruptedException | ExecutionException e) { System.err.println("Error: "
	 * + e); } } }.execute();
	 */

	/**
	 * Synchronous GET directly
	 */
	/*
	 * System.out.println("Avant la requete..."); OkHttpClient httpClient = new
	 * OkHttpClient(); Request request = new Request.Builder() .url(forecastUrl)
	 * .build(); try { Response response = httpClient.newCall(request) .execute();
	 * System.out.println(response.body().string()); } catch (Exception e) {
	 * System.err.println("Error: " + e); }
	 */
}
