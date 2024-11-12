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
import java.util.ArrayList;

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
            String query = "INSERT INTO foodBatch VALUES (?, ?, ?)";
            PreparedStatement ps = connection.prepareStatement(query);

            // Use the generated batchId
            ps.setString(1, batchId);
            ps.setString(2, foodBatchDto.getFoodAmount()); // Assuming foodBatchDto provides the amount
            ps.setString(3, LocalDate.now().toString());

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
        String query = "INSERT INTO foodBatchDetails VALUES (?, ?)";
        PreparedStatement ps = connection.prepareStatement(query);

        ps.setString(1, dto.getFoodID());
        ps.setString(2, dto.getBatchId()); // Ensure this ID exists in foodBatch

        int rows = ps.executeUpdate();
        return rows > 0;
    }

    public ArrayList<FoodBatchDto> getAllBatchDetails() throws SQLException {
        ResultSet rst = CrudUtil.execute("select * from foodBatch");

        ArrayList<FoodBatchDto> batchDtos = new ArrayList<>();

        while (rst.next()) {
            FoodBatchDto foodBatchDto = new FoodBatchDto(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3)
            );
            batchDtos.add(foodBatchDto);
        }
        return batchDtos;
    }



}
