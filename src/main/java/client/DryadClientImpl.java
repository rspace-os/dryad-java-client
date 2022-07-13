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
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

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
    public DryadSubmission createSubmission(DryadSubmission submission) {
        ResponseEntity<DryadSubmission> responseEntity = restTemplate.exchange(apiUrlBase + "/datasets", HttpMethod.POST, new HttpEntity<>(getHttpHeaders(token)), DryadSubmission.class, submission);
        log.debug(Objects.requireNonNull(responseEntity.getBody()).toString());
        return responseEntity.getBody();
    }

    @Override
    public List<DryadDataset> getDatasets() {
        ResponseEntity<DryadDatasets> responseEntity = restTemplate.exchange(apiUrlBase + "/datasets", HttpMethod.GET, new HttpEntity<>(getHttpHeaders(token)), DryadDatasets.class);
        log.debug(Objects.requireNonNull(responseEntity.getBody()).toString());
        return responseEntity.getBody().getEmbedded().getDatasets();
    }

    @Override
    public DryadDataset getDataset(String doi) {
        ResponseEntity<DryadDataset> responseEntity = restTemplate.exchange(apiUrlBase + "/datasets/" + doi, HttpMethod.GET, new HttpEntity<>(getHttpHeaders(token)), DryadDataset.class);
        log.debug(Objects.requireNonNull(responseEntity.getBody()).toString());
        return responseEntity.getBody();
    }

    @Override
    public DryadDataset updateDataset(String doi) {
        return null;
    }

    @Override
    public DryadFile stageFile(String doi, String filename, MultipartFile file) {
        return null;
    }

    @Override
    public DryadFile stageFile(String doi, String url) {
        ResponseEntity<DryadFile> responseEntity = restTemplate.exchange(apiUrlBase + "/datasets/" + doi + "/urls", HttpMethod.POST, new HttpEntity<>(getHttpHeaders(token)), DryadFile.class);
        log.debug(Objects.requireNonNull(responseEntity.getBody()).toString());
        return responseEntity.getBody();
    }

    private HttpHeaders getHttpHeaders(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);
        return headers;
    }
}
