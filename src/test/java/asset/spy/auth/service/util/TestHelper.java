package asset.spy.auth.service.util;

import asset.spy.auth.service.config.IntegrationBaseTest;
import asset.spy.auth.service.dto.request.LoginRequestDto;
import asset.spy.auth.service.dto.request.RegisterRequestDto;
import asset.spy.auth.service.dto.response.JwtResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TestHelper extends IntegrationBaseTest {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    public TestHelper(MockMvc mockMvc, ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    public void registerUserWithValidData(RegisterRequestDto registerRequestDto) throws Exception {
        mockMvc.perform(post("/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequestDto)))
                .andExpect(status().isOk());
    }

    public JwtResponseDto authenticateUserWithValidData(LoginRequestDto loginRequestDto, String userAgent) throws Exception {
        MvcResult result = mockMvc.perform(post("/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("User-Agent", userAgent)
                        .content(objectMapper.writeValueAsString(loginRequestDto)))
                .andExpect(status().isOk())
                .andReturn();
        return objectMapper.readValue(result.getResponse().getContentAsString(), JwtResponseDto.class);
    }

    public JwtResponseDto registerAndAuthenticateUserWithValidData(String userAgent) throws Exception {
        registerUserWithValidData(registerRequest);
        return authenticateUserWithValidData(loginRequest, userAgent);
    }
}
