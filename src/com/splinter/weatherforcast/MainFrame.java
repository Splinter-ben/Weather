package com.splinter.weatherforcast;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;

import com.splinter.weatherforcast.models.CurrentWeather;
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
	private static final Color BLUE_COLOR = Color.decode("#8EA2C6");
	private static final Color WHITE_COLOR = Color.WHITE;
	private static final Font DEFAULT_FONT = new Font("San Fransisco", Font.PLAIN, 24);
	private static final Color LIGHT_GREY_COLOR = new Color(255, 255, 255, 128);
	
	private JLabel locationLabel;
	private JLabel timeLabel;
	private JLabel temperatureLabel;
	private JPanel otherInfoPanel;
	private JLabel humidityLabel;
	private JLabel humidityValue;
	private JLabel precipLabel;
	private JLabel precipyValue;
	private JLabel summaryLabel;

	private CurrentWeather currentWeather;

	public MainFrame(String title) {
		super(title);
		
		JPanel contentPane = new JPanel();		
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));		
		contentPane.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
		contentPane.setBackground(BLUE_COLOR);
		
		locationLabel = new JLabel("Lyon, FR", SwingConstants.CENTER);
		locationLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
		locationLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		locationLabel.setForeground(WHITE_COLOR);
		locationLabel.setFont(DEFAULT_FONT);
		
		timeLabel = new JLabel("--");
		timeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		timeLabel.setForeground(LIGHT_GREY_COLOR );
		timeLabel.setFont(DEFAULT_FONT.deriveFont(18f));
		
		temperatureLabel =  new JLabel("--", SwingConstants.CENTER);
		temperatureLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		temperatureLabel.setForeground(WHITE_COLOR);
		temperatureLabel.setFont(DEFAULT_FONT.deriveFont(160f));
		
		otherInfoPanel = new JPanel(new GridLayout(2, 2));
		otherInfoPanel.setBackground(BLUE_COLOR);
		
		humidityLabel = new JLabel("Humidité".toUpperCase(), SwingConstants.CENTER);
		humidityLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		humidityLabel.setForeground(LIGHT_GREY_COLOR);
		
		humidityValue = new JLabel("--", SwingConstants.CENTER);
		humidityValue.setAlignmentX(Component.CENTER_ALIGNMENT);
		humidityValue.setForeground(WHITE_COLOR);
		humidityValue.setFont(DEFAULT_FONT);
		
		precipLabel = new JLabel("Risque de pluie".toUpperCase(), SwingConstants.CENTER);
		precipLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		precipLabel.setForeground(LIGHT_GREY_COLOR);
		
		precipyValue = new JLabel("--", SwingConstants.CENTER);
		precipyValue.setAlignmentX(Component.CENTER_ALIGNMENT);
		precipyValue.setForeground(WHITE_COLOR);
		precipyValue.setFont(DEFAULT_FONT);
		
		otherInfoPanel.add(humidityLabel);
		otherInfoPanel.add(precipLabel);
		otherInfoPanel.add(humidityValue);
		otherInfoPanel.add(precipyValue);
		
		summaryLabel =  new JLabel("--", SwingConstants.CENTER);
		summaryLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		summaryLabel.setForeground(WHITE_COLOR);
		summaryLabel.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));
		summaryLabel.setFont(DEFAULT_FONT.deriveFont(14f));
		
		
		contentPane.add(locationLabel);
		contentPane.add(timeLabel);
		contentPane.add(temperatureLabel);
		contentPane.add(otherInfoPanel);
		contentPane.add(summaryLabel);
		
		setContentPane(contentPane);

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
								currentWeather= getCurrentWeatherDetails(jsonData);
								
								// Lambda expression
								EventQueue.invokeLater(() -> updateScreen());
								
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

	private void updateScreen() {
		timeLabel.setText("Il est " + currentWeather.getFormattedTime() + " et la température actuelle est de:");	
		temperatureLabel.setText(currentWeather.get_temperature() + "°C");
		humidityValue.setText(currentWeather.get_humidity() + "");
		precipyValue.setText(currentWeather.get_precipProbability() + "%");
		summaryLabel.setText(currentWeather.get_summary());
	}

	private CurrentWeather getCurrentWeatherDetails(String jsonData) throws ParseException {
		CurrentWeather currentWeather = new CurrentWeather();
		JSONObject forecast = (JSONObject) JSONValue.parseWithException(jsonData);
		JSONObject currently = (JSONObject) forecast.get("currently"); 
		
		currentWeather.set_timezone((String) forecast.get("timezone"));
		currentWeather.set_time((long) currently.get("time"));
		currentWeather.set_temperature(Double.parseDouble(currently.get("temperature") + ""));
		currentWeather.set_humidity(Double.parseDouble(currently.get("humidity") + ""));
		currentWeather.set_precipProbability(Double.parseDouble(currently.get("precipProbability") + ""));
		currentWeather.set_summary((String) currently.get("summary"));
		
		return currentWeather;
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
