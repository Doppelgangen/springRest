import com.Application;
import com.model.User;
import com.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
public class MainControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;

    @Test
    public void shouldReturnOKMessage() throws Exception {
        mockMvc.perform(get("/")).andExpect(status().isOk());
    }

    @Test
    public void shouldAddUserToDatabase() throws Exception {
        mockMvc.perform(post("/new")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"firstName\": \"Marvin\", \"nickname\": \"Coolio\"}"))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldGetUserById() throws Exception {
        User user = new User("Marvin", "Coolio");
        userRepository.save(user);
        mockMvc.perform(get("/1")).andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Marvin"))
                .andExpect(jsonPath("$.nickname").value("Coolio"));
    }

    @Test
    public void shouldUpdateUser() throws Exception {
        User user = new User("Marvin", "Coolio");
        userRepository.save(user);
        user.setId(2L);
        userRepository.save(user);
        mockMvc.perform(put("/2").contentType(MediaType.APPLICATION_JSON)
                .content("{\"nickname\": \"Billy\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nickname").value("Billy"));
    }

    @Test
    public void shouldDeleteUser() throws Exception {
        User user = new User("Marvin", "Coolio");
        userRepository.save(user);
        user.setId(3L);
        userRepository.save(user);
        user.setId(4L);
        userRepository.save(user);
        mockMvc.perform(delete("/3")).andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Marvin"));
    }
}
