package com.webapp.util;

import com.webapp.model.Company;
import com.webapp.model.Period;

import java.time.LocalDate;
import java.util.List;

public class HtmlUtil {
    public static boolean isEmpty(String str) {
        return str == null || str.trim().length() == 0;
    }

    public static String formatDates(Company company) {
        List<Period> periods = company.getPeriods();

        if (periods == null || periods.isEmpty()) {
            return "No periods available";
        }

        LocalDate startDate = periods.get(0).getStartDate();
        LocalDate endDate = periods.get(periods.size() - 1).getEndDate();

        return DateUtil.format(startDate) + " - " + DateUtil.format(endDate);
    }
}