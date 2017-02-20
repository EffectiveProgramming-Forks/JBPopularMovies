package com.breunig.jeff.project1.models;

/**
 * Created by jkbreunig on 2/2/17.
 */

public enum MovieSortType {
    POPULAR(0), TOP_RATED(1), FAVORITES(2);

    private int mIntValue;

    MovieSortType(int value) {
        mIntValue = value;
    }

    public int getIntValue() {
        return mIntValue;
    }

    public static MovieSortType fromInt(int intValue) {
        for (MovieSortType sortType : MovieSortType.values()) {
            if (sortType.getIntValue() == intValue) {
                return sortType;
            }
        }
        return null;
    }
}
