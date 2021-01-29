package my.service;

import lombok.extern.slf4j.Slf4j;
import my.Application;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest(classes = Application.class)
@Slf4j
public class JonahomeHtmlServiceTest {

    @Autowired
    private JonahomeHtmlService jonahomeHtmlService;

    @Test
    public void crawlAll() throws IOException {
        jonahomeHtmlService.crawlAll();
    }
}