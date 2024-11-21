package com.project.zeidot.model.popups;

import com.project.zeidot.db.DBConnection;
import com.project.zeidot.dto.DonationDto;
import com.project.zeidot.dto.FoodBankDto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SelectMailModel {

    public ArrayList<FoodBankDto> getFoodBankDetails() throws SQLException {
        Connection con = DBConnection.getInstance().getConnection();
        String query = "SELECT FBName , emailAddress FROM foodBank";
        PreparedStatement ps = con.prepareStatement(query);

        ArrayList<FoodBankDto> detailList = new ArrayList<>();
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            FoodBankDto dto = new FoodBankDto();
            dto.setFBKName(rs.getString("FBName"));
            dto.setFBKEmail(rs.getString("emailAddress"));
            detailList.add(dto);
        }
        return detailList;
    }
}
