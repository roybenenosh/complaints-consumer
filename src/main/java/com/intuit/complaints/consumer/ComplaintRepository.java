package com.intuit.complaints.consumer;

import com.intuit.complaints.dal.Complaint;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ComplaintRepository extends CrudRepository<Complaint, UUID> {
}
