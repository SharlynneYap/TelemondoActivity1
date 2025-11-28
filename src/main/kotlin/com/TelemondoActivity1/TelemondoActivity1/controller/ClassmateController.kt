package com.TelemondoActivity1.TelemondoActivity1.controller

import com.TelemondoActivity1.TelemondoActivity1.repo.ClassmateRepo
import com.TelemondoActivity1.TelemondoActivity1.model.Classmate
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody

@RestController
@RequestMapping("/classmates")
class ClassmateController(
    private val classmateRepo: ClassmateRepo
){
    @GetMapping("/getAllClassmates")
    fun getAllClassmates(): ResponseEntity<List<Classmate>> {
        return try {
            val classmates = classmateRepo.findAll()

            if (classmates.isEmpty()) {
                ResponseEntity.noContent().build()
            } else {
                ResponseEntity.ok(classmates)
            }
        } catch (ex: Exception) {
            ResponseEntity.internalServerError().build()
        }
    }

    @PostMapping("/addClassmate")
    fun addClassmate(@RequestBody classmate: Classmate): ResponseEntity<Classmate> {
        val saved = classmateRepo.save(classmate)
        return ResponseEntity.ok(saved)
    }

    @PutMapping("/updateClassmate/{id}")
    fun updateClassmate(
        @PathVariable id: Long,
        @RequestBody newData: Classmate
    ): ResponseEntity<Classmate> {

        val oldData = classmateRepo.findById(id)

        return if (oldData.isPresent) {
            val updated = oldData.get().apply {
                name = newData.name
                age = newData.age
            }

            val saved = classmateRepo.save(updated)
            ResponseEntity.ok(saved)

        } else {
            ResponseEntity.notFound().build()
        }
    }
    @DeleteMapping("/deleteClassmate/{id}")
    fun deleteClassmate(@PathVariable id: Long): ResponseEntity<Void> {
        classmateRepo.deleteById(id)
        return ResponseEntity.ok().build()
    }
}

