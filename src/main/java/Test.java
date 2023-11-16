import model.BoxedSet;
import model.Brand;
import model.Controller;
import model.Gauge;
import model.Locomotive;
import model.Product;
import model.RollingStock;
import model.BoxedSet.BoxedType;
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
            
            // // Test ProductDAO 
            // Product product = new Product(brand, "Test Insert", "T000", 8.8, "??", 0);
            // int id = ProductDAO.insertProduct(product);
            // ProductDAO.getAllProduct();
            // ProductDAO.findProductByCode("T000");
            // product = new Product(brand, "Test Update", "T000", 8.8, "??", 0);
            // product.setProductID(id);
            // ProductDAO.updateProduct(product);
            // ProductDAO.findProductByID(id);
            // ProductDAO.deleteProduct(id);
            // ProductDAO.getAllProduct();
            
            // // Test LocomotiveDAO 
            // Product product1 = new Product(brand, "Test Locomotive Insert1", "L213", 99.8, "A simple test case", 8);
            // Product product2 = new Product(brand, "Test Locomotive Insert2", "L233", 99.8, "A simple test case", 8);
            // int[] era1 = {1,3};
            // int[] era2 = {2,3};
            // Locomotive lo1 = new Locomotive(product1, "OO", "Fitted", era1);
            // LocomotiveDAO.insertLocomotive(lo1);
            // Locomotive lo2 = new Locomotive(product2, "TT", "Sound", era2);
            // LocomotiveDAO.insertLocomotive(lo2);

            // int productID1 = ProductDAO.findIDByName("Test Locomotive Insert1");
            // int productID2 = ProductDAO.findIDByName("Test Locomotive Insert2");
            // int[] era3 = {1,3,5,7};
            // lo1 = new Locomotive(product1, "OO", "Analogue", era3);
            // lo1.setProductID(productID1);
            // LocomotiveDAO.updateLocomotive(lo1);
            
            // System.out.println("Test findLocomotiveByID() Should be 2");
            // System.out.println(LocomotiveDAO.findLocomotiveByID(productID2).getProductName());
            // System.out.println("Test findLocomotivesByDCCType Should be 2");
            // System.out.println(LocomotiveDAO.findLocomotivesByDCCType(DCCType.SOUND).get(0).getProductName());
            // System.out.println("Test findLocomotivesByEra Should be 1&2");
            // int[] era4 = {3};
            // ArrayList<Locomotive> list = LocomotiveDAO.findLocomotivesByEra(era4);
            // for (Locomotive loco : list) {
            //     System.out.println(loco.getProductName());
            // }
            // System.out.println("Test findLocomotivesByGauge Should be 2");
            // System.out.println(LocomotiveDAO.findLocomotivesByGauge(Gauge.TT).get(0).getProductName());

            
            // LocomotiveDAO.deleteLocomotive(productID1);
            // LocomotiveDAO.deleteLocomotive(productID2);

            // // Test LocomotiveDAO 
            // Product product1 = new Product(brand, "Test RollingStock Insert1", "S344", 89.8, "A simple test case", 5);
            // Product product2 = new Product(brand, "Test RollingStock Insert2", "S686", 114.5, "A simple test case", 6);
            // int[] era1 = {1,3};
            // int[] era2 = {2,3};
            // RollingStock ro1 = new RollingStock(product1, "Wagon", "OO", era1);
            // RollingStockDAO.insertRollingStock(ro1);
            // RollingStock ro2 = new RollingStock(product2, "Carriage", "TT", era2);
            // RollingStockDAO.insertRollingStock(ro2);

            // int productID1 = ProductDAO.findIDByName("Test RollingStock Insert1");
            // int productID2 = ProductDAO.findIDByName("Test RollingStock Insert2");
            // int[] era3 = {1,3,5,7};
            // ro1 = new RollingStock(product1,"Carriage", "OO", era3);
            // ro1.setProductID(productID1);
            // RollingStockDAO.updateRollingStock(ro1);
            
            // System.out.println("Test findRollingStockByID Should be 2");
            // System.out.println(RollingStockDAO.findRollingStockByID(productID2).getProductName());
            // System.out.println("Test indRollingStocksByType Should be 2");
            // System.out.println(RollingStockDAO.findRollingStocksByType(RollingStockType.CARRIAGE).get(0).getProductName());
            // System.out.println("Test findRollingStocksByEra Should be 1&2");
            // int[] era4 = {3};
            // ArrayList<RollingStock> list = RollingStockDAO.findRollingStocksByEra(era4);
            // for (RollingStock roll : list) {
            //     System.out.println(roll.getProductName());
            // }
            // System.out.println("Test findRollingStocksByGauge Should be 2");
            // System.out.println(RollingStockDAO.findRollingStocksByGauge(Gauge.TT).get(0).getProductName());

            
            // RollingStockDAO.deleteRollingStock(productID1);
            // RollingStockDAO.deleteRollingStock(productID2);

            //Test BoxedSetDAO
            Product product = new Product(brand, "Test BoxedSet Insert1", "M022", 900.0, "A simple test case", 5);
            Product product1 = new Product(brand, "BoxedSet item1", "S874", 89.8, "A simple test case", 5);
            Product product2 = new Product(brand, "BoxedSet item2", "L584", 114.5, "A simple test case", 6);
            Product product3 = new Product(brand, "BoxedSet item3", "C5444", 114.5, "A simple test case", 6);
            int[] era1 = {1,3};
            int[] era2 = {2,3};

            RollingStock ro1 = new RollingStock(product1, "Wagon", "OO", era1);
            Locomotive lo1 = new Locomotive(product2, "TT", "Analogue",era2);
            Controller controller = new Controller(product3, true);

            RollingStockDAO.insertRollingStock(ro1);
            LocomotiveDAO.insertLocomotive(lo1);
            ControllerDAO.insertController(controller);

            int roID = ProductDAO.findIDByName("BoxedSet item1");
            ro1.setProductID(roID);
            int loID = ProductDAO.findIDByName("BoxedSet item2");
            lo1.setProductID(loID);
            int cID = ProductDAO.findIDByName("BoxedSet item3");
            controller.setProductID(cID);

            BoxedSet set = new BoxedSet(product, "Train Set");
            set.addProduct(RollingStockDAO.findRollingStockByID(roID), 2);
            set.addProduct(LocomotiveDAO.findLocomotiveByID(loID), 1);
            set.addProduct(ControllerDAO.findControllerByID(cID), 1);

            BoxedSetDAO.insertBoxedSet(set);
            BoxedSetDAO.findAllBoxedSet();
            BoxedSetDAO.findBoxedSetByType(BoxedType.TRACKPACK);
            int id = ProductDAO.findIDByName("Test BoxedSet Insert1");
            System.out.println(BoxedSetDAO.findBoxedSetByID(id).getBrand().getBrandName());

            BoxedSetDAO.deleteBoxedSet(id);
            RollingStockDAO.deleteRollingStock(roID);
            LocomotiveDAO.deleteLocomotive(loID);
            ControllerDAO.deleteController(cID);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseConnectionHandler.shutdown();
        }
    }
}