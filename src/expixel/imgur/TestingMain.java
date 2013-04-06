package expixel.imgur;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;

public class TestingMain {
	public static void main(String[] args) {
		System.out.println("Posting...");
		Imgur.checkAuth();
		HttpGet get = new HttpGet("https://api.imgur.com/3/account/me");
		Imgur.signGet(get);
		HttpResponse response = HttpUtil.request(get);
		if(response == null) {
			System.out.println("Post returned null.");
			return;
		}
		try {
			System.out.println("Stuff: " + IOUtils.toString(response.getEntity().getContent()));
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Posted.");
	}
}
