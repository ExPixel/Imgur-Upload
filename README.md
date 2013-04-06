Imgur Upload
===

To make this work:

1. Open your terminal or CMD.
2. Navigate to the directory of the project.
3. Run 'oauth2quickgenerator.jar' using **java -jar [file]**
4. Enter your client id and your client secret.

Or you could create ImgurOAuth2Config yourself in package expixel.OAuth2.

It would look something like this :
```java
package expixel.OAuth2;
public class ImgurOAuth2Config {
    public static final String clientID = "4567949933323434";
    public static final String clientSecret = "8a648j0bm4m05442952038522sd5479fe";
}
```