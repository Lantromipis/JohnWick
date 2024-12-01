package ru.ifmo.se.johnwick.service.impl;

import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import ru.ifmo.se.johnwick.exception.EntityNotFoundByIdException;
import ru.ifmo.se.johnwick.exception.ValidationException;
import ru.ifmo.se.johnwick.model.dto.UserDto;
import ru.ifmo.se.johnwick.model.entity.UserEntity;
import ru.ifmo.se.johnwick.repository.UserRepository;
import ru.ifmo.se.johnwick.service.api.UserService;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class UserServiceImplTest {
    @Inject
    UserService userService;

    @InjectMock
    UserRepository userRepository;

    private static final String EXISTING_USERNAME = "test";
    private static final String NEW_USER_DATA = "new";

    @Test
    void testCreateUser() {
        UserDto newUser = new UserDto();

        newUser.setUsername(NEW_USER_DATA);
        newUser.setPassword(NEW_USER_DATA);
        newUser.setDisplayName(NEW_USER_DATA);

        UserDto createdUser = userService.createUser(newUser);
        // returned dto must not contain password
        assertNull(createdUser.getPassword());

        // service must call repository with hashed password
        ArgumentCaptor<UserEntity> captor = ArgumentCaptor.forClass(UserEntity.class);
        Mockito.verify(userRepository).persistAndFlush(captor.capture());
        assertTrue(BcryptUtil.matches(NEW_USER_DATA, captor.getValue().getPassword()));
    }

    @Test
    void testCreateUserWithExistingUsername() {
        UserEntity existingUser = new UserEntity();
        existingUser.setUsername(EXISTING_USERNAME);
        existingUser.setPassword(EXISTING_USERNAME);

        Mockito.when(userRepository.findByUsername(EXISTING_USERNAME)).thenReturn(existingUser);

        UserDto newUser = new UserDto();
        newUser.setUsername(EXISTING_USERNAME);
        newUser.setPassword(EXISTING_USERNAME);
        newUser.setDisplayName(NEW_USER_DATA);

        assertThrows(ValidationException.class, () -> userService.createUser(newUser));
    }

    @Test
    void testCreateUserPayloadValidations() {
        UserDto newUser = new UserDto();

        assertThrows(ValidationException.class, () -> userService.createUser(newUser));

        newUser.setUsername(NEW_USER_DATA);
        assertThrows(ValidationException.class, () -> userService.createUser(newUser));

        newUser.setPassword(NEW_USER_DATA);
        assertThrows(ValidationException.class, () -> userService.createUser(newUser));

        newUser.setDisplayName(NEW_USER_DATA);
        assertDoesNotThrow(() -> userService.createUser(newUser));
    }

    @Test
    void testUpdateUser() {
        UUID userId = UUID.randomUUID();

        UserEntity existingUser = new UserEntity();
        existingUser.setId(userId);

        Mockito.when(userRepository.findById(userId)).thenReturn(existingUser);

        UserDto userToUpdate = new UserDto();
        userToUpdate.setId(userId);
        userToUpdate.setUsername(NEW_USER_DATA);
        userToUpdate.setPassword(NEW_USER_DATA);
        userToUpdate.setDisplayName(NEW_USER_DATA);

        UserDto updatedUser = userService.updateUser(userToUpdate);

        assertEquals(updatedUser.getId(), userToUpdate.getId());
        assertEquals(updatedUser.getUsername(), userToUpdate.getUsername());
        assertEquals(updatedUser.getDisplayName(), userToUpdate.getDisplayName());

        // returned dto must not contain password
        assertNull(updatedUser.getPassword());

        // service must call repository with hashed password
        ArgumentCaptor<UserEntity> captor = ArgumentCaptor.forClass(UserEntity.class);
        Mockito.verify(userRepository).persistAndFlush(captor.capture());
        assertTrue(BcryptUtil.matches(NEW_USER_DATA, captor.getValue().getPassword()));
    }

    @Test
    void testUpdateUnknownUser() {
        UserDto userToUpdate = new UserDto();
        userToUpdate.setId(UUID.randomUUID());
        userToUpdate.setUsername(NEW_USER_DATA);

        Mockito.when(userRepository.findById(Mockito.any())).thenReturn(null);

        assertThrows(EntityNotFoundByIdException.class, () -> userService.updateUser(userToUpdate));
    }

    @Test
    void testUpdateUserWithExistingUsername() {
        UUID userId = UUID.randomUUID();

        UserEntity existingUser = new UserEntity();
        existingUser.setId(userId);

        Mockito.when(userRepository.findById(userId)).thenReturn(existingUser);

        UserEntity existingUserWithUsername = new UserEntity();
        existingUserWithUsername.setUsername(EXISTING_USERNAME);
        Mockito.when(userRepository.findByUsername(EXISTING_USERNAME)).thenReturn(existingUserWithUsername);

        UserDto userToUpdate = new UserDto();
        userToUpdate.setId(userId);
        userToUpdate.setUsername(EXISTING_USERNAME);
        userToUpdate.setPassword(NEW_USER_DATA);
        userToUpdate.setDisplayName(NEW_USER_DATA);

        assertThrows(ValidationException.class, () -> userService.updateUser(userToUpdate));
    }
}