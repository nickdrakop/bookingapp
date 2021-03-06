/**
 @author nick.drakopoulos
 */

package com.market.bookingapp;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource({"classpath:environment.default.properties"})
public class BookingappConfig {
}
