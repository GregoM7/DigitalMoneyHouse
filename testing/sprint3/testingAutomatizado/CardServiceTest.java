import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
public class CardControllerTest {

    @InjectMocks
    private CardController cardController;

    @Mock
    private CardService cardService;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(cardController).build();
    }

    @Test
    public void testGetAllByAccountId() throws Exception {
        Integer accountId = 1;
        List<Card> cards = new ArrayList<>();
        // Agrega algunas tarjetas a la lista "cards"

        when(cardService.getAllByAccountId(accountId)).thenReturn(Optional.of(cards));

        mockMvc.perform(MockMvcRequestBuilders.get("/cards/{accountId}", accountId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty());
    }

    @Test
    public void testCreateCard() throws Exception {
        Card card = new Card();
        // Configura los datos de la tarjeta para la prueba

        when(cardService.create(card)).thenReturn(card);

        mockMvc.perform(MockMvcRequestBuilders.post("/cards")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(card)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());
    }

    @Test
    public void testDeleteCard() throws Exception {
        Integer accountId = 1;
        Integer cardId = 1;

        mockMvc.perform(MockMvcRequestBuilders.delete("/cards/{accountId}/{cardId}", accountId, cardId))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testGetCard() throws Exception {
        Integer accountId = 1;
        Integer cardId = 1;
        Card card = new Card();
        // Configura los datos de la tarjeta para la prueba

        when(cardService.getCard(accountId, cardId)).thenReturn(card);

        mockMvc.perform(MockMvcRequestBuilders.get("/cards/{accountId}/{cardId}", accountId, cardId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());
    }

    // Método de utilidad para convertir objetos a JSON
    private String asJsonString(Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
