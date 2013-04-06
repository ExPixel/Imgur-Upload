package expixel.imgur.database;

import java.io.File;

import org.json.JSONObject;

public class ImgurImage {
	String id;
	String deletehash;
	String link;

	File imageFile;

	public ImgurImage() {}

	public ImgurImage(JSONObject jsonData) {
		this.id = jsonData.getString("id");
		this.deletehash = jsonData.getString("deletehash");
		this.link = jsonData.getString("link");
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return this.id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the deletehash
	 */
	public String getDeletehash() {
		return this.deletehash;
	}

	/**
	 * @param deletehash the deletehash to set
	 */
	public void setDeletehash(String deletehash) {
		this.deletehash = deletehash;
	}

	/**
	 * @return the link
	 */
	public String getLink() {
		return this.link;
	}

	/**
	 * @param link the link to set
	 */
	public void setLink(String link) {
		this.link = link;
	}

	/**
	 * @return the imageFile
	 */
	public File getImageFile() {
		return this.imageFile;
	}

	/**
	 * @param imageFile the imageFile to set
	 */
	public void setImageFile(File imageFile) {
		this.imageFile = imageFile;
	}


}
