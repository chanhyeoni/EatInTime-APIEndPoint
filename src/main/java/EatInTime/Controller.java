package EatInTime;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

/**
* @RestController annotation tells you that this is a RESTful Web service component
**/
@RestController
public class Controller {
	
	@RequestMapping("/")
	public String index(){
		return "Hello World from Greeter" + "\n";
	}
}