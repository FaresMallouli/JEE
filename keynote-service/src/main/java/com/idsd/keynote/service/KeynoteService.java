package com.idsd.keynote.service; // Correct package

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.idsd.keynote.dto.KeynoteDTO;        // Correct import
import com.idsd.keynote.entity.Keynote;       // Correct import
import com.idsd.keynote.mapper.KeynoteMapper; // Correct import
import com.idsd.keynote.repository.KeynoteRepository; // Correct import
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor // Lombok for constructor injection
public class KeynoteService {

    private final KeynoteRepository keynoteRepository;
    private final KeynoteMapper keynoteMapper;

    @Transactional(readOnly = true) // Good practice for read operations
    public List<KeynoteDTO> getAllKeynotes() {
        return keynoteRepository.findAll().stream()
                .map(keynoteMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public Optional<KeynoteDTO> getKeynoteById(Long id) {
        return keynoteRepository.findById(id).map(keynoteMapper::toDto);
    }

    @Transactional // Default read/write transaction
    public KeynoteDTO saveKeynote(KeynoteDTO keynoteDTO) {
        Keynote keynote = keynoteMapper.toEntity(keynoteDTO);
        // Basic logic: If DTO has ID, it's an update, otherwise create
        // More robust checks (like checking existence for update) can be added
        Keynote savedKeynote = keynoteRepository.save(keynote);
        return keynoteMapper.toDto(savedKeynote);
    }

    @Transactional
    public void deleteKeynote(Long id) {
         if (keynoteRepository.existsById(id)) {
            keynoteRepository.deleteById(id);
             System.out.println("Deleted keynote with ID: " + id);
         } else {
            // Consider throwing a custom NotFoundException
             System.err.println("Keynote not found for deletion with ID: " + id);
             throw new RuntimeException("Keynote not found with id: " + id); // Or custom exception
         }
    }
}