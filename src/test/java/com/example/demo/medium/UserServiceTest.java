package com.example.demo.medium;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;

import com.example.demo.user.domain.User;
import com.example.demo.user.exception.CertificationCodeNotMatchedException;
import com.example.demo.user.exception.ResourceNotFoundException;
import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.domain.UserCreate;
import com.example.demo.user.domain.UserUpdate;
import com.example.demo.user.service.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.jdbc.SqlGroup;

@SpringBootTest
@TestPropertySource(locations = "classpath:test-application.properties")
@SqlGroup({
    @Sql(value = "/sql/user-service-test-data.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD),
    @Sql(value = "/sql/delete-all-data.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
})
public class UserServiceTest {
  @Autowired
  private UserServiceImpl userService;
  @MockBean
  private JavaMailSender javaMailSender;

  @Test
  void getByEmail은_ACTIVE_상태인_유저를_찾아올_수_있다(){
    //given
    String email = "enaenen@naver.com";

    //when
    User result = userService.getByEmail(email);

    //then
    assertThat(result.getNickname()).isEqualTo("enaenen");
  }

  @Test
  void getByEmail은_PENDING_상태인_유저를_찾아올_수_없다(){
    //given
    String email = "space@naver.com";

    //when
    //then
    assertThatThrownBy(() -> userService.getByEmail(email))
        .isInstanceOf(ResourceNotFoundException.class);
  }

  @Test
  void getById는_ACTIVE_상태인_유저를_찾아올_수_있다(){
    //given
    //when
    User result = userService.getById(1L);

    //then
    assertThat(result.getNickname()).isEqualTo("enaenen");
  }

  @Test
  void getById는_PENDING_상태인_유저를_찾아올_수_없다(){
    //given
    //when
    //then
    assertThatThrownBy(() -> userService.getById(2L))
        .isInstanceOf(ResourceNotFoundException.class);
  }


  @Test
  void userCreateDto_를_이용하여_유저를_생성할_수_있다(){
    //given
    UserCreate userCreateDto = UserCreate.builder()
        .email("enaenen@naver.com")
        .address("서울시 강남구")
        .nickname("enaenen")
        .build();
    BDDMockito.doNothing().when(javaMailSender).send(any(SimpleMailMessage.class));

    //when
    User result = userService.create(userCreateDto);
    //then
    assertThat(result.getId()).isNotNull();
    assertThat(result.getStatus()).isEqualTo(UserStatus.PENDING);
//    assertThat(result.getCertificationCode()).isEqualTo("???");
  }


  @Test
  void userCreateDto_를_이용하여_유저를_수정할_수_있다(){
    //given
    UserUpdate userUpdate = UserUpdate.builder()
        .address("경기도 성남시")
        .nickname("enaenen-1")
        .build();

    //when
   userService.update(1, userUpdate);

    //then
    User user = userService.getById(1);
    assertThat(user.getId()).isNotNull();
    assertThat(user.getAddress()).isEqualTo("경기도 성남시");
    assertThat(user.getNickname()).isEqualTo("enaenen-1");
  }


  @Test
  void user를_로그인_시키면_마지막_로그인_시간이_변경된다(){
    //given
    //when
    userService.login(1);

    //then
    User userEntity = userService.getById(1);
    assertThat(userEntity.getLastLoginAt()).isGreaterThan(0L);
//    assertThat(userEntity.getLastLoginAt()).isEqualTo("..."); // fixme
  }


  @Test
  void PENDING_상태의_사용자는_인증_코드로_ACTIVE_시킬_수_있다(){
    //given
    //when
    userService.verifyEmail(2,"aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab");

    //then
    User user = userService.getById(2);
    assertThat(user.getStatus()).isEqualTo(UserStatus.ACTIVE);
//    assertThat(userEntity.getLastLoginAt()).isEqualTo("..."); // fixme
  }

  @Test
  void PENDING_상태의_사용자는_잘못된_인증_코드를_받으면_에러를_던진다(){
    //given
    //when
    //then
    assertThatThrownBy(() -> {
      userService.verifyEmail(2,"aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaC");
    }).isInstanceOf(CertificationCodeNotMatchedException.class);
  }
}
