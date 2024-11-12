package com.project.zeidot.model;

import com.project.zeidot.db.DBConnection;
import com.project.zeidot.dto.UserDto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginModel {
    public boolean login(UserDto userDto) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement statement = connection.prepareStatement("select * from user where username = ? and password = ?");
        statement.setString(1, userDto.getUsername());
        statement.setString(2, userDto.getPassword());
        ResultSet resultSet = statement.executeQuery();
        UserDto dto = new UserDto();
        while (resultSet.next()) {
            String username = resultSet.getString("userName");
            String password = resultSet.getString("password");
            if (username.equals(userDto.getUsername()) && password.equals(userDto.getPassword())) {
                dto.setUsername(username);
                dto.setPassword(password);
                return true;
            }
        }
        return false;
    }
}
