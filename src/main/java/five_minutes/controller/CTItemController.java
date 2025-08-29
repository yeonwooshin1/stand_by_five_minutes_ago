package five_minutes.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CTItemController {
    private final CTItemService ctItemService;

} // class end
