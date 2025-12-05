package com.TelemondoActivity1.TelemondoActivity1

import com.TelemondoActivity1.TelemondoActivity1.controller.ClassmateController
import com.TelemondoActivity1.TelemondoActivity1.repo.ClassmateRepo
import com.TelemondoActivity1.TelemondoActivity1.service.ClassmateService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.util.UUID

class ClassmateServiceTest : TestContainersBaseTest() {

    @Autowired
    lateinit var service: ClassmateService

    @Autowired
    lateinit var repo: ClassmateRepo

    @Test
    fun testCreate() {
        // Given
        val req = ClassmateController.ClassmateCreateDTO(
            name = "User Create Test",
            age = 1
        )

        val countBefore = repo.count() // counts the list before adding

        // When
        val result = service.add(req)

        // Then
        assertThat(result.id).isNotNull()
        assertThat(result.name).isEqualTo(req.name)
        assertThat(result.age).isEqualTo(req.age)

        // Checking
        val countAfter = repo.count() // this counts the list after adding
        assertThat(countAfter).isEqualTo(countBefore + 1) // checks if the counts are equal
    }

    @Test
    fun testUpdate() {
        // Given
        val existingId = UUID.fromString("067ac38f-8b35-42b9-9f83-15a6dd3fadf0")
        val updateDto = ClassmateController.ClassmateUpdateDTO(
            name = "User Update Test",
            age = 1
        )

        // When
        val updated = service.update(existingId, updateDto)

        // Then
        assertThat(updated.id).isEqualTo(existingId)
        assertThat(updated.name).isEqualTo("User Update Test")
        assertThat(updated.age).isEqualTo(1)

        //Checking
        val fromDb = repo.findById(existingId).orElseThrow() // tried to find a record with the ID
        assertThat(fromDb.name).isEqualTo("User Update Test")  // compares to what is actually saved in the db
        assertThat(fromDb.age).isEqualTo(1)
    }

    @Test
    fun testDelete() {
        // Given
        val existingId = UUID.fromString("9e8ab20a-2fa8-4437-a319-c68f00926eb8")
        val countBefore = repo.count()

        // When
        service.delete(existingId)

        // Then
        val countAfter = repo.count()
        assertThat(countAfter).isEqualTo(countBefore - 1)

        // Checking
        val maybeDeleted = repo.findById(existingId) // Tries to find a record with the ID
        assertThat(maybeDeleted).isEmpty
    }
}