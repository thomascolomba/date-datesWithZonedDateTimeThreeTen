package datesWithThreeTen;

import java.util.Set;

import org.threeten.bp.Instant;
import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.LocalTime;
import org.threeten.bp.Month;
import org.threeten.bp.ZoneId;
import org.threeten.bp.ZonedDateTime;
import org.threeten.bp.format.DateTimeFormatter;
import org.threeten.bp.format.DateTimeFormatterBuilder;
import org.threeten.bp.temporal.WeekFields;

public class ThreeTenZonedDateTimeHandling {
	public static void main(String[] args) {
		showYear1970();
		showYearMinus5();
		showYearSumerCivilization();
		showYearTyrannosaurus();
		showAddingHourChangesDay();
		showAddingDayChangesMonth();
		showAddingDayChangesYear();
		showAddingMonthChangesYear();
		showLeapAndNonLeapYear();
		showAllTimezonesAvailable();
		showDifferencesBetweenDifferentTimezones();
		showSomeDatesThroughPatterns();
		showParsingOfSomeStringsThroughPatterns();
		showSomeDST();
		showHourDifferenceBetweenLondonAndNewYorkVariesDueToDST();
		showSomeZonedDateTimeAndTimezoneClassesBehaviourDependingOnSystemTimezone();
		showSomeZonedDateTimeAndTimezoneClassesBehaviourNotDependingOnSystemTimezone();
		showZonedDateTimeDoesnotHandleLeapSecond();
		showHowToAddressAnHourThatHappensTwiceDuringTheSameDayWhenTheClockGoesBackward();
	}
	
	private static void showHowToAddressAnHourThatHappensTwiceDuringTheSameDayWhenTheClockGoesBackward() {
		System.out.println("showHowToAddressAnHourThatHappensTwiceDuringTheSameDayWhenTheClockGoesBackward");
		//London Sunday, October 25, 2:00 am -> 1:00 am. So 1:30 am happens twice. (see method showDSTLondonClockBackward()) 
		long timestampFirstTimeItSHalfOne = 1603585800000L;//1603585800000L is the timestamp for the first time it's 1:30 (before clock goes backward)
		ZonedDateTime zdtFirstTime = ZonedDateTime.ofInstant(Instant.ofEpochMilli(timestampFirstTimeItSHalfOne), ZoneId.of("Europe/London"));
		showZonedDateTimeAndTimeAndTimezone(zdtFirstTime);
		System.out.println(zdtFirstTime.toInstant().toEpochMilli());
		
		long timestampSecondTimeItSHalfOne = 1603589400000L;//1603589400000L is the timestamp for the second time it's 1:30 (after clock goes backward)
		ZonedDateTime zdtSecondTime = ZonedDateTime.ofInstant(Instant.ofEpochMilli(timestampSecondTimeItSHalfOne), ZoneId.of("Europe/London"));
		showZonedDateTimeAndTimeAndTimezone(zdtSecondTime);
		System.out.println(zdtSecondTime.toInstant().toEpochMilli());
	}
	
	private static void showZonedDateTimeDoesnotHandleLeapSecond() {
		System.out.println("showZonedDateTimeDoesnotHandleLeapSecond");
		ZonedDateTime zdt = ZonedDateTime.parse("2005-12-31T23:59:59.000Z");
		ZonedDateTime plus1Second = zdt.plusSeconds(1);
		System.out.println("Does ZonedDateTime handle the leap second at the end of 2005 : "+(plus1Second.getYear() == 2005));
	}
	
