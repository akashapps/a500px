package com.akash.a500px.helper;

import android.annotation.SuppressLint;
import com.skrumble.ally.R;
import com.skrumble.ally.models.util.RemainingTime;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

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

    private static SimpleDateFormat getDateFormatServer() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        return format;
    }

    public static long getTimeInUTC() {
        return System.currentTimeMillis();
    }

    // *********************************************************************************************
    // region Poll

    public static String getPollDurationString(long seconds){
        int day = (int)TimeUnit.SECONDS.toDays(seconds);
        long hours = TimeUnit.SECONDS.toHours(seconds) - (day * 24);
        long minute = TimeUnit.SECONDS.toMinutes(seconds) - (TimeUnit.SECONDS.toHours(seconds)* 60);

        String finalString = "";

        if (day > 0){
            finalString = DataHelper.getResourceString(day == 1 ? R.string.day_with_number : R.string.days_with_number, day) + ",";
        }

        if (hours > 0){
            finalString = finalString + " " + DataHelper.getResourceString( hours == 1 ? R.string.hour_with_number : R.string.hours_with_number, hours) + ",";
        }

        if (minute > 0){
            finalString = finalString + " " + DataHelper.getResourceString(minute == 1 ? R.string.minute_with_number : R.string.minutes_with_number, minute);
        }

        int index = finalString.lastIndexOf(",");
        if (index == finalString.length() - 1){
            try {
                finalString = new StringBuilder(finalString).replace(index, index + 1, "").toString();
            }catch (Exception e){
                finalString = "";
            }
        }

        return finalString;
    }

    // endregion

    // *********************************************************************************************
    // region Secret Chat

    // 00:00:00
    public static String getTimeFormatForSecretMessageCounter(long milliSecond){

        DecimalFormat formatter = new DecimalFormat("00");

        long hours = TimeUnit.MILLISECONDS.toHours(milliSecond);
        String hourString = formatter.format(hours);

        milliSecond -= TimeUnit.HOURS.toMillis(hours);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(milliSecond);
        String minuteString = formatter.format(minutes);

        milliSecond -= TimeUnit.MINUTES.toMillis(minutes);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(milliSecond);
        String secondString = formatter.format(seconds);

        if (hours > 0){
            return hourString + ":" + minuteString + ":" + secondString;
        }else {
            return minuteString + ":" + secondString;
        }
    }

    // full: 1 hour 1 minute 52 second
    // short: 1h 1m 1s
    public static String getTimeForTimerIcon(long seconds, boolean fullForm){
        if (seconds <= 0){
            return DataHelper.getResourceString(R.string.off);
        }

        if (seconds < 60){
            String lastPart = DataHelper.getResourceString(R.string.s);
            if (fullForm){
                lastPart = " " + DataHelper.getResourceString(R.string.sec);
            }
            return seconds +  lastPart;
        }

        int min = (int) TimeUnit.SECONDS.toMinutes(seconds);

        if (min < 60){
            String lastPart = DataHelper.getResourceString(R.string.m);
            if (fullForm){
                if (min == 1){
                    lastPart = " " + DataHelper.getResourceString(R.string.minute);
                }else {
                    lastPart = " " + DataHelper.getResourceString(R.string.minutes);
                }
            }

            return min + lastPart;
        }

        int hour = (int) TimeUnit.MINUTES.toHours(min);

        if (hour < 24){

            String lastPart = DataHelper.getResourceString(R.string.h);
            if (fullForm){
                if (hour == 1){
                    lastPart = " " + DataHelper.getResourceString(R.string.hour);
                }else {
                    lastPart = " " + DataHelper.getResourceString(R.string.hours);
                }
            }

            return hour + lastPart;
        }

        int day = (int) TimeUnit.HOURS.toDays(hour);

        if (day < 7){

            String lastPart = DataHelper.getResourceString(R.string.d);
            if (fullForm){
                if (day == 1){
                    lastPart = " " + DataHelper.getResourceString(R.string.day);
                }else {
                    lastPart = " " + DataHelper.getResourceString(R.string.days);
                }
            }

            return day + lastPart;
        }

        int week = day / 7;

        String lastPart = DataHelper.getResourceString(R.string.w);
        if (fullForm){
            lastPart = " " + DataHelper.getResourceString(R.string.week);
        }

        return week + lastPart;
    }

    // endregion

    // *********************************************************************************************
    // region Message Section Header

    /**
     * Message Section Header
     * @param date Date
     * @return
     * Today -> Today
     * Yesterday -> Yesterday
     * Same Week -> Mon, Tue etc...
     * Older - MM/DD/YYYY
     */

    public static String getTimeStringChatMessageSectionHeader(Date date) {
        Calendar calendar = getCalendar(date);
        Calendar currCalendar = getCalendar(new Date());

        // today
        if (checkDatesAreSameDate(calendar, currCalendar)) {
            return DataHelper.getResourceString(R.string.date_format_today);
        }
        // yesterday
        else if (checkDatesAreYesterdayDate(calendar, currCalendar)) {
            return DataHelper.getResourceString(R.string.date_format_yesterday);
        }
        // day of week
        else if (checkDatesAreFromSameWeek(calendar, currCalendar)) {
            return getWeekShortName(calendar.get(Calendar.DAY_OF_WEEK));
        }

        return getDateStringInMonthDayYearFormate(date);
    }

    // endregion

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

    public static String getTimeStringForChatList(Date date) {
        Calendar calendar = getCalendar(date);

        Calendar currCalendar = getCalendar(new Date());

        // now
        if (DatesAreSameMinute(date, new Date())){
            return DataHelper.getResourceString(R.string.date_format_now);
        }
        // today's hour
        else if (checkDatesAreSameDate(calendar, currCalendar)) {
            return getDisplayHourWithDate(date);
        }
        // yesterday
        else if (checkDatesAreYesterdayDate(calendar, currCalendar)) {
            return DataHelper.getResourceString(R.string.date_format_yesterday);
        }
        // day of week
        else if (checkDatesAreFromSameWeek(calendar, currCalendar)) {
            return getWeekShortName(calendar.get(Calendar.DAY_OF_WEEK));
        }

        return getDateStringInMonthDayYearFormate(date);
    }

    // endregion

    // *********************************************************************************************
    // region Message Cell

    /**
     * [ New / Calls/ Pinned ]
     *
     * Today -> HH:MM AM/PM
     * Before -> MM/DD/YYYY HH:MM AM/PM
     *
     * [ File ]
     *
     * Today -> HH:MM AM/PM
     * Before -> MM/DD/YYYY
     *
     * @return Formatted string.
     */
    public static String getWorkspaceDateTime(Date date, boolean dateOnly) {
        Calendar calendar = getCalendar(date);
        Calendar currCalendar = getCalendar(new Date());

        if (checkDatesAreSameDate(calendar, currCalendar)) {
            return getDisplayHourWithDate(date);
        }

        if (dateOnly) {
            return getDateStringInMonthDayYearFormate(date);
        } else {
            return getDateStringInMonthDayYearFormate(date) + " " + getDisplayHourWithDate(date);
        }
    }

    // endregion

    // *********************************************************************************************
    // region Candy Center

    /**
     * return a string time for range between 2 dates with month day format
     * example "Oct 2 - 6", "Oct 8 - Nov 13"
     *
     * @param date1 the first date of the range
     * @param date2 the second date of the range
     * @return the range time string
     */
    public static String getMonthDayRangeTime(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            return "";
        }
        Calendar calendarToday = getCalendar(new Date());
        Calendar calendar1 = getCalendar(date1);
        Calendar calendar2 = getCalendar(date2);

        int month1 = calendar1.get(Calendar.MONTH);
        int month2 = calendar2.get(Calendar.MONTH);
        int day1 = calendar1.get(Calendar.DAY_OF_MONTH);
        int day2 = calendar2.get(Calendar.DAY_OF_MONTH);
        int year1 = calendar1.get(Calendar.YEAR);
        int year2 = calendar2.get(Calendar.YEAR);
        int yearToday = calendarToday.get(Calendar.YEAR);

        boolean shouldDisplayYear = (year1 != year2) || (yearToday != year1) || (yearToday != year2);

        String dateRange = getRangeMonthDayString(calendar1, shouldDisplayYear);

        // same day no need for a range
        if (month1 == month2 && day1 == day2) {
            return dateRange;
        }

        dateRange = dateRange + " - ";

        if (month1 == month2 && shouldDisplayYear == false) {
            dateRange = dateRange + day2;
        } else {
            dateRange = dateRange + getRangeMonthDayString(calendar2, shouldDisplayYear);
        }
        return dateRange;
    }

    private static String getRangeMonthDayString(Calendar calendar, boolean displayYear) {
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String dateRange = getMonthShortName(month) + " " + day;
        if (displayYear) {
            int year = calendar.get(Calendar.YEAR);
            dateRange = dateRange + ", " + year;
        }
        return dateRange;
    }

    public static String getDaysAndHoursCountDownTime(Date date) {
        RemainingTime remainingTime = RemainingTime.remainingTimeFromNowToDate(date);
        return remainingTime.getRemaningTimeString();
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
     * @param calendar Calendar
     * @return Dec 16th 2017 will be return format
     */

    private static String getDayAndMonthDateLetterFormat(Calendar calendar) {
        String monthName = getMonthShortName(calendar.get(Calendar.MONTH));
        String dayNumber = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        String year = String.valueOf(calendar.get(Calendar.YEAR));
        return monthName + " " + dayNumber + ", " + year;
    }

    /**
     * @param month Month index in Int
     * @return Short name will return depend on month index
     */

    private static String getMonthShortName(int month) {
        if (month == 0) { return DataHelper.getResourceString(R.string.jan); }
        if (month == 1) { return DataHelper.getResourceString(R.string.feb); }
        if (month == 2) { return DataHelper.getResourceString(R.string.mar); }
        if (month == 3) { return DataHelper.getResourceString(R.string.apr); }
        if (month == 4) { return DataHelper.getResourceString(R.string.may); }
        if (month == 5) { return DataHelper.getResourceString(R.string.june); }
        if (month == 6) { return DataHelper.getResourceString(R.string.july); }
        if (month == 7) { return DataHelper.getResourceString(R.string.aug); }
        if (month == 8) { return DataHelper.getResourceString(R.string.sep); }
        if (month == 9) { return DataHelper.getResourceString(R.string.oct); }
        if (month == 10) { return DataHelper.getResourceString(R.string.nov); }
        if (month == 11) { return DataHelper.getResourceString(R.string.dec); }
        return DataHelper.getResourceString(R.string.jan);
    }

    /**
     * @param day Day index in Int
     * @return Get week day short name depend on day index
     */

    private static String getWeekShortName(int day) {
        if (day == 1) { return DataHelper.getResourceString(R.string.sunday); }
        if (day == 2) { return DataHelper.getResourceString(R.string.monday); }
        if (day == 3) { return DataHelper.getResourceString(R.string.tuesday); }
        if (day == 4) { return DataHelper.getResourceString(R.string.wednesday); }
        if (day == 5) { return DataHelper.getResourceString(R.string.thursday); }
        if (day == 6) { return DataHelper.getResourceString(R.string.friday); }
        if (day == 7) { return DataHelper.getResourceString(R.string.saturday); }
        return DataHelper.getResourceString(R.string.sunday);
    }

    // endregion

    // *********************************************************************************************
    // region TIME FORMAT

    private static String getFormattedNumberTimeFromInt(int time) {
        String formattedTime;
        if (time == 0) { formattedTime = "00"; }
        else if (time < 10) { formattedTime = "0" + time; }
        else { formattedTime = "" + time; }
        return formattedTime;
    }

    public static String getDisplayHourWithDate(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("hh:mm aa");
        return df.format(date.getTime());
    }

    public static String    getDisplayHourFormatString(long milliseconds) {
        int hourTime = (int) (milliseconds / 3600000);
        int minuteTime = (int) ((milliseconds - (hourTime * 3600000)) / 60000);
        return decodeTime(hourTime, minuteTime);
    }

    private static String decodeTime(int hour, int minute) {
        String AMPM = "AM";
        String minuteString = "" + minute;
        int trueHour = hour;
        if (hour >= 12) {
            trueHour = hour - 12;
            AMPM = "PM";
        }
        if (trueHour == 0) {
            trueHour = 12;
        }

        if (minute < 10) {
            minuteString = "0" + minute;
        }
        return trueHour + ":" + minuteString + " " + AMPM;
    }

    // endregion

    // *********************************************************************************************
    // region DATE CONVERSION

    public static String getStringServerFormatFromDate(Date date) {
        if (date == null) {
            return "";
        }
        // "2017-01-31T15:15:15.000Z"
        return getDateFormatServer().format(date);
    }

    public static String getStringHeaderFormatFromDate(Date date) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
        return dateformat.format(date);
    }

    public static Date getDateHeaderFromStringFormat(String dateString) {
        if (DataHelper.isStringEmpty(dateString)) {
            return null;
        }
        dateString += "T12:00:00Z";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        Date date;
        try {
            date = format.parse(dateString);
        } catch (ParseException e) {
            date = null;
            e.printStackTrace();
        }
        return date;
    }

    public static Date getDateFromStringFormat(String dateString) {
        if (DataHelper.isStringEmpty(dateString)) {
            return null;
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        format.setTimeZone(TimeZone.getTimeZone(Calendar.getInstance().getTimeZone().toString()));
        Date date;
        try {
            date = format.parse(dateString);
        } catch (ParseException e) {
            date = null;
            e.printStackTrace();
        }
        return date;
    }

    public static Date getDateForVoiceMail(String dateString) {
        if (DataHelper.isStringEmpty(dateString)) {
            return null;
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        format.setTimeZone(TimeZone.getTimeZone(Calendar.getInstance().getTimeZone().toString()));
        Date date;
        try {
            date = format.parse(dateString);
        } catch (ParseException e) {
            date = null;
            e.printStackTrace();
        }
        return date;
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

    public static boolean checkNowIsNightTime() {
//        return true;
        Calendar calendar = getCalendar(new Date());
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        if (hour >= 0 && hour <= 7) {
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

    // *********************************************************************************************
    // region THREAD DATE AND TIME

    /**
     * OUTPUT FORMAT: "Now" - "11:30AM" - "Yesterday 11:30AM" - "NOV 16, 2018 11:30AM"
     * @param date
     * @return "Now" - "11:30AM" - "Yesterday 11:30AM" - "NOV 16, 2018 11:30AM"
     */
    public static String getDateFormatShortMonthDayYearAndHour(Date date) {
        return getDateFormatShortMonthDayYearAndHour(date, true);
    }

    public static String getDateFormatShortMonthDayYearAndHour(Date date, boolean onlyFullDate) {

        Calendar calendar = getCalendar(date);

        Calendar currCalendar = getCalendar(new Date());

        // now
        if (onlyFullDate == false && DatesAreSameMinute(date, new Date())){
            return DataHelper.getResourceString(R.string.date_format_now);
        }
        // today's hour
        else if (onlyFullDate == false && checkDatesAreSameDate(calendar, currCalendar)) {
            return getDisplayHourWithDate(date);
        }
        // yesterday
        else if (onlyFullDate == false && checkDatesAreYesterdayDate(calendar, currCalendar)) {
            return DataHelper.getResourceString(R.string.date_format_yesterday) + " " + getDisplayHourWithDate(date);
        }
        // day of week
        else if (onlyFullDate == false && checkDatesAreFromSameWeek(calendar, currCalendar)) {
            return getWeekShortName(calendar.get(Calendar.DAY_OF_WEEK)) + " " + getDisplayHourWithDate(date);
        }

        return getDayAndMonthDateLetterFormat(calendar) + "  " + getDisplayHourWithDate(date);
    }

    // endregion

    public static boolean checkDatesAreSameDate(String firstDate, String secondDate) {
        if (firstDate == null || firstDate.length() == 0 || secondDate == null || secondDate.length() == 0) {
            return false;
        }
        Date fDate = new Date(Long.parseLong(firstDate) * 1000);
        Calendar fCalendar = getCalendar(fDate);
        Date sDate = new Date(Long.parseLong(secondDate) * 1000);
        Calendar sCalendar = getCalendar(sDate);
        if (fCalendar.get(Calendar.YEAR) == sCalendar.get(Calendar.YEAR)) {
            if (fCalendar.get(Calendar.MONTH) == sCalendar.get(Calendar.MONTH)) {
                if (fCalendar.get(Calendar.DAY_OF_MONTH) == sCalendar.get(Calendar.DAY_OF_MONTH)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isSameDay(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            return false;
        }
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        return isSameDay(cal1, cal2);
    }

    private static boolean isSameDay(Calendar cal1, Calendar cal2) {
        if (cal1 == null || cal2 == null) {
            return false;
        }
        return (cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA) &&
                cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR));
    }

    public static boolean checkDatesWithinFifteenMinutes(String firstDate, String secondDate) {
        long fDate = Long.parseLong(firstDate);
        long sDate = Long.parseLong(secondDate);
        if (Math.abs(fDate - sDate) < (15 * 60)) {
            return true;
        }
        return false;
    }

    public static String getDateFromEpoch(String epochTime) {
        Date date = new Date(Long.parseLong(epochTime) * 1000);
        Calendar calendar = getCalendar(date);
        return getDateFromCalendar(calendar);
    }

    public static String getTimeFromEpoch(String epochTime) {
        Date date = new Date(Long.parseLong(epochTime) * 1000);
        Calendar calendar = getCalendar(date);
        return getTimeFromCalendar(calendar);
    }

    private static String getTimeFromCalendar(Calendar calendar) {
        return decodeTime(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
    }

    private static String getDateFromCalendar(Calendar calendar) {
        Date currDate = new Date(System.currentTimeMillis());
        Calendar currCalendar = getCalendar(currDate);
        if (calendar.get(Calendar.YEAR) == currCalendar.get(Calendar.YEAR)) {
            if (calendar.get(Calendar.MONTH) == currCalendar.get(Calendar.MONTH)) {
                if (calendar.get(Calendar.DAY_OF_MONTH) == currCalendar.get(Calendar.DAY_OF_MONTH)) {
                    return DataHelper.getResourceString(R.string.date_format_today);
                }
            }
        }
        return getMonthShortName(calendar.get(Calendar.MONTH)) + " " + calendar.get(Calendar.DAY_OF_MONTH) + ", " + calendar.get(Calendar.YEAR);
    }

    public static String getSecondsFormatFromSeconds(int duration) {
        if (duration <= 0) {
            return "00:00";
        }

        int second = duration / 1000;
        int miliseconds = duration - (second * 1000);
        String secondToDisplay = getFormattedNumberTimeFromInt(second);
        return secondToDisplay + ":" + miliseconds;
    }

    public static String getMinutesFormatFromSeconds(int duration) {
        if (duration <= 0) {
            return "00:00";
        }

        int second = duration % 60;
        int minute = (duration / 60) % 60;
        int hour = duration / 3600;
        String secondToDisplay = getFormattedNumberTimeFromInt(second);
        String minuteToDisplay = getFormattedNumberTimeFromInt(minute);
        String hourToDisplay = getFormattedNumberTimeFromInt(hour);

        String formatedMinutedString = "";
        if (hour == 0) {
            formatedMinutedString = minuteToDisplay + ":" + secondToDisplay;
        } else {
            formatedMinutedString = hourToDisplay + ":" + minuteToDisplay + ":" + secondToDisplay;
        }
        return formatedMinutedString;
    }

    private long secondsElapsed(Date date) {
        return secondsElapsed(date.getTime());
    }

    public static long secondsElapsed(long timestamp) {
        return (System.currentTimeMillis() - timestamp) / 1000;
    }
}
