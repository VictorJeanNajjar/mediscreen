package org.docnotes;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface DocNotesRepository extends MongoRepository<DocNotes, ObjectId> {
    Optional<DocNotes> findByPatientId(int id);
}