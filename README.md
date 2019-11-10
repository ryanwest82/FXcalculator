# FX Calculator

A console application for converting currencies


## Features

* Core Java only :)

### Prerequisites

* Java 8+
* Maven 3+ (to build + run tests)

### Configuration
Any configurable application properties can be found in the `config.properties` file

The default implementation of the application relies upon 2 input files in CSV format, one containing valid currencies 
and the other containing conversion rate information.

**currencies.csv schema**
`currency code, decimal precision`

**rates.csv schema**
`base currency, terms currency, conversion rate/cross-via currency`

### Build
Simply run a `mvn clean install` to compile and run tests

### Console Commands
Conversion commands must be in the format of {CURRENCY} {AMOUNT} in {CURRENCY}, for example :
```bash
AUD 100.49 in GBP
```

Type `QUIT` to exit
