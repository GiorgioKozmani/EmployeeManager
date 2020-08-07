package com.mieszko.employeesmanager.data.mappers

import com.mieszko.employeesmanager.data.model.dto.EmployeeProfileDTO
import com.mieszko.employeesmanager.domain.model.EmployeeProfile
import com.mieszko.employeesmanager.domain.model.Gender
import org.junit.Assert.assertEquals
import org.junit.Test

class EmployeeProfileMapperKtTest {

    @Test
    fun `gender is mapped correctly`() {
        listOf(
            "Female" to Gender.Female,
            "Male" to Gender.Male,
            null to null
        ).forEach {
            // Gender? -> String? conversion
            assertEquals(
                mapEmployeeProfile(EmployeeProfile(1, "", "", it.second)).gender,
                it.first
            )
        }

        listOf(
            "Female" to Gender.Female,
            "Male" to Gender.Male,
            null to null
        ).forEach {
            assertEquals(
                it.second,
                mapEmployeeProfileDto(EmployeeProfileDTO(1, null, null, it.first))
                    .gender
            )
        }
    }

}