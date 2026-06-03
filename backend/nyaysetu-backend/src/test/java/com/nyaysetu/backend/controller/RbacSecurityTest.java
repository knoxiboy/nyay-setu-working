package com.nyaysetu.backend.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class RbacSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "litigant@example.com", roles = {"LITIGANT"})
    public void shouldDenyLitigantAccessToJudgeEndpoint() throws Exception {
        mockMvc.perform(get("/api/v1/judge/cases"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "litigant@example.com", roles = {"LITIGANT"})
    public void shouldDenyLitigantAccessToAdminEndpoint() throws Exception {
        mockMvc.perform(get("/api/v1/cases/pending-assignment"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "lawyer@example.com", roles = {"LAWYER"})
    public void shouldDenyLawyerAccessToJudgeEndpoint() throws Exception {
        mockMvc.perform(get("/api/v1/judge/cases"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "lawyer@example.com", roles = {"LAWYER"})
    public void shouldDenyLawyerAccessToAdminEndpoint() throws Exception {
        mockMvc.perform(get("/api/v1/cases/pending-assignment"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "litigant@example.com", roles = {"LITIGANT"})
    public void shouldAllowLitigantAccessToLitigantEndpointButGetNotFound() throws Exception {
        // Litigant has the role, so request shouldn't be Forbidden (403), it should be 404 (NotFound) if endpoint isn't mocked.
        mockMvc.perform(get("/api/v1/client/fir/non-existent"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "lawyer@example.com", roles = {"LAWYER"})
    public void shouldAllowLawyerAccessToLawyerEndpointButGetNotFound() throws Exception {
        // Lawyer has the role, so request shouldn't be Forbidden (403), it should be 404 (NotFound) if endpoint isn't mocked.
        mockMvc.perform(get("/api/v1/lawyer/non-existent"))
                .andExpect(status().isNotFound());
    }
}
