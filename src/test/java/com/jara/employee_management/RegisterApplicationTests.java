package com.jara.employee_management;

//import org.junit.Test;/*Para JUnit 4 */
import org.junit.jupiter.api.Test; /*Para JUnit 5 */
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;//Activar el perfil

/*No es necesario que ni la clases, método sean public en JUnit5 - está modernizado*/
@SpringBootTest
@ActiveProfiles("test")
class RegisterApplicationTests {

	@Test
	void contextLoads() {
	}

}
