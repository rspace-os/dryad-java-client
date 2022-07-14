package client;


import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import model.DryadDataset;
import model.DryadDatasets;
import model.DryadFile;
import model.DryadSubmission;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Slf4j
public class DryadClientImpl implements DryadClient {

    private String apiUrlBase;
    private String token;
    private RestTemplate restTemplate;


    public DryadClientImpl(String apiUrlBase, String token) {
        this.apiUrlBase = apiUrlBase;
        this.token = token;
        this.restTemplate = new RestTemplate();

    }


    @Override
    public DryadDataset createSubmission(DryadSubmission submission) {
        ResponseEntity<DryadDataset> responseEntity = restTemplate.exchange(apiUrlBase + "/datasets", HttpMethod.POST,
                new HttpEntity<>(getHttpHeaders()), DryadDataset.class, submission);
        log.debug(Objects.requireNonNull(responseEntity.getBody()).toString());
        return responseEntity.getBody();
    }

    @Override
    public List<DryadDataset> getDatasets() {
        ResponseEntity<DryadDatasets> responseEntity = restTemplate.exchange(apiUrlBase + "/datasets", HttpMethod.GET,
                new HttpEntity<>(getHttpHeaders()), DryadDatasets.class);
        log.debug(Objects.requireNonNull(responseEntity.getBody()).toString());
        return responseEntity.getBody().getEmbedded().getDatasets();
    }

    @Override
    public DryadDataset getDataset(String doi) {
        ResponseEntity<DryadDataset> responseEntity = restTemplate.exchange(apiUrlBase + "/datasets/" + doi, HttpMethod.GET,
                new HttpEntity<>(getHttpHeaders()), DryadDataset.class);
        log.debug(Objects.requireNonNull(responseEntity.getBody()).toString());
        return responseEntity.getBody();
    }

    @Override
    public DryadDataset updateDataset(String doi, DryadDataset dryadDataset) {
        ResponseEntity<DryadDataset> responseEntity = restTemplate.exchange(apiUrlBase + "/datasets/" + doi, HttpMethod.PUT,
                new HttpEntity<>(getHttpHeaders()), DryadDataset.class, dryadDataset);
        log.debug(Objects.requireNonNull(responseEntity.getBody()).toString());
        return responseEntity.getBody();
    }

    @Override
    public DryadFile stageFile(String doi, String filename, File file) {
        HttpHeaders headers = getHttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", file);
        return restTemplate.postForObject(apiUrlBase + "/datasets/" + doi + "/files/" + filename,
                new HttpEntity<>(body, headers), DryadFile.class);
    }

    @Override
    public DryadFile stageFile(String doi, String url) {
        ResponseEntity<DryadFile> responseEntity = restTemplate.exchange(apiUrlBase + "/datasets/" + doi + "/urls", HttpMethod.POST, new HttpEntity<>(getHttpHeaders()), DryadFile.class);
        log.debug(Objects.requireNonNull(responseEntity.getBody()).toString());
        return responseEntity.getBody();
    }

    private HttpHeaders getHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);
        return headers;
    }
}
