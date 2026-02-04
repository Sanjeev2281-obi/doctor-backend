@RestController
@RequestMapping("/ping")
public class PingController {

    @GetMapping
    public String ping() {
        return "OK";
    }
}
