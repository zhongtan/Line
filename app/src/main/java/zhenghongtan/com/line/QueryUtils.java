package zhenghongtan.com.line;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZhengHong on 07/08/2017.
 */

public final class QueryUtils {

    // Tag for the log messages
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    /**
     * Create a private constructor to prevent anyone from creating a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {

    }

    /**
     * Return a list of {@link Restaurant} objects that is built from parsing the given
     * JSON response.
     */
    public static List<Restaurant> extractResultFromJson(String restaurantJSON) {
        // If the JSON string is empty or null, return early
        if (TextUtils.isEmpty(restaurantJSON)) {
            return null;
        }

        List<Restaurant> restaurants = new ArrayList<>();

        // Try to parse the JSON response. If there is a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so that the app doesn't crash and then print the error message to
        // the logs.
        try {
            // Create a JSON object from the JSON response string
            JSONObject root = new JSONObject(restaurantJSON);

            // extract the JSONArray associated with the key called "results",
            // which represents a list of restaurants.
            JSONArray results = root.getJSONArray("results");
            for (int i = 0; i < results.length(); i++) {
                JSONObject restaurant = results.getJSONObject(i);
                if (restaurant.has("rating")) {
                    restaurants.add(new Restaurant(restaurant.getDouble("rating"),
                            restaurant.getString("name")));
                }
            }
        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app does not crash. Print the log message with the
            // message from the exception.
            Log.e("QueryUtils", "Problem parsing the restaurant JSON results", e);
        }

        return restaurants;
    }

    /**
     * Query for the nearest restaurants and returns a list of {@link Restaurant} objects.
     */
    public static List<Restaurant> fetchRestaurantData(String requestURL) {
        // Create a URL object
        URL url = createUrl(requestURL);

        // Perform a HTTP request to the URL and receive a JSON response back.
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request", e);
        }

        return extractResultFromJson(jsonResponse);
    }

    /**
     * Returns a new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }
}