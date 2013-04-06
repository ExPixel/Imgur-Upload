package expixel.imgur;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.cache.HeaderConstants;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.message.BasicNameValuePair;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.json.JSONObject;

import expixel.OAuth2.ImgurOAuth2Config;
import expixel.imgur.database.ImgurDatabase;
import expixel.imgur.database.ImgurImage;
import expixel.imgur.natives.OSInterface;

public class Imgur {

	private static Preferences preferences = Preferences.userRoot().node("imguruploader");

	private static final int UPLOAD_LIMIT = 1000;

	static OSInterface osInterface;
	static Robot robot;

	private static StandardPBEStringEncryptor textEncryptor;

	private static final String encryptionAlgorithm = "PBEWITHSHA1ANDRC2_40";

	static {
		osInterface = OSInterface.getOSInterface();
		try {
			robot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}

	public static BufferedImage screenCapture( Rectangle rectangle ) {
		return robot.createScreenCapture(rectangle);
	}

	public static String authLink() {
		return String.format(
				"https://api.imgur.com/oauth2/authorize?client_id=%s&response_type=pin&state=imguruploadlogin",
				ImgurOAuth2Config.clientID);
	}

	public static void auth( String pin ) {
		List<NameValuePair> data = new ArrayList<>();
		data.add(new BasicNameValuePair("client_id", ImgurOAuth2Config.clientID));
		data.add(new BasicNameValuePair("client_secret", ImgurOAuth2Config.clientSecret));
		data.add(new BasicNameValuePair("grant_type", "pin"));
		data.add(new BasicNameValuePair("pin", pin));

		HttpResponse response = HttpUtil.post("https://api.imgur.com/oauth2/token", data);

		processReponseToAuth(response);
	}

	public static void refreshAuth() {

		System.out.println("Refreshing credentials...");

		String refreshToken = preferences.get("refresh_token", "");
		refreshToken = getTextEncryptor().decrypt(refreshToken);

		List<NameValuePair> data = new ArrayList<>();
		data.add(new BasicNameValuePair("refresh_token", refreshToken));
		data.add(new BasicNameValuePair("client_id", ImgurOAuth2Config.clientID));
		data.add(new BasicNameValuePair("client_secret", ImgurOAuth2Config.clientSecret));
		data.add(new BasicNameValuePair("grant_type", "refresh_token"));

		HttpResponse response = HttpUtil.post("https://api.imgur.com/oauth2/token", data);

		processReponseToAuth(response);
	}

	private static void processReponseToAuth(HttpResponse response) {
		HttpEntity entity = response.getEntity();

		String auth_token = null;

		try {
			auth_token = IOUtils.toString(entity.getContent());
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		JSONObject output = new JSONObject(auth_token);

		String accessToken = output.getString("access_token");
		accessToken = getTextEncryptor().encrypt(accessToken);

		String refreshToken = output.getString("refresh_token");
		refreshToken = getTextEncryptor().encrypt(refreshToken);

		String accountUsername = "";
		if(output.has("account_username")) {
			accountUsername = output.getString("account_username");
		}

		preferences.put("access_token", accessToken);
		preferences.put("refresh_token", refreshToken);
		preferences.put("account_username", accountUsername);

		DateTime dt = new DateTime();
		preferences.putLong("start_time", dt.getMillis());

		preferences.putBoolean("logged_in", true);

		String user = getUsername();
		if(user == null || user.length() < 1) {
			getAccountInfo();
		}
	}

	public static void checkAuth() {
		DateTime last = new DateTime( preferences.getLong("start_time", new DateTime().getMillis()) );
		DateTime cur = new DateTime();

		Duration duration = new Duration(last, cur);

		if(duration.getStandardHours() >= 1) {
			refreshAuth();
		}
	}

	public static boolean isLoggedIn() {
		return preferences.getBoolean("logged_in", false);
	}

	public static void logOut() {
		preferences.remove("logged_in");
		preferences.remove("access_token");
		preferences.remove("refresh_token");
		preferences.remove("start_time");
		preferences.remove("account_username");
	}

	public static HttpPost signPost(HttpPost post) {
		if(!isLoggedIn()) return post;
		checkAuth();
		String accessToken = preferences.get("access_token", "");
		accessToken = getTextEncryptor().decrypt(accessToken);
		post.addHeader(HeaderConstants.AUTHORIZATION, "Bearer " + accessToken);
		return post;
	}

	public static HttpDelete signDelete(HttpDelete delete) {
		if(!isLoggedIn()) return delete;
		checkAuth();
		String accessToken = preferences.get("access_token", "");
		accessToken = getTextEncryptor().decrypt(accessToken);
		delete.addHeader(HeaderConstants.AUTHORIZATION, "Bearer " + accessToken);
		return delete;
	}

	public static HttpGet signGet(HttpGet get) {
		if(!isLoggedIn()) return get;
		checkAuth();
		String accessToken = preferences.get("access_token", "");
		accessToken = getTextEncryptor().decrypt(accessToken);
		get.addHeader(HeaderConstants.AUTHORIZATION, "Bearer " + accessToken);
		return get;
	}

	public static ImgurImage uploadFile(File file) {

		if(uploadLimitReached())
			return null;

		addUploadAgainstLimit();

		HttpPost post = new HttpPost("https://api.imgur.com/3/image");
		FileBody bin = new FileBody(file);
		MultipartEntity entity = new MultipartEntity();
		entity.addPart("image", bin);
		post.setEntity(entity);
		signPost(post);
		HttpResponse response = HttpUtil.request(post);

		String responseS = "";

		try {
			responseS = IOUtils.toString(response.getEntity().getContent());
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return processImageData(responseS);
	}

	public static ImgurImage processImageData(String uploadJSON) {
		JSONObject json = new JSONObject(uploadJSON);

		if(!json.getBoolean("success"))
			return null;

		ImgurImage imgurImage = new ImgurImage(json.getJSONObject("data"));

		String ext = FilenameUtils.getExtension(imgurImage.getLink());
		if(ext.length() < 1) ext = "img";

		File file = new File("images/", imgurImage.getId() + "." + ext);

		URL url = null;
		try {
			url = new URL(imgurImage.getLink());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		if(url != null)
			downloadFile( url, file );

		imgurImage.setImageFile(file);

		ImgurDatabase.pushImageToDatabase(imgurImage);

		return imgurImage;
	}

	public static boolean downloadFile(URL url, File file) {


		InputStream inputStream = null;
		FileOutputStream outputStream = null;

		boolean success = false;

		try {

			if(file.getParentFile() != null) {
				file.getParentFile().mkdirs();
			}

			if(!file.exists()) {
				file.createNewFile();
			}

			inputStream = url.openStream();
			outputStream = new FileOutputStream(file);

			IOUtils.copy(inputStream, outputStream);

			success = true;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(inputStream);
			IOUtils.closeQuietly(outputStream);
		}
		return success;
	}

	public static void deleteImage(ImgurImage toDelete) {
		FileUtils.deleteQuietly(toDelete.getImageFile());
		HttpDelete deleteRequest = new HttpDelete("https://api.imgur.com/3/image/" + toDelete.getId());
		signDelete(deleteRequest);
		HttpUtil.request(deleteRequest);
	}

	/*{"data":{"id":"APIeWlD","deletehash":"7dDNKTKdAjtw5d7","link":"http:\/\/i.imgur.com\/APIeWlD.png"},"success":true,"status":200}*/
	public static void prepareDatabase() {
		System.out.println("Preparing database...");
		try {
			ImgurDatabase.initDatabase();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		System.out.println("Database prepared.");
		System.out.println("Images: " + ImgurDatabase.getNumberOfImgurImages());
	}

	public static boolean uploadLimitReached() {
		long last_time_millis = preferences.getLong("last_upload_start_time", -1);
		if(last_time_millis == -1) {
			DateTime dt = new DateTime();
			preferences.putLong("last_upload_start_time", dt.getMillis());
			last_time_millis = dt.getMillis();
		}
		DateTime last_time = new DateTime(last_time_millis);
		DateTime current_time = new DateTime();
		Duration dur = new Duration(last_time, current_time);
		System.out.println("Days: " + dur.getStandardDays());
		if(dur.getStandardDays() >= 1) {
			preferences.putInt("uploads_within_limit_time", 0);
			return false;
		}
		int uploads = preferences.getInt("uploads_within_limit_time", 0);
		System.out.println("UPLOADS: " + uploads);
		if(uploads >= UPLOAD_LIMIT) {
			return true;
		}
		return false;
	}

	public static void addUploadAgainstLimit() {
		int i = preferences.getInt("uploads_within_limit_time", 0);
		preferences.putInt("uploads_within_limit_time", i + 1);
	}

	/**
	 * 
	 * @return Gets the encryptor for this security object.
	 */
	public static StandardPBEStringEncryptor getTextEncryptor() {
		if(textEncryptor == null) {
			textEncryptor = new StandardPBEStringEncryptor();

			/**
			 * Encrypts using the password specified below.
			 */
			String pass = getUniqueID(); // It's really just your MAC Address
			textEncryptor.setPassword(pass);
			textEncryptor.setAlgorithm(encryptionAlgorithm);
		}
		return textEncryptor;
	}

	/**
	 * 
	 * @return The MAC address or a less reliable unique ID that is somewhat persistent.
	 */
	public static String getUniqueID() {
		try {
			InetAddress address = InetAddress.getLocalHost();
			byte[] ni = NetworkInterface.getByInetAddress(address).getHardwareAddress();
			BigInteger bigInt = new BigInteger(ni);
			return ImgurUtils.sha256Hash(bigInt.toString(2));
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return uniqueIDFallback();
	}

	/**
	 * Fallback for getUniqueID(). This may be less reliable because it's stored
	 * in a place where it can easily be removed or changed.
	 * @return
	 */
	public static String uniqueIDFallback() {
		String r = preferences.get("unique_id", null);
		if(r == null) {
			r = ImgurUtils.randomString(32);
			preferences.put("unique_id", r);
		}
		return ImgurUtils.sha256Hash(r);
	}

	public static String getUsername() {
		return preferences.get("account_username", "{unknown}");
	}

	public static void getAccountInfo() {
		HttpGet get = new HttpGet("https://api.imgur.com/3/account/me");
		get = signGet(get);
		HttpResponse response = HttpUtil.request(get);
		try {
			String output_string = IOUtils.toString(response.getEntity().getContent());
			JSONObject output = new JSONObject(output_string);
			JSONObject data = output.getJSONObject("data");
			preferences.put("account_username", data.getString("url"));
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}