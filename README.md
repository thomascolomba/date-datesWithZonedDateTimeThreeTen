Handling some dates with library ThreeTen (to be used before java 8).<br/>
<br/>
How to compile and execute :<br/>
I executed it from within eclipse directly.<br/>
<br/>
I) Generalities<br/>
a)ZonedDateTime<br/>
A date-time with a time-zone in the ISO-8601 calendar system.<br/>
<br/>
<br/>
b)ZoneId<br/>
An ID for a timezone.<br/>
<br/>
<br/>
<br/>
II)The code<br/>
<b>showYear1970()</b> shows how to represent Unix epoch.<br/>
<b>showYearMinus5()</b> shows how to represent a date before current era.<br/>
<b>showYearSumerCivilization()</b> shows how to represent early Sumer civilization date (-3400 BC).<br/>
<b>showYearTyrannosaurus()</b> shows how to... well, you know.<br/>
<b>showAddingHourChangesDay() - showAddingDayChangesMonth() - showAddingDayChangesYear() - showAddingMonthChangesYear()</b> shows how to add days/month to a ZonedDateTime through a java.util.ZonedDateTime.<br/>
<b>showLeapAndNonLeapYear()</b> tells whether a year is leap or not.<br/>
<b>showDifferencesBetweenDifferentTimezones()</b> shows that same "day/month/year" are different depending on the timezone.<br/>
<b>showAllTimezonesAvailable()</b> shows the timezones available.<br/>
<b>showSomeDatesThroughPatterns()</b> shows how to print dates with different formats, ex : "dd/MM/yyyy", "yyyy.MM.dd HH:mm:ss", "yyyy-MM-dd'T'HH:mm:ss.SSSXXX"...<br/>
<b>showParsingOfSomeStringsThroughPatterns()</b> parses strings into dates through different formats.<br/>
<b>showSomeDST()</b> shows DST of London, New York and Berlin in march 2020.<br/> 
<b>showHourDifferenceBetweenLondonAndNewYorkVariesDueToDST()</b> shows how time difference varies between New York and London due to DST not being applied at the same moment.<br/>
<b>showSomeZonedDateTimeAndTimezoneClassesBehaviourDependingOnSystemTimezone()</b> shows that a ZonedDateTime instance behaviour depends of the execution environment (the operating system/jvm configuration) by default.<br/>
<b>showSomeZonedDateTimeAndTimezoneClassesBehaviourNotDependingOnSystemTimezone()</b> shows how to have a  ZonedDateTime behaving regardless of the system configuration.<br/>
<b>showZonedDateTimeDoesnotHandleLeapSecond()</b> shows ZonedDateTime don't handle leap seconds.<br/>
<b>showHowToAddressAnHourThatHappensTwiceDuringTheSameDayWhenTheClockGoesBackward()</b> shows how a developer can set a ZonedDateTime to both times referred by the same hour if that hour happens twice in the same day (DST clock backward). It is done through elapsed milliseconds.<br/>
<br/>









