package com.project.zeidot.model;

import com.project.zeidot.db.DBConnection;
import com.project.zeidot.dto.BatchDetailsDto;
import com.project.zeidot.dto.FoodBatchDto;
import com.project.zeidot.dto.FoodDto;
import com.project.zeidot.util.CrudUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;

public class FoodBatchModel {
    private String nextBatchID;

    // Retrieve the latest batch ID from the database
    public String getCurrentBatchID() throws SQLException {
        ResultSet rs = CrudUtil.execute("SELECT FBId FROM foodBatch ORDER BY FBId DESC LIMIT 1");

        if (rs.next()) {
            System.out.println(nextBatchID == null ? "Null bn" : nextBatchID);
            return rs.getString(1); // Return the latest batch ID
        }
        System.out.println(nextBatchID == null ? "Null bn" : nextBatchID);
        return null; // Return null if no batches exist
    }

    // Generate the next batch ID based on the last batch ID in the database
    public String getNextBatchId() throws SQLException {
        ResultSet rs = CrudUtil.execute("SELECT FBId FROM foodBatch ORDER BY FBId DESC LIMIT 1");

        if (rs.next()) {
            String lastId = rs.getString(1); // Get the last batch ID
            if (lastId != null && lastId.length() > 1) {
                String substring = lastId.substring(1);  // Extract the numeric part (after 'B')
                try {
                    int i = Integer.parseInt(substring); // Convert the numeric part to an integer
                    int newIdIndex = i + 1; // Increment the number by 1
                    nextBatchID = String.format("B%03d", newIdIndex);
                    return nextBatchID; // Return the new batch ID in format Bnnn
                } catch (NumberFormatException e) {
                    throw new SQLException("Invalid batch ID format in the database: " + lastId);
                }
            }
        }
        nextBatchID = "B001"; // Set default batch ID if no records are found
        return nextBatchID;
    }

