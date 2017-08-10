package zhenghongtan.com.line;

/**
 * Created by ZhengHong on 04/08/2017.
 */

public class Restaurant {
    private double mRating;        // rating of the restaurant
    private String mName;       // name of the restaurant

    /**
     * Constructs a new {@link Restaurant} object.
     *
     * @param name is the name of the restaurant.
     * @param rating is the rating of the restaurant.
     */
    public Restaurant(double rating, String name) {
        mRating = rating;
        mName = name;
    }

    // Returns the rating of the restaurant.
    public double getRating() {
        return mRating;
    }

    // Returns the name of the restaurant.
    public String getName() {
        return mName;
    }
}
