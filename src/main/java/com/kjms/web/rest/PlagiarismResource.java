package com.kjms.web.rest;

import com.kjms.service.dto.SearchResult;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Document;
import javax.swing.text.rtf.RTFEditorKit;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * REST controller for managing the Organization Group.
 */
@Tag(name = "Plagiarism Checker")
@RestController
@RequestMapping("/api/v1/plagiarism-check")
public class PlagiarismResource {

    private static final String[] ALLOWED_EXTENSIONS = {"txt", "docx","rtf"};

    // Function to check if the file extension is allowed
    private boolean isAllowedFile(String filename) {
        String extension = filename.substring(filename.lastIndexOf(".") + 1);
        for (String ext : ALLOWED_EXTENSIONS) {
            if (ext.equalsIgnoreCase(extension)) {
                return true;
            }
        }
        return false;
    }

    private List<SearchResult> searchText(String apiKey, String searchEngineId, String query) throws UnsupportedEncodingException {
        String baseUrl = "https://www.googleapis.com/customsearch/v1";
        String url = baseUrl + "?key=" + apiKey + "&cx=" + searchEngineId + "&q=" + URLEncoder.encode(query, StandardCharsets.UTF_8);;

        HttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);

        try {
            HttpResponse response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            String responseData = EntityUtils.toString(response.getEntity());

            if (entity != null) {

                JSONObject jsonObject = new JSONObject(responseData);

                List<SearchResult> itemsList = new ArrayList<>();

                if (jsonObject.has("items")) {

                    ArrayList<JSONObject> items = new ArrayList<>();

                    JSONArray childArray = jsonObject.getJSONArray("items");
                    for (int i = 0; i < childArray.length(); i++) {
                        items.add(childArray.getJSONObject(i));
                    }

                    for (JSONObject item : items) {
                        SearchResult itemData = new SearchResult();
                        itemData.setTitle(item.get("title").toString());
                        itemData.setLink(item.get("link").toString());
                        itemData.setSentence(query);
                        itemsList.add(itemData);
                    }

                }

                return itemsList;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file,
                                        @RequestParam("api_key") String apiKey,
                                        @RequestParam("search_engine_id") String searchEngineId) {

        if (file == null || file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No file part in the request.");
        }

        if (!isAllowedFile(Objects.requireNonNull(file.getOriginalFilename()))) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("File type not allowed. Allowed extensions: txt, docs.");
        }

        if (apiKey == null || apiKey.isEmpty() || searchEngineId == null || searchEngineId.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("API key and search engine ID are required in form data.");
        }

        try {

            String filename = file.getOriginalFilename();

            String fileContents;

            if (filename.endsWith(".docx")) {
                fileContents = extractTextFromDocx(file);
            } else if (filename.endsWith(".rtf")) {
                fileContents = extractTextFromRtf(file);
            } else {
                fileContents = new String(file.getBytes());
            }

            List<SearchResult> results = new ArrayList<>();

            String[] sentences = fileContents.split("\\.");

            for (String sentence : sentences) {
                String trimmedSentence = sentence.trim();
                if (trimmedSentence.length() > 3) {
                    List<SearchResult> response = searchText(apiKey, searchEngineId, trimmedSentence);
                    results.addAll(response);
                }
            }

            return ResponseEntity.ok().body(results);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing the file.");
        } catch (BadLocationException e) {
            throw new RuntimeException(e);
        }
    }

    private String extractTextFromDocx(MultipartFile file) throws IOException {
        try (InputStream inputStream = file.getInputStream()) {
            XWPFDocument docx = new XWPFDocument(inputStream);
            List<XWPFParagraph> paragraphs = docx.getParagraphs();
            StringBuilder fullText = new StringBuilder();
            for (XWPFParagraph para : paragraphs) {
                fullText.append(para.getText()).append(" ");
            }
            return fullText.toString().trim();
        }
    }

    private String extractTextFromRtf(MultipartFile file) throws IOException, BadLocationException {
        RTFEditorKit rtfEditorKit = new RTFEditorKit();
        Document document = new DefaultStyledDocument();
        InputStream inputStream = file.getInputStream();
        // Read the RTF file into the document
        rtfEditorKit.read(inputStream, document, 0);
        inputStream.close();

        // Extract the text from the document
        String text = document.getText(0, document.getLength());

        // Print or process the extracted text
        System.out.println(text);

        return text;
    }
}
