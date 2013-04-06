package expixel.imgur;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class ImgurUtils {

	static Random random = new Random( System.currentTimeMillis() );

	static String chars = "1234567890~`-=_+qwertyuiopasdfghjklzxcvbnm,<>,./;'{}|!@##$%%^&&*()*(";

	public static String sha256Hash(String text) {
		return hash("SHA-256", text);
	}

	public static String hash(String algorithm, String text) {
		byte[] bytes = digestEncodeQuietly(algorithm, text);
		BigInteger bigInt = new BigInteger(bytes);
		return bigInt.toString(16);
	}

	public static byte[] digestEncodeQuietly(String algorithm, String text) {
		try {
			return digestEncode(algorithm, text);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return new byte[0];
	}

	public static byte[] digestEncode(String algorithm, String text) throws UnsupportedEncodingException, NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance(algorithm);
		md.update(text.getBytes("UTF-8"));
		byte[] digest = md.digest();
		return digest;
	}

	public static String randomString(int length) {
		StringBuilder builder = new StringBuilder();
		while(builder.length() < length) {
			builder.append( chars.charAt(random.nextInt(chars.length())) );
		}
		return builder.toString();
	}
}