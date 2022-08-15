package client;

import com.fasterxml.jackson.databind.ObjectMapper;
import model.DryadDataset;
import model.DryadFile;
import model.DryadFunder;
import model.DryadSubmission;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

class DryadClientTest {

    private DryadClientImpl dryadClientImpl;
    private MockRestServiceServer mockServer;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void startUp() throws MalformedURLException {
        dryadClientImpl = new DryadClientImpl(new URL("https://dryad-stg.cdlib.org/api/v2"), "token");
        mockServer = MockRestServiceServer.createServer(dryadClientImpl.getRestTemplate());
        objectMapper = new ObjectMapper();
    }

    @AfterEach
    public void tearDown() {

    }

    @Test
    public void testTestConnection() {
        mockServer.expect(requestTo("https://dryad-stg.cdlib.org/api/v2/test"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK));
        assertEquals(true, dryadClientImpl.testConnection());
    }

    @Test
    public void testDryadSubmission() throws IOException {
        String submissionRequestJson = IOUtils.resourceToString("/dryadSubmissionRequest.json", Charset.defaultCharset());
        String submissionResponseJson = IOUtils.resourceToString("/dryadSubmissionResponse.json", Charset.defaultCharset());
        mockServer.expect(requestTo(dryadClientImpl.getApiUrlBase() + "/datasets"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(submissionResponseJson));
        DryadSubmission toSubmit = objectMapper.readValue(submissionRequestJson, DryadSubmission.class);
        DryadDataset submissionResponse = dryadClientImpl.createSubmission(toSubmit);
        assertNotNull(submissionResponse);
        assertEquals("doi:10.5072/FK2708471V", submissionResponse.getIdentifier());
        assertEquals("A Study of Red-Black Trees with Weight", submissionResponse.getTitle());

    }

    @Test
    public void testDryadGetDatasets() throws IOException {
        String datasetsJson = IOUtils.resourceToString("/dryadDatasetsResponse.json", Charset.defaultCharset());
        mockServer.expect(requestTo(dryadClientImpl.getApiUrlBase() + "/datasets"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(datasetsJson));

        List<DryadDataset> datasets = dryadClientImpl.getDatasets();
        assertNotNull(datasets);
        assertEquals(20, datasets.size());
        assertEquals("doi:10.5061/dryad.pq269b0", datasets.get(0).getIdentifier());
    }

    @Test
    void testDryadGetDataset() throws IOException {
        String datasetJson = IOUtils.resourceToString("/dryadDatasetResponse.json", Charset.defaultCharset());
        mockServer.expect(requestTo(dryadClientImpl.getApiUrlBase() + "/datasets/doi%253A10.5061%252Fdryad.pq269b0"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(datasetJson));

        DryadDataset dataset = dryadClientImpl.getDataset("doi:10.5061/dryad.pq269b0");
        assertNotNull(dataset);
        assertEquals("doi:10.5061/dryad.pq269b0", dataset.getIdentifier());
    }

    @Test
    void testStageFileUrl() {
        mockServer.expect(requestTo(dryadClientImpl.getApiUrlBase() + "/datasets/doi%253A10.5061%252Fdryad.pq269b0/urls"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body("{  \"path\": \"I0006475A.jpg\",\n" +
                                "  \"size\": 218594,\n" +
                                "  \"mimeType\": \"image/jpeg\",\n" +
                                "  \"status\": \"created\"\n" +
                                "}"));
        DryadFile dryadFile = dryadClientImpl.stageFile("doi:10.5061/dryad.pq269b0", "http://www.example.com/I0006475A.jpg");
        assertNotNull(dryadFile);
        assertEquals(dryadFile.getPath(), "I0006475A.jpg");
        assertEquals(dryadFile.getSize(), 218594);
        assertEquals(dryadFile.getMimeType(), "image/jpeg");
        assertEquals(dryadFile.getStatus(), "created");
    }

    @Test
    void testStageFileFile() throws IOException {
        mockServer.expect(requestTo(dryadClientImpl.getApiUrlBase() + "/datasets/doi%253A10.5061%252Fdryad.pq269b0/files/ethane.jpg"))
                .andExpect(method(HttpMethod.PUT))
                .andRespond(withStatus(HttpStatus.CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body("{  \"path\": \"ethane.jpg\",\n" +
                                "  \"size\": 218594,\n" +
                                "  \"mimeType\": \"image/jpeg\",\n" +
                                "  \"status\": \"created\"\n" +
                                "}"));
        File testFile = new File("src/main/resources/ethane.jpg");

        DryadFile dryadFile = dryadClientImpl.stageFile("doi:10.5061/dryad.pq269b0", "ethane.jpg", testFile);
        assertNotNull(dryadFile);
        assertEquals(dryadFile.getPath(), "ethane.jpg");
        assertEquals(dryadFile.getSize(), 218594);
        assertEquals(dryadFile.getMimeType(), "image/jpeg");
        assertEquals(dryadFile.getStatus(), "created");
    }

    @Test
    void testCrossrefFunderDeserialization() throws IOException {
        String funderJson = IOUtils.resourceToString("/crossrefFunder.json", Charset.defaultCharset());
        DryadFunder funder = objectMapper.readValue(funderJson, DryadFunder.class);
        assertNotNull(funder);
        assertEquals("http://dx.doi.org/10.13039/100000001", funder.getIdentifier());
        assertEquals("National Science Foundation", funder.getOrganization());
    }




}
