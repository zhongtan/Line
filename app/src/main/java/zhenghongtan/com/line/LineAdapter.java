package zhenghongtan.com.line;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by ZhengHong on 04/08/2017.
 */

public class LineAdapter extends ArrayAdapter<Restaurant> {

    /**
     * Constructs a new LineAdapter.
     * @param context of the app
     * @param restaurants is the list of restaurants, which is the data source of the adapter.
     */
    public LineAdapter(Context context, List<Restaurant> restaurants) {
        super(context, 0, restaurants);
    }

    /**
     * Returns a list item view that displays information about the restaurant at the given position.
     * in the list of restaurants.
     */
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.activity_line, parent, false);
        }

        // Find the restaurant at the given position in the list of restaurants
        Restaurant restaurant = getItem(position);

        // Find the TextView with the View ID rating
        TextView ratingView = (TextView) listItemView.findViewById(R.id.rating);
        // Display the rating of the restaurant in the TextView
        ratingView.setText(formatRating(restaurant.getRating()));

        // Set the proper background color on the rating circle.
        GradientDrawable ratingCircle = (GradientDrawable) ratingView.getBackground();

        // Set the proper rating color.
        ratingCircle.setColor(getRatingColor(restaurant.getRating()));

        // Find the TextView with the View ID restaurant
        TextView restaurantView = (TextView) listItemView.findViewById(R.id.restaurant);
        restaurantView.setText(restaurant.getName());

        return listItemView;
    }

    // Returns the formatted rating string from a decimal rating value.
    private String formatRating(double rating) {
        DecimalFormat ratingFormat = new DecimalFormat("0.0");
        return ratingFormat.format(rating);
    }

    // Returns the proper rating color given the rating value.
    private int getRatingColor(double rating) {
        int ratingColorResourceID;
        int ratingFloor = (int) Math.floor(rating);

        switch(ratingFloor) {
            case 0:
            case 1:
                ratingColorResourceID = R.color.color1;
                break;
            case 2:
                ratingColorResourceID = R.color.color2;
                break;
            case 3:
                ratingColorResourceID = R.color.color3;
                break;
            case 4:
            default:
                ratingColorResourceID = R.color.color4;
                break;
        }

        return ContextCompat.getColor(getContext(), ratingColorResourceID);
    }
}
