package com.splinter.weatherforcast;

import java.awt.Dimension;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import com.splinter.weatherforcast.utilities.Alert;
import com.splinter.weatherforcast.utilities.Api;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainFrame extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8921112008679215898L;

	public MainFrame(String title) {
		super(title);

		double latitude = 37.8267;
		double longitude = -122.4233;
		
		/**
		 * Asynchronous GET
		 */
		OkHttpClient client = new OkHttpClient();
		Request request = new Request.Builder()
			      .url(Api.getForeCastUrl(latitude, longitude))
			      .build();
		client.newCall(request)
				// Anonymous Class Callback
				.enqueue(new Callback() {
					
					@Override
					public void onResponse(Call arg0, Response response) throws IOException {
						if (response.isSuccessful()) {
							System.out.println(response.body().string());	
						} else {
							Alert.error(MainFrame.this, "Error", "Oops an error has occurred");
							System.err.println("Error: " + response.body().string());		
						}
										
					}
					
					@Override
					public void onFailure(Call call, IOException e) {
						Alert.error(MainFrame.this, "Error", "Please check your internet connection");
			
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
	String forecastUrl= String.format("https://api.darksky.net/forecast/%s/%f,%f", apiKey, latitude, longitude);
	
	class ForeCastWorker extends SwingWorker<String, Void> {

		private String _forecastUrl; 
		
		public ForeCastWorker(String forecastUrl) {
			this._forecastUrl = forecastUrl;
		}


		@Override
		protected String doInBackground() throws Exception {
			OkHttpClient httpClient = new OkHttpClient();
			Request request = new Request.Builder()
				      .url(_forecastUrl)
				      .build();
			  try {
				  Response response = httpClient.newCall(request)
						  .execute();
				  // Response verification
				  if(response.isSuccessful()) {					  
					  return	response.body().string();
				  }
			} catch (Exception e) {
				System.err.println("Error: " + e);
			}
			return null;
		}
		
		@Override
		protected void done() {
			try {
				// CallBack GET method
				System.out.println(get());
			} catch (InterruptedException | ExecutionException e) {
				System.err.println("Error: " + e);
			}
		}		
		
	}
	*/
	
	/**
	 * 	Synchronous GET with an anonymous class
	 */
	/*
	new SwingWorker<String, Void>() {

		@Override
		protected String doInBackground() throws Exception {
			OkHttpClient httpClient = new OkHttpClient();
			Request request = new Request.Builder()
				      .url(forecastUrl)
				      .build();
			  try {
				  Response response = httpClient.newCall(request)
						  .execute();
			return	response.body().string();
			} catch (Exception e) {
				System.err.println("Error: " + e);
			}
			return null;
		}
		
		// when the task is done
		@Override
		protected void done() {
			try {
				System.out.println(get());
			} catch (InterruptedException | ExecutionException e) {
				System.err.println("Error: " + e);
			}
		}
	}.execute();		
	*/

	
	/**
	 *  Synchronous GET directly
	 */
	/*
	System.out.println("Avant la requete...");
	OkHttpClient httpClient = new OkHttpClient();
	Request request = new Request.Builder()
		      .url(forecastUrl)
		      .build();
	  try {
		  Response response = httpClient.newCall(request)
				  .execute();
		System.out.println(response.body().string());
	} catch (Exception e) {
		System.err.println("Error: " + e);
	}
  */
}
