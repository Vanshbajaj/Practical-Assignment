package com.practical.presentation

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Test

class CoreServiceTest {

    private val service = mockk<CoreService>()

    @Test
    fun `test service function`() {
        every { service.calculate(1, 2) } returns 3

        val result = service.calculate(1, 2)

        assertEquals(3, result)
        verify { service.calculate(1, 2) }
    }

}

interface CoreService {
    fun calculate(a: Int, b: Int): Int

}
