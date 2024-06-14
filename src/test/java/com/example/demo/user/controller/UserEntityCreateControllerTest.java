package com.example.demo.user.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.demo.user.domain.UserCreate;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@SqlGroup({
		@Sql(value = "/sql/delete-all-data.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
})
class UserEntityCreateControllerTest {

	@Autowired
	private MockMvc mockMvc;
	private final ObjectMapper objectMapper = new ObjectMapper();
	@MockBean
	private JavaMailSender javaMailSender;

	@Test
	void 사용자는_회원_가입을_할_수_있고_회원가입된_사용자는_PENDING_상태이다() throws Exception {
		//given
		UserCreate userCreateDto = UserCreate.builder()
				.nickname("enaenen")
				.email("enaenen@naver.com")
				.address("Pangyo")
				.build();
		BDDMockito.doNothing().when(javaMailSender).send(any(SimpleMailMessage.class));
		//when
		//then
		mockMvc.perform(post("/api/users")
						.header("EMAIL", "enaenen@naver.com")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(userCreateDto))
				)
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").value(1))
				.andExpect(jsonPath("$.email").value("enaenen@naver.com"))
				.andExpect(jsonPath("$.nickname").value("enaenen"))
				.andExpect(jsonPath("$.status").value("PENDING"));
	}


}