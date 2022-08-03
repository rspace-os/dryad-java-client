package client;


import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import model.DryadDataset;
import model.DryadDatasets;
import model.DryadEmbedded;
import model.DryadFile;
import model.DryadSubmission;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Slf4j
public class DryadClientImpl implements DryadClient {

    private URL apiUrlBase;
    private String token;
    private RestTemplate restTemplate;


    public DryadClientImpl(URL apiUrlBase, String token) {
        this.apiUrlBase = apiUrlBase;
        this.token = token;
        this.restTemplate = new RestTemplate();
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setBufferRequestBody(false);
        this.restTemplate.setRequestFactory(requestFactory);

    }


    @Override
    public boolean testConnection() {
        try {
            ResponseEntity<String> response = restTemplate.exchange(apiUrlBase + "/test", HttpMethod.GET, new HttpEntity<>(getHttpHeaders()), String.class);
            log.info("Test connection response: {}", response.getBody());
            return response.getStatusCode().is2xxSuccessful();
        } catch (RestClientException e) {
            log.error("Error testing connection to Dryad API: " + e.getMessage());
            return false;
        }
    }

    @Override
    public DryadDataset createSubmission(DryadSubmission submission) {
        HttpEntity<DryadSubmission> request = new HttpEntity<>(submission, getHttpHeaders());
        ResponseEntity<DryadDataset> responseEntity = restTemplate.postForEntity(apiUrlBase + "/datasets", request, DryadDataset.class);
        log.debug(Objects.requireNonNull(responseEntity.getBody()).toString());
        return responseEntity.getBody();
    }

    @Override
    public List<DryadDataset> getDatasets() {
        log.debug("Getting datasets");
        ResponseEntity<DryadDatasets> responseEntity = restTemplate.exchange(apiUrlBase + "/datasets", HttpMethod.GET,
                new HttpEntity<>(getHttpHeaders()), DryadDatasets.class);
        log.debug(Objects.requireNonNull(responseEntity.getBody()).toString());
        DryadEmbedded embedded = responseEntity.getBody().getEmbedded();
        if (embedded == null) {
            return Collections.emptyList();
        } else {
            return embedded.getDatasets();
        }
    }

    @Override
    public DryadDataset getDataset(String doi) {
        log.debug("Getting dataset with doi: {}", doi);
        ResponseEntity<DryadDataset> responseEntity = restTemplate.exchange(apiUrlBase + "/datasets/" + urlEncode(doi), HttpMethod.GET,
                new HttpEntity<>(getHttpHeaders()), DryadDataset.class);
        log.debug(Objects.requireNonNull(responseEntity.getBody()).toString());
        return responseEntity.getBody();
    }

    @Override
    public DryadDataset updateDataset(String doi, DryadDataset dryadDataset) {
        log.debug("Updating dataset {}", doi);
        ResponseEntity<DryadDataset> responseEntity = restTemplate.exchange(apiUrlBase + "/datasets/" + urlEncode(doi), HttpMethod.PUT,
                new HttpEntity<>(getHttpHeaders()), DryadDataset.class, dryadDataset);
        log.debug(Objects.requireNonNull(responseEntity.getBody()).toString());
        return responseEntity.getBody();
    }

    @Override
    public DryadFile stageFile(String doi, String filename, File file) throws IOException {
        String url = apiUrlBase + "/datasets/" + urlEncode(doi) + "/files/" + urlEncode(filename);
        log.debug("URL: {}", url);
        HttpHeaders headers = getHttpHeaders();
        String contentType = Files.probeContentType(file.toPath());
        log.debug("Content type: {}", contentType);
        headers.setContentType(MediaType.valueOf(contentType));
        HttpEntity<FileSystemResource> request = new HttpEntity<>(new FileSystemResource(file), headers);
        log.debug("Staging file {}", filename);
        log.debug("Headers: {}", headers);
        return restTemplate.exchange(url, HttpMethod.PUT, request, DryadFile.class).getBody();
    }

    @Override
    public DryadFile stageFile(String doi, String url) {
        ResponseEntity<DryadFile> responseEntity = restTemplate.exchange(apiUrlBase + "/datasets/" + urlEncode(doi) + "/urls", HttpMethod.POST, new HttpEntity<>(getHttpHeaders()), DryadFile.class);
        log.debug(Objects.requireNonNull(responseEntity.getBody()).toString());
        return responseEntity.getBody();
    }

    private HttpHeaders getHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);
        headers.add("Content-Type", "application/json");
        return headers;
    }

    private String urlEncode(String string) {
        return URLEncoder.encode(string, StandardCharsets.UTF_8);
    }
}
