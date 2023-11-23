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
        public PriceRange(float low, float high){
            this.minVal = low;
            this.maxVal = high;
        }
        @Override
        public String toString()
        {
            return "£" + this.minVal + " - £" + this.maxVal;
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
    }
}
