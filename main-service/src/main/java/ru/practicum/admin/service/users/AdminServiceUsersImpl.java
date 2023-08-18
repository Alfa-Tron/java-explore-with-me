package ru.practicum.admin.service.users;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.admin.repository.UserRepository;
import ru.practicum.dto.User.UserDto;
import ru.practicum.dto.mapper.user.UserMapper;
import ru.practicum.exeptions.ObjectNotFoundException;
import ru.practicum.model.User;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminServiceUsersImpl implements AdminServiceUsers {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public UserDto createUser(User user) {
        User user1 = userRepository.save(user);
        return userMapper.userToDto(user1);
    }

    @Override
    public List<UserDto> getUsers(List<Long> ids, Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from, size, Sort.by(Sort.Direction.ASC, "id"));
        List<User> users = userRepository.getAllUsers(ids, pageable);
        return userMapper.usersToDtos(users);

    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        userRepository.findById(userId).orElseThrow(
                () -> new ObjectNotFoundException(String.format("User with id=%s was not found", userId)));
        userRepository.deleteById(userId);
    }
}
