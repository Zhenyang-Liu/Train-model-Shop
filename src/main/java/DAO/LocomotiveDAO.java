package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import model.Era;
import model.Gauge;
import model.Locomotive;
import model.Locomotive.DCCType;

public class LocomotiveDAO extends ProductDAO {

    LocomotiveDAO(Connection connection) {
        super(connection);
    }

    public void insertLocomotive(Locomotive locomotive) {
        
    }

    public void updateLocomotive(Locomotive locomotive) {

    }

    public void deleteLocomotive(int productID) {

    }

    public ArrayList<Locomotive> findLocomotivesByGauge(Gauge gauge) {
        
    }

    public ArrayList<Locomotive> findLocomotivesByEra(ArrayList<Era> eraList) {
        
    }

    public ArrayList<Locomotive> findLocomotivesByDCCType(DCCType dccType) {
        
    }
    
}
