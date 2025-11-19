package com.example.demo.service;

import com.example.demo.model.entity.Faq;
import com.example.demo.repository.FaqRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class FaqService {

    @Autowired
    private FaqRepository faqRepository;

    // ✅ Create FAQ
    public Faq createFaq(Faq faq) {
        faq.setCreatedAt(LocalDateTime.now());
        faq.setIsActive(true);
        return faqRepository.save(faq);
    }

    // ✅ Get All FAQs
    public List<Faq> getAllFaqs() {
        return faqRepository.findAll();
    }

    // ✅ Get Only Active FAQs
    public List<Faq> getActiveFaqs() {
        return faqRepository.findByIsActiveTrue();
    }

    // ✅ Get FAQ by ID
    public Optional<Faq> getFaqById(Long id) {
        return faqRepository.findById(id);
    }

    // ✅ Update FAQ
    public Faq updateFaq(Long id, Faq updatedFaq) {
        Optional<Faq> existingFaqOpt = faqRepository.findById(id);
        if (existingFaqOpt.isPresent()) {
            Faq existingFaq = existingFaqOpt.get();
            existingFaq.setQuestion(updatedFaq.getQuestion());
            existingFaq.setAnswer(updatedFaq.getAnswer());
            existingFaq.setUpdatedAt(LocalDateTime.now());
            existingFaq.setUpdatedBy(updatedFaq.getUpdatedBy());
            existingFaq.setIsActive(updatedFaq.getIsActive());
            return faqRepository.save(existingFaq);
        }
        throw new RuntimeException("FAQ not found with id " + id);
    }

    // ✅ Delete FAQ (Soft Delete)
    public void deleteFaq(Long id) {
        Optional<Faq> existingFaqOpt = faqRepository.findById(id);
        if (existingFaqOpt.isPresent()) {
            Faq faq = existingFaqOpt.get();
            faq.setIsActive(false);
            faqRepository.save(faq);
        } else {
            throw new RuntimeException("FAQ not found with id " + id);
        }
    }
}
