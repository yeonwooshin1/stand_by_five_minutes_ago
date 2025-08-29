package five_minutes.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CTItemService {
    private final CTItemDao ctItemDao;

} // class end
