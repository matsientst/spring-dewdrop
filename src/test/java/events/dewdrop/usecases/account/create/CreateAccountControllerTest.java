package events.dewdrop.usecases.account.create;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.endsWith;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.UUID;
import events.dewdrop.message.command.account.CreateAccountCommand;
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
class CreateAccountControllerTest {
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    CreateAccountController createAccountController;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders
            .standaloneSetup(createAccountController)
            .build();
    }

    @Test
    @DisplayName("createAccount() - Given a valid CreateAccountCommand, should return a 201 status code with a Location header of the created account UUID")
    void createAccount() throws Exception {
        CreateAccountCommand createAccountCommand = new CreateAccountCommand(UUID.randomUUID(), UUID.randomUUID(), new BigDecimal(123432));
        String json = objectMapper.writeValueAsString(createAccountCommand);
        MvcResult mvcResult = this.mockMvc.perform(patch("/accounts/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .characterEncoding("UTF-8"))
            .andDo(print())
            .andExpect(status().is(201))
            .andReturn();

        String result = mvcResult.getResponse()
            .getHeader("Location");
        assertThat(result, endsWith(createAccountCommand.getAccountId()
            .toString()));
    }
}