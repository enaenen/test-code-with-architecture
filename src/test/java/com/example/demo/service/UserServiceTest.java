package com.example.demo.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.UserEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.jdbc.SqlGroup;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
@SqlGroup({
    @Sql(value = "/sql/user-service-test-data.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD),
    @Sql(value = "/sql/delete-all-data.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
})
public class UserServiceTest {
  @Autowired
  private UserService userService;

  @Test
  void getByEmail은_ACTIVE_상태인_유저를_찾아올_수_있다(){
    //given
    String email = "enaenen@naver.com";

    //when
    UserEntity result = userService.getByEmail(email);

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

}
