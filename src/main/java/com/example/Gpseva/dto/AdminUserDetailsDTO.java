package com.example.Gpseva.dto;

import java.util.List;

import com.example.Gpseva.entity.GramPanchayat;
import com.example.Gpseva.entity.Payment;
import com.example.Gpseva.entity.UploadedDocument;

public class AdminUserDetailsDTO {

    private GramPanchayat registration;
    private List<Payment> payments;
    private List<UploadedDocument> documents;

    public AdminUserDetailsDTO(
            GramPanchayat registration,
            List<Payment> payments,
            List<UploadedDocument> documents) {

        this.registration = registration;
        this.payments = payments;
        this.documents = documents;
    }

    public GramPanchayat getRegistration() {
        return registration;
    }

    public void setRegistration(GramPanchayat registration) {
        this.registration = registration;
    }

    public List<Payment> getPayments() {
        return payments;
    }

    public void setPayments(List<Payment> payments) {
        this.payments = payments;
    }

    public List<UploadedDocument> getDocuments() {
        return documents;
    }

    public void setDocuments(List<UploadedDocument> documents) {
        this.documents = documents;
    }
}
