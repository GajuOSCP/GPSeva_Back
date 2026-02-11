package com.example.Gpseva.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.example.Gpseva.dto.AdminUserDetailsDTO;
import com.example.Gpseva.entity.GramPanchayat;
import com.example.Gpseva.entity.Payment;
import com.example.Gpseva.entity.UploadedDocument;
import com.example.Gpseva.repository.GramPanchayatRepository;
import com.example.Gpseva.repository.PaymentRepository;
import com.example.Gpseva.repository.UploadedDocumentRepository;

@RestController
@RequestMapping("/api/admin/dashboard")
@CrossOrigin(origins = "http://localhost:3000")
public class AdminDashboardController {

    private final GramPanchayatRepository gpRepo;
    private final PaymentRepository paymentRepo;
    private final UploadedDocumentRepository documentRepo;

    public AdminDashboardController(
            GramPanchayatRepository gpRepo,
            PaymentRepository paymentRepo,
            UploadedDocumentRepository documentRepo) {

        this.gpRepo = gpRepo;
        this.paymentRepo = paymentRepo;
        this.documentRepo = documentRepo;
    }

    // 🔥 MAIN ADMIN DASHBOARD API
    @GetMapping("/users")
    public List<AdminUserDetailsDTO> getAllUserDetails() {

        List<GramPanchayat> registrations = gpRepo.findAll();
        List<AdminUserDetailsDTO> response = new ArrayList<>();

        for (GramPanchayat gp : registrations) {

            Long userId = gp.getId();

            // Payments (userId stored as String)
            List<Payment> payments =
                    paymentRepo.findByUserId(String.valueOf(userId));

            // Documents
            List<UploadedDocument> documents =
                    documentRepo.findByUserId(userId);

            response.add(
                new AdminUserDetailsDTO(gp, payments, documents)
            );
        }

        return response;
    }
}
