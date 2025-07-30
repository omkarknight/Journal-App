package com.example.myspringboot.Service;


import com.example.myspringboot.Entity.JournalEntry;
import com.example.myspringboot.Entity.User;
import com.example.myspringboot.Repository.JournalEntryRepository;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Slf4j
@Service
public class JournalEntryService {


    @Autowired
    private JournalEntryRepository journalEntryRepository;
    @Autowired
    private UserService userService;

    @Transactional  // Ensures both journal save and user update happen in one DB transaction
    public void saveEntry(JournalEntry journalEntry, String userName) {
        try {
            // Find the currently authenticated user using their username
            User user = userService.findByUserName(userName);

            // If user is not found, throw an error to prevent orphan journal entry creation
            if (user == null) {
                throw new RuntimeException("User not found: " + userName);
            }

            // Set current timestamp on the journal entry
            journalEntry.setDate(LocalDateTime.now());

            // Link the journal entry to the username
            journalEntry.setUserName(user.getUserName());

            // Save the journal entry to the database — now it will have an ID and timestamps
            JournalEntry saved = journalEntryRepository.save(journalEntry);

            // Check if the user's journal entry list is null (first time) and initialize if needed
            if (user.getJournalEntries() == null) {
                user.setJournalEntries(new ArrayList<>());
            }

            // Attach the saved journal entry to the user's journal list
            user.getJournalEntries().add(saved);

            /// Save the user again with the updated journal entry list
            /// NOTE: This method should not re-encode the password
            userService.saveUserWithoutEncoding(user);

        } catch (Exception e) {
            log.error("Something went wrong with : {} ", journalEntry.getUserName());
            throw new RuntimeException(e);
        }
    }

    public void updateEntry(JournalEntry journalEntry) {
            journalEntryRepository.save(journalEntry);
    }

    public List<JournalEntry> getAll() {
       return journalEntryRepository.findAll();
    }

    public Optional<JournalEntry> getById(ObjectId id) {
      return journalEntryRepository.findById(id);
    }

    @Transactional
    public void deleteById(ObjectId id, String userName) {
            User user = userService.findByUserName(userName);

            if (user == null) {
                throw new RuntimeException("User not found" + userName);
            }

            boolean remove = user.getJournalEntries().removeIf(x -> x.getId().equals(id));

            if (remove) {
                userService.saveUserWithoutEncoding(user); ///Caution save if not carefully handled
                journalEntryRepository.deleteById(id);
            }
            else{
                throw new RuntimeException("Journal entry not found in user's list.");
            }
    }

    public void deleteAll() {
        journalEntryRepository.deleteAll();
    }

}
