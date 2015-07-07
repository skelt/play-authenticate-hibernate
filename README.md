# Play Authenticate sample Java application for JPA/Hibernate

Authentication providers that require further configuration parameters
are commented out in `conf/play.plugins`. Please review the configuration
in `conf/play-authenticate/mine.conf` and reenable them if you want to
use them.

Example of play authentication using Hibernate and MySql

Play version: 2.4.2
play authenticate: 0.7.0-SNAPSHOT
MySQL

This is a experiment of using session factory - trying it with one session on the main thread
Not appearing to be a good idea (not confident its thread safe and you can't do lazy loading), pitty as the code is more maintainable than the master.