# Introduction #

This brief howto will go through the necessary steps of installing and configuring the server part of SharedHere.


# Prerequisites #

  * Webserver with PHP support (Apache)
  * Database (MySQL)

How to install and configure the software listed in the prerequisites are not covered in this guide. The SharedHere project uses a Debian 6.0.x GNU/Linux system with Apache 2.2.x, php 5.3.x and MySQL 5.1.x. However, any similar setup should work.

# Installation #

Clone latest version of sharedhere from the repository:
```
$ git clone https://code.google.com/p/sharedhere/
```

Copy files into place:
```
$ cp -r sharedhere/server/php/sharedhere /var/www
```

Set proper permissions on content folder:
```
$ chown www-data:www-data /var/www/sharedhere/content
$ chmod 0750 /var/www/sharedhere/content
```

Create the **sharedhere** database.
```
$ mysql -u USER -p < sharedhere/server/mysql/sharedhere.sql
```

# Configuration #

Edit **Constants.php** and set **DB\_USER** and **DB\_PASS**.

All done!