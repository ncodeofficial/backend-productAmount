package dcode.util;

import lombok.extern.slf4j.Slf4j;

import java.util.Date;

@Slf4j
public class DateUtil {

    public static boolean isAvailableCoupon(Date startDate, Date endDate) {
        Date today = new Date();
        log.info(">>> " + today);
        return today.after(startDate) && today.before(endDate);
    }
}
