package com.AccionesUD.AccionesUD;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AccionesUdApplicationTests {

    @Test
    void contextLoads() {
        // Esta prueba verifica que el contexto de Spring se carga correctamente
    }
	@Test
    void mainMethodRunsWithoutExceptions() {
        AccionesUdApplication.main(new String[] {});
    }
}
