package expixel.imgur;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.List;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;


public class HttpUtil {

	public static HttpResponse post(String url, List<NameValuePair> data) {
		try {
			return post(new URL(url), data);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static HttpResponse post(URL url, List<NameValuePair> data) {
		try {
			return post(url.toURI(), data);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static HttpResponse post(URI uri, List<NameValuePair> data) {
		try {
			HttpPost httppost = new HttpPost(uri);
			httppost.setEntity(new UrlEncodedFormEntity(data));
			return request(httppost);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Deprecated
	public static HttpResponse post(HttpPost post) {
		return request(post);
	}

	@Deprecated
	public static HttpResponse delete(HttpDelete delete) {
		return request(delete);
	}

	@Deprecated
	public static HttpResponse get(HttpGet get) {
		return request(get);
	}

	public static HttpResponse request(HttpUriRequest request) {
		try {
			HttpClient client = new DefaultHttpClient();

			SSLContext context = SSLContext.getInstance("TLS");
			context.init(null, null, new SecureRandom());

			SSLSocketFactory sf = new SSLSocketFactory(
					context,
					SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
			Scheme sch = new Scheme("https", 443, sf);
			client.getConnectionManager().getSchemeRegistry().register(sch);

			HttpResponse response = client.execute(request);
			return response;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (KeyManagementException e) {
			e.printStackTrace();
		}
		return null;
	}
}
