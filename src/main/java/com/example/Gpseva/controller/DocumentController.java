package com.example.Gpseva.controller;

import java.io.IOException;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.Gpseva.entity.UploadedDocument;
import com.example.Gpseva.repository.UploadedDocumentRepository;

@RestController
@RequestMapping("/api/documents")
@CrossOrigin(origins = "http://localhost:3000")
public class DocumentController {

	private final UploadedDocumentRepository repository;

	public DocumentController(UploadedDocumentRepository repository) {
		super();
		this.repository = repository;
	}

	@PostMapping("/upload")
	public ResponseEntity<String> uploadDocument(@RequestParam("file") MultipartFile file,
			@RequestParam("documentType") String documentType, @RequestParam("userId") Long userId) {
		try {
			UploadedDocument doc = new UploadedDocument();
			doc.setUserId(userId);
			doc.setDocumentType(documentType);
			doc.setFileName(file.getOriginalFilename());
			doc.setContentType(file.getContentType());
			doc.setFileData(file.getBytes());

			repository.save(doc);

			return ResponseEntity.ok("Document uploaded successfully");
		} catch (Exception e) {
			return ResponseEntity.internalServerError().body("Upload failed");
		}
	}
	
	
	@GetMapping("/download/file/{id}")
	public ResponseEntity<byte[]> downloadUploadedDocument(@PathVariable Long id) {

	    UploadedDocument doc = repository.findById(id)
	            .orElseThrow(() -> new RuntimeException("Document not found"));

	    return ResponseEntity.ok()
	            .contentType(MediaType.parseMediaType(doc.getContentType()))
	            .header(HttpHeaders.CONTENT_DISPOSITION,
	                    "attachment; filename=\"" + doc.getFileName() + "\"")
	            .body(doc.getFileData());
	}

	

	@GetMapping("/download/{code}")
	public ResponseEntity<Resource> downloadDocument(@PathVariable String code) throws IOException {

		String filePath;

		switch (code) {
		case "GP_FORM":
			filePath = "documents/registration-process.pdf";
			break;
		case "AUTH_CERT":
			filePath = "documents/faq.pdf";
			break;
		case "BANK_FORM":
			filePath = "documents/letter-format.pdf";
			break;
		default:
			return ResponseEntity.notFound().build();
		}

		ClassPathResource resource = new ClassPathResource(filePath);

		return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF)
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
				.body(resource);
	}
}
