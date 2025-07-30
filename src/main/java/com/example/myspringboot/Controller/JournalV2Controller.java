package com.example.myspringboot.Controller;


import com.example.myspringboot.Entity.JournalEntry;
import com.example.myspringboot.Entity.User;
import com.example.myspringboot.Service.JournalEntryService;
import com.example.myspringboot.Service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/MyEntries")
public class JournalV2Controller {

    @Autowired
    private JournalEntryService journalEntryService;
    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<?> getAllJournalEntriesOfUser() {

        String authenticationUser = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userService.findByUserName(authenticationUser);

        if (user == null) {
            return ResponseEntity.status(404)
                    .body(Map.of("message","User not found"));
        }

        List<JournalEntry> all = user.getJournalEntries();
        if (all == null || all.isEmpty()) {
            return ResponseEntity.status(404)
                    .body(Map.of("message", "Journal Entries Not Found!!"));
        }

        return  ResponseEntity.ok(all);

    }

    @GetMapping("id/{myID}")
    public ResponseEntity<?> getJournalById(@PathVariable ObjectId myID) {

        String authenticationUser = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userService.findByUserName(authenticationUser);

        List<JournalEntry> collect = user.getJournalEntries()
                .stream()
                .filter(x -> x.getId().equals(myID))
                .toList();

        if(!collect.isEmpty()) {
            Optional<JournalEntry> journalEntry = journalEntryService.getById(myID);

            if(journalEntry.isPresent()) {
                return ResponseEntity.ok(journalEntry.get());
            }
        }
        return ResponseEntity.status(404).body(Map.of("message", "Journal Entry not found"));
    }

    @PostMapping
    public ResponseEntity<?> createEntry(@RequestBody @Valid JournalEntry myEntry) {
        try {

            String authenticationUser = SecurityContextHolder.getContext().getAuthentication().getName();

            journalEntryService.saveEntry(myEntry,authenticationUser);
            return ResponseEntity.status(201).body(myEntry);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Something went wrong"));
        }
    }

    @PutMapping("update/{journalId}")
    public ResponseEntity<?> updateEntry(@RequestBody @Valid JournalEntry newEntry,
                                         @PathVariable("journalId") ObjectId myId) {

        String authenticationUser = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userService.findByUserName(authenticationUser);

        List<JournalEntry> collect = user.getJournalEntries()
                .stream()
                .filter(x -> x.getId().equals(myId))
                .toList();

        if(!collect.isEmpty()) {
            Optional<JournalEntry> journalEntry = journalEntryService.getById(myId);

            if (journalEntry.isPresent()) {
                JournalEntry old = journalEntry.get();
                old.setTitle(!newEntry.getTitle().isEmpty() ? newEntry.getTitle() : old.getTitle());
                old.setContent(newEntry.getContent() != null && !newEntry.getContent().isEmpty() ? newEntry.getContent() : old.getContent());
                journalEntryService.updateEntry(old);
                return new ResponseEntity<>(old, HttpStatus.OK);
            }

        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    @DeleteMapping("delete/{journalId}")
    public ResponseEntity<?> deletedJournalEntryById(@PathVariable("journalId") ObjectId myID) {

        try{
            String authenticationUser = SecurityContextHolder.getContext().getAuthentication().getName();

            journalEntryService.deleteById(myID ,authenticationUser);
            return ResponseEntity.status(200).body("Entry deleted successfully");
        }
        catch (Exception e){
            return ResponseEntity.status(401).body(Map.of("message", "Please enter your valid journal Id"));
        }
    }

    @DeleteMapping("delete")
    public ResponseEntity<?> deletedAll() {
      List<JournalEntry> all =  journalEntryService.getAll();
      if (!all.isEmpty()) {
            journalEntryService.deleteAll();
            return new ResponseEntity<>(all, HttpStatus.OK);
      }
      return new ResponseEntity<>("No entries found",HttpStatus.NOT_FOUND);

    }
}
