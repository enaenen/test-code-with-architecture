package com.example.demo.medium;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.infrastructure.UserEntity;
import com.example.demo.user.infrastructure.UserJpaRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest(showSql = true)
@Sql("/sql/user-repository-test-data.sql")
public class UserEntityJpaRepositoryTest {

  @Autowired
  private UserJpaRepository userJpaRepository;


  @Test
  void findByIdAndStatus_로_유저를_찾을_수_있다() {
    // given

    // when
    Optional<UserEntity> result = userJpaRepository.findByIdAndStatus(1, UserStatus.ACTIVE);
    // then
    assertThat(result.isPresent()).isTrue();
  }

  @Test
  void findByIdAndStatus_는_데이터가_없으면_Optional_Empty_를_내려준다() {
    // given

    // when
    Optional<UserEntity> result = userJpaRepository.findByIdAndStatus(1, UserStatus.PENDING);
    // then
    assertThat(result.isEmpty()).isTrue();
  }


  @Test
  void findByEmailAndStatus_로_유저를_찾을_수_있다() {
    // given

    // when
    Optional<UserEntity> result = userJpaRepository.findByEmailAndStatus("enaenen@naver.com", UserStatus.ACTIVE);
    // then
    assertThat(result.isPresent()).isTrue();
  }

  @Test
  void findByEmailAndStatus_는_데이터가_없으면_Optional_Empty_를_내려준다() {
    // given

    // when
    Optional<UserEntity> result = userJpaRepository.findByEmailAndStatus("enaenen@naver.com", UserStatus.PENDING);
    // then
    assertThat(result.isEmpty()).isTrue();
  }

}