    // Set batch values and ensure `nextBatchID` is up-to-date
    public String setBatchValues(FoodBatchDto foodBatchDto) throws SQLException {
        // Generate a new batch ID to ensure it’s unique
        String batchId = getNextBatchId();
        System.out.println("Generated Batch ID: " + batchId);

        try {
            Connection connection = DBConnection.getInstance().getConnection();
            String query = "INSERT INTO foodBatch VALUES (?, ?, ?, ?,?)";
            PreparedStatement ps = connection.prepareStatement(query);

            // Use the generated batchId
            ps.setString(1, batchId);
            ps.setString(2, foodBatchDto.getFoodAmount()); // Assuming foodBatchDto provides the amount
            ps.setString(3, LocalDate.now().toString());
            ps.setString(4, "Available");
            ps.setString(5, LocalTime.now().toString());

            int rows = ps.executeUpdate();

            return rows > 0 ? batchId : null; // Return the batchId if successful
        } catch (SQLException e) {
            System.err.println("SQL Exception in setBatchValues: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public boolean setBatchDetailsValues(BatchDetailsDto dto) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        String query = "INSERT INTO foodBatchDetails VALUES (?, ?)"; //Food ID & Current BatchID
        PreparedStatement ps = connection.prepareStatement(query);

        ps.setString(1, dto.getFoodID());
        ps.setString(2, dto.getBatchId());

        int rows = ps.executeUpdate();
        return rows > 0;
    } //Set values for BatchDetails Associate Table

    public ArrayList<FoodBatchDto> getAllBatchDetails() throws SQLException {
        ResultSet rst = CrudUtil.execute("select * from foodBatch");

        ArrayList<FoodBatchDto> batchDtos = new ArrayList<>();

        while (rst.next()) {
            FoodBatchDto foodBatchDto = new FoodBatchDto(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4),
                    rst.getString(5)
            );
            batchDtos.add(foodBatchDto);
        }
        return batchDtos;
    }


    //Counting Average Time for Food Batch Expiration -----------------------------------------------------------------
//    public ArrayList<FoodBatchDto> getAllBatchFoodID(String batchID) throws SQLException {
//        Connection conn = DBConnection.getInstance().getConnection();
//        String query = "SELECT f.foodID,fbatch.FBId, f.foodName, f.FoodWeight, f.duration\n" +
//                "FROM food f\n" +
//                "JOIN foodbatchdetails fb ON f.foodID = fb.foodID\n" +
//                "JOIN foodbatch fbatch ON fb.FBId = fbatch.FBId\n" +
//                "WHERE fbatch.FBId = ?";
//        PreparedStatement ps = conn.prepareStatement(query);
//        ps.setString(1, batchID);
//        ResultSet rs = ps.executeQuery();
//        ArrayList<FoodBatchDto> FoodBatchDtos = new ArrayList<>();
//        while (rs.next()) {
//            FoodBatchDto foodBatchDto = new FoodBatchDto();
//            foodBatchDto.setFoodBatchId(rs.getString(1));
//            foodBatchDto.setFoodAmount(rs.getString(2));
//            foodBatchDto.setDate(rs.getString(3));
//            foodBatchDto.setIsAvailable(rs.getString(4));
//            foodBatchDto.setDuration(rs.getString(5));
//            FoodBatchDtos.add(foodBatchDto);
//        }
//        return FoodBatchDtos;
//    }
//    public int countAverageTime(ArrayList<FoodBatchDto> foodBatchDtos) throws SQLException {
//        int averageTime = 10;
//        for (int i = 0; i < foodBatchDtos.size(); i++) {
//            if (Integer.parseInt(foodBatchDtos.get(i).getDuration()) < averageTime) {
//                averageTime = Integer.parseInt(foodBatchDtos.get(i).getDuration());
//                System.out.println(Integer.parseInt(foodBatchDtos.get(i).getDuration()) + " hehee");
//            }
//        }
//        return averageTime;
//    }

    //Food Batch Expire Time Methods
    public boolean updateFoodBatchTime(LocalTime newTime , String batchID) throws SQLException {
        Connection conn = DBConnection.getInstance().getConnection();
        String query = "UPDATE foodBatch SET batchDuration= ? WHERE FBId = ?";
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setObject(1, newTime);
        ps.setString(2, batchID);
        int rows = ps.executeUpdate();
        return rows > 0;
    }
    public LocalTime checkTime(LocalTime time, String batchID) throws SQLException {
        Connection conn = DBConnection.getInstance().getConnection();
        LocalTime newTime = time;

        String query = "SELECT batchDuration, FBId " +
                "FROM foodBatch " +
                "WHERE FBId = ? " +
                "ORDER BY batchDuration DESC " +
                "LIMIT 1";

        PreparedStatement ps = conn.prepareStatement(query);
        ps.setString(1, batchID);

        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            // Parse the `batchDuration` value from the database
            LocalTime dbBatchDuration = rs.getTime("batchDuration").toLocalTime();

            // Compare the times and update `newDate`
            if (time.isAfter(dbBatchDuration)) {
                newTime = time;
            } else {
                newTime = dbBatchDuration;
            }
        }
        System.out.println(newTime);
        return newTime;
    }
    //Food Batch Expire Time Methods End


    //Delete Batch Methods
    public boolean deleteBatch(String batchID) throws SQLException {
        Connection conn = DBConnection.getInstance().getConnection();
        String query = "DELETE FROM foodBatch WHERE FBId = ?";
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setString(1, batchID);
        int rows = ps.executeUpdate();
        return rows > 0;
    }
    public void deleteFoodsOfDeletedBatch(String batchID) throws SQLException {
        Connection conn = DBConnection.getInstance().getConnection();
        String query = "SELECT foodID FROM foodBatchDetails where FBId = ? ORDER BY FBId";
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setString(1 , batchID);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            String foodID = rs.getString("foodID");
            deleteFoodOnebyOne(foodID);
            System.out.println("deleted : " + foodID);
        }
    }
    public void deleteFoodOnebyOne(String foodID) throws SQLException {
        Connection conn = DBConnection.getInstance().getConnection();
        String query = "DELETE FROM food WHERE foodID = ?";
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setString(1, foodID);
        int rows = ps.executeUpdate();
        if (rows > 0) {
            System.out.println("shariiii");
        }
    }
    //Delete Batch Methods END

}
