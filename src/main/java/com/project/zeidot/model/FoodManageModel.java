package com.project.zeidot.model;

import com.project.zeidot.db.DBConnection;
import com.project.zeidot.dto.FoodDto;
import com.project.zeidot.util.CrudUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class FoodManageModel {
    public boolean updateFood(FoodDto food) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement ps = connection.prepareStatement("UPDATE food SET foodWeight = ?, FoodName = ? , duration = ? WHERE foodID = ?");
        ps.setString(1 , food.getWeight());
        ps.setString(2 , food.getFoodName());
        ps.setString(3 , food.getDuration());
        ps.setString(4 , food.getFoodID());
        int rows = ps.executeUpdate();
        return rows > 0;
    }
    public boolean deleteFood(String name) throws SQLException {
        Connection con = DBConnection.getInstance().getConnection();
        PreparedStatement ps = con.prepareStatement("DELETE FROM food WHERE foodID = ?");
        ps.setString(1, name);
        return ps.executeUpdate() > 0;
    }
    public boolean saveFood(FoodDto dto) throws SQLException {
        Connection conn = DBConnection.getInstance().getConnection();
        PreparedStatement ps = conn.prepareStatement("insert into food values(?,?,?,?)");
        ps.setString(1 , dto.getFoodID());
        ps.setString(2 , dto.getWeight());
        ps.setString(3 , dto.getFoodName());
        ps.setString(4 , dto.getDuration());
        int rows = ps.executeUpdate();
        return rows > 0;
    }
    public String getNextCustomerId() throws SQLException {
        ResultSet rs = CrudUtil.execute("SELECT foodID FROM food ORDER BY foodID DESC LIMIT 1");

        if (rs.next()) {
            String lastId = rs.getString(1); // Get the last customer ID
            if (lastId != null && lastId.length() > 1) {
                // Extract the numeric part, make sure there is at least one character after 'C'
                String substring = lastId.substring(1); // Extract the numeric part (after 'C')
                try {
                    int i = Integer.parseInt(substring); // Convert the numeric part to an integer
                    int newIdIndex = i + 1; // Increment the number by 1
                    return String.format("F%03d", newIdIndex); // Return the new customer ID in format Cnnn
                } catch (NumberFormatException e) {
                    // Handle cases where the numeric part is invalid
                    throw new SQLException("Invalid customer ID format in the database: " + lastId);
                }
            }
        }
        // Return default customer ID if no records are found or ID is improperly formatted
        return "F001";
    }
//    public boolean batchDetailsAdd(String foodID , String batchID) throws SQLException {
//        Connection con = DBConnection.getInstance().getConnection();
//    }

    public ArrayList<FoodDto> getAllCustomers() throws SQLException {
        ResultSet rst = CrudUtil.execute("select * from food");

        ArrayList<FoodDto> foodDtos = new ArrayList<>();

        while (rst.next()) {
            FoodDto customerDTO = new FoodDto(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4)
            );
            foodDtos.add(customerDTO);
        }
        return foodDtos;
    }

    //food Batch amount change Mehthods
    public double getCurrentWeight(String FBId) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        String query = "SELECT foodAmount FROM foodBatch WHERE FBId = ?";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setString(1, FBId);
        ps.executeQuery();
        ResultSet rst = ps.getResultSet();
        if (rst.next()) {
            System.out.println(rst.getString(1) + "Current weight returned");
            return Double.parseDouble(rst.getString(1));
        }
        return 0;
    }

    public boolean updateAmount(double CurrentWeight , double foodWeight) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        String query = "UPDATE foodBatch SET FoodAmount = ? WHERE FBId = ?";
        PreparedStatement ps = connection.prepareStatement(query);
        double w = CurrentWeight + foodWeight;
        String lastWeight = String.valueOf(w);
        System.out.println(lastWeight);
        ps.setString(1, lastWeight);
        ps.setString(2 , getCurrentBatchID());
        int rows = ps.executeUpdate();
        return rows > 0;
    }

    public boolean decreaseAmount(double CurrentWeight , double foodWeight) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        if (CurrentWeight > 0) {
            String query = "UPDATE foodBatch SET FoodAmount = ? WHERE FBId = ?";
            PreparedStatement ps = connection.prepareStatement(query);
            double w = CurrentWeight - foodWeight;
            String lastWeight = String.valueOf(w);
            System.out.println(lastWeight);
            ps.setString(1, lastWeight);
            ps.setString(2, getCurrentBatchID());
            int rows = ps.executeUpdate();
            return rows > 0;
        }
        return false;
    }

    public String getCurrentBatchID() throws SQLException {
        ResultSet rs = CrudUtil.execute("SELECT FBId FROM foodBatch ORDER BY FBId DESC LIMIT 1");

        if (rs.next()) {
            return rs.getString(1); // Return the latest batch ID
        }
        return null; // Return null if no batches exist
    }
    //food Batch amount change Mehthods
}