	private static void showSomeZonedDateTimeAndTimezoneClassesBehaviourDependingOnSystemTimezone() {
		System.out.println("showSomeZonedDateTimeAndTimezoneClassesBehaviourDependingOnSystemTimezone");
		ZonedDateTime zdt = ZonedDateTime.now();
		//ZonedDateTime.now()" uses jvm/system configuration (timezone), therefore the result of "ZonedDateTime.now()" varies when executed in different timezones.
		//You may test it by executing that method using different OS timezone configuration.
		showSomeZonedDateTimeAndTimezoneClassesBehaviour(zdt);
	}
	private static void showSomeZonedDateTimeAndTimezoneClassesBehaviourNotDependingOnSystemTimezone() {
		System.out.println("showSomeZonedDateTimeAndTimezoneClassesBehaviourNotDependingOnSystemTimezone");
		ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("UTC"));
		//other possibilities to have DateTime not depending on execution environment : 
		//	1) change value of DateTimeZone.getDefault.(), but this is very invasive,and may affect every part of the program using the default timezone
		//	2) Instead of using "ZonedDateTime.now()" each time we need it, we could use one "BusinessZonedDateTimeFactory" (or several if needed) that returns a ZonedDateTime initialized with the appropriate timezone so we would avoid bugs due to wrongly initialized ZonedDateTime objects.
		showSomeZonedDateTimeAndTimezoneClassesBehaviour(zdt);
	}
	private static void showSomeZonedDateTimeAndTimezoneClassesBehaviour(ZonedDateTime zdt) {
		showZonedDateTimeAndTimeAndTimezone(zdt);
	}
	private static void showHourDifferenceBetweenLondonAndNewYorkVariesDueToDST() {
		System.out.println("showHourDifferenceBetweenLondonAndNewYorkVariesDueToDST");
		//Date in year : 	January		NY-switchToSummerTime	Lon-switchToSummerTime	Lon-switchToWinterTime	NY-switchToWinterTime	...EnfOfYear
		//hour difference :	+5			+4						+5						+4						+5
		System.out.println("beginning of the year, hour difference between London and New York is : 5 hours");
		whenItIsXHourAtYTimezoneThenWhatTimeIsItInZTimezone(2020, Month.JANUARY.getValue(),  8,  12,  0, 0, "America/New_York", "Europe/London");//usual +5 hours
		System.out.println("New York clock goes 1 hour forward : hour difference now is : 4 hours");
		whenItIsXHourAtYTimezoneThenWhatTimeIsItInZTimezone(2020, Month.MARCH.getValue(),  8,  2,  30, 0, "America/New_York", "Europe/London");//delta is 4 due to DST(summer time hour change) for NewYork at this date
		System.out.println("London clock goes 1 hour forward : hour difference now is : 5 hours");
		whenItIsXHourAtYTimezoneThenWhatTimeIsItInZTimezone(2020,  Month.MARCH.getValue(),  29, 0, 30, 0, "America/New_York", "Europe/London");//usual +5 hours because DST for 
		//London Sunday, October 25, 2:00 am
		System.out.println("London clock goes 1 hour backward : hour difference now is : 4 hours");
		whenItIsXHourAtYTimezoneThenWhatTimeIsItInZTimezone(2020,  Month.OCTOBER.getValue(),  25, 0, 30, 0, "America/New_York", "Europe/London");//delta is 4 hours
		//NY Sunday, November 1, 2:00 am
		System.out.println("New York clock goes 1 hour backward : hour difference now is : 5 hours");
		whenItIsXHourAtYTimezoneThenWhatTimeIsItInZTimezone(2020,  Month.NOVEMBER.getValue(),  1, 2, 30, 0, "America/New_York", "Europe/London");//back to 5
	}
	private static void showSomeDST() {
		System.out.println("showSomeDST");
		showDSTNewYork();
		showDSTLondon();
		showDSTBerlin();
		showDSTLondonClockBackward();
	}
	private static void showDSTNewYork() {
		//8th of March 2020 2am -> 3am
		ZonedDateTime zdt = ZonedDateTime.of(2020, Month.MARCH.getValue(), 8, 1, 30, 0, 0, ZoneId.of("America/New_York"));
		showZonedDateTimeAndTimeAndTimezone(zdt);
		ZonedDateTime oneHourLater = zdt.plusHours(1);
		showZonedDateTimeAndTimeAndTimezone(oneHourLater);
	}
	private static void showDSTLondon() {
		//29th of March 2020 1am -> 2am
		ZonedDateTime zdt = ZonedDateTime.of(2020, Month.MARCH.getValue(), 29, 0, 30, 0, 0, ZoneId.of("Europe/London"));
		showZonedDateTimeAndTimeAndTimezone(zdt);
		ZonedDateTime oneHourLater = zdt.plusHours(1);
		showZonedDateTimeAndTimeAndTimezone(oneHourLater);
	}
	private static void showDSTBerlin() {
		//29th of March 2020 2am -> 3am
		ZonedDateTime zdt = ZonedDateTime.of(2020, Month.MARCH.getValue(), 29, 1, 30, 0, 0, ZoneId.of("Europe/Berlin"));
		showZonedDateTimeAndTimeAndTimezone(zdt);
		ZonedDateTime oneHourLater = zdt.plusHours(1);
		showZonedDateTimeAndTimeAndTimezone(oneHourLater);
	}
	private static void showDSTLondonClockBackward() {
		//25th of October 2020 2am -> 1am
		ZonedDateTime zdt = ZonedDateTime.of(2020, Month.OCTOBER.getValue(), 25, 1, 30, 0, 0, ZoneId.of("Europe/London"));
		showZonedDateTimeAndTimeAndTimezone(zdt);
		System.out.println(zdt.toInstant().toEpochMilli());
		System.out.println("+ 1 hour");
		ZonedDateTime oneHourLater = zdt.plusHours(1);
		showZonedDateTimeAndTimeAndTimezone(oneHourLater);
		System.out.println(oneHourLater.toInstant().toEpochMilli());
	}
	private static void showParsingOfSomeStringsThroughPatterns() {
		System.out.println("showParsingOfSomeStringsThroughPatterns");
		showParsingOfSomeStringsThroughDateTimeFormatWithPatterns();
		showParsingOfSomeStringsThroughISO8601UTC();
		showParsingOfSomeStringsInAPredefinedTimezone();
	}
	private static void showParsingOfSomeStringsThroughDateTimeFormatWithPatterns() {
		showZonedDateTime(ZonedDateTime.of(LocalDate.parse("01/01/2000", DateTimeFormatter.ofPattern("dd/MM/yyyy")), LocalTime.MIN, ZoneId.of("UTC")));
		showZonedDateTimeAndTime(ZonedDateTime.of(LocalDateTime.parse("01/01/2000-08", DateTimeFormatter.ofPattern("dd/MM/yyyy-HH")), ZoneId.of("UTC")));
		showZonedDateTime(ZonedDateTime.of(LocalDate.parse("2020/1", new DateTimeFormatterBuilder().appendPattern("YYYY/w").parseDefaulting(WeekFields.ISO.dayOfWeek(), 1).toFormatter()), LocalTime.MIN, ZoneId.of("UTC")));
		showZonedDateTimeAndTimeAndNano(ZonedDateTime.of(LocalDateTime.parse("2001-07-04T12:08:56.235", DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS")), ZoneId.of("UTC")));
		showZonedDateTimeAndTimeAndNanoAndTimezone(ZonedDateTime.parse("2001-07-04T12:08:56.235-0500", DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ")));
		showZonedDateTimeAndTimeAndNanoAndTimezone(ZonedDateTime.parse("2001-07-04T12:08:56.235+0600", DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ")));
	}
	private static void showParsingOfSomeStringsThroughISO8601UTC() {
		showZonedDateTimeAndTimeAndNanoAndTimezone(ZonedDateTime.parse("2000-01-01T12:00:00-05:00"));
		showZonedDateTimeAndTimeAndNanoAndTimezone(ZonedDateTime.parse("2000-01-01T12:00:00.123-05:00"));
	}
	private static void showParsingOfSomeStringsInAPredefinedTimezone() {
		String stringDateToParseToDateTime = "2000-01-01";
		showParsingOfADateStringInAPredefinedTimezone(stringDateToParseToDateTime, ZoneId.of("Europe/Berlin"));
		showParsingOfADateStringInAPredefinedTimezone(stringDateToParseToDateTime, ZoneId.of("Europe/London"));
		showParsingOfADateStringInAPredefinedTimezone(stringDateToParseToDateTime, ZoneId.of("America/New_York"));
		
		String stringDateHourToParseToDateTime = "2000-01-01T15";
		showParsingOfADateHourStringInAPredefinedTimezone(stringDateHourToParseToDateTime, ZoneId.of("Europe/Berlin"));
		showParsingOfADateHourStringInAPredefinedTimezone(stringDateHourToParseToDateTime, ZoneId.of("Europe/London"));
		showParsingOfADateHourStringInAPredefinedTimezone(stringDateHourToParseToDateTime, ZoneId.of("America/New_York"));
		
		String stringDateHourMinuteToParseToDateTime = "2000-01-01T15:10";
		showParsingOfADateHourMinuteStringInAPredefinedTimezone(stringDateHourMinuteToParseToDateTime, ZoneId.of("Europe/Berlin"));
		showParsingOfADateHourMinuteStringInAPredefinedTimezone(stringDateHourMinuteToParseToDateTime, ZoneId.of("Europe/London"));
		showParsingOfADateHourMinuteStringInAPredefinedTimezone(stringDateHourMinuteToParseToDateTime, ZoneId.of("America/New_York"));
	}
	private static void showParsingOfADateStringInAPredefinedTimezone(String stringToParseToDateTime, ZoneId zoneId) {
		showZonedDateTimeAndTimezone(ZonedDateTime.of(LocalDate.parse(stringToParseToDateTime, DateTimeFormatter.ofPattern("yyyy-MM-dd")), LocalTime.MIN, zoneId));
	}
	private static void showParsingOfADateHourStringInAPredefinedTimezone(String stringToParseToDateTime, ZoneId zoneId) {
		showZonedDateTimeAndTimeAndTimezone(ZonedDateTime.of(LocalDateTime.parse(stringToParseToDateTime, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH")), zoneId));
	}
	private static void showParsingOfADateHourMinuteStringInAPredefinedTimezone(String stringToParseToDateTime, ZoneId zoneId) {
		showZonedDateTimeAndTimeAndTimezone(ZonedDateTime.of(LocalDateTime.parse(stringToParseToDateTime, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")), zoneId));
	}
	
	private static void showSomeDatesThroughPatterns() {
		System.out.println("showSomeDatesThroughPatterns");
		showSomeDatesThroughPatternsAsISO8601();
		showSomeDatesThroughPatternsWithDateTimeFormatter();
	}
	private static void showSomeDatesThroughPatternsAsISO8601() {
		ZonedDateTime dateTime = ZonedDateTime.now();
		System.out.println(dateTime.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
	}
	private static void showSomeDatesThroughPatternsWithDateTimeFormatter() {
		System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
		System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd")));
		System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
		System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd G")));
		System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss")));
		System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss:SSS")));
		System.out.println(ZonedDateTime.now(ZoneId.of("UTC")).format(DateTimeFormatter.ofPattern("MM/dd/yyyy - HH:mm:ss Z")));		
	}
	private static void showDifferencesBetweenDifferentTimezones() {
		System.out.println("showDifferencesBetweenDifferentTimezones");
		System.out.println("Berlin offset : "+ZoneId.of("Europe/Berlin").getRules().getOffset(Instant.now()));
		System.out.println("London offset : "+ZoneId.of("Europe/London").getRules().getOffset(Instant.now()));
		System.out.println("Belfast offset : "+ZoneId.of("Europe/Belfast").getRules().getOffset(Instant.now()));
	}
	private static void showAllTimezonesAvailable() {
		System.out.println("showAllTimezonesAvailable");
		for (String id : ZoneId.getAvailableZoneIds()) {
			ZoneId zone = ZoneId.of(id);
			System.out.println(zone.getId() +" offset : "+zone.getRules().getOffset(Instant.now()));
		}
	}
	private static void showLeapAndNonLeapYear() {
		System.out.println("showLeapAndNonLeapYear");
		System.out.println("is "+2003+" a leap year : "+isLeapYear(2003));
		System.out.println("is "+2004+" a leap year : "+isLeapYear(2004));
	}
	private static void showAddingMonthChangesYear() {
		System.out.println("showAddingMonthChangesYear");
		ZonedDateTime zonedDateTime = ZonedDateTime.of(2000, Month.DECEMBER.getValue(), 15, 0, 0, 0, 0, ZoneId.of("UTC"));
		showZonedDateTimeAndTime(zonedDateTime);
		System.out.println("+1 month");
		ZonedDateTime oneMonthLater = zonedDateTime.plusMonths(1);
		showZonedDateTimeAndTime(oneMonthLater);
	}
	private static void showAddingDayChangesYear() {
		System.out.println("showAddingDayChangesYear");
		ZonedDateTime zonedDateTime = ZonedDateTime.of(2000, Month.DECEMBER.getValue(), 31, 0, 0, 0, 0, ZoneId.of("UTC"));
		showZonedDateTimeAndTime(zonedDateTime);
		System.out.println("+1 day");
		ZonedDateTime oneDayLater = zonedDateTime.plusDays(1);
		showZonedDateTimeAndTime(oneDayLater);
	}
	private static void showAddingDayChangesMonth() {
		System.out.println("showAddingDayChangesMonth");
		ZonedDateTime zonedDateTime = ZonedDateTime.of(2000, Month.JANUARY.getValue(), 31, 0, 0, 0, 0, ZoneId.of("UTC"));
		showZonedDateTimeAndTime(zonedDateTime);
		System.out.println("+1 day");
		ZonedDateTime oneDayLater = zonedDateTime.plusDays(1);
		showZonedDateTimeAndTime(oneDayLater);
	}
	private static void showAddingHourChangesDay() {
		System.out.println("showAddingHourChangesDay");
		ZonedDateTime zonedDateTime = ZonedDateTime.of(2000, Month.JANUARY.getValue(), 01, 23, 30, 0, 0, ZoneId.of("UTC"));
		showZonedDateTimeAndTime(zonedDateTime);
		ZonedDateTime oneHourLater = zonedDateTime.plusHours(1);
		System.out.println("+1 hour");
		showZonedDateTimeAndTime(oneHourLater);
	}
	private static void showYear1970() {
		System.out.println("showYear1970");
		ZonedDateTime zonedDateTimeOf = ZonedDateTime.of(1970, Month.JANUARY.getValue(), 01, 0, 0, 0, 0, ZoneId.of("UTC"));
		showZonedDateTime(zonedDateTimeOf);
	}
	private static void showYearMinus5() {
		System.out.println("showYearMinus5");
		ZonedDateTime zonedDateTimeOf = ZonedDateTime.of(-5, Month.JANUARY.getValue(), 01, 0, 0, 0, 0, ZoneId.of("UTC"));
		showZonedDateTime(zonedDateTimeOf);
	}
	private static void showYearSumerCivilization() {
		System.out.println("showYearSumerCivilization");
		ZonedDateTime zonedDateTimeOf = ZonedDateTime.of(-3400, Month.JANUARY.getValue(), 01, 0, 0, 0, 0, ZoneId.of("UTC"));
		showZonedDateTime(zonedDateTimeOf);
	}
	private static void showYearTyrannosaurus() {
		System.out.println("showYearTyrannosaurus");
		ZonedDateTime zonedDateTimeOf = ZonedDateTime.of(-68000000, Month.JANUARY.getValue(), 01, 0, 0, 0, 0, ZoneId.of("UTC"));
		showZonedDateTime(zonedDateTimeOf);
	}
	
	//util methods
	private static void whenItIsXHourAtYTimezoneThenWhatTimeIsItInZTimezone(int year, int month, int day, int hour, int minute, int second, String timezoneIdY, String timezoneIdZ) {
		ZonedDateTime zdtY = ZonedDateTime.of(year, month, day, hour, minute, second, 0, ZoneId.of(timezoneIdY));
		showZonedDateTimeAndTimeAndNanoAndTimezone(zdtY);
		showZonedDateTimeAndTimeAndNanoAndTimezone(zdtY.withZoneSameInstant(ZoneId.of(timezoneIdZ)));
	}
	private static Boolean isLeapYear(int year) {
		return ZonedDateTime.of(year, Month.JANUARY.getValue(), 1, 0, 0, 0, 0, ZoneId.of("UTC")).toLocalDate().isLeapYear();
	}
	private static void showZonedDateTime(ZonedDateTime zdt) {
		System.out.println(zdt.getYear() +" - "+zdt.getMonthValue() +" - "+zdt.getDayOfMonth());
	}
	private static void showZonedDateTimeAndTime(ZonedDateTime zdt) {
		System.out.println(zdt.getYear() +" - "+zdt.getMonthValue() +" - "+zdt.getDayOfMonth()+" - "+zdt.getHour()+" - "+zdt.getMinute()+" - "+zdt.getSecond());
	}
	private static void showZonedDateTimeAndTimeAndTimezone(ZonedDateTime zdt) {
		System.out.println(zdt.getYear() +" - "+zdt.getMonthValue() +" - "+zdt.getDayOfMonth()+" - "+zdt.getHour()+" - "+zdt.getMinute()+" - "+zdt.getSecond()+" - "+zdt.getZone().getId());
	}
	private static void showZonedDateTimeAndTimeAndNanoAndTimezone(ZonedDateTime zdt) {
		System.out.println(zdt.getYear() +" - "+zdt.getMonthValue() +" - "+zdt.getDayOfMonth()+" - "+zdt.getHour()+" - "+zdt.getMinute()+" - "+zdt.getSecond() + " - "+zdt.getNano()+" - "+zdt.getZone().getId());
	}
	private static void showZonedDateTimeAndTimezone(ZonedDateTime zdt) {
		System.out.println(zdt.getYear() +" - "+zdt.getMonthValue() +" - "+zdt.getDayOfMonth()+" - "+zdt.getZone().getId());
	}
	private static void showZonedDateTimeAndTimeAndNano(ZonedDateTime zdt) {
		System.out.println(zdt.getYear() +" - "+zdt.getMonthValue() +" - "+zdt.getDayOfMonth()+" - "+zdt.getHour()+" - "+zdt.getMinute()+" - "+zdt.getSecond() + " - "+zdt.getNano());
	}
}
