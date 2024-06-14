package com.example.demo.user.service;

import com.example.demo.common.service.ClockHolder;
import com.example.demo.common.service.UuidHolder;
import com.example.demo.user.controller.port.UserService;
import com.example.demo.user.domain.User;
import com.example.demo.user.exception.ResourceNotFoundException;
import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.domain.UserCreate;
import com.example.demo.user.domain.UserUpdate;
import com.example.demo.user.service.port.UserRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Builder
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final CertificationService certificationService;
	private final UuidHolder uuidHolder;
	private final ClockHolder clockHolder;

	@Override
	public User getByEmail(String email) {
		return userRepository.findByEmailAndStatus(email, UserStatus.ACTIVE)
				.orElseThrow(() -> new ResourceNotFoundException("Users", email));
	}

	@Override

	public User getById(long id) {
		return userRepository.findByIdAndStatus(id, UserStatus.ACTIVE)
				.orElseThrow(() -> new ResourceNotFoundException("Users", id));
	}

	@Override
	@Transactional
	public User create(UserCreate userCreate) {
		User user = User.from(userCreate, uuidHolder);
		user = userRepository.save(user);
		certificationService.send(userCreate.getEmail(), user.getId(), user.getCertificationCode());
		return user;
	}

	@Override
	@Transactional
	public User update(long id, UserUpdate userUpdate) {
		User user = getById(id);
		user = user.update(userUpdate);
		user = userRepository.save(user);
		return user;
	}

	@Override
	@Transactional
	public void login(long id) {
		User user = userRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Users", id));
		user = user.login(clockHolder);
		userRepository.save(user);//JPA와의 의존성 끊어짐(따로 저장 필요)
	}

	@Override
	@Transactional
	public void verifyEmail(long id, String certificationCode) {
		User user = userRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Users", id));
		user = user.certificate(certificationCode);
		userRepository.save(user);
	}
}