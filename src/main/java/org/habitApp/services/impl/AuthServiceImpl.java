package org.habitApp.services.impl;

import lombok.RequiredArgsConstructor;
import org.habitApp.auth.JwtUtil;
import org.habitApp.domain.dto.userDto.UserDtoLogin;
import org.habitApp.domain.dto.userDto.UserDtoRegisterUpdate;
import org.habitApp.domain.entities.UserEntity;
import org.habitApp.exceptions.InvalidCredentialsException;
import org.habitApp.exceptions.UserAlreadyExistsException;
import org.habitApp.mappers.UserMapper;
import org.habitApp.repositories.UserRepository;
import org.habitApp.repositories.impl.UserRepositoryImpl;
import org.habitApp.services.AuthService;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.Optional;

/**
 * Сервис для аутентификации пользователей.
 * Содержит бизнес-логику для регистрации, аутентификации пользователей.
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;

    /**
     * Регистрация нового пользователя.
     *
     * @param userDtoRegisterUpdate Данные для регистрации пользователя
     * @throws UserAlreadyExistsException Если пользователь с таким email уже существует
     * @throws SQLException В случае ошибок при работе с базой данных
     */
    @Override
    public void registerUser(UserDtoRegisterUpdate userDtoRegisterUpdate) throws UserAlreadyExistsException, SQLException {
        if (userRepository.findByEmail(userDtoRegisterUpdate.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("User already exists.");
        }
        UserEntity user = userMapper.userDtoRegisterUpdateToUser(userDtoRegisterUpdate);

        userRepository.create(user);
    }

    /**
     * Вход пользователя в систему.
     *
     * @param userDtoLogin Данные для входа (email и пароль)
     * @return Данные пользователя после успешного входа
     * @throws SQLException В случае ошибок при работе с базой данных
     * @throws InvalidCredentialsException Если указаны неверные учетные данные
     */
    @Override
    public String loginUser(UserDtoLogin userDtoLogin) throws SQLException, InvalidCredentialsException {
        Optional<UserEntity> user = userRepository.findByEmail(userDtoLogin.getEmail());
        if (user.isPresent() && user.get().getPassword().equals(userDtoLogin.getPassword())) {
            return jwtUtil.generate(user.get().getEmail());
        }
        throw new InvalidCredentialsException("Invalid email or password.");
    }
}
