package uk.gov.hmcts.reform.em.npa.redaction;

import org.apache.commons.io.FilenameUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import uk.gov.hmcts.reform.em.npa.Application;
import uk.gov.hmcts.reform.em.npa.TestSecurityConfiguration;
import uk.gov.hmcts.reform.em.npa.service.dto.redaction.MarkUpDTO;
import uk.gov.hmcts.reform.em.npa.service.dto.redaction.RectangleDTO;
import uk.gov.hmcts.reform.em.npa.service.dto.redaction.RedactionDTO;

import java.io.File;
import java.io.IOException;
import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class, TestSecurityConfiguration.class})
public class ImageRedactionTest {

    private static final File TEST_IMAGE_FILE = new File(
            ClassLoader.getSystemResource("fist.png").getPath()
    );

    @Autowired
    private ImageRedaction imageRedaction;

    private Set<RectangleDTO> rectangles = new HashSet<>();

    @Before
    public void setup() {
        initRedactionDTOList();
    }

    public void initRedactionDTOList() {
        for (int i = 0; i < 5 ; i++) {
            RectangleDTO rectangle = new RectangleDTO();
            rectangle.setId(UUID.randomUUID());
            rectangle.setX(100.00);
            rectangle.setY(100.00);
            rectangle.setHeight(100.00);
            rectangle.setWidth(100.00);

            rectangles.add(rectangle);
        }
    }

    @Test
    public void pdfRedactionTest() throws IOException {
        File result = imageRedaction.redaction(TEST_IMAGE_FILE, rectangles);
        Assert.assertTrue(result.getName().contains("altered"));
        Assert.assertTrue(result.getName().contains(FilenameUtils.getExtension(TEST_IMAGE_FILE.getName())));
    }

    @Test(expected = IOException.class)
    public void pdfRedactionFailureTest() throws IOException {
        imageRedaction.redaction(new File("invalid_file"), rectangles);
    }
}
