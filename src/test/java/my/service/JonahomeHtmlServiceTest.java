package my.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.IOException;

@Slf4j
public class JonahomeHtmlServiceTest {

    private JonahomeHtmlService jonahomeHtmlService = new JonahomeHtmlService();

    @Test
    public void crawlAll() throws IOException {
        jonahomeHtmlService.crawlAll();
    }
}