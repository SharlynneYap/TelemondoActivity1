package com.TelemondoActivity1.TelemondoActivity1.repo

import com.TelemondoActivity1.TelemondoActivity1.model.Classmate
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface  ClassmateRepo: JpaRepository<Classmate, Long> {
}