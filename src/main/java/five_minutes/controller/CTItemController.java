package five_minutes.controller;

import five_minutes.service.CTItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CTItemController {
    private final CTItemService ctItemService;

} // class end
