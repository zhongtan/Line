package zhenghongtan.com.line;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class LineActivity extends AppCompatActivity {

    // url to access the Google Maps API
    private static final String PRE_URL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=";

    // last section of the url to access the Google Places API and get restaurants within a 500
    // meter radius
    private static final String POST_URL = "&radius=500&type=restaurant";

    // API key used in querying for information
    private static final String API_KEY = "&key=...";

    // adapter for the list of restaurants
    private LineAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line);

        // TODO: get the user's latitude and longitude and then build the URL used for querying the data
        String requestURL = createUrl();

        // start the AsyncTask to fetch the restaurant data
        RestaurantAsyncTask task = new RestaurantAsyncTask();
        task.execute(requestURL);

        // Find a reference to the {@link ListView} in the layout.
        ListView restaurantListView = (ListView) findViewById(R.id.restaurant_list);

        // Create a new adapter with the list of restaurants as input
        mAdapter = new LineAdapter(this, new ArrayList<Restaurant>());

        /**
         * Set the adapter on the {@link ListView} so the list can be populated in the
         * user interface.
         */
        restaurantListView.setAdapter(mAdapter);

        // upon clicking the list view, brings the user to the exact map location
        restaurantListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                // Find the restaurant that was clicked on
                Restaurant restaurant = mAdapter.getItem(i);

                // TODO: create a different intent
                // Creates an Intent that will load a map of San Francisco
                Uri gmmIntentUri = Uri.parse("geo:37.7749,-122.4194");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });
    }

    /**
     * {@link AsyncTask} to perform the network request on a background thread, and then update the
     * UI with the list of restaurants that are within a 500 meter range from the user.
     */
    private class RestaurantAsyncTask extends AsyncTask<String, Void, List<Restaurant>> {

        /**
         * This method runs on a background thread and performs the network request
         * No updating of the UI from a background thread, so we return a list of {@link
         * Restaurant}s as the result.
         */
        @Override
        protected List<Restaurant> doInBackground(String... urls) {
            if (urls.length < 1 || urls[0] == null) {
                return null;
            }

            List<Restaurant> result = QueryUtils.fetchRestaurantData(urls[0]);
            return result;
        }

        /**
         * This method runs on the main UI thread after the background work has been completed.
         * This method receives as input, the return value from the doInBackground() method.
         */
        @Override
        protected void onPostExecute(List<Restaurant> data) {
            // clear the adapter of previous restaurant data
            mAdapter.clear();

            // if there is a valid list of {@link Restaurant}s, add them to the adapter's data set.
            // This will trigger the ListView to update.
            if (data != null && !data.isEmpty()) {
                mAdapter.addAll(data);
            }
        }
    }

    /**
     * Create the URL with the user's given location and returns a request URL for querying
     * restaurants that are 500 meters away from the user.
     */
    private String createUrl() {
        StringBuilder url = new StringBuilder();
        url.append(PRE_URL);
        url.append("47.6553929,-122.3072732");
        url.append(POST_URL);
        url.append(API_KEY);

        return url.toString();
    }
}
