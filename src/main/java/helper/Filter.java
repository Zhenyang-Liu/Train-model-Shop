package helper;

public class Filter {
    public class SortBy{
        public SortBy(String name, String dbHandle){
            this.name = name;
            this.dbHandle = dbHandle;
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

        private String name;
        private String dbHandle;
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
            return displayName;
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
}
