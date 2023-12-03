package helper;

import java.nio.charset.StandardCharsets;

/**
 * Utility class to support filtering of products.
 * 
 * Contains helper enums and classes that can be used with JComboBox
 */
public class Filter {

    public class SortBy{
        /**
         * Allows for sorting of products
         * 
         * Contains support for ascending, descending sorting
         * 
         * @param name The display name to be used for JComboBoxes etc 
         * @param dbHandle The database column that will be sorted (leave blank for no sorting)
         * @param asc Ascending if true otherwise Descending
         */
        public SortBy(String name, String dbHandle, boolean asc){
            this.name = name;
            this.dbHandle = dbHandle;
            this.isAscending = asc;
        }
        public SortBy(String name, String dbHandle){
            this(name, dbHandle, true);
        }

        @Override
        public String toString()
        {
            return this.name;
        }

        public String getDbHandle()
        {
            return this.dbHandle;
        }

        public boolean isAscending(){
            return this.isAscending;
        }

        private String name;
        private String dbHandle;
        private boolean isAscending;
    };

    public class PriceRange{
        /**
         * Allows for filtering by a given range of prices.
         * 
         * @param low the lowest price of product to be returned by this filter
         * @param high the highest price of product to be returned by this filter
         * @param displayName the display name used for JComboBoxes etc
         */
        public PriceRange(float low, float high, String displayName){
            this.minVal = low;
            this.maxVal = high;
            this.displayName = displayName;
        }
        /**
         * Allows for filtering by a given range of prices.
         * 
         * Automatically sets the display name to "£<low> - £<high>""
         * 
         * @param low the lowest price of product to be returned by this filter
         * @param high the highest price of product to be returned by this filter
         */
        public PriceRange(float low, float high){
            this.minVal = low;
            this.maxVal = high;
            this.displayName = "£" + low + " - £" + high;
        }
        @Override
        public String toString()
        {
            return new String(displayName.getBytes(), StandardCharsets.UTF_8);
        }

        public float getMin()
        {
            return this.minVal;
        }
        
        public float getMax()
        {
            return this.maxVal;
        }

        private float minVal;
        private float maxVal;
        private String displayName;
    }

    public class BrandFilter {
        /**
         * Allows for filtering by brand name
         * 
         * @param brandName the string name of the brand to filter by
         * @param displayName the display name of the brand to filter by
         */
        public BrandFilter(String brandName, String displayName) {
            this.brandName = brandName;
            this.displayName = displayName;
        }

        public BrandFilter(String brandName) {
            this.brandName = brandName;
            this.displayName = brandName;
        }
    
        @Override
        public String toString() {
            return displayName;
        }
        public String getBrand() {
            return brandName;
        }
    
        private String brandName;
        private String displayName;
    }
    
    public class TypeFilter{
        /**
         * Allows for filtering by type of product.
         * 
         * This includes locomotive, track, box set and rolling stock
         * 
         * @param dbColumn the db column to filter by
         * @param displayName the name to be used for jcomboboxes
         * @param subFilterCol the column to be used for type subfilters (DEPRECATED)
         */
        public TypeFilter(String dbColumn, String displayName, String subFilterCol){
            this.dbHandle = dbColumn;
            this.displayName = displayName;
            this.subFilterDbHandle = subFilterCol;
        }

        public String toString(){
            return displayName;
        }

        public String getDbTable(){
            return this.dbHandle;
        }

        public void setSubFilter(String subFilter){
            this.subFilter = subFilter;
        }

        public String getSubFilter(){
            return subFilter;
        }

        public String getSubFilterColumn(){
            return subFilterDbHandle;
        }

        private String dbHandle;
        private String displayName;
        private String subFilter;
        private String subFilterDbHandle;
    }
    /**
     * A class to allow for adding generic type filters to combo boxes
     */
    public class SubFilter<T>{
        private T value;
        private String dbColumn;
        /**
         * Creates a sub-filter using the type and column that the sub filter filters from
         * 
         * @param value the value of this specific filter value
         * @param dbColumn the column that is filtered
         */
        public SubFilter(T value, String dbColumn){
            this.value = value;
            this.dbColumn = dbColumn;
        }

        public String toString(){
            return value.toString();   
        }

        public T getValue(){
            return this.value;
        }

        public String getDbColumn(){
            return this.dbColumn;
        }
    }
}
