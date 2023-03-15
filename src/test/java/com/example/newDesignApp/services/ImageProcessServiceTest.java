package com.example.newDesignApp.services;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;

@ContextConfiguration(classes = {ImageProcessService.class})
@ExtendWith(SpringExtension.class)
class ImageProcessServiceTest {
    @Autowired
    private ImageProcessService imageProcessService;

    @Test
    void testConvertMultipartToByteArray() throws IOException {
        byte[] actualConvertMultipartToByteArrayResult = imageProcessService.convertMultipartToByteArray(
                new MockMultipartFile("Name", new ByteArrayInputStream("AXAXAXAX".getBytes("UTF-8"))));
        assertEquals(8, actualConvertMultipartToByteArrayResult.length);
        assertEquals('A', actualConvertMultipartToByteArrayResult[0]);
        assertEquals('X', actualConvertMultipartToByteArrayResult[1]);
        assertEquals('A', actualConvertMultipartToByteArrayResult[2]);
        assertEquals('X', actualConvertMultipartToByteArrayResult[3]);
        assertEquals('A', actualConvertMultipartToByteArrayResult[4]);
        assertEquals('X', actualConvertMultipartToByteArrayResult[5]);
        assertEquals('A', actualConvertMultipartToByteArrayResult[6]);
        assertEquals('X', actualConvertMultipartToByteArrayResult[7]);
    }
}

