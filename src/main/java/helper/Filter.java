package helper;

import java.nio.charset.StandardCharsets;

import model.Brand;

public class Filter {
    public class SortBy{
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
        public PriceRange(float low, float high, String displayName){
            this.minVal = low;
            this.maxVal = high;
            this.displayName = displayName;
        }
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
    public class BrandFilter{
        public BrandFilter(Brand b, String displayName){
            this.enclosedBrand = b;
            this.displayName = displayName;
        }
        public BrandFilter(Brand b){
            this(b, b.getBrandName());
        }
        @Override
        public String toString(){
            return displayName;
        }
        public int getBrand(){
            return enclosedBrand != null ? enclosedBrand.getBrandID() : -1;
        }
        private Brand enclosedBrand;
        private String displayName;
    }
    public class TypeFilter{
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
}
