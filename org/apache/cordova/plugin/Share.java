package org.apache.cordova.plugin;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Share extends CordovaPlugin {

	public static final String LOG_PROV = "PhoneGapLog";
	public static final String LOG_NAME = "Share Plugin";

	@Override
	public boolean execute(final String action, final JSONArray args, final CallbackContext callbackContext) {
		cordova.getThreadPool().execute(new Runnable() {
			@Override
			public void run() {
				try {
					final JSONObject jo = args.getJSONObject(0);
					doSendIntent(jo.getString("subject"), jo.getString("text"),jo.getString("file"));
					callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK));
				} catch (JSONException e) {
					Log.e(LOG_PROV, LOG_NAME + ": Error: " + PluginResult.Status.JSON_EXCEPTION);
					e.printStackTrace();
					callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.JSON_EXCEPTION));
				}
			}
		});
		return true;
	}
	
	
	private void doSendIntent(String subject, String text, String image) {
		final Intent sendIntent = new Intent(android.content.Intent.ACTION_SEND);
		
	    if(image.length() > 0)
		{
			// the filename needs a valid extension, so it renders correctly in target apps
		    final String imgExtension = image.substring(image.indexOf("/") + 1, image.indexOf(";base64"));
		    String fileName = "file." + imgExtension;
		    final String encodedImg = image.substring(image.indexOf(";base64,") + 8);
		    String dir = "";
			try {
				dir = getDownloadDir();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    try {
				saveFile(Base64.decode(encodedImg, Base64.DEFAULT), dir, fileName);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    String localImage = "android.resource://" + dir + "/" + fileName;
			sendIntent.setType("image/*");		
			sendIntent.putExtra(android.content.Intent.EXTRA_STREAM, Uri.parse(localImage));
		}
	    else
	    {
	    	sendIntent.setType("text/plain");
			sendIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
			sendIntent.putExtra(android.content.Intent.EXTRA_TEXT, text);
	    }
	   
		cordova.getActivity().startActivityForResult(sendIntent, 0);
	}
	
	private String getDownloadDir() throws IOException {
		    final String dir = webView.getContext().getExternalFilesDir(null) + "/socialsharing-downloads"; // external
		    //final String dir = cordova.file.dataDirectory;
		    createOrCleanDir(dir);
		    return dir;
	}
	
	private void createOrCleanDir(final String downloadDir) throws IOException {
		final File dir = new File(downloadDir);
		if (!dir.exists()) {
	      if (!dir.mkdirs()) {
	        throw new IOException("CREATE_DIRS_FAILED");
		      }
		    } else {
		      cleanupOldFiles(dir);
		    }
	}
	 private void cleanupOldFiles(File dir) {
		    for (File f : dir.listFiles()) {
		      //noinspection ResultOfMethodCallIgnored
		      f.delete();
		    }
	}
	private void saveFile(byte[] bytes, String dirName, String fileName) throws IOException {
		    final File dir = new File(dirName);
		    final FileOutputStream fos = new FileOutputStream(new File(dir, fileName));
		    fos.write(bytes);
		    fos.flush();
		    fos.close();
	}
}
