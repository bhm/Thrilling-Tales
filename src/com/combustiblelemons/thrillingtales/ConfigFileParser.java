package com.combustiblelemons.thrillingtales;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.R.raw;
import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.util.Log;

public class ConfigFileParser {
	
	private static final String LOG_TAG = "Thrilling Tales";
	private Context mContext;
	private Settings settings;
	public ConfigFileParser(Context context){
		this.mContext = context;
		this.settings  = new Settings(mContext);		
	}	
	/**
	 * 
	 * @param itemName villains, locations, hooks, etc
	 * @return {@literal HashMap<value, column>}
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public HashMap<String, String> getItems(String itemName) throws FileNotFoundException, IOException{				
				
		HashMap<String,String> _items = new HashMap<String,String>();		
		String[] files = settings.getFiles(itemName);
		try {
			for (String file : files){			
				int resourceId = mContext.getResources().getIdentifier(file, "raw", mContext.getPackageName());
				Log.v(LOG_TAG, file + "Resource id: " + resourceId);			
				DataInputStream dis = new DataInputStream(mContext.getResources().openRawResource(resourceId));
				BufferedReader buffer = new BufferedReader(new InputStreamReader(dis));
				String line;
				while ((line=buffer.readLine()) != null){ 
					_items.put(line, file);
				}			
			}	
		} catch (NotFoundException e){
			e.printStackTrace();			
		}
		return _items;
	}	
	
	/**
	 * Pass the item name so the function reads from settings all associated with it files with values in it, and then returns them in HashMap
	 * @param itemName villains, locations, hooks, etc Get the item name for which all possible columns has to be filled.
	 * @return HashMap<value, column>
	 * @throws FileNotFoundException
	 * @throws NotFoundException thrown when file the identifier for a file was not found thus file does not exist in {@link raw} directory
	 * @throws IOException
	 */
	public HashMap<String, String> getColumns(String itemName) throws FileNotFoundException, IOException{
		HashMap<String,String> _items = new HashMap<String,String>();	
		try {
			String[] files = settings.getFiles(itemName);
			for (String file : files){			
				int resourceId = mContext.getResources().getIdentifier(file, "raw", mContext.getPackageName());
				Log.d(LOG_TAG, "Resource id for file  " + file + " is " + resourceId);			
				DataInputStream dis = new DataInputStream(mContext.getResources().openRawResource(resourceId));
				BufferedReader buffer = new BufferedReader(new InputStreamReader(dis));
				String line;
				while ((line=buffer.readLine()) != null){ 
					_items.put(line, file);
				}			
			}	
		} catch (NotFoundException e){
			e.printStackTrace();			
		}
		return _items;
	}	
	/**
	 * Parses through descriptions.xml file
	 * @param itemName
	 * @return
	 * @throws XmlPullParserException 
	 * @throws IOException 
	 */
	public HashMap<String,String> getDescriptions(String[] items) throws XmlPullParserException, IOException, NotFoundException{
		HashMap<String, String> _items = new HashMap<String,String>();	
		XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
		factory.setNamespaceAware(true);
		XmlPullParser xpp = factory.newPullParser();			
		int resourceId = mContext.getResources().getIdentifier(settings.getDescriptionsFileName(), "raw", mContext.getPackageName());	
		Log.v(LOG_TAG, "Resource id for file " + settings.getDescriptionsFileName() + " is " + resourceId);
		xpp.setInput(mContext.getResources().openRawResource(resourceId), null);
		
		int event = xpp.getEventType();
		while(event != XmlPullParser.END_DOCUMENT){
			String name = "";
			if (event == XmlPullParser.START_TAG){
				name = xpp.getName();
				name = name.replace("_", " ");				
				if (xpp.next() == XmlPullParser.TEXT){					
					String text = xpp.getText();
					if (!text.equalsIgnoreCase("descriptions")){
						_items.put(text, name);
						Log.v(LOG_TAG, text + " | " + name);
					}
				}
			}
			event = xpp.next();			
		}		
		return _items;
	}
}
