package ru.iteranet;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.xmlunit.builder.Input;
import ru.iteranet.entity.City;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = City.class)
public class test {

        @Autowired
        private MockMvc mvc;

        @Autowired
        private ObjectMapper objectMapper;

        @Test
        public void whenInputIsInvalid_thenReturnsStatus400() throws Exception {
            Object string = "{\"name\":\"Smolensk\", \"country\":{ \"name\":\"Russia\"}}";
            //ObjectMapper objectMapper = new ObjectMapper();
            String body = objectMapper.writeValueAsString(string);
            //MockMvc mvc = new MockMvc();

            mvc.perform(post("/city")
                    .contentType("application/json")
                    .content(body))
                    .andExpect(status().isBadRequest());
        }

}


