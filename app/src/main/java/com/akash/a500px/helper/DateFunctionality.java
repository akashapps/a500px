package com.akash.a500px.helper;

import android.annotation.SuppressLint;
import android.content.Context;
import com.akash.a500px.R;
import com.google.common.base.Strings;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * Created by ryan on 1/7/16.
 */

@SuppressLint("SimpleDateFormat")
public class DateFunctionality {

    // Format And Calendar

    private static Calendar getCalendar(Date date) {
        Calendar calendar = new GregorianCalendar(Calendar.getInstance().getTimeZone());
        calendar.setTime(date);
        return calendar;
    }

    // endregion

    public static Date getDateFromStringFormat(String dateString) {
        if (Strings.isNullOrEmpty(dateString)) {
            return new Date();
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        format.setTimeZone(TimeZone.getTimeZone(Calendar.getInstance().getTimeZone().toString()));
        Date date;
        try {
            date = format.parse(dateString);
        } catch (ParseException e) {
            date = new Date();
            e.printStackTrace();
        }
        return date;
    }


    // *********************************************************************************************
    // region Chat List

    /**
     * Get time string for chat list
     * @param date Date
     * @return
     * Same minute -> Now
     * Today -> HH:MM am/pm
     * Yesterday -> Yesterday
     * Same Week -> Mon, Tue, Wed
     * Older - MM/DD/YYYY
     */

    public static String getTimeString(Context context, Date date) {
        Calendar calendar = getCalendar(date);

        Calendar currCalendar = getCalendar(new Date());

        // now
        if (DatesAreSameMinute(date, new Date())){
            return context.getString(R.string.date_format_now);
        }
        // today's hour
        else if (checkDatesAreSameDate(calendar, currCalendar)) {
            return getDisplayHourWithDate(date);
        }
        // yesterday
        else if (checkDatesAreYesterdayDate(calendar, currCalendar)) {
            return context.getString(R.string.date_format_yesterday);
        }
        // day of week
        else if (checkDatesAreFromSameWeek(calendar, currCalendar)) {
            return getWeekShortName(context, calendar.get(Calendar.DAY_OF_WEEK));
        }

        return getDateStringInMonthDayYearFormate(date);
    }

    // endregion

    // *********************************************************************************************
    // region Utils

    /**
     * Get date in string format (MM/DD/YYYY)
     * @param date Date
     * @return MM/DD/YYYY will be return format
     */

    private static String getDateStringInMonthDayYearFormate(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
        return simpleDateFormat.format(date);
    }

    /**
     *
     * @param context
     * @param day Day index in Int
     * @return Get week day short name depend on day index
     */

    private static String getWeekShortName(Context context, int day) {
        if (day == 1) { return context.getString(R.string.sunday); }
        if (day == 2) { return context.getString(R.string.monday); }
        if (day == 3) { return context.getString(R.string.tuesday); }
        if (day == 4) { return context.getString(R.string.wednesday); }
        if (day == 5) { return context.getString(R.string.thursday); }
        if (day == 6) { return context.getString(R.string.friday); }
        if (day == 7) { return context.getString(R.string.saturday); }
        return context.getString(R.string.sunday);
    }

    // endregion

    // *********************************************************************************************
    // region TIME FORMAT

    public static String getDisplayHourWithDate(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("hh:mm aa");
        return df.format(date.getTime());
    }

    // endregion

    // *********************************************************************************************
    // region DATE CHECK

    private static boolean checkDatesAreSameDate(Calendar fCalendar, Calendar currCalendar) {
        if (fCalendar.get(Calendar.YEAR) == currCalendar.get(Calendar.YEAR)) {
            if (fCalendar.get(Calendar.MONTH) == currCalendar.get(Calendar.MONTH)) {
                if (fCalendar.get(Calendar.DAY_OF_MONTH) == currCalendar.get(Calendar.DAY_OF_MONTH)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean checkDatesAreYesterdayDate(Calendar fCalendar, Calendar currCalendar) {
        if (fCalendar.get(Calendar.YEAR) == currCalendar.get(Calendar.YEAR)) {
            if (fCalendar.get(Calendar.MONTH) == currCalendar.get(Calendar.MONTH)) {
                int dayfirstCalendar = fCalendar.get(Calendar.DAY_OF_MONTH);
                int daysecondCalendar = currCalendar.get(Calendar.DAY_OF_MONTH);
                if (dayfirstCalendar + 1 == daysecondCalendar) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean checkDatesAreFromSameWeek(Calendar newCalendar, Calendar currCalendar) {

        //TODO:
        // These comparision may create conflict at 31 Dec and 1 Jan
        // Need to Check more.
        int newWeek = newCalendar.get(Calendar.WEEK_OF_YEAR);
        int currentWeek = currCalendar.get(Calendar.WEEK_OF_YEAR);
        if (newWeek == currentWeek) {
            return true;
        }
        return false;
    }

    public static boolean DatesAreSameMinute(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            return false;
        }

        Calendar calendar1 = getCalendar(date1);
        Calendar calendar2 = getCalendar(date2);

        boolean sameday = checkDatesAreSameDate(calendar1, calendar2);
        boolean sameMinute = (calendar1.get(Calendar.MINUTE) == calendar2.get(Calendar.MINUTE));
        return sameday && sameMinute;
    }

    // endregion
}
