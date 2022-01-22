package my.service;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class PushPlusServiceTest {

    @Test
    public void sendForMy() throws IOException {
        PushPlusService.sendForMy("my", "my1234");
    }
}