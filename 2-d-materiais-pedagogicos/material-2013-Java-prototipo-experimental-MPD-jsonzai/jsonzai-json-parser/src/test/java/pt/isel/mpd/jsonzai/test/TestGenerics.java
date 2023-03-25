package pt.isel.mpd.jsonzai.test;

import junit.framework.Assert;
import junit.framework.TestCase;
import pt.isel.mpd.jsonzai.test.worldweather.WeatherData;
import pt.isel.mpd.jsonzai.test.worldweather.WeatherRequest;

import java.lang.reflect.ParameterizedType;

/**
 * Created by mcarvalho on 08-04-2015.
 */
public class TestGenerics extends TestCase{
    public void test_get_actual_type_parameter_from_weather_data() throws NoSuchFieldException {
        ParameterizedType requestType  = (ParameterizedType) WeatherData.class.getDeclaredField("request").getGenericType();
        Class<?> itemType = (Class<?>) requestType.getActualTypeArguments()[0];
        Assert.assertEquals(WeatherRequest.class, itemType);
    }
}
