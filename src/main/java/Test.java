import model.BoxedSet;
import model.Brand;
import model.Gauge;
import model.Locomotive;
import model.Product;
import model.RollingStock;
import model.Locomotive.DCCType;
import model.RollingStock.RollingStockType;
import model.BoxedSet;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import DAO.*;

public class Test {
    public static void main(String[] args) {
        performDatabaseOperations();

    }

    private static void performDatabaseOperations() {
        try (Connection connection = DatabaseConnectionHandler.getConnection()) {            
            Brand brand = new Brand("Bachmann", "UK");
            brand.setBrandID(1);
            
            /** Test ProductDAO 
            ProductDAO opera = new ProductDAO();
            Product product = new Product(brand, "Test Insert", "T000", 8.8, "??", 0);
        
            // Perform database operations using the DAO
            int id = opera.insertProduct(product);
            opera.getAllProduct();
            opera.findProductByCode("T000");
            product = new Product(brand, "Test Update", "T000", 8.8, "??", 0);
            product.setProductID(id);
            opera.updateProduct(product);
            opera.findProductByID(id);
            opera.deleteProduct(id);
            opera.getAllProduct();*/
            
            /** Test LocomotiveDAO 
            LocomotiveDAO op = new LocomotiveDAO();  

            Product product1 = new Product(brand, "Test Locomotive Insert1", "L213", 99.8, "A simple test case", 8);
            Product product2 = new Product(brand, "Test Locomotive Insert2", "L233", 99.8, "A simple test case", 8);
            int[] era1 = {1,3};
            int[] era2 = {2,3};
            Locomotive lo1 = new Locomotive(product1, "OO", "Fitted", era1);
            op.insertLocomotive(lo1);
            Locomotive lo2 = new Locomotive(product2, "TT", "Sound", era2);
            op.insertLocomotive(lo2);

            int productID1 = ProductDAO.findIDByName("Test Locomotive Insert1");
            int productID2 = ProductDAO.findIDByName("Test Locomotive Insert2");
            int[] era3 = {1,3,5,7};
            lo1 = new Locomotive(product1, "OO", "Analogue", era3);
            lo1.setProductID(productID1);
            op.updateLocomotive(lo1);
            
            System.out.println("Test findLocomotiveByID() Should be 2");
            System.out.println(op.findLocomotiveByID(productID2).getProductName());
            System.out.println("Test findLocomotivesByDCCType Should be 2");
            System.out.println(op.findLocomotivesByDCCType(DCCType.SOUND).get(0).getProductName());
            System.out.println("Test findLocomotivesByEra Should be 1&2");
            int[] era4 = {3};
            ArrayList<Locomotive> list = op.findLocomotivesByEra(era4);
            for (Locomotive loco : list) {
                System.out.println(loco.getProductName());
            }
            System.out.println("Test findLocomotivesByGauge Should be 2");
            System.out.println(op.findLocomotivesByGauge(Gauge.TT).get(0).getProductName());

            
            op.deleteLocomotive(productID1);
            op.deleteLocomotive(productID2);*/

            // Test LocomotiveDAO
            RollingStockDAO op = new RollingStockDAO();

            Product product1 = new Product(brand, "Test RollingStock Insert1", "S344", 89.8, "A simple test case", 5);
            Product product2 = new Product(brand, "Test RollingStock Insert2", "S686", 114.5, "A simple test case", 6);
            int[] era1 = {1,3};
            int[] era2 = {2,3};
            RollingStock ro1 = new RollingStock(product1, "Wagon", "OO", era1);
            op.insertRollingStock(ro1);
            RollingStock ro2 = new RollingStock(product2, "Carriage", "TT", era2);
            op.insertRollingStock(ro2);

            int productID1 = ProductDAO.findIDByName("Test RollingStock Insert1");
            int productID2 = ProductDAO.findIDByName("Test RollingStock Insert2");
            int[] era3 = {1,3,5,7};
            ro1 = new RollingStock(product1,"Carriage", "OO", era3);
            ro1.setProductID(productID1);
            op.updateRollingStock(ro1);

            System.out.println("Test findRollingStockByID Should be 2");
            System.out.println(op.findRollingStockByID(productID2).getProductName());
            System.out.println("Test indRollingStocksByType Should be 2");
            System.out.println(op.findRollingStocksByType(RollingStockType.CARRIAGE).get(0).getProductName());
            System.out.println("Test findRollingStocksByEra Should be 1&2");
            int[] era4 = {3};
            ArrayList<RollingStock> list = op.findRollingStocksByEra(era4);
            for (RollingStock roll : list) {
                System.out.println(roll.getProductName());
            }
            System.out.println("Test findRollingStocksByGauge Should be 2");
            System.out.println(op.findRollingStocksByGauge(Gauge.TT).get(0).getProductName());


//            op.deleteRollingStock(productID1);
//            op.deleteRollingStock(productID2);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseConnectionHandler.shutdown();
        }
    }
}