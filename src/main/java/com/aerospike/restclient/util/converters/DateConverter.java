/*
 * Copyright 2022 Aerospike, Inc.
 *
 * Portions may be licensed to Aerospike, Inc. under one or more contributor
 * license agreements WHICH ARE COMPATIBLE WITH THE APACHE LICENSE, VERSION 2.0.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.aerospike.restclient.util.converters;

import com.aerospike.restclient.util.RestClientErrors.InvalidDateFormat;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class DateConverter {
    public static Calendar iso8601StringToCalendar(String dateString) {
        Calendar calendar = null;
        if (dateString != null && !dateString.isEmpty()) {
            ZonedDateTime zdt;
            /* Parse a date like 2000 12 31 T 23 59 59 Z  With the spaces omitted*/
            DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
            try {
                zdt = ZonedDateTime.parse(dateString, formatter);
            } catch (DateTimeParseException d) {
                throw new InvalidDateFormat(dateString);
            }
            calendar = GregorianCalendar.from(zdt);
        }

        return calendar;
    }

}
