package com.project.zeidot.model.popups;

import com.project.zeidot.db.DBConnection;
import com.project.zeidot.dto.FoodBatchDetailsDto;
import com.project.zeidot.dto.FoodBatchDto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class FoodBatchSelectModel {
    public ArrayList<FoodBatchDto> getFoodBatchDetails() throws SQLException {
        Connection con = DBConnection.getInstance().getConnection();
        String query = "SELECT * FROM foodBatch WHERE isAvailable = 'Available'";
        PreparedStatement ps = con.prepareStatement(query);

        ArrayList<FoodBatchDto> detailList = new ArrayList<>();
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            FoodBatchDto dto = new FoodBatchDto();
            dto.setFoodBatchId(rs.getString(1));
            dto.setFoodAmount(rs.getString(2));
            dto.setDate(rs.getString(3));
            dto.setIsAvailable(rs.getString(4));
            detailList.add(dto);
        }
        return detailList;
    }

    public boolean changeAvailability(String batchID) throws SQLException {
        Connection conn = DBConnection.getInstance().getConnection();
        String query = "UPDATE foodBatch SET isAvailable = 'UnAvailable' WHERE FBId = ?";
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setString(1, batchID);
        return ps.executeUpdate() > 0;
    }
    public boolean changeToAvailable(String batchID) throws SQLException {
        Connection conn = DBConnection.getInstance().getConnection();
        String query = "UPDATE foodBatch SET isAvailable = 'Available' WHERE FBId = ?";
        if (!batchID.equals(null)) {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, batchID);
            return ps.executeUpdate() > 0;
        }
        return false;
    }
}
