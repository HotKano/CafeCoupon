package com.example.cafe;

/**
 * 데이터를 담고 있을 아이템 정의
 *
 * @author Mike
 */
public class ReviewTextItem {

    /**
     * Icon
     */

    /**
     * Data array
     */
    private String[] mData;

    /**
     * True if this item is selectable
     */
    private boolean mSelectable = true;

    /**
     * Initialize with icon and data array
     *
     * @param obj
     */
    public ReviewTextItem(String[] obj) {

        mData = obj;
    }

    /**
     * Initialize with icon and strings
     *
     * @param obj01
     * @param obj02
     * @param obj03
     */
    public ReviewTextItem(String obj01, String obj02, String obj03, String obj04, String obj05, String obj06, String obj07, String obj08, String obj09) {

        mData = new String[9];
        mData[0] = obj01;
        mData[1] = obj02;
        mData[2] = obj03;
        mData[3] = obj04;
        mData[4] = obj05;
        mData[5] = obj06;
        mData[6] = obj07;
        mData[7] = obj08;
        mData[8] = obj09;

    }

    /**
     * True if this item is selectable
     */
    public boolean isSelectable() {
        return mSelectable;
    }

    /**
     * Set selectable flag
     */
    public void setSelectable(boolean selectable) {
        mSelectable = selectable;
    }

    /**
     * Get data array
     *
     * @return
     */
    public String[] getData() {
        return mData;
    }

    /**
     * Get data
     */
    public String getData(int index) {
        if (mData == null || index >= mData.length) {
            return null;
        }

        return mData[index];
    }

    /**
     * Set data array
     *
     * @param obj
     */
    public void setData(String[] obj) {
        mData = obj;
    }

    /**
     * Set icon
     *
     * @param icon
     */


    /**
     * Get icon
     *
     * @return
     */


    /**
     * Compare with the input object
     *
     * @param other
     * @return
     */
    public int compareTo(ReviewTextItem other) {
        if (mData != null) {
            String[] otherData = other.getData();
            if (mData.length == otherData.length) {
                for (int i = 0; i < mData.length; i++) {
                    if (!mData[i].equals(otherData[i])) {
                        return -1;
                    }
                }
            } else {
                return -1;
            }
        } else {
            throw new IllegalArgumentException();
        }

        return 0;
    }

}
