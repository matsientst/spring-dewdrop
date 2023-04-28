package events.dewdrop.usecases.user.create;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.endsWith;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.UUID;
import events.dewdrop.message.command.user.CreateUserCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@SpringBootTest
class CreateUserControllerTest {
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    CreateUserController createUserController;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders
            .standaloneSetup(createUserController)
            .build();
    }

    @Test
    @DisplayName("createUser() - Given a valid CreateUserCommand, should return a 201 status code with a Location header of the created user UUID")
    void createUser() throws Exception {
        CreateUserCommand createUserCommand = new CreateUserCommand(UUID.randomUUID(), "test username", "test@test.com");
        String json = objectMapper.writeValueAsString(createUserCommand);
        MvcResult mvcResult = this.mockMvc.perform(patch("/users/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .characterEncoding("UTF-8"))
            .andDo(print())
            .andExpect(status().is(201))
            .andReturn();

        String result = mvcResult.getResponse()
            .getHeader("Location");
        assertThat(result, endsWith(createUserCommand.getUserId().toString()));
    }
}