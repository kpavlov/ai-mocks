package me.kpavlov.mokksy

import assertk.assertThat
import assertk.assertions.isNegative
import assertk.assertions.isPositive
import assertk.assertions.isZero
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock

internal class StubComparatorTest {
    lateinit var request1: RequestSpecification
    lateinit var request2: RequestSpecification
    lateinit var response: AbstractResponseDefinition<String>

    @BeforeEach
    fun beforeEach() {
        response = mock()
    }

    @Test
    fun `compare should return zero when priorities are equal`() {
        request1 = RequestSpecification(priority = 1)
        request2 = RequestSpecification(priority = 1)

        val stub1 = Stub(request1, response)
        val stub2 = Stub(request2, response)

        val result = StubComparator.compare(stub1, stub2)

        assertThat(result).isZero()
    }

    @Test
    fun `compare should return a negative value when the first priority is less`() {
        request1 = RequestSpecification(priority = 1)
        request2 = RequestSpecification(priority = 2)

        val stub1 = Stub(request1, response)
        val stub2 = Stub(request2, response)

        val result = StubComparator.compare(stub1, stub2)

        assertThat(result).isNegative()
    }

    @Test
    fun `compare should return a positive value when the first priority is greater`() {
        request1 = RequestSpecification(priority = 2)
        request2 = RequestSpecification(priority = 1)

        val stub1 = Stub(request1, response)
        val stub2 = Stub(request2, response)

        val result = StubComparator.compare(stub1, stub2)

        assertThat(result).isPositive()
    }
}
