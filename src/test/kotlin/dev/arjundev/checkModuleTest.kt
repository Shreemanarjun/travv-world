package dev.arjundev


import org.junit.Test

import org.koin.test.verify.verify

class CheckModulesTest {

    @Test
    fun `check modules`(){
        appModule.verify()
    }
}