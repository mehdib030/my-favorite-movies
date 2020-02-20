package com.e.myfavoritemovies;

import com.e.myfavoritemovies.utils.DateUtils;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

/**
 * DateUtils test class
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class DatesUtilsTest {

    @Test
    public void testFormatDate(){
        String formattedDate = DateUtils.formatDate("2020-02-02");

        System.out.println(formattedDate);

        assertNotNull("The date is not null",formattedDate);
        assertEquals("The date format should be Month day year ","Feb 02 2020",formattedDate);
    }
}