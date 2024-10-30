package org.habitApp.servlets.userServlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.habitApp.annotations.Loggable;
import org.habitApp.domain.dto.userDto.UserDto;
import org.habitApp.mappers.UserMapper;
import org.habitApp.domain.entities.UserEntity;
import org.habitApp.repositories.UserRepository;
import org.habitApp.services.UserService;
import org.mapstruct.factory.Mappers;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Сервлет удаления пользователя админом
 */
@Loggable
@WebServlet("/admin/user/delete")
public class DeleteUserServlet extends HttpServlet {
    private final UserService userService;
    private final ObjectMapper objectMapper;
    private final UserMapper userMapper;

    public DeleteUserServlet() {
        userService = new UserService(new UserRepository());
        this.objectMapper = new ObjectMapper();
        this.objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        this.userMapper = Mappers.getMapper(UserMapper.class);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            UserEntity currentUser = (UserEntity) req.getSession().getAttribute("user");

            UserDto userDto = objectMapper.readValue(req.getInputStream(), UserDto.class);
            userService.deleteUser(userDto.getId(), currentUser);

            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write("User deleted successfully.");

        } catch (IllegalAccessException e) {
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            resp.getWriter().write("Access denied: " + e.getMessage());

        } catch (SQLException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("Database error: " + e.getMessage());
        }
    }
}