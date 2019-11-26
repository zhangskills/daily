package my.service;

import lombok.extern.slf4j.Slf4j;
import my.Application;
import my.service.JonahomeHtmlService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;

@RunWith(SpringJUnit4ClassRunner.class)
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