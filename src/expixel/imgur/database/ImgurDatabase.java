package expixel.imgur.database;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class ImgurDatabase {

	static Connection connection = null;

	public static void initDatabase() throws ClassNotFoundException {
		Class.forName("org.sqlite.JDBC");

		try {
			connection = DriverManager.getConnection("jdbc:sqlite:imgur_upload.db");
		} catch (SQLException e) {
			e.printStackTrace();
		}

		createTables();
	}

	public static void closeDatabase() {
		if(isConnected()) {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static boolean isConnected() {
		try {
			return connection != null && !connection.isClosed();
		} catch (SQLException e) {
			return false;
		}
	}

	public static Statement getStatement() {
		try {
			return connection.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static PreparedStatement prepareStatement(String sql) {
		try {
			return connection.prepareStatement(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void createTables() {

		System.out.println("Creating images table...");

		String tableSQL = "create table if not exists images (" +
				"id VARCHAR(256) NOT NULL," +
				"deletehash VARCHAR(256) NOT NULL," +
				"link VARCHAR(512) NOT NULL," +
				"file VARCHAR(512)," +
				"PRIMARY KEY(id)," +
				"UNIQUE(id)" +
				")";

		Statement sql = getStatement();

		try {
			sql.executeUpdate(tableSQL);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		System.out.println("Created images table.");

	}

	public static void pushImageToDatabase( ImgurImage imgurImage ) {
		PreparedStatement statement = prepareStatement("INSERT INTO images (id, deletehash, link, file) VALUES (?, ?, ?, ?)");
		try {
			statement.setString(1, imgurImage.getId());
			statement.setString(2, imgurImage.getDeletehash());
			statement.setString(3, imgurImage.getLink());
			statement.setString(4, imgurImage.getImageFile().getPath());
			statement.executeUpdate();
			System.out.println("Image pushed to database.");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static ImgurImage getImgurImage( String id ) {
		try {
			PreparedStatement statement = prepareStatement("SELECT * FROM images WHERE id=?");
			statement.setString(1, id);
			ResultSet set = statement.executeQuery();

			if(set.next()) {
				String img_id = set.getString(1);
				String img_deletehash = set.getString(2);
				String img_link = set.getString(3);
				String img_file_path = set.getString(4);
				return createImage( img_id, img_deletehash, img_link, img_file_path );
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static int getNumberOfImgurImages() {
		try {
			Statement statement = getStatement();
			ResultSet set = statement.executeQuery("SELECT COUNT(*) FROM images");
			if(set.next()) {
				int num = set.getInt(1);
				return num;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public static ImgurImage[] getImgurImages( int offset, int count ) {
		PreparedStatement preparedStatement;
		try {
			if(offset == -1 || count == -1) {
				preparedStatement = prepareStatement("SELECT * FROM images WHERE 1=1");
			} else {
				preparedStatement = prepareStatement("SELECT * FROM images LIMIT ? OFFSET ?");
				preparedStatement.setInt(1, count);
				preparedStatement.setInt(2, offset);
			}

			ResultSet set = preparedStatement.executeQuery();

			ArrayList<ImgurImage> images = new ArrayList<>();

			while(set.next()) {
				String img_id = set.getString(1);
				String img_deletehash = set.getString(2);
				String img_link = set.getString(3);
				String img_file_path = set.getString(4);
				ImgurImage img = createImage( img_id, img_deletehash, img_link, img_file_path );
				images.add(img);
			}

			return images.toArray(new ImgurImage[0]);

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void deleteID( String id ) {
		PreparedStatement statement = prepareStatement("DELETE FROM images WHERE id=?");
		try {
			statement.setString(1, id);
			statement.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private static ImgurImage createImage( String id, String deletehash, String link, String filepath ) {
		ImgurImage img = new ImgurImage();
		img.setId(id);
		img.setDeletehash(deletehash);
		img.setLink(link);
		img.setImageFile(new File(filepath));
		return img;
	}
}
