package com.idsd.keynote.controller; // Correct package

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.idsd.keynote.dto.KeynoteDTO;    // Correct import
import com.idsd.keynote.service.KeynoteService; // Correct import

import java.util.List;

@RestController
@RequestMapping("/keynotes") // Base path used by Gateway routing
@RequiredArgsConstructor
@Tag(name = "Keynote API", description = "Endpoints for Keynote Management")
public class KeynoteController {

    private final KeynoteService keynoteService;

    @GetMapping
    @Operation(summary = "Get all keynotes")
    public List<KeynoteDTO> getAll() {
        return keynoteService.getAllKeynotes();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get keynote by ID")
    public ResponseEntity<KeynoteDTO> getById(@PathVariable Long id) {
        return keynoteService.getKeynoteById(id)
                .map(ResponseEntity::ok) // Shortcut for .map(dto -> ResponseEntity.ok(dto))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Create a new keynote")
    public ResponseEntity<KeynoteDTO> create(@RequestBody KeynoteDTO keynoteDTO) {
        // Ensure ID is null for creation to avoid accidental updates
        if (keynoteDTO.id() != null) {
             return ResponseEntity.badRequest().build(); // Or throw specific error
        }
        KeynoteDTO savedDto = keynoteService.saveKeynote(keynoteDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedDto);
    }

     @PutMapping("/{id}")
     @Operation(summary = "Update an existing keynote")
     public ResponseEntity<KeynoteDTO> update(@PathVariable Long id, @RequestBody KeynoteDTO keynoteDTO) {
         // Ensure the ID in the path matches the ID in the body (if present)
         if (keynoteDTO.id() != null && !keynoteDTO.id().equals(id)) {
            return ResponseEntity.badRequest().build(); // ID mismatch
         }
         // Create DTO with correct ID for saving
         KeynoteDTO dtoToUpdate = new KeynoteDTO(id, keynoteDTO.nom(), keynoteDTO.prenom(), keynoteDTO.email(), keynoteDTO.fonction());

         return keynoteService.getKeynoteById(id) // Check if keynote exists
                 .map(existing -> ResponseEntity.ok(keynoteService.saveKeynote(dtoToUpdate))) // If exists, update and return 200 OK
                 .orElse(ResponseEntity.notFound().build()); // If not exists, return 404
     }

     @DeleteMapping("/{id}")
     @Operation(summary = "Delete a keynote by ID")
     public ResponseEntity<Void> delete(@PathVariable Long id) {
         try {
            keynoteService.deleteKeynote(id);
            return ResponseEntity.noContent().build(); // Return 204 No Content on success
         } catch (RuntimeException e) { // Catch exception if keynote not found
             System.err.println(e.getMessage());
            return ResponseEntity.notFound().build(); // Return 404 if not found
         }
     }
}