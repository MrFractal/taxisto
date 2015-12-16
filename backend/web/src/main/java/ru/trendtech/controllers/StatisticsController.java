package ru.trendtech.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.trendtech.common.mobileexchange.model.client.LoginClientResponse;
import ru.trendtech.common.mobileexchange.model.common.LoginRequest;
import ru.trendtech.utils.PhoneUtils;

/**
 * Created by petr on 23.09.2014.
 */


@Controller
@RequestMapping("/stat")
@Transactional
public class StatisticsController {

}
