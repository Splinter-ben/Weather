package com.splinter.weatherforcast;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import javax.swing.JFrame;
import javax.swing.SwingWorker;

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
		
		String apiKey = "9ddcbca98bab559410fd7bbdffc14e68";
		double latitude = 37.8267;
		double longitude = -122.4233;
		
		// String forcastUrl = "https://api.darksky.net/forecast/" + apiKey + "/" + latitude + "," + longitude;
		String forecastUrl= String.format("https://api.darksky.net/forecast/%s/%f,%f", apiKey, latitude, longitude);
		
		System.out.println("Avant la requete...");
		// Call our internal Class
		new ForeCastWorker(forecastUrl).execute();
		System.out.println("Après la requete...");
		
	}
		  
	/**
	 * Internal Class
	 */
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
	
	
		/**
		 * 	Asynchronous GET
		 */
		/*
		// Anonymous Class
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
		 *  Synchronous GET
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
